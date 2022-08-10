package webtool.pojo;

import org.apache.commons.csv.CSVRecord;

public class GroupCSV {

	String iNum;
	String dept;
	String group;
	String teamName;
	
	public static GroupCSV load(CSVRecord rec) {
		GroupCSV grp = new GroupCSV();		
		grp.setiNum(rec.get("ID"));
		grp.setDept(rec.get("dept").strip().replaceAll("\\s\\s", " "));
		grp.setGroup(rec.get("group").strip().replaceAll("\\s\\s", " "));
		grp.setTeamName(rec.get("team-name").strip().replaceAll("\\s\\s", " "));
		return grp;
	}
	
	public String getFQDN() {
		return OrgContainer.getFQDN(dept,group, teamName);
	}
	
	public String getiNum() {
		return iNum;
	}
	public void setiNum(String iNum) {
		this.iNum = iNum;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	
	
}
