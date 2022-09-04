package webtool.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import webtool.pojo.AggContList;
import webtool.pojo.Employee;
import webtool.pojo.GoJSData;
import webtool.pojo.GoJSLinkData;
import webtool.pojo.GoJSNodeData;
import webtool.pojo.OrgContainer;
import webtool.pojo.OrgView;
import webtool.pojo.OrgViewItem;
import webtool.pojo.Person;
import webtool.pojo.ProcStatus;
import webtool.pojo.RespStatus;
import webtool.pojo.ViewByType;
import webtool.pojo.WebEmployeeView;
import webtool.pojo.WebUpdateContainer;
import webtool.pojo.WebViewUpdate;
import webtool.repository.EmployeeRepository;
import webtool.repository.OrgContainerRepository;
import webtool.repository.OrgViewItemRepository;
import webtool.repository.OrgViewRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.service.DataLoaderService;
import webtool.service.SystemStatusService;

@Component
@Scope("singleton")
public class CoreDAO {

	public static final String FIELD_DELIM = "|";
	public static final String CRLF = "\\r\\n";
	public static final String DEFAULT_VIEW = "default";
	public static final String DEFAULT_DEPT = "= all =";
	public static final String WILDCARD = "%";
	public static final String MULTI_DELIMITER = "/";
	
	static Logger log = Logger.getLogger(CoreDAO.class);
	static public int RATING_NOT_SET = -1;

	public static final EnumSet<ProcStatus> canEditUpdate = EnumSet.of(ProcStatus.CREATED, ProcStatus.EXPIRED); // orders
																												// in

	final SimpleDateFormat ordIdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	final SimpleDateFormat webDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
	final SimpleDateFormat webDateFormat = new SimpleDateFormat("dd MMM yyyy");
	final BigDecimal WEI = new BigDecimal("1000000000000000000");

	int ACCEPT_TO_EXPIRE = 120; // minute
	int DEPOSIT_TO_EXPIRE = 120; // minute

	int INVITED_MAX = 50; // max invited users per order

	Map<String, List<WebEmployeeView>> mgrMap = new HashMap<String, List<WebEmployeeView>>();
	Map<String, WebEmployeeView> perMap = new HashMap<String, WebEmployeeView>();
	Map<String, Long> containerBox = new HashMap<String, Long>();

	Map<String, String> mgrDecl = new HashMap<String, String>();

	static String FILT = "Supply Chain";
	static final boolean USE_FILT = false;
	static final boolean USE_ID = true;

	long INT_CTR = 10000;

	@Autowired
	PaymentTypeRepository paymentTypeRepository;
	@Autowired
	MailDAO emailDAO;
	@Autowired
	UserObjectDAO userDAO;

	@Autowired
	private SystemStatusService systemStatusService;

	@Autowired
	private DataLoaderService dataloaderService;

	@Autowired
	private OrgViewItemRepository orgViewItemRepository;

	@Autowired
	EmployeeRepository employeeRepositiory;
	@Autowired
	OrgContainerRepository orgContainerRepository;
	@Autowired
	OrgViewRepository orgViewRepository;

	// private ThreadedQueueProcessor<TradeCmd> qProc;
	final ExecutorService executorService = Executors.newFixedThreadPool(1);

	@PostConstruct
	private void init() {
		initProc();
	}

	private void initProc() {
		systemStatusService.setWorking(true);
		systemStatusService.setNewMessage("System OK");
		dataloaderService.initDefaultView();
		// dataloaderService.load();

	}

	public DataLoaderService getDataService() {
		return dataloaderService;
	}

	public AggContList getAggContainerData(String viewName, ViewByType vType) {

		AggContList agg = new AggContList();
		List<OrgContainer> orgContainers = orgContainerRepository.findByViewDeptSorted(viewName);
		for (OrgContainer oc : orgContainers) {
			if ((oc.getDeptName() != null) && (oc.getGroupName() != null) && (oc.getTeamName() != null)) {
				if (vType == ViewByType.ViewByTeam)
					agg.addContainer(oc.getTeamName(), oc.getGroupName(), oc.getDeptName());
				else
					agg.addContainer(oc.getDeptName(), oc.getGroupName(), oc.getTeamName());
			}
		}
		return agg;
	}

	/**
	 * Match string with delimter
	 *  str = 'valuations' filt = 'valuations/another team' == match
	 * @param str
	 * @param filt
	 * @return
	 */
	private boolean matchByStringSliced(String teamDept,String matchStr) {
		if (teamDept==null) return true;
		if (teamDept.equalsIgnoreCase(matchStr) ) return true;
		String args[] = matchStr.split(CoreDAO.MULTI_DELIMITER);
		for(String ag : args) {
			if ( teamDept.equalsIgnoreCase(ag) ) return true;
		}
		return false;
	}
	
	public List<WebEmployeeView> getViewData(String viewName, String teamOrdept, boolean showLeavers, ViewByType vType) {

		List<WebEmployeeView> result = new ArrayList<WebEmployeeView>();
		List<Employee> emps = ImmutableList.copyOf(employeeRepositiory.findAll());
		Map<String, Employee> empMap = emps.stream().collect(Collectors.toMap(Employee::getInum, it -> it));

		List<OrgContainer> orgContainers = orgContainerRepository.findByViewName(viewName);
		Map<Long, OrgContainer> contMap = orgContainers.stream()
				.collect(Collectors.toMap(OrgContainer::getId, it -> it));

		List<OrgViewItem> ovOpt = orgViewItemRepository.findByViewName(viewName);
		Map<String, Employee> missing = emps.stream().collect(Collectors.toMap(Employee::getInum, it -> it));
		OrgContainer ORPH_CONT = new OrgContainer(viewName, "not-set", "not-set", "not-set");

		for (OrgViewItem ovi : ovOpt) {
			Employee emp = empMap.get(ovi.getiNum());
			if (emp != null) {
				missing.remove(ovi.getiNum());
				if (!emp.getLeaver() || (showLeavers && emp.getLeaver())) {

					final WebEmployeeView wev = new WebEmployeeView();
					wev.setEmployee(emp);
					OrgContainer oc = contMap.get(ovi.getContainerId());
					if (oc != null) {				
						if ((teamOrdept == null) || (teamOrdept != null))
						  if (ViewByType.ViewByDept==vType && matchByStringSliced(teamOrdept,oc.getDeptName()) || ViewByType.ViewByTeam==vType && matchByStringSliced(teamOrdept,oc.getTeamName()))
						{
							wev.setContainer(oc);
							wev.setOrgViewItem(ovi);
							result.add(wev);
						}
						
					} else {
						log.error("Container does not exist " + ovi);
					}
				}
			} else {
				log.error("empl does not exist " + ovi);
			}
		}

		// generate orphan container
		for (Entry<String, Employee> iter : missing.entrySet()) {
			final WebEmployeeView wev = new WebEmployeeView();
			OrgViewItem ovi = new OrgViewItem(viewName, iter.getValue().getInum(), ORPH_CONT.getId());
			wev.setEmployee(iter.getValue());
			wev.setOrgViewItem(ovi);
			wev.setContainer(ORPH_CONT);
			result.add(wev);
		}

		return result;
	}

	/**
	 * EXPORT current view data to CSV
	 * @param writer
	 * @param view
	 * @return
	 * @throws IOException
	 */
	public CSVPrinter exportStaffView(Writer writer,String view) throws IOException {
		final CSVPrinter csvPrinter = new CSVPrinter(writer,
				CSVFormat.RFC4180.withFirstRecordAsHeader());
		
		List<WebEmployeeView> viewData = this.getViewData(view, null, false, ViewByType.ViewByDept);
		
		csvPrinter.printRecord(DataLoaderService.EXPORT_HDR);
		for(WebEmployeeView wev : viewData) {
			csvPrinter.printRecord( wev.getExportValues() );
		}
		
		return csvPrinter;
	}
	
	
	public RespStatus updateStaffContainer(WebEmployeeView empView) {
		final String viewName = empView.getDescr();
		// 1. find container id
		Optional<OrgContainer> orgContOpt = orgContainerRepository.findByAll(viewName, empView.getDeptName(),
				empView.getGroupName(), empView.getTeamName());
		if (orgContOpt.isPresent()) {

			// get existing container assignment
			Optional<OrgViewItem> ovItemOpt = orgViewItemRepository.findByviewNameAndiNum(viewName, empView.getInum());
			final OrgViewItem ovItem;
			if (ovItemOpt.isPresent()) {
				// create one
				ovItem = ovItemOpt.get();
				ovItem.setContainerId(orgContOpt.get().getId());
			} else {
				ovItem = new OrgViewItem(viewName, empView.getInum(), orgContOpt.get().getId());
			}
			// update it
			// save it
			orgViewItemRepository.save(ovItem);
			return RespStatus.OK;
		}

		return new RespStatus(987, "container not found");

	}

	public List<WebViewUpdate> getViews() {
		List<WebViewUpdate> emps = ImmutableList.copyOf(orgViewRepository.findAll()).stream()
				.map(orgView -> new WebViewUpdate(orgView)).collect(Collectors.toList());
		return emps;
	}

	public List<WebUpdateContainer> getContainers(String viewName, ViewByType viewBy) {
		
		final List<WebUpdateContainer> emps;
		if (viewBy==ViewByType.ViewByTeam) {
			emps = orgContainerRepository
					.findByViewTeamSorted(viewName).stream().map(orgCont -> new WebUpdateContainer(orgCont.getId(),
							viewName, orgCont.getDeptName(), orgCont.getGroupName(), orgCont.getTeamName(), 0, ""))
					.collect(Collectors.toList());
		}else {
			emps = orgContainerRepository
					.findByViewDeptSorted(viewName).stream().map(orgCont -> new WebUpdateContainer(orgCont.getId(),
							viewName, orgCont.getDeptName(), orgCont.getGroupName(), orgCont.getTeamName(), 0, ""))
					.collect(Collectors.toList());
		}
		return emps;
	}

	public RespStatus addContainer(WebUpdateContainer upd) {
		// upd.setViewName(CoreDAO.DEFAULT_VIEW);
		// should have id = 0
		// see if it already exists
		Optional<OrgContainer> orgContOpt = orgContainerRepository.findByAll(upd.getViewName(), upd.getDeptName(),
				upd.getGroupName(), upd.getTeamName());
		// if not add it
		if (!orgContOpt.isPresent()) {
			orgContainerRepository.save(new OrgContainer(upd));
			return RespStatus.OK;
		}
		return new RespStatus(987, "container exists");
	}

	public RespStatus updateContainer(WebUpdateContainer upd) {
		Optional<OrgContainer> orgContOpt = orgContainerRepository.findById(upd.getId());
		if (orgContOpt.isPresent()) {
			if (orgContOpt.get().updateDetails(upd)) {
				orgContainerRepository.save(orgContOpt.get());
			}
			return RespStatus.OK;
		}
		return new RespStatus(987, "container id not found");
	}

	public RespStatus addView(WebViewUpdate upd) {
		// should have id = 0
		// see if it already exists
		Optional<OrgView> orgContOpt = orgViewRepository.findById(upd.getName());
		// if not add it
		if (!orgContOpt.isPresent()) {
			orgViewRepository.save(new OrgView(upd));
			return RespStatus.OK;
		}
		return new RespStatus(987, "view exists");
	}

	public RespStatus updateView(WebViewUpdate upd) {
		Optional<OrgView> orgContOpt = orgViewRepository.findById(upd.getName());
		if (orgContOpt.isPresent()) {
			if (orgContOpt.get().updateDetails(upd)) {
				orgViewRepository.save(orgContOpt.get());
			}
			return RespStatus.OK;
		}
		return new RespStatus(987, "container id not found");
	}

	public boolean validateEmail(String email) {
		return true;
	}

	public boolean validateUser(String user) {
		return true;
	}

	/************************ generate the model **********************************/
	// remove middle names and white spaces
	public String trimStrip(String str) {
		String st = str.trim();
		st = st.replace(", ", ",");
		String fname = st.substring(0, st.indexOf(",") + 1);
		String lname = st.substring(st.indexOf(",") + 1);
		if (lname.contains(" ")) {
			lname = lname.substring(0, lname.indexOf(" "));
		}
		return fname + lname;
	}

	public String trimFirstName(String str) {
		String st = str.trim();
		if (st.contains(" ")) {
			st = st.substring(0, st.indexOf(" "));
		}
		return st;
	}

	public void addToPerson(WebEmployeeView per) {
		perMap.put(USE_ID ? per.getInum() : per.getLastName() + "," + per.getFirstName(), per);
	}

	public void addToMgr(final WebEmployeeView per) {
		String mgr = per.getLinkiNum();
		List<WebEmployeeView> mList = mgrMap.get(mgr);
		if (mList == null) {
			mList = new ArrayList<WebEmployeeView>();
			mgrMap.put(mgr, mList);
		}
		mList.add(per);
	}

	public void addContainerTeamView(GoJSData goData, final WebEmployeeView per) {

		String fqdn = per.getFQDN(true);
		if (fqdn.isEmpty()) // FQDN can be dept|group|team - should create dept & grp & team
			return;
		fqdn = per.getTeamName();
		Long team = containerBox.get(fqdn);
		if (team == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getTeamName(), "", "", "blue", true, Long.getLong(""));
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			team = gsn.getKey();
		}
		fqdn = fqdn + FIELD_DELIM + per.getGroupName();
		Long grp = containerBox.get(fqdn);
		if (grp == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getGroupName(), "", "", "green", true, team);
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			grp = gsn.getKey();
		}
		fqdn = fqdn + FIELD_DELIM + per.getDeptName();
		Long dept = containerBox.get(fqdn);
		if (dept == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getDeptName(), "", "", "purple", true, grp);
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			dept = gsn.getKey();
		}

		per.setCid(dept);
	}

	public void addContainerDeptView(GoJSData goData, final WebEmployeeView per) {
		if (per.getInum().equalsIgnoreCase("33294")) {
			log.info("debug");
		}

		String fqdn = per.getFQDN(false);
		if (fqdn.isEmpty()) // FQDN can be dept|group|team - should create dept & grp & team
			return;
		fqdn = per.getDeptName();
		Long dept = containerBox.get(fqdn);
		if (dept == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getDeptName(), "", "", "orange", true, Long.getLong(""));
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			dept = gsn.getKey();
		}
		fqdn = fqdn + FIELD_DELIM + per.getGroupName();
		Long grp = containerBox.get(fqdn);
		if (grp == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getGroupName(), "", "", "green", true, dept);
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			grp = gsn.getKey();
		}
		fqdn = fqdn + FIELD_DELIM + per.getTeamName();
		Long team = containerBox.get(fqdn);
		if (team == null) {
			// create and add container
			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getTeamName(), "", "", "blue", true, grp);
			goData.getNodedata().add(gsn);
			containerBox.put(fqdn, gsn.getKey());
			// s group
			team = gsn.getKey();
		}

		per.setCid(team);
	}

	public void addPeople(List<GoJSNodeData> ndata, WebEmployeeView per) {
		// print this person and subords, then recurs subords
		if (!per.getInum().isBlank()) {
			long inum = Integer.parseInt(per.getInum());
			String color = per.isLeaver() ? "lightgrey" : per.isContractor() ? "lightblue" : "lightgreen";
			String title = per.isContractor() ? per.getVendor() : per.getJobTitle();
			ndata.add(new GoJSNodeData(inum, per.getDetails(), title, per.getCity(), color, false, per.getCid()));

//			for (WebEmployeeView p : per.subord) {
//				recursePeople(ndata, p);
//			}
		}
	}

	public void recursePeople(StringBuffer sb, WebEmployeeView per) {
		// print this person and subords, then recurs subords
		sb.append(per.getPrintFmt() + CRLF);
//		addTeamBox(per);
		for (WebEmployeeView p : per.subord) {
			sb.append(per.getInum() + " --- " + p.getPrintFmt());
		}
		for (WebEmployeeView p : per.subord) {
//			addTeamBox(p);
			recursePeople(sb, p);
		}
	}

//	public StringBuffer displayTeams() {
//		StringBuffer sb = new StringBuffer();
//		for (java.util.Map.Entry<String, List<WebEmployeeView>> iter : teamBox.entrySet()) {
//			String teamName = iter.getKey();
//			sb.append("subgraph " + teamName);
//			for (WebEmployeeView p : iter.getValue()) {
//				sb.append(p.getInum() + CRLF);
//			}
//			sb.append("end" + CRLF);
//		}
//		log.info("Done");
//		return sb;
//	}

	public WebEmployeeView getPerson(String mgrName) {
		WebEmployeeView mgr = perMap.get(mgrName);
		if (mgr == null) {
			// log.info("manager person Not found "+mgrName);
			// declare manager with team members...
			mgr = WebEmployeeView.loadAnon(mgrName);
		}
		return mgr;
	}

	public void loadMaps(GoJSData gojsdata, final String view, final String dept, boolean showLeavers,
			ViewByType vType) {
		List<WebEmployeeView> webview = getViewData(view, dept, showLeavers, vType);
		for (WebEmployeeView emp : webview) {

			// if ( (!USE_FILT) || (USE_FILT && !emp.getDeptName().trim().isEmpty() ) ) {
			emp.setFirstName(trimFirstName(emp.getFirstName()));
			addToPerson(emp);
			addToMgr(emp);
			if (ViewByType.ViewByTeam==vType)
				addContainerTeamView(gojsdata, emp);
			else
				addContainerDeptView(gojsdata, emp);
			// }
		}
	}

	public GoJSData genModelGoJS(String view, String dept, boolean links, boolean leavers, ViewByType vType) {

		INT_CTR = 10000;
		mgrMap = new HashMap<String, List<WebEmployeeView>>();
		perMap = new HashMap<String, WebEmployeeView>();
		containerBox = new HashMap<String, Long>();

		GoJSData gojs = new GoJSData();
		loadMaps(gojs, view, dept, leavers, vType);
		buildOrg(); // populate subords

		if (links) {
			link(gojs.getLinkdata());
		}
		// build groups / declare

		// WebEmployeeView per = getPerson(JoeLev);
		for (WebEmployeeView iter : perMap.values()) {
			addPeople(gojs.getNodedata(), iter);
		}
		return gojs;
	}

//	public StringBuffer genModelMermaid(String inum) {
//		final String RobAntczak = "23105";
//		final String Deepak = "31493";
//		final String IanLeggett = "34976";
//
//		loadMaps();
//
//		StringBuffer sb = new StringBuffer();
//		link(sb);
//		buildOrg();
//
//		WebEmployeeView per = getPerson(Deepak);
//		recursePeople(sb, per);
//		return sb;
//		// displayTeams();
//	}

	public void buildOrg() {
		log.info("Building");
		// for every person, add yourself to the internal manager list
		for (WebEmployeeView iter : perMap.values()) {
			WebEmployeeView mgr = perMap.get(iter.getLinkiNum());
			if (mgr != null)
				mgr.addSubordinate(iter);
		}
	}

	public void link(List<GoJSLinkData> linkData) {

		for (WebEmployeeView per : perMap.values()) {
			// for each emp, get sub ord and link to this
			if (!per.getInum().isBlank()) {
				long bossNum = Integer.parseInt(per.getInum());
				for (WebEmployeeView subo : per.subord) {
					long subNum = Integer.parseInt(subo.getInum());
					linkData.add(new GoJSLinkData(subNum, bossNum, "black"));
				}
			}

		}

	}

	/// if ( !USE_FILT || USE_FILT && (per.getTeamByOrg().equalsIgnoreCase(FILT) ||
	/// per.getDept().toLowerCase().endsWith(FILT))) {

	public void link(StringBuffer sb) {
		// connect each person to their manager
		for (java.util.Map.Entry<String, List<WebEmployeeView>> iter : mgrMap.entrySet()) {
			// declare all magagers first
			WebEmployeeView mgr = perMap.get(iter.getKey()); // this is the manager
			if (mgr == null) {
				log.error("MGR no exits " + iter.getKey());
				mgr = getPerson(iter.getKey()); // create one
				perMap.put(mgr.getInum(), mgr);
			}
		}

		for (java.util.Map.Entry<String, List<WebEmployeeView>> iter : mgrMap.entrySet()) {

			WebEmployeeView mgr = getPerson(iter.getKey());
			// if (mgr.getFirstName().toLowerCase().startsWith("deepak")) {

			if (!mgrDecl.containsKey(iter.getKey())) {
				mgrDecl.put(mgr.getInum(), "");
				sb.append(mgr.getPrintFmt() + CRLF);
			}
			for (WebEmployeeView p : iter.getValue()) {
				if (!USE_FILT
//						|| (USE_FILT 
//						&& (p.getDept().toLowerCase().endsWith(FILT)
//						|| p.getTeamByOrg().toLowerCase().endsWith(FILT)))
				) {
					sb.append(mgr.getInum() + " --- " + p.getPrintFmt() + CRLF);
				}
			}
			// }
		}

		// now do the dept & team boxes,
//
//		// first do team boxes
//		for (java.util.Map.Entry<String, List<WebEmployeeView>> iter : teamBox.entrySet()) {
//			String teamName = iter.getKey();
//			sb.append("subgraph " + teamName + CRLF);
//			for (WebEmployeeView p : iter.getValue()) {
//				sb.append(p.getInum() + CRLF);
//			}
//			sb.append("end");
//		}
//		for (java.util.Map.Entry<String, List<Person>> iter : deptBox.entrySet()) {
//			String deptName = iter.getKey();
//			System.out.println("subgraph " + deptName);
//			for (Person p : iter.getValue()) {
//				System.out.println(p.getId());
//			}
//			System.out.println("end");
//		}

	}

}
