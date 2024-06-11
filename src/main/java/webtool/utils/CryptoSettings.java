package webtool.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.util.WebUtils;

public class CryptoSettings {

	String selectedProfile;
	private Map<String,CryptNetworkConfig> crypsSettings = new HashMap<String,CryptNetworkConfig>();
		
//	public CryptNetworkConfig getCurrent() {
//		CryptNetworkConfig prof = this.getCurrentRaw();
//		// protect private key, last 4 digits
//		prof.setBrokerPrivateKey( UtilsWeb.hideKey(prof.getBrokerPrivateKey(),3) );		
//		return prof;
//	}	
		
	public CryptNetworkConfig getCurrent() {
		CryptNetworkConfig prof = crypsSettings.get(selectedProfile);
		if (prof==null) {
			prof = new CryptNetworkConfig();
			crypsSettings.put(selectedProfile,prof);
		}				
		return prof;
	}	
	
	public boolean isProfilesEmpty() {
		return crypsSettings.isEmpty();
	}
	
	public void setCurrent(CryptNetworkConfig newSetting) {	
		crypsSettings.put(selectedProfile, newSetting);
	}
	
	public void setConfig(String name,CryptNetworkConfig newSetting) {			
		crypsSettings.put(name, newSetting);
	}
	
	public String getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(String selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

//	public Map<String, CryptNetworkConfig> getCrypsSettings() {
//		return crypsSettings;
//	}
//
//	public void setCrypsSettings(Map<String, CryptNetworkConfig> crypsSettings) {
//		this.crypsSettings = crypsSettings;
//	}	
	
}
