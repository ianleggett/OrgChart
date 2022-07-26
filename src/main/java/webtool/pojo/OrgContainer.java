package webtool.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ocontainer",uniqueConstraints = { @UniqueConstraint(columnNames = { "viewName", "deptName", "groupName","teamName"  }) })
public class OrgContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_generator")
    @SequenceGenerator(name="c_generator", sequenceName = "c_seq",initialValue = 1)
    @Column(name = "id", updatable = false, nullable = false)   
    private long id;
    
    String viewName;
    String deptName;
	String groupName;
	String teamName;
	
//	@OneToMany(targetEntity=Employee.class, fetch=FetchType.EAGER,cascade = CascadeType.ALL)		
//	List<Employee> ipeople = new ArrayList<Employee>();
//
//	public boolean addEmployee(Employee emp) {
//		for(Employee e : ipeople) {
//			if (e.getInum().equalsIgnoreCase(emp.getInum()))
//				return false; // already added
//		}
//		ipeople.add( emp );
//		return true;
//	}
	public boolean updateDetails(WebUpdateContainer upd) {
		if (this.id==upd.getId()) {
			this.deptName = upd.getDeptName();
			this.groupName = upd.getGroupName();
			this.teamName = upd.getTeamName();
			return true;
		}
		return false;
	}
	public static String getFQDN(String dept,String group,String team) {
		return dept+"_"+group+"_"+team; 
	}
	
	public String getFQDN() {
		return getFQDN(getDeptName(),getGroupName(),getTeamName());
	}

	public OrgContainer() {
		super();
	}

	public OrgContainer(String viewName, String deptName, String groupName, String teamName) {
		super();
		this.viewName = viewName;
		this.deptName = deptName;
		this.groupName = groupName;
		this.teamName = teamName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

//	public List<Employee> getIpeople() {
//		return ipeople;
//	}
//
//	public void setIpeople(List<Employee> ipeople) {
//		this.ipeople = ipeople;
//	}
	
	
	
}
