package webtool.utils;

import java.util.HashMap;
import java.util.Map;

public class PrivateKeys {
	
	//indexed by profile
	private Map<String,String> privateKeys = new HashMap<String,String>();
	
	public String getPartialKey(String profileName) {
		String key = getKey(profileName);
		return (key!=null) ? UtilsWeb.hideKey(key, 3) : "not key set";		
	}	
	
	public String getKey(String profileName) {
		return privateKeys.get(profileName);
	}	
		
	public void setKey(String profileName,String key) {			
		privateKeys.put(profileName, key);
	}
	
}
