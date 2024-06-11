package webtool;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.WebKeyValue;
import webtool.security.SecurityUserDetailInterface;
import webtool.service.ShimPrimaryData;
import webtool.utils.CryptNetworkConfig;
import webtool.utils.Settings;
import webtool.utils.SettingsDAO;
import webtool.utils.UtilsWeb;

@Secured({ "ROLE_SUPERUSER","ROLE_ADMIN", "ROLE_USER"})
@RestController
@RequestMapping("/system")
public class ControllerPrimaryData {

	@Autowired
	ShimPrimaryData shimPrimaryData;	
	
//	@RequestMapping(value = "/getnetworkconfig.json", method = RequestMethod.GET)
//	public CryptNetworkConfig getnetworkconfig() {	
//		return shimPrimaryData.getnetworkconfig();		
//	}
//	
//	@RequestMapping(value = "/getnetworkprofile.json", method = RequestMethod.GET)
//	public WebKeyValue getnetworkprofile() {	
//		return shimPrimaryData.getnetworkprofile();
//	}
	
//	@Secured({ "ROLE_SUPERUSER","ROLE_ADMIN"})
//	@RequestMapping(value = "/setnetworkprofile.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus setnetworkprofile(@RequestParam(value = "profile", required = true) String profile) {
//		return shimPrimaryData.setnetworkprofile(profile);
//	}
}
