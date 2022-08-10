package webtool.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AggContList {
	
	Map<String,Map<String,List<String>>>  strMapList = new TreeMap<String,Map<String,List<String>>>();
		
	public void addContainer(String dep, String grp, String team) {
		
		Map<String,List<String>> grpMap = strMapList.get(dep);
		if (grpMap == null) {
			grpMap = new TreeMap<String,List<String>>();
			strMapList.put(dep, grpMap);
		}
		List<String> tList = grpMap.get(grp);
		if (tList == null) {
			tList = new ArrayList<String>();
			grpMap.put(grp,tList);
		}
		tList.add(team);		
	}

	public Map<String, Map<String, List<String>>> getStrMapList() {
		return strMapList;
	}

	public void setStrMapList(Map<String, Map<String, List<String>>> strMapList) {
		this.strMapList = strMapList;
	}
	
}
