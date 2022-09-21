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
@Table(name = "ocontainer", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "viewName", "teamName" }) })
public class OrgContainer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_generator")
	@SequenceGenerator(name = "c_generator", sequenceName = "c_seq", initialValue = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	String viewName;
	String teamName;
	String teamDesc;

	public OrgContainer(WebUpdateContainer upd) {
		super();
		this.id = upd.getId();
		this.viewName = upd.getViewName();
		this.teamName = upd.getTeamName();
		this.teamDesc = upd.getTeamDesc();
	}

	public boolean updateDetails(WebUpdateContainer upd) {
		if (this.id == upd.getId()) {
			this.viewName = upd.getViewName();
			this.teamName = upd.getTeamName();
			this.teamDesc = upd.getTeamDesc();
			return true;
		}
		return false;
	}

	public OrgContainer() {
		super();
	}

	public OrgContainer(String viewName, String teamName) {
		super();
		this.viewName = viewName;
		this.teamName = teamName;
		this.teamDesc = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getTeamDesc() {
		return teamDesc;
	}

	public void setTeamDesc(String teamDesc) {
		this.teamDesc = teamDesc;
	}


}
