package webtool.service;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import webtool.pojo.Person;



@Service("DataLoaderService")
public class DataLoaderService {
	static Logger log = Logger.getLogger(DataLoaderService.class);
	
	static String FILT = "engineering";
	static final boolean USE_FILT = false;
	static final boolean USE_ID = true;

	static final String FILE = "/Users/i34976/Downloads/PDT_PA_Teams_Mapping_Line Managers.csv";
	// static final String FILE = "/Users/i34976/Downloads/PDT_test.csv";
	Map<String, List<Person>> mgrMap = new HashMap<String, List<Person>>();
	Map<String, Person> perMap = new HashMap<String, Person>();
	Map<String, List<Person>> teamBox = new HashMap<String, List<Person>>();
	Map<String, List<Person>> deptBox = new HashMap<String, List<Person>>();

	Map<String, String> mgrDecl = new HashMap<String, String>();
	
	PrintWriter prn;

	public PrintWriter getOutput() {
		return prn;
	}

	public void setOutput(PrintWriter output) {
		this.prn = output;
		prn.println("graph LR");
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

	public void addTeamBox(final Person per) {
		if (per.getTeamName().isEmpty())
			return;
		String tname = !per.getTeamName().isEmpty() ? per.getTeamName() : "none";
		List<Person> mList = teamBox.get(tname);
		if (mList == null) {
			mList = new ArrayList<Person>();
			teamBox.put(tname, mList);
		}
		if (!mList.contains(per))
			mList.add(per);
	}

	public void addDeptBox(final Person per) {
		if (per.getDept().isEmpty())
			return;
		String tname = !per.getDept().isEmpty() ? per.getDept() : "none";
		List<Person> mList = deptBox.get(tname);
		if (mList == null) {
			mList = new ArrayList<Person>();
			deptBox.put(tname, mList);
		}
		mList.add(per);
	}

	public void load() throws IOException {
		Reader in = new FileReader(FILE);

		log.info("loading");

		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

		for (CSVRecord rec : records) {
			Person per = Person.load(rec);
			if (!per.hasLeft()) {
				per.setFirstName(trimFirstName(per.getFirstName()));
				addToPerson(per);
				addToMgr(per);
				// addDeptBox(per);
				// addTeamBox(per);
				// log.info(per);
			}
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

	public void recursePeople(Person per) {
		
		
		// print this person and subords, then recurs subords
		prn.println(per.getPrintFmt());
		addTeamBox(per);
		for (Person p : per.subord) {
			prn.println(per.getId() + " --- " + p.getPrintFmt());
		}
		for (Person p : per.subord) {
			addTeamBox(p);
			recursePeople(p);
		}
	}

	public void displayTeams() {
		for (java.util.Map.Entry<String, List<Person>> iter : teamBox.entrySet()) {
			String teamName = iter.getKey();
			prn.println("subgraph " + teamName);
			for (Person p : iter.getValue()) {
				prn.println(p.getId());
			}
			prn.println("end");
		}
		log.info("Done");
	}

	/// if ( !USE_FILT || USE_FILT && (per.getTeamByOrg().equalsIgnoreCase(FILT) ||
	/// per.getDept().toLowerCase().endsWith(FILT))) {

	public void link() {
		// connect each person to their manager
		for (java.util.Map.Entry<String, List<Person>> iter : mgrMap.entrySet()) {
			// declare all magagers first
			Person mgr = perMap.get(iter.getKey()); // this is the manager
			if (mgr == null) {
				log.error("MGR no exits " + iter.getKey());
				mgr = getPerson(iter.getKey()); // create one
				perMap.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), mgr);
				// System.out.println(mgr.getPrintFmt());
				// mgrDecl.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), "");
			}
		}

		for (java.util.Map.Entry<String, List<Person>> iter : mgrMap.entrySet()) {

			Person mgr = getPerson(iter.getKey());
	//		if (mgr.getFirstName().toLowerCase().startsWith("deepak")) {

				if (!mgrDecl.containsKey(iter.getKey())) {
					mgrDecl.put(USE_ID ? mgr.getiNum() : mgr.getFirstName(), "");
					prn.println(mgr.getPrintFmt());
				}
				for (Person p : iter.getValue()) {
					if (!USE_FILT || (USE_FILT && (p.getDept().toLowerCase().endsWith(FILT)
							|| p.getTeamByOrg().toLowerCase().endsWith(FILT)))) {
						prn.println(mgr.getId() + " --- " + p.getPrintFmt());
					}
				}
	//		}
		}

		// now do the dept & team boxes,

		// first do team boxes
		for (java.util.Map.Entry<String, List<Person>> iter : teamBox.entrySet()) {
			String teamName = iter.getKey();
			prn.println("subgraph " + teamName);
			for (Person p : iter.getValue()) {
				prn.println(p.getId());
			}
			prn.println("end");
		}
//		for (java.util.Map.Entry<String, List<Person>> iter : deptBox.entrySet()) {
//			String deptName = iter.getKey();
//			System.out.println("subgraph " + deptName);
//			for (Person p : iter.getValue()) {
//				System.out.println(p.getId());
//			}
//			System.out.println("end");
//		}

	}

	public static void main(String args[]) throws IOException {

//		final String RobAntczak = "23105";
//		final String Deepak = "31493";
//		final String IanLeggett = "34976";
//
//		Application app = new Application();
//		app.load();
//		// link managers?
//		// app.link();
//		app.buildOrg();
//		
//		// Creates a FileOutputStream
//		PrintWriter output = new PrintWriter(new FileOutputStream("deepak.txt"), true);
//		app.setOutput(output);
//		Person per = app.getPerson(Deepak);
//		app.recursePeople(per);
//		app.displayTeams();
//		// create a big chart
//
//		// list people by mgr ref --- person details

	}
	
}
