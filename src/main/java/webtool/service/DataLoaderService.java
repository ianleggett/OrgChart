package webtool.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import webtool.pojo.Employee;
import webtool.pojo.TeamCSV;
import webtool.pojo.OrgContainer;
import webtool.pojo.OrgView;
import webtool.pojo.OrgViewItem;
import webtool.pojo.Person;
import webtool.pojo.RespStatus;
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

	static final String EMPL_FILE = "/home/ian/Downloads/PDT_PA_Teams_Mapping_Line Managers.csv";
	static final String GRP_FILE = "/home/ian/Downloads/PDT-default-group.csv";
	// static final String FILE = "/Users/i34976/Downloads/PDT_test.csv";

	public static final List<String> STAFF_HDR = List.of("Leaver/Active", "ID", "First Name", "Last Name", "City Descr",
			"Descr", "Contract Type", "Job Category", "Job Title", "Service Dt", "Geographic Reg", "Email ID", "Vendor",
			"Mgr Name", "Mgr ID", "Function", "Team");
	public static final List<String> GROUP_HDR = List.of("ID", "team-name");
	// Team by org chart (level1) ,Department (Level 2), Team Name (Level 3)

	public static final List<String> EXPORT_HDR = List.of("Leaver/Active", "ID", "First Name", "Last Name", "City Descr",
			"Descr", "Contract Type", "Job Category", "Job Title", "Service Dt", "Geographic Reg", "Email ID", "Vendor",
			"Mgr Name", "Mgr ID", "group", "dept", "team-name");

	boolean inprogress = false;
	StringBuilder sb = new StringBuilder();

	public boolean isInProgress() {
		return inprogress;
	}

	public String getStatus() {
		return sb.toString();
	}

	public synchronized RespStatus uploadStaff(final Path destFile) {

		if (isInProgress()) {
			return new RespStatus(999, "Upload is already in progress");
		}

		Runnable thisRun = new Runnable() {
			@Override
			public void run() {
				try {
					inprogress = true;
					sb = new StringBuilder();
					sb.append("<b>Importing Staff</b><br/>");
					if (destFile != null) {
						sb.append("Checking data integrity..");
						OrgView defView = getDefaultView();						
						if (defView != null) {
							sb.append("OK<br/>");
							RespStatus res = validateCSV(destFile, STAFF_HDR); 
							if (res.getStatusCode()==0) {
								sb.append(res.getMsg());
								sb.append("Clearing employee data...");
								employeeRepository.deleteAll();
								sb.append("OK<BR/>");
								sb.append("Reading employee data...");
								final long prodCount = loadStaff(destFile);
								sb.append("OK, loaded " + prodCount + " <br/>");
								sb.append("Complete !! <br/>");
							}else {
								sb.append(res.getMsg());	
							}
						} else {
							sb.append("FAIL<br/>Default view does not exist");
						}
					} else {
						sb.append("FAIL<br/>Problem with uploaded file");
					}
				} catch (Exception e) {
					sb.append("FAIL<br/>Problem with uploaded file");
				}
				inprogress = false;
			}
		};

		Thread t = new Thread(thisRun);
		t.start();
		return RespStatus.OK;
	}

	public synchronized RespStatus uploadGroup(final Path destFile, final String viewName) {

		if (isInProgress()) {
			return new RespStatus(999, "Upload is already in progress");
		}

		Runnable thisRun = new Runnable() {
			@Override
			public void run() {
				inprogress = true;
				sb = new StringBuilder();
				sb.append("<b>Import Group data</b><br/>");
				if (destFile != null) {
					sb.append("Checking data integrity..");

					sb.append("OK<br/>");
					sb.append(validateCSV(destFile, GROUP_HDR));
					sb.append("Clearing existing groups...");

					sb.append("OK<BR/>");
					sb.append("Reading Group data...");
					final long prodCount = loadGroupData(viewName, destFile, sb);
					sb.append("OK, loaded " + prodCount + " <br/>");
					sb.append("Complete !! <br/>");

				} else {
					sb.append("FAIL<br/>Problem with uploaded file");
				}

				inprogress = false;
			}
		};

		Thread t = new Thread(thisRun);
		t.start();
		return RespStatus.OK;
	}
//	public void loadCSV(String args[]) throws IOException {
//
//		load();
//	}

	public void initDefaultView() {
		// check if defaultview exists,if not create this
		Optional<OrgView> defView = orgViewRepository.findById(CoreDAO.DEFAULT_VIEW);
		if (!defView.isPresent()) {
			orgViewRepository.save(new OrgView(CoreDAO.DEFAULT_VIEW, "Default Org view", true));
		}
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

	public OrgView getDefaultView() {
		log.info("loading");
		Optional<OrgView> defaultViewOpt = orgViewRepository.findById(CoreDAO.DEFAULT_VIEW);
		if (!defaultViewOpt.isPresent()) {
			log.error("no default view found");
			return null;
		}
		return defaultViewOpt.get();
	}

	public RespStatus validateCSV(final Path destFile, List<String> hdr) {
		try {

			Reader in = new FileReader(destFile.toString());

			Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

			log.info("Check header data exists....");
			int count = 0;

			Iterator<CSVRecord> iter = records.iterator();

			CSVRecord firstRec = iter.next();
			for (String str : hdr) {
				String r = firstRec.get(str);
				if (r == null)
					return new RespStatus(99,"Missing field " + str);
			}
			while (iter.hasNext()) {
				firstRec = iter.next();
				count++;
			}
			return new RespStatus(0,"Found " + count + "records <BR/>");
		} catch (Exception e) {
			log.error(e);
			return new RespStatus(999,e.getMessage());
		}
	}

	public long loadStaff(final Path destFile) {

		Map<String, Employee> empMap = new HashMap<String, Employee>();
		try {
			log.info("loading");

			Reader in = new FileReader(destFile.toString());

			Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			log.info("reading employee data...");
			List<Person> perList = new ArrayList<Person>(); // create local store

			for (CSVRecord rec : records) {
				Person per = Person.load(rec);
				// dont add leavers
				if (!per.isLeaver()) {
					perList.add(per);
					Employee emp = addOrUpdateEmployee(per);
					empMap.put(emp.getInum(), emp);
			   }
			}

			// need to create both container and link info
			// load containers for this view

			for (Person per : perList) {
				final Employee empTo = empMap.get(per.getMgriNum());
				final Employee empFrom = empMap.get(per.getiNum());

//				if ((empFrom != null) && (empTo != null))
//					if (!empFrom.getInum().isEmpty() && !empTo.getInum().isEmpty()) {
//						OrgViewItem saveOrgItem;
//						// find this employee entry for this view (if any)
//						List<OrgViewItem> oviList = orgViewItemRepository.findByviewNameAndiNum(CoreDAO.DEFAULT_VIEW,
//								per.getiNum());
//						if (!oviOpt.isPresent()) {
//							// create
//							saveOrgItem = orgViewItemRepository.save(new OrgViewItem(CoreDAO.DEFAULT_VIEW,
//									per.getiNum(), "reportsTo", per.getMgriNum()));
//						} else {
//							saveOrgItem = oviOpt.get();
//							// update
//							saveOrgItem.setLinkiNum(per.getMgriNum());
//							saveOrgItem = orgViewItemRepository.save(saveOrgItem);
//						}
//					}
			}
			return perList.size();
		} catch (Exception e) {
			log.error("Problem", e);
		}
		return 0;
	}

	public long loadGroupData(final String viewname, final Path destFile, StringBuilder sb) {

		try {
			log.info("loading Group/view data");
			sb.append("loading Group/view data<br/>");

			Optional<OrgView> viewOpt = orgViewRepository.findById(viewname);
			if (!viewOpt.isPresent()) {
				log.error("no view found :" + viewname);
				sb.append("Error - no view found:" + viewname);
				return 0;
			}
			OrgView thisView = viewOpt.get();

			// for default view
			Reader in = new FileReader(destFile.toString());

			List<OrgContainer> orgContainers = orgContainerRepository.findByViewName(viewname);
			Map<String, OrgContainer> contMap = orgContainers.stream()
					.collect(Collectors.toMap(OrgContainer::getTeamName, it -> it));

			// get th complete list of emps
			List<Employee> empList = ImmutableList.copyOf(employeeRepository.findAll());
			Map<String, Employee> empMap = empList.stream().collect(Collectors.toMap(Employee::getInum, it -> it));

			Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			log.info("reading group data...");
			long itemCount = 0;
			for (CSVRecord rec : records) {
				TeamCSV grpItem = TeamCSV.load(rec);
				// is this emp already in the category?
				Employee emp = empMap.get(grpItem.getiNum());
				if (emp!=null) {
				for (String tm : grpItem.getTeamName()) {
					// find the fun/grp & team exists in container ??
					String fqdn = tm.isBlank() ? "-not-set-" : tm.trim();
					// does this container exist??
					OrgContainer oc = contMap.get(fqdn);
					if (oc == null) {
						oc = orgContainerRepository.save(new OrgContainer(viewname, fqdn));
						contMap.put(fqdn,oc);
					}

					List<OrgViewItem> oviList = orgViewItemRepository.findByviewNameAndiNum(viewname,
							grpItem.getiNum());

					// remove existing mappings? or just add to them???
					boolean alreadyInContainer = false;
					for (OrgViewItem iter : oviList) {
						if (iter.getContainerId() == oc.getId())
							alreadyInContainer = true;
					}
					if (!alreadyInContainer) {
						OrgViewItem saveOrgItem = orgViewItemRepository
								.save(new OrgViewItem(viewname, grpItem.getiNum(), oc.getId()));
						saveOrgItem.setContainerId(oc.getId());
						saveOrgItem = orgViewItemRepository.save(saveOrgItem);
						log.info("Adding " + emp.getInum());
					}
				}
				}
				//orgViewRepository.save(thisView);
			}
			log.info("Finished");
			return itemCount;
		} catch (Exception e) {
			log.error("Problem", e);
		}
		return 0;
	}

}
