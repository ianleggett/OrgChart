package webtool.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtool.pojo.Employee;
import webtool.pojo.GroupCSV;
import webtool.pojo.OrgContainer;
import webtool.pojo.OrgView;
import webtool.pojo.OrgViewItem;
import webtool.pojo.Person;
import webtool.repository.EmployeeRepository;
import webtool.repository.OrgContainerRepository;
import webtool.repository.OrgViewItemRepository;
import webtool.repository.OrgViewRepository;
import webtool.utils.CoreDAO;

@Service("DataLoaderService")
public class DataLoaderService {
	static Logger log = Logger.getLogger(DataLoaderService.class);

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	OrgViewRepository orgViewRepository;
	@Autowired
	OrgViewItemRepository orgViewItemRepository;
	
	@Autowired
	OrgContainerRepository orgContainerRepository;

	static String FILT = "engineering";
	static final boolean USE_FILT = false;
	static final boolean USE_ID = true;

	static final String EMPL_FILE = "/home/ian/Downloads/PDT_PA_Teams_Mapping_Line Managers.csv";
	static final String GRP_FILE = "/home/ian/Downloads/PDT-default-group.csv";
	// static final String FILE = "/Users/i34976/Downloads/PDT_test.csv";
	Map<String, List<Person>> mgrMap = new HashMap<String, List<Person>>();
	Map<String, Person> perMap = new HashMap<String, Person>();
	Map<String, List<Person>> teamBox = new HashMap<String, List<Person>>();
	Map<String, List<Person>> deptBox = new HashMap<String, List<Person>>();

	Map<String, String> mgrDecl = new HashMap<String, String>();

	public void loadCSV(String args[]) throws IOException {

		final String RobAntczak = "23105";
		final String Deepak = "31493";
		final String IanLeggett = "34976";

		load();
		// link managers?
		// app.link();
//		buildOrg();
//		
//		// Creates a FileOutputStream
//		PrintWriter output = new PrintWriter(new FileOutputStream("deepak.txt"), true);
//		setOutput(output);
//		Person per = getPerson(Deepak);
//		recursePeople(per);					
		

//		displayTeams();
		// create a big chart

		// list people by mgr ref --- person details

	}

	PrintWriter prn;

	public PrintWriter getOutput() {
		return prn;
	}

	public void setOutput(PrintWriter output) {
		this.prn = output;
		prn.println("graph LR");
	}

	public void initDefaultView() {
		// check if defaultview exists,if not create this
		Optional<OrgView> defView = orgViewRepository.findById(CoreDAO.DEFAULT_VIEW);
		if (!defView.isPresent()) {
			orgViewRepository.save(new OrgView(CoreDAO.DEFAULT_VIEW, "Default Org view", true));
		}
	}

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

	public void addToPerson(Person per) {
		perMap.put(USE_ID ? per.getiNum() : per.getLastName() + "," + per.getFirstName(), per);
	}

	public void addToMgr(final Person per) {
		String mgr = USE_ID ? per.getMgriNum() : trimStrip(per.getMgrName());
		List<Person> mList = mgrMap.get(mgr);
		if (mList == null) {
			mList = new ArrayList<Person>();
			mgrMap.put(mgr, mList);
		}
		mList.add(per);
	}

	public Employee addOrUpdateEmployee(Person per) {
		Optional<Employee> emp = employeeRepository.findById(per.getiNum());
		if (emp.isPresent()) {
			// update
			return emp.get();
		} else {
			// insert new
			return employeeRepository.save(Employee.from(per));
		}

	}

	public void load() {

		Map<String, Employee> empMap = new HashMap<String, Employee>();
		try {
			log.info("loading");
			Optional<OrgView> defaultViewOpt = orgViewRepository.findById(CoreDAO.DEFAULT_VIEW);
			if (!defaultViewOpt.isPresent()) {
				log.error("no default view found");
				return;
			}
			OrgView defaultView = defaultViewOpt.get();

			Reader in = new FileReader(EMPL_FILE);

			Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			log.info("reading employee data...");

			List<Person> perList = new ArrayList<Person>(); // create local store

			for (CSVRecord rec : records) {
				Person per = Person.load(rec);
				perList.add(per);
				Employee emp = addOrUpdateEmployee(per);
				empMap.put(emp.getInum(), emp);
			}			
			
			// need to create both container and link info
			// load containers for this view
			List<OrgContainer> orgContainers = orgContainerRepository.findByViewName(CoreDAO.DEFAULT_VIEW);
			Map<String,OrgContainer> contMap = orgContainers.stream().collect(Collectors.toMap( OrgContainer::getFQDN, it->it) );
			
			for (Person per : perList) {
				final Employee empTo = empMap.get(per.getMgriNum());
				final Employee empFrom = empMap.get(per.getiNum());
				
				if ((empFrom != null) && (empTo != null))
					if (!empFrom.getInum().isEmpty() && !empTo.getInum().isEmpty()) {								
						OrgViewItem saveOrgItem;
						// find this employee entry for this view (if any)
						Optional<OrgViewItem> oviOpt = orgViewItemRepository.findByviewNameAndiNum(CoreDAO.DEFAULT_VIEW,per.getiNum());
						if (!oviOpt.isPresent()) {
							// create
							saveOrgItem = orgViewItemRepository.save( new OrgViewItem(CoreDAO.DEFAULT_VIEW,per.getiNum(),"reportsTo",per.getMgriNum()));							
						}else {
							saveOrgItem = oviOpt.get();
							// update
							saveOrgItem.setLinkiNum( per.getMgriNum() );
							saveOrgItem = orgViewItemRepository.save( saveOrgItem );
						}
					}
			}
			

			// for default view

			in = new FileReader(GRP_FILE);

			records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			log.info("reading group data...");
			for (CSVRecord rec : records) {
				GroupCSV grpItem = GroupCSV.load(rec);				
				OrgContainer oc = contMap.get( grpItem.getFQDN() );
				if (oc == null) {
					// category does not exist
					oc = new OrgContainer(CoreDAO.DEFAULT_VIEW, grpItem.getDept(), grpItem.getGroup(), grpItem.getTeamName());
					oc = orgContainerRepository.save(oc);
					contMap.put(oc.getFQDN(),oc);
					//defaultView = orgViewRepository.save(defaultView);
				}else {
					log.info("Found "+grpItem.getFQDN());
				}
				// is this emp already in the category?
				Employee emp = empMap.get(grpItem.getiNum());
				// check if not already there?
				if (emp.getInum().equalsIgnoreCase("33843")) {
					log.info(" look ");
				}
								
				Optional<OrgViewItem> oviOpt = orgViewItemRepository.findByviewNameAndiNum(CoreDAO.DEFAULT_VIEW,grpItem.getiNum());				
				OrgViewItem saveOrgItem;
				if (!oviOpt.isPresent()) {					
					// create
					saveOrgItem = orgViewItemRepository.save( new OrgViewItem(CoreDAO.DEFAULT_VIEW,grpItem.getiNum(),oc.getId()));							
				}else {
					saveOrgItem = oviOpt.get();
					// update
					saveOrgItem.setContainerId( oc.getId() );
					saveOrgItem = orgViewItemRepository.save( saveOrgItem );
				}
							
					log.info("Adding "+emp.getInum());				
				
			}
			orgViewRepository.save(defaultView);			
			log.info("Finished");
			// find out container, subbox
//			if (!contMap.containsKey(per.getDept()+"_"+per.getTeamByOrg())) {
//				// create the 
//			}

			// get reporting mgr inum

//			if (!per.hasLeft()) {
//				per.setFirstName(trimFirstName(per.getFirstName()));
//				addToPerson(per);
//				addToMgr(per);
//				// addDeptBox(per);
//				// addTeamBox(per);
//				// log.info(per);
//			}

		} catch (Exception e) {
			log.error("Problem", e);
		}
	}

	public Person getPerson(String mgrName) {
		Person mgr = perMap.get(mgrName);
		if (mgr == null) {
			// log.info("manager person Not found "+mgrName);
			// declare manager with team members...
			mgr = Person.loadAnon(mgrName);
		}
		return mgr;
	}

	public void buildOrg() {
		log.info("Building");
		// for every person, add yourself to the internal manager list
		for (Person iter : perMap.values()) {
			Person mgr = perMap.get(iter.getMgriNum());
			if (mgr != null)
				mgr.addSubordinate(iter);
		}
	}

	/// if ( !USE_FILT || USE_FILT && (per.getTeamByOrg().equalsIgnoreCase(FILT) ||
	/// per.getDept().toLowerCase().endsWith(FILT))) {

//	public void link() {
//		// connect each person to their manager
//		for (java.util.Map.Entry<String, List<Person>> iter : mgrMap.entrySet()) {
//			// declare all magagers first
//			Person mgr = perMap.get(iter.getKey()); // this is the manager
//			if (mgr == null) {
//				log.error("MGR no exits " + iter.getKey());
//				mgr = getPerson(iter.getKey()); // create one
//				perMap.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), mgr);
//				// System.out.println(mgr.getPrintFmt());
//				// mgrDecl.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), "");
//			}
//		}
//
//		for (java.util.Map.Entry<String, List<Person>> iter : mgrMap.entrySet()) {
//
//			Person mgr = getPerson(iter.getKey());
//			// if (mgr.getFirstName().toLowerCase().startsWith("deepak")) {
//
//			if (!mgrDecl.containsKey(iter.getKey())) {
//				mgrDecl.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), "");
//				prn.println(mgr.getPrintFmt());
//			}
//			for (Person p : iter.getValue()) {
//				if (!USE_FILT || (USE_FILT && (p.getDept().toLowerCase().endsWith(FILT)
//						|| p.getTeamByOrg().toLowerCase().endsWith(FILT)))) {
//					prn.println(mgr.getId() + " --- " + p.getPrintFmt());
//				}
//			}
//			// }
//		}
//
//		// now do the dept & team boxes,
//
//		// first do team boxes
//		for (java.util.Map.Entry<String, List<Person>> iter : teamBox.entrySet()) {
//			String teamName = iter.getKey();
//			prn.println("subgraph " + teamName);
//			for (Person p : iter.getValue()) {
//				prn.println(p.getId());
//			}
//			prn.println("end");
//		}
////		for (java.util.Map.Entry<String, List<Person>> iter : deptBox.entrySet()) {
////			String deptName = iter.getKey();
////			System.out.println("subgraph " + deptName);
////			for (Person p : iter.getValue()) {
////				System.out.println(p.getId());
////			}
////			System.out.println("end");
////		}
//
//	}

}
