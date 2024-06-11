package webtool.service;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.WebKeyValue;
import webtool.security.SecurityUserDetailInterface;
import webtool.utils.Settings;
import webtool.utils.SettingsDAO;
import webtool.utils.UtilsWeb;

@Service("shimPrimaryData")
public class ShimPrimaryData {

	static Logger log = Logger.getLogger(ShimPrimaryData.class);
	UtilsWeb utils = new UtilsWeb();
	@Autowired
	private SettingsDAO settingsDAO;
	@Autowired
	private SecurityUserDetailInterface securityService;	
	
//	public CryptNetworkConfig getnetworkconfig() {		
//		Optional<UserAndRole> optusr = securityService.getCurrentUser();
//		if (optusr.isPresent()) {
//			
//			Settings settings = settingsDAO.getSettings();
//			CryptNetworkConfig net = settings.getCryptoSettings().getCurrent();
//			//CryptNetworkConfig secure = new CryptNetworkConfig(net.getHttpService(),net.getEscrowCtrAddr(),net.getUSDTCoinCtrAddr(),net.getEtherScanPrefix());			
//			return net;
//		}		
//		return null;
//	}
//		
//	public RespStatus setnetworkprofile(String profile) {
//		log.info("setnetworkprofile "+profile);
//		Optional<UserAndRole> optusr = securityService.getCurrentUser();
//		if (optusr.isPresent()) {
//			Settings settings = settingsDAO.getSettings();
//			settings.getCryptoSettings().setSelectedProfile(profile);
//			settings.getCryptoSettings().getSelectedProfile();
//			return RespStatus.OK;
//		}	
//		return new RespStatus(99,"Could not find usr ");
//	}
	
}
