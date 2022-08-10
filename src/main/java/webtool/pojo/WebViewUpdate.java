package webtool.pojo;

public class WebViewUpdate {

	String name;   // default org view is read only
	String descr;
	Boolean readOnly;
	
	
	public WebViewUpdate(OrgView ov) {
		super();
		this.name=ov.name;
		this.descr=ov.descr;
		this.readOnly=ov.readOnly;
	}
	public WebViewUpdate() {
		super();
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
