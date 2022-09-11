package webtool.pojo;

import java.util.Map;
import java.util.TreeMap;

public class AggContList {
	
	Map<String,String>  strMapList = new TreeMap<String,String>();
		
	public void addContainer(String team) {				
		if (!strMapList.containsKey(team)) {			
			strMapList.put(team, team);
		}		
	}

	
}
