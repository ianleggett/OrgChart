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
import webtool.pojo.TableData;
import webtool.pojo.UpdateLog;
import webtool.pojo.UpdateType;
import webtool.pojo.UtilAggCount;
import webtool.pojo.ViewByType;
import webtool.pojo.WebEmployeeTeams;
import webtool.pojo.WebEmployeeView;
import webtool.pojo.WebUpdateContainer;
import webtool.pojo.WebViewUpdate;
import webtool.repository.EmployeeRepository;
import webtool.repository.OrgContainerRepository;
import webtool.repository.OrgViewItemRepository;
import webtool.repository.OrgViewRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UpdateLogRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.service.DataLoaderService;

@Component
@Scope("singleton")
public class CoreDAO {

	public static final String FIELD_DELIM = "|";
	public static final String CRLF = "\\r\\n";
	public static final String DEFAULT_VIEW = "default";
	public static final String DEFAULT_DEPT = "-all-";
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

//	@Autowired
//	private SystemStatusService systemStatusService;

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
	
	@Autowired
	UpdateLogRepository updateLogRepository;

	// private ThreadedQueueProcessor<TradeCmd> qProc;
	final ExecutorService executorService = Executors.newFixedThreadPool(1);

	@PostConstruct
	private void init() {
		initProc();
	}

	private void initProc() {
//		systemStatusService.setWorking(true);
//		systemStatusService.setNewMessage("System OK");
		dataloaderService.initDefaultView();
		// dataloaderService.load();

	}

	public DataLoaderService getDataService() {
		return dataloaderService;
	}

	public List<OrgContainer> getAggContainerData(String viewName) {		
		return orgContainerRepository.findByViewTeamSorted(viewName);
	}

	
	public List<UpdateLog> getImportLog(){
		return updateLogRepository.findAllByType( List.of(UpdateType.IMPORT_STAFF.ordinal()) );
	}
	
	public List<UpdateLog> getUpdateLog(){
		return updateLogRepository.findAllByType( List.of(UpdateType.STAFF_ADDED.ordinal(),UpdateType.STAFF_REMOVED.ordinal()) );
	}
	
	public List<UpdateLog> getMoversLog(){
		return updateLogRepository.findAllByType( List.of(UpdateType.STAFF_MOVED_FROM.ordinal(), UpdateType.STAFF_MOVED_TO.ordinal()) );
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
	
	public Map<String, Employee> getEmployeeMap(){
		List<Employee> emps = ImmutableList.copyOf(employeeRepositiory.findAll());
		return emps.stream().collect(Collectors.toMap(Employee::getInum, it -> it));
	}
	
	
	public List<WebEmployeeView> getViewData(String viewName, List<String> teamList, boolean showLeavers, ViewByType vType) {

		Map<String,String> teamMap = teamList.stream().collect(Collectors.toMap(String::toString, iter->iter));
		
		List<WebEmployeeView> result = new ArrayList<WebEmployeeView>();
		List<Employee> emps = ImmutableList.copyOf(employeeRepositiory.findAll());
		Map<String, Employee> empMap = emps.stream().collect(Collectors.toMap(Employee::getInum, it -> it));

		List<OrgContainer> orgContainers = orgContainerRepository.findByViewName(viewName);
		Map<Long, OrgContainer> contMap = orgContainers.stream()
				.collect(Collectors.toMap(OrgContainer::getId, it -> it));

		List<OrgViewItem> ovOpt = orgViewItemRepository.findByViewName(viewName); // gets multiple members of the team, need to rationalise to 1 entry per employee
		Map<String, Employee> missing = emps.stream().collect(Collectors.toMap(Employee::getInum, it -> it));
		OrgContainer ORPH_CONT = new OrgContainer(viewName,"-orphan-team-");

		// create a map of webEmployeeView since we need to add multiple teams to it
		Map<String,WebEmployeeView> wevMap = new HashMap<String,WebEmployeeView>();
		
		for (OrgViewItem ovi : ovOpt) {
			Employee emp = empMap.get(ovi.getiNum());
			if (emp != null) {
				missing.remove(ovi.getiNum());
				if (!emp.getLeaver() || (showLeavers && emp.getLeaver())) {

					WebEmployeeView wev = wevMap.get(ovi.getiNum());
					if (wev==null) {
						wev = new WebEmployeeView();
						wev.setEmployee(emp);
						wevMap.put(ovi.getiNum(),wev);
					}
										
					OrgContainer oc = contMap.get(ovi.getContainerId());
					if (oc != null) {										
						  if (teamMap.isEmpty() || teamMap.containsKey(oc.getTeamName())){
							wev.addContainer(oc);
						    wev.setOrgViewItem(ovi);							
						}
						
					} else {
						log.error("Container does not exist " + ovi);
					}
				}
			} else {
				// save time log.error("empl does not exist " + ovi);
			}
		}

		for(WebEmployeeView wev: wevMap.values()) {			
			result.add(wev);
		}
		
		// generate orphan container
		for (Entry<String, Employee> iter : missing.entrySet()) {
			final WebEmployeeView wev = new WebEmployeeView();
			OrgViewItem ovi = new OrgViewItem(viewName, iter.getValue().getInum(), ORPH_CONT.getId());
			wev.setEmployee(iter.getValue());
			wev.setOrgViewItem(ovi);
			wev.addContainer(ORPH_CONT);
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
		
		List<WebEmployeeView> viewData = this.getViewData(view, new ArrayList<String>(), false, ViewByType.ViewByDept);
		
		csvPrinter.printRecord(DataLoaderService.EXPORT_HDR);
		for(WebEmployeeView wev : viewData) {
			csvPrinter.printRecord( wev.getExportValues() );
		}
		
		return csvPrinter;
	}
	
	/**
	 * Modifyed by the user as a complete team list
	 * @param empView
	 * @return
	 */
	public RespStatus updateStaffContainer(WebEmployeeTeams empView) {
		final String viewName = empView.getView();
		
		Optional<Employee> thisEmployee = employeeRepositiory.findById(empView.getInum());
		if (!thisEmployee.isPresent()) {
			return new RespStatus(99,"User " + empView.getInum() + " not found");
		}
		final Employee emp = thisEmployee.get();
		// get existing container assignment
		List<OrgViewItem> ovItemList = orgViewItemRepository.findByviewNameAndiNum(viewName, empView.getInum());
		Map<Long,OrgViewItem> ovContainerMap = ovItemList.stream().collect(Collectors.toMap(OrgViewItem::getContainerId, it -> it));
		Map<Long,Long> ovCidMap = empView.getCids().stream().collect(Collectors.toMap( Long::longValue ,iter -> iter ) );
		
		// remove items not in the new request
		for(OrgViewItem ovi : ovContainerMap.values()) {
			if (!ovCidMap.containsKey(ovi.getContainerId())) {
				// need to delete
				orgViewItemRepository.deleteById(ovi.getId());
				// following code is just for audit
				Optional<OrgContainer> orgContOpt = orgContainerRepository.findById(viewName, ovi.getContainerId());
				if (orgContOpt.isPresent()) {
					updateLogRepository.save(new UpdateLog(UpdateType.STAFF_MOVED_FROM, orgContOpt.get().getTeamName(), emp.getDisplayName() + " ("+empView.getInum()+")"  ));
				}				
			}
		}
		
		for (Long cid : empView.getCids()) {		
			// 1. find container id
			Optional<OrgContainer> orgContOpt = orgContainerRepository.findById(viewName, cid);
			if (orgContOpt.isPresent()) {
				OrgContainer oc = orgContOpt.get();
				// remove existing mappings? or just add to them???
				if ( !ovContainerMap.containsKey(oc.getId()) ) {
					OrgViewItem saveOrgItem = orgViewItemRepository.save(new OrgViewItem(viewName,empView.getInum(), orgContOpt.get().getId()));
					saveOrgItem.setContainerId(oc.getId());
					saveOrgItem = orgViewItemRepository.save(saveOrgItem);
					log.info("Adding " + empView.getInum());
					updateLogRepository.save(new UpdateLog(UpdateType.STAFF_MOVED_TO, orgContOpt.get().getTeamName(), emp.getDisplayName() + " ("+empView.getInum()+")" ));
				}				
			}else {
				return new RespStatus(987, "container not found");
			}
		}
		
		return RespStatus.OK;
	}

	/**
	 * Generate a list of emails based upon one or more team/container ids
	 * @param view
	 * @param cids
	 * @return
	 */
	public TableData<String> getEmailData(String view, String cids){
		
		List<String> eList = new ArrayList<String>();
		// get viewitems for container id
		Map<String,Employee> empMap = getEmployeeMap();
		List<OrgViewItem> oviList = orgViewItemRepository.findInContainer( view, cids );		
		// get inum from viewitems
		oviList.forEach( ovi -> {
			Employee emp = empMap.get(ovi.getiNum());
			if (emp!=null) eList.add( emp.getEmail() );
		});
		return new TableData<String>(eList);
	}

	public List<WebViewUpdate> getViews() {
		List<WebViewUpdate> emps = ImmutableList.copyOf(orgViewRepository.findAll()).stream()
				.map(orgView -> new WebViewUpdate(orgView)).collect(Collectors.toList());
		return emps;
	}

	public List<WebUpdateContainer> getContainers(String viewName) {
				
		final List<Object[]> aggCountObj = orgViewItemRepository.countContainerUsage(viewName);
		List<UtilAggCount> aggCount = aggCountObj.stream()
				.map(objArray -> new UtilAggCount( Long.parseLong(objArray[0].toString()),Long.parseLong(objArray[1].toString()) ))
				.collect(Collectors.toList());
		
		log.info(aggCount);
		Map<Long,UtilAggCount> ovCidMap = aggCount.stream().collect(Collectors.toMap( UtilAggCount::getCid , iter->iter) );
		
		final List<WebUpdateContainer> emps = orgContainerRepository
				.findByViewTeamSorted(viewName).stream().map(orgCont -> new WebUpdateContainer(orgCont.getId(),
						viewName, orgCont.getTeamName(),orgCont.getTeamDesc(), ovCidMap.get( orgCont.getId()) != null ? ovCidMap.get( orgCont.getId()).getCnt() : 0 , ""))
				.collect(Collectors.toList());
		return emps;

	}

	public RespStatus addContainer(WebUpdateContainer upd) {
		// upd.setViewName(CoreDAO.DEFAULT_VIEW);
		// should have id = 0
		// see if it already exists
		Optional<OrgContainer> orgContOpt = orgContainerRepository.findByAll(upd.getViewName(), upd.getTeamName());
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

//		String fqdn = per.getFQDN(true);
//		if (fqdn.isEmpty()) // FQDN can be dept|group|team - should create dept & grp & team
//			return;		

		// for each team in the list, add it !!
		for(String teamStr : per.getTeams()) {
			String fqdn = teamStr;
			Long team = containerBox.get(teamStr);
			if (team == null) {
				// create and add container
				GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, teamStr, "", "", "", "blue", true, Long.getLong(""));
				goData.getNodedata().add(gsn);
				containerBox.put(teamStr, gsn.getKey());
				// s group
				team = gsn.getKey();
			}
			fqdn = fqdn + FIELD_DELIM + per.getGroupName();
			Long grp = containerBox.get(fqdn);
			if (grp == null) {
				// create and add container
				GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getGroupName(), "", "", "", "green", true, team);
				goData.getNodedata().add(gsn);
				containerBox.put(fqdn, gsn.getKey());
				// s group
				grp = gsn.getKey();
			}
			fqdn = fqdn + FIELD_DELIM + per.getDeptName();
			Long dept = containerBox.get(fqdn);
			if (dept == null) {
				// create and add container
				GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getDeptName(), "", "", "", "purple", true, grp);
				goData.getNodedata().add(gsn);
				containerBox.put(fqdn, gsn.getKey());
				// s group
				dept = gsn.getKey();
			}
			per.addCid(dept);
		}
	}

//	public void addContainerDeptView(GoJSData goData, final WebEmployeeView per) {
//		if (per.getInum().equalsIgnoreCase("33294")) {
//			log.info("debug");
//		}
//
//		String fqdn = per.getFQDN(false);
//		if (fqdn.isEmpty()) // FQDN can be dept|group|team - should create dept & grp & team
//			return;
//		fqdn = per.getDeptName();
//		Long dept = containerBox.get(fqdn);
//		if (dept == null) {
//			// create and add container
//			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getDeptName(),"", "", "", "orange", true, Long.getLong(""));
//			goData.getNodedata().add(gsn);
//			containerBox.put(fqdn, gsn.getKey());
//			// s group
//			dept = gsn.getKey();
//		}
//		fqdn = fqdn + FIELD_DELIM + per.getGroupName();
//		Long grp = containerBox.get(fqdn);
//		if (grp == null) {
//			// create and add container
//			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getGroupName(), "", "", "", "green", true, dept);
//			goData.getNodedata().add(gsn);
//			containerBox.put(fqdn, gsn.getKey());
//			// s group
//			grp = gsn.getKey();
//		}
//		fqdn = fqdn + FIELD_DELIM + per.getTeamName();
//		Long team = containerBox.get(fqdn);
//		if (team == null) {
//			// create and add container
//			GoJSNodeData gsn = new GoJSNodeData(++INT_CTR, per.getTeamName(), "", "", "", "blue", true, grp);
//			goData.getNodedata().add(gsn);
//			containerBox.put(fqdn, gsn.getKey());
//			// s group
//			team = gsn.getKey();
//		}
//
//		per.setCid(team);
//	}

	public void addPeople(List<GoJSNodeData> ndata, WebEmployeeView per) {
		// print this person and subords, then recurs subords
		if (!per.getInum().isBlank()) {
			long inum = Integer.parseInt(per.getInum());
			String color = per.isLeaver() ? "lightgrey" : per.isContractor() ? "lightblue" : "lightgreen";
			String jobCat = per.getJobCat();
			String title = per.isContractor() ? per.getVendor() : (per.getJobTitle().isBlank() ? per.getJobCat() : per.getJobTitle());
			String city = per.getCity().isBlank() ? per.getGeoReg() : per.getCity();
			// need at add the same person per container/team
			for(Long contId : per.getCids()) {
				ndata.add(new GoJSNodeData(inum, per.getDetails(), jobCat, title, city, color, false, contId));
			}
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

	public void loadMaps(GoJSData gojsdata, final String view, final List<String> teamList, boolean showLeavers,
			ViewByType vType) {
		List<WebEmployeeView> webview = getViewData(view, teamList, showLeavers, vType);
		for (WebEmployeeView emp : webview) {

			// if ( (!USE_FILT) || (USE_FILT && !emp.getDeptName().trim().isEmpty() ) ) {
			emp.setFirstName(trimFirstName(emp.getFirstName()));
			addToPerson(emp);
			addToMgr(emp);
//			if (ViewByType.ViewByTeam==vType)
				addContainerTeamView(gojsdata, emp);
//			else
//				addContainerDeptView(gojsdata, emp);
			// }
		}
	}

	public GoJSData genModelGoJS(String view, List<String> teamList, boolean links, boolean leavers, ViewByType vType) {

		INT_CTR = 10000;
		mgrMap = new HashMap<String, List<WebEmployeeView>>();
		perMap = new HashMap<String, WebEmployeeView>();
		containerBox = new HashMap<String, Long>();

		GoJSData gojs = new GoJSData();
		loadMaps(gojs, view, teamList, leavers, vType);
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
