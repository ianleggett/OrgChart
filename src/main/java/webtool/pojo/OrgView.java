package webtool.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "oview")
public class OrgView {

    @Id	
	String name;   // default org view is read only
	String descr;
	Boolean readOnly;
	
	public OrgView(WebViewUpdate wvu) {
		super();
		this.name = wvu.name;
		this.descr = wvu.descr;
		this.readOnly = wvu.readOnly;
	}
	
	public OrgView() {
		super();
	}
	public OrgView(String name, String descr, Boolean readOnly) {
		super();
		this.name = name;
		this.descr = descr;
		this.readOnly = readOnly;
	}
	
	public boolean updateDetails(WebViewUpdate wvu) {		
	//	this.name = wvu.name; can not change name its the ID
		this.descr = wvu.descr;
		this.readOnly = wvu.readOnly;
		return true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
