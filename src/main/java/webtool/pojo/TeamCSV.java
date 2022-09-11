package webtool.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class TeamCSV {

	String iNum;
	List<String> teamName = new ArrayList<String>();
	
	
	public static List<String> getTeams(String str){
		return Arrays.asList( str.split("[\\/|,|;]") );
	}
	
	public static TeamCSV load(CSVRecord rec) {
		TeamCSV grp = new TeamCSV();		
		grp.setiNum(rec.get("ID"));
		String str = rec.get("team-name").strip().replaceAll("\\s\\s", " ").replace("&", " and ");		
		grp.setTeamName( getTeams(str) );
		return grp;
	}
		
	public String getiNum() {
		return iNum;
	}
	public void setiNum(String iNum) {
		this.iNum = iNum;
	}

	public List<String> getTeamName() {
		return teamName;
	}

	public void setTeamName(List<String> teamName) {
		this.teamName = teamName;
	}
	
	
}
