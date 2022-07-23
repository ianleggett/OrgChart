package webtool.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class Person {
	
	static final String BLANK = "";
	static final String LV_LEAVER_STYLE = " fill:#f9f,stroke:#333,stroke-width:px";
	static final String LV_CONTR_STYLE = " fill:#ffdddd,stroke:#333,stroke-width:px";
	static int CTR = 1000;
	static final String HAS_LEFT_CO = "left";
	
	String id;
	
	String leaver;
	String iNum;
	String firstName;
	String lastName;
	String city;
	String contrType; // contract type
	
	String teamByOrg;
	String descr;
	
	String jobCat;
	String jobTitle;
	String started;
	Person mgr;
	String mgriNum;
	String geoReg;
	String email;
	
	String dept;
	String teamName;
	String vendor;
	
	String mgrName;
	public List<Person> subord = new ArrayList<Person>();
	
	
	public void addSubordinate(Person per) {
		if (!subord.contains(per))
			subord.add(per);
	}
	
	public boolean hasLeft() {
		return leaver.toLowerCase().contains(HAS_LEFT_CO);
	}
	
	public static Person load(CSVRecord rec) {
		Person per = new Person();
		per.id = "A"+CTR;
		CTR++;
		per.setLeaver(rec.get(0));
		per.setiNum(rec.get("ID"));
		per.setFirstName(rec.get("First Name"));
		per.setLastName(rec.get("Last"));
		per.setCity(rec.get("City Descr"));
		per.setDescr(rec.get("Descr"));
		
		per.setContrType(rec.get("Contract Type"));
		per.setJobCat(rec.get("Job Category"));
		per.setJobTitle(rec.get("Job Title").replace("(", "").replace(")",""));	
		
		per.setStarted(rec.get("Service Dt"));
		per.setTeamByOrg(rec.get("Team by org chart (level1)"));
		
		
		//per.setMgr(rec.get("Service Dt"));
		per.setGeoReg(rec.get("Geo Reg"));
		per.setEmail(rec.get("Email ID"));
		per.setDept(rec.get("Department (Level 2)"));
		per.setTeamName(rec.get("Team Name (Level 3)"));
		per.setVendor(rec.get("Vendor"));
		per.setMgrName(rec.get("Mgr Name"));
		per.setMgriNum(rec.get("Mgr ID"));
		
		return per;
	}
	
	public static Person loadAnon(String nameMissing) {
		Person per = new Person();
		per.id = "A"+CTR;
		CTR++;
		per.setLeaver(BLANK);
		per.setiNum(BLANK);
		per.setFirstName(nameMissing);
		per.setLastName(BLANK);
		per.setCity(BLANK);
		per.setDescr(BLANK);
		per.setJobCat(BLANK);
		per.setJobTitle(BLANK);	
		per.setContrType(BLANK);
		per.setStarted(BLANK);
		per.setTeamByOrg(BLANK);
		
		//per.setMgr(rec.get("Service Dt"));
		per.setGeoReg(BLANK);
		per.setEmail(BLANK);
		per.setDept(BLANK);
		per.setTeamName(BLANK);
		per.setVendor(BLANK);
		per.setMgrName(BLANK);
		per.setMgriNum(BLANK);
		
		return per;
	}
	
	public String getPrintFmt() {
		
		String str = getId()+"(" +getFirstName()+" "+getLastName()+ "<br/>"+ 
				getJobTitle()+ ")" ;
		
		if (isLeaver()) {
			str += "\nstyle " + getId() +  LV_LEAVER_STYLE;
		}else if (isContractor()) {
			str += "\nstyle " + getId() +  LV_CONTR_STYLE;
		}
				
		
		return str;
		
		//		getDept() + "<br/>" +
		//		getTeamName() + ")";
	}
	
	public boolean isContractor() {
		return this.contrType.toLowerCase().startsWith("contr");
	}
	
	public boolean isLeaver() {
		return this.leaver.toLowerCase().startsWith("y");
	}
	
	public String getMgriNum() {
		return mgriNum;
	}

	public void setMgriNum(String mgriNum) {
		this.mgriNum = mgriNum;
	}

	public String getiNum() {
		return iNum;
	}

	public void setiNum(String iNum) {
		this.iNum = iNum;
	}

	public String getContrType() {
		return contrType;
	}

	public void setContrType(String contrType) {
		this.contrType = contrType;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getLeaver() {
		return leaver;
	}

	public void setLeaver(String leaver) {
		this.leaver = leaver;
	}

	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTeamByOrg() {
		return teamByOrg;
	}
	public void setTeamByOrg(String teamByOrg) {
		this.teamByOrg = teamByOrg;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getJobCat() {
		return jobCat;
	}
	public void setJobCat(String jobCat) {
		this.jobCat = jobCat;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getStarted() {
		return started;
	}


	public void setStarted(String started) {
		this.started = started;
	}


	public Person getMgr() {
		return mgr;
	}
	public void setMgr(Person mgr) {
		this.mgr = mgr;
	}
	public String getGeoReg() {
		return geoReg;
	}
	public void setGeoReg(String geoReg) {
		this.geoReg = geoReg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", city=" + city + ", teamByOrg="
				+ teamByOrg + ", descr=" + descr + ", jobCat=" + jobCat + ", jobTitle=" + jobTitle + ", started="
				+ started + ", mgr=" + mgr + ", geoReg=" + geoReg + ", email=" + email + ", dept=" + dept
				+ ", teamName=" + teamName + ", vendor=" + vendor + ", mgrName=" + mgrName + "]";
	}


	
}
