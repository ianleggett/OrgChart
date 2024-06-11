package webtool.pojo;

public class WebUpdateContainer {
	
    private long id;
    String viewName;
	String teamName;
	String teamDesc;
	
	long count;
	String urltag;
	

	public WebUpdateContainer() {
		super();
	}
	public WebUpdateContainer(long id, String viewName, String teamName, String teamDesc, long count,
			String urltag) {
		super();
		this.id = id;
		this.viewName = viewName;
		this.teamName = teamName;
		this.teamDesc = teamDesc;
		this.count = count;
		this.urltag = urltag;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getUrltag() {
		return urltag;
	}
	public void setUrltag(String urltag) {
		this.urltag = urltag;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
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
	
	public String getTeamDesc() {
		return teamDesc;
	}
	public void setTeamDesc(String teamDesc) {
		this.teamDesc = teamDesc;
	}
	@Override
	public String toString() {
		return "WebUpdateContainer [id=" + id + ", viewName=" + viewName + ", teamName=" + teamName + ", teamDesc="
				+ teamDesc + ", count=" + count + ", urltag=" + urltag + "]";
	}

	
}
