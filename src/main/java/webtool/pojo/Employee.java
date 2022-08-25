package webtool.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	String inum;// unique index

	String firstName;
	String lastName;
	String city;
	String contrType; // contract type

	Boolean leaver;

	String descr;

	String jobCat;
	String jobTitle;
	String started;

	String geoReg;
	String email;

	String vendor;

	public static Employee from(Person per) {
		return new Employee(per.iNum, per.firstName, per.lastName, per.city, per.contrType, per.descr, per.jobCat,
				per.jobTitle, per.started, per.geoReg, per.email, per.vendor, per.isLeaver());
	}
	

	public Employee() {
		super();
	}

	public Employee(String inum, String firstName, String lastName, String city, String contrType, String descr,
			String jobCat, String jobTitle, String started, String geoReg, String email, String vendor, Boolean leaver) {
		super();
		this.inum = inum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.contrType = contrType;
		this.descr = descr;
		this.jobCat = jobCat;
		this.jobTitle = jobTitle;
		this.started = started;
		this.geoReg = geoReg;
		this.email = email;
		this.vendor = vendor;
		this.leaver = leaver;
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

	public Boolean getLeaver() {
		return leaver;
	}

	public void setLeaver(Boolean leaver) {
		this.leaver = leaver;
	}



}
