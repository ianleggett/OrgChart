package webtool.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import webtool.utils.CoreDAO;

/**
 * Represents the table data for an employye / view
 * @author ian
 *
 */
public class WebEmployeeView {

	static int CTR = 1000;
	static final String BLANK = "";
	static final String DELIM = ";";
	static final String LV_LEAVER_STYLE = " fill:#f9f,stroke:#333,stroke-width:px";
	static final String LV_CONTR_STYLE = " fill:#ffdddd,stroke:#333,stroke-width:px";
	
	String inum;// unique index
	
	String firstName;
	String lastName;
	String city;
	String contrType; // contract type
	
	String descr;
	
	String jobCat;
	String jobTitle;
	String started;
	Boolean leaver;
	
	String geoReg;
	String email;
	
	String vendor;	

	// orgviewItem stuff
	String role;  // this role
	String linkName;  // link type
	String linkValue;  // reports to
	String linkiNum;  // reports to

	///// old container data
	String deptName;  // group
	String groupName; // function
	
	List<String> teams = new ArrayList<String>();
	List<Long> cids = new ArrayList<Long>();
	
	/**** container stuff ****/
//	long cid;        // is this used???
//	String teamName;
	
	public List<WebEmployeeView> subord = new ArrayList<WebEmployeeView>();
	
	public void addContainer(final OrgContainer oc) {
		teams.add(oc.getTeamName());
	//	cids.add(oc.getId());
	}
		
	public void addCid(Long cid) {
		cids.add(cid);
	}
	
	public List<String> getFQDNs(boolean teamDept) {
		// use the team + func + grp
		return teams.stream().map(team -> team+this.getGroupName()+CoreDAO.FIELD_DELIM+this.getDeptName()).collect(Collectors.toList());
	}
	
	public String getDelimitedTeams() {
		StringBuilder sb = new StringBuilder();
		for(String t : teams) {
			sb.append(t+DELIM);
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public List<String> getTeams() {
		return teams;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public void addSubordinate(WebEmployeeView per) {
		if (!subord.contains(per))
			subord.add(per);
	}
	
	public static WebEmployeeView loadAnon(String nameMissing) {
		WebEmployeeView per = new WebEmployeeView();
		per.inum = "99"+CTR;
		CTR++;
		per.setFirstName(nameMissing);
		per.setLastName(BLANK);
		per.setCity(BLANK);
		per.setDescr(BLANK);
		per.setJobCat(BLANK);
		per.setJobTitle(BLANK);	
		per.setContrType(BLANK);
		per.setStarted(BLANK);
		
		//per.setMgr(rec.get("Service Dt"));
		per.setGeoReg(BLANK);
		per.setEmail(BLANK);
		per.setVendor(BLANK);
		per.setRole(BLANK);
		per.setLinkiNum(BLANK);
		
		return per;
	}
	/**
	 * For exporting a record to CSV
	 * @return
	 */
	public List<String> getExportValues(){
		List<String> strList = new ArrayList<String>();
		strList.add(this.leaver ? "Leaver" : "Active");
		strList.add(this.inum);
		strList.add(this.firstName);
		strList.add(this.lastName);
		strList.add(this.city);
		strList.add(this.descr);
		strList.add(this.contrType);
		strList.add(this.jobCat);
		strList.add(this.jobTitle);
		strList.add(this.started);
		strList.add(this.geoReg);
		strList.add(this.email);
		strList.add(this.vendor);
		strList.add(this.linkName);
		strList.add(this.linkiNum);
		strList.add(this.groupName);
		strList.add(this.deptName);
		strList.add(getDelimitedTeams());		
		return strList;
	}
	
	public boolean isContractor() {
		return this.contrType.toLowerCase().startsWith("contr");
	}
	
	public boolean isLeaver() {
		return this.leaver;
	}
	
	public String getDetails() {
		return getFirstName()+" "+getLastName();
	}
	
	public String getPrintFmt() {
		
		String str = getInum()+"(" +getFirstName()+" "+getLastName()+ "<br/>"+ 
				getJobTitle()+ ")" ;
		
		if (isLeaver()) {
			str += "\nstyle " + getInum() +  LV_LEAVER_STYLE;
		}else if (isContractor()) {
			str += "\nstyle " + getInum() +  LV_CONTR_STYLE;
		}

		return str;
		
		//		getDept() + "<br/>" +
		//		getTeamName() + ")";
	}
	
	public void setEmployee(Employee emp) {
		this.inum = emp.inum;
		this.firstName = emp.firstName;
		this.lastName = emp.lastName;
		this.city = emp.city;
		this.contrType = emp.contrType;
		this.descr = emp.descr;
		this.jobCat = emp.jobCat;
		this.jobTitle = emp.jobTitle;
		this.started = emp.started;
		this.geoReg = emp.geoReg;
		this.email = emp.email;
		this.vendor = emp.vendor;
		this.leaver = emp.leaver;
		this.groupName = emp.grp;
		this.deptName = emp.func;
	}
	
//	public void setContainer(OrgContainer oCont) {
//	//	this.cid =  oCont.getId();
//		this.deptName =  oCont.getDeptName();
//		this.groupName = oCont.getGroupName();
//		this.teamName = oCont.getTeamName();
//	}
	
	public void setOrgViewItem(OrgViewItem ovi) {
		this.role = ovi.getRole();
		this.linkiNum = ovi.getLinkiNum();
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}

	public String getLinkiNum() {
		return linkiNum;
	}

	public void setLinkiNum(String linkiNum) {
		this.linkiNum = linkiNum;
	}

	public String getInum() {
		return inum;
	}
	public void setInum(String inum) {
		this.inum = inum;
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
	public String getContrType() {
		return contrType;
	}
	public void setContrType(String contrType) {
		this.contrType = contrType;
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
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Boolean getLeaver() {
		return leaver;
	}

	public void setLeaver(Boolean leaver) {
		this.leaver = leaver;
	}

	public List<Long> getCids() {
		return cids;
	}

	public void setCids(List<Long> cids) {
		this.cids = cids;
	}


	
}
