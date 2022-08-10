package webtool.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "oviewitem",uniqueConstraints = { @UniqueConstraint(columnNames = { "viewName", "iNum" }) })
public class OrgViewItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "l_generator")
	@SequenceGenerator(name = "l_generator", sequenceName = "l_seq", initialValue = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	String viewName;
	String iNum;
	
	long containerId;
	
	String role;
	String linkName;
	String linkValue;
	String linkiNum;
	
	
	public OrgViewItem() {
		super();
	}
	public OrgViewItem(String viewName, String iNum, String linkName, String linkiNum) {
		super();
		this.viewName = viewName;
		this.iNum = iNum;
		this.linkName = linkName;
		this.linkiNum = linkiNum;
	}	
	
	public OrgViewItem(String viewName, String iNum, long containerId) {
		super();
		this.viewName = viewName;
		this.iNum = iNum;
		this.containerId = containerId;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public long getContainerId() {
		return containerId;
	}
	public void setContainerId(long containerId) {
		this.containerId = containerId;
	}
	public String getiNum() {
		return iNum;
	}
	public void setiNum(String iNum) {
		this.iNum = iNum;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "OrgViewItem [id=" + id + ", viewName=" + viewName + ", iNum=" + iNum + ", containerId=" + containerId
				+ ", role=" + role + ", linkName=" + linkName + ", linkValue=" + linkValue + ", linkiNum=" + linkiNum
				+ "]";
	}
	
	
}
