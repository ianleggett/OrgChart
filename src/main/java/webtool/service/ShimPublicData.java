package webtool.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import webtool.pojo.Ccy;
import webtool.pojo.PaymentType;
import webtool.pojo.PwdResetObject;
import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.UserInfo;
import webtool.pojo.WebChangeUserDetails;
import webtool.pojo.WebFilterObject;
import webtool.pojo.WebKeyValue;
import webtool.pojo.WebSignupDetails;
import webtool.repository.CcyRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.utils.CryptNetworkConfig;
import webtool.utils.Settings;
import webtool.utils.SettingsDAO;
import webtool.utils.UserObjectDAO;

@Service("shimPublicData")
public class ShimPublicData {

	static Logger log = Logger.getLogger(ShimPublicData.class);	
	@Autowired
	CcyRepository ccyRepository;
	@Autowired
	PaymentTypeRepository paymentTypeRepository;
	@Autowired
	private SecurityUserDetailInterface securityService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private UserObjectDAO userObjectDAO;
	@Autowired
	private SettingsDAO settingsDAO;
	
	
	public RespStatus publicsetpwd(String vcode,String pwd,String isnewuser) {
		return userObjectDAO.publicSetPwd(vcode,pwd,isnewuser);	
	}		
	/**
	 * Standard logged in user password reset
	 * @param pwdreset
	 * @return
	 */
	public RespStatus forgotPwd(PwdResetObject pwdreset) {
		return userObjectDAO.forgotPwd(pwdreset);
	}	
	
	public UserAndRole validateCode(String valcode) {
		return userObjectDAO.validateCode(valcode);
	}
	
	/**
	 * Get the regex codes to validate user fields across the app
	 * @return
	 */
	public Map<String,WebKeyValue> getValidationMap(){
		return userObjectDAO.getValidationMap();
	}
	
	/**
	 * 
	 * @param newusr
	 * @return
	 */
	public RespStatus usersignup(WebSignupDetails newusr) {
		return userObjectDAO.createNewUserWeb(newusr);		
	}	
		
	public List<Ccy> getccycodes() {		
		return ccyRepository.findEnabledAll();		
	}
		
	public List<PaymentType> paymenttypes() {		
		return paymentTypeRepository.findEnabledAll();		
	}


	/**
	 * Get Market Orders
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */   /// Browse Offers 
//	public List<CustomerOrder> allOffers(WebFilterObject filter) {
//		try {
//			//log.info(filter);
//			return ordersDAO.allOffers(filter,null);  //Date.from(tm.toInstant(ZoneOffset.UTC))
//		}catch (Exception e) {
//			log.error("request exception ",e);
//		}
//		return new ArrayList<CustomerOrder>();
//	}
	/**
	 * Get the user trading orders & history
	 * @param request
	 * @param response
	 * @return
	 */
//	public List<ContractMatch> userTrades(WebFilterObject filter,Long cid) {
//		LocalDateTime tm = LocalDateTime.now();
//		tm = tm.minusDays(30);
//		if (cid==null) {
//			Optional<UserAndRole> userOpt = securityService.getCurrentUser();
//			if (userOpt.isPresent()) {
//				cid = userOpt.get().getUserDetails().getCid();
//			}else {
//				return new ArrayList<ContractMatch>(); 
//			}
//		}
//		return ordersDAO.getUserTrades(filter,cid.longValue(),tm);
//	}
//	

}
