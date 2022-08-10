package webtool.pojo;

public class WebUpdateContainer {
	
    private long id;
    String viewName;
    String deptName;
	String groupName;
	String teamName;
	
	long count;
	String urltag;
	

	public WebUpdateContainer() {
		super();
	}
	public WebUpdateContainer(long id, String viewName, String deptName, String groupName, String teamName, long count,
			String urltag) {
		super();
		this.id = id;
		this.viewName = viewName;
		this.deptName = deptName;
		this.groupName = groupName;
		this.teamName = teamName;
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
	@Override
	public String toString() {
		return "WebUpdateContainer [id=" + id + ", viewName=" + viewName + ", deptName=" + deptName + ", groupName="
				+ groupName + ", teamName=" + teamName + ", count=" + count + ", urltag=" + urltag + "]";
	}
	
}
