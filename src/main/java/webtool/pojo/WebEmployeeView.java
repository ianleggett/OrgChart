package webtool.pojo;


/**
 * Represents the table data for an employye / view
 * @author ian
 *
 */
public class WebEmployeeView {

	String inum;// unique index
	
	String firstName;
	String lastName;
	String city;
	String contrType; // contract type
	
	String descr;
	
	String jobCat;
	String jobTitle;
	String started;

	String geoReg;
	String email;
	
	String vendor;	

	/**** container stuff ****/
	long cid;
	String deptName;
	String groupName;
	String teamName;
	
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
	}
	
	public void setContainer(OrgContainer oCont) {
		this.cid =  oCont.getId();
		this.deptName =  oCont.getDeptName();
		this.groupName = oCont.getGroupName();
		this.teamName = oCont.getTeamName();
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
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
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
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
