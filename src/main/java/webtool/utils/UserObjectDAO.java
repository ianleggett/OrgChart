package webtool.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import webtool.pojo.EmailMsg;
import webtool.pojo.PaymentType;
import webtool.pojo.PwdResetObject;
import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.UserInfo;
import webtool.pojo.UserRole;
import webtool.pojo.WebChangeUserDetails;
import webtool.pojo.WebChgPwd;
import webtool.pojo.WebKeyValue;
import webtool.pojo.WebSignupDetails;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserInfoRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.utils.MailDAO.EMAIL_TYPE;

@Component
@Scope("singleton")
public class UserObjectDAO {
	Logger log = Logger.getLogger(UserObjectDAO.class);

	@Autowired
	UserInfoRepository userObjectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	PaymentTypeRepository paymentTypeRepository;
	@Autowired
	private SecurityUserDetailInterface securityService;
	@Autowired
	MailDAO emailDAO;
	
	public enum FieldType{		
		username,
		email,
		phone,
		password,
		country,
		crypto;
	}
	
	public final static Map<String,WebKeyValue> validMap = Map.of( 
			FieldType.username.name(), new WebKeyValue("[0-9a-zA-Z]{5,20}","5 to 20 alpha numeric characters"), 
			FieldType.email.name(), new WebKeyValue("^[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\\.){1,125}[a-zA-Z]{2,63}$","standard email address"),  // "^[a-zA-Z0-9]*@[a-zA-Z0-9-]*\\.[a-zA-Z0-9-]*$",//"[a.Z]{4,30}@", 
			FieldType.phone.name(), new WebKeyValue("\\+{0,1}[0-9]{1,4}\\s{0,1}[0-9]{8,20}","+country code (8 to 20 digits)"),
			FieldType.country.name(), new WebKeyValue("[A-Z]{3}","3 uppercase characters"),  // only 3 letters
			FieldType.password.name(), new WebKeyValue("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$","5 characters, 1 or more letters & 1 numeric"), // 6 chars, 1 letter 1 number
			     //"(^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32}$)?(^(?=.*\\d)(?=.*[a-z])(?=.*[@#$%^&+=]).{8,32}$)?(^(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,32}$)?(^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,32}$)?"
			FieldType.crypto.name(), new WebKeyValue("0x[a-zA-Z0-9]{35,}","about 40 characters")  // only 3 letters
			); 
	
	public Map<String,WebKeyValue> getValidationMap(){
		return validMap;
	}
	
	
	public void unsetUserRating(final UserAndRole usr, int rating) {
		usr.getUserDetails().unsetRating(rating);
		userObjectRepository.save(usr.getUserDetails());
	}
	
	public void addUserRating(final UserAndRole usr, int rating) {
		usr.getUserDetails().addRating(rating);
		userObjectRepository.save(usr.getUserDetails());
	}
	
	public void addTrade(final UserInfo usrInfo , long usdtVol) {
		usrInfo.addTradeCount(usdtVol);
		userObjectRepository.save(usrInfo);
	}
	
	public void addCancel(final UserInfo usrInfo) {
		usrInfo.addCancelCount();
		userObjectRepository.save(usrInfo);
	}

	public RespStatus publicSetPwd(String validationCode,String newpwd,String isnewuser) {
		String username = securityService.getUserName();
		if (username != null) {
			log.error("Trying to set password, but logged in with :"+username);
			return new RespStatus(121,"Trying to set password, but logged in with :"+username);
		}
		if ( !validate(FieldType.password, newpwd) ) return new RespStatus(128,"Invalid password");
		Optional<UserAndRole> userOpt = userRepository.findByValidationCode(validationCode);
		// check user is good
		if (userOpt.isPresent() && userOpt.get().isEnabled()) {	
			// reset validation code			
			if (setPassword(userOpt.get().getId(),newpwd) ) {
				Properties extraParams = new Properties();
				extraParams.put("newuser", userOpt.get() );
				if ((isnewuser!=null)&&(isnewuser.equalsIgnoreCase("true"))) {
					emailDAO.createCustomerEmail(userOpt.get(),EMAIL_TYPE.EMAIL_NEWUSER);					
					emailDAO.createAdminEmail(EMAIL_TYPE.EMAIL_STAFF_NEWUSER,extraParams);
				}else {
					emailDAO.createCustomerEmail(userOpt.get(),EMAIL_TYPE.EMAIL_PWD_CHANGED);					
					// emailDAO.createAdminEmail(EMAIL_TYPE.EMAIL_STAFF_PWDCHANGED,extraParams);
				}
				userOpt.get().setValidationCode("");
				return RespStatus.OK;
			}else
				new RespStatus(129,"Could not change password ");
		}
		return new RespStatus(1300,"Bad validation code");
	}
	
	public UserAndRole validateCode(String validationCode) {
		String username = securityService.getUserName();
		if (username != null) {
			log.error("Trying to valiate new user, but logged in with :"+username);
			return null;	
		}
		Optional<UserAndRole> userOpt = userRepository.findByValidationCode(validationCode);
		// check user is good
		if (userOpt.isPresent()) {
			// new users disabled by default
			userOpt.get().setEnabled(true);
			userOpt.get().setVerified(true);
			// userOpt.get().setValidationCode( UUID.randomUUID().toString().replace("-", "") );
			// set the password validation code and save DB
			return userRepository.save(userOpt.get());	
		}		
		return null;
	}
			
	/**
	 * Send user email to validate 
	 * @param usr
	 * @return
	 */
	public RespStatus createValidationLink(UserAndRole usr,EMAIL_TYPE eType) {
		if (usr!=null) {
			// allow user to login existing, only reset if link is clicked
			usr.setValidationCode( UUID.randomUUID().toString().replace("-", "") );
			usr = userRepository.save(usr);			
			emailDAO.createCustomerEmail(usr, eType);
			return RespStatus.OK;
		}
		return new RespStatus(2005,"User is null for reset password");		
	}
	
	/**
	 * Changes a password when logged in
	 * @param oldNewPwd
	 * @return
	 */
	public RespStatus changePwd(WebChgPwd oldNewPwd) {
		// check same user logged in
		Optional<UserAndRole> userOpt = securityService.getCurrentUser();
		if (userOpt.isPresent()) {
			final UserAndRole uar = userOpt.get();			
			// 	check old pwd is correct
			if (uar.getId()!=oldNewPwd.getUserid()) {
				log.error("User changepwd attempt actualuid:"+uar.getId()+" attempt uid:"+oldNewPwd.getUserid());
				return new RespStatus(999,"Security warning, details logged");
			}
			if ( !passwordEncoder.matches(oldNewPwd.getOldpwd(), uar.getPassword()) ) {
				// change password
				return new RespStatus(998,"Old password does not match");
			}
			if (oldNewPwd.getNewpwd().length() < 6) {
				return new RespStatus(995,"New password not long enough");
			}			
			if ( setPassword( uar.getId(), oldNewPwd.getNewpwd()) ) {
				return RespStatus.OK;
			}
			return new RespStatus(997,"Password NOT changed");
		}
		return new RespStatus(99,"User not logged in");
		
	}
	/**
	 * Starts A Reset password when not logged in
	 * @param pwdreset
	 * @return
	 */
	public RespStatus forgotPwd(PwdResetObject pwdreset) {
		// make sure not logged in already!		
		String username = securityService.getUserName();
		if (username != null) return new RespStatus(2003,"Can not reset password, User logged in already");	
		
		final Optional<UserAndRole> userOpt;		
		
		if (pwdreset.getUsername()!=null && !pwdreset.getUsername().isEmpty()) {
			userOpt = userRepository.findByUsername(pwdreset.getUsername());
		}else if (pwdreset.getEmail()!=null && !pwdreset.getEmail().isEmpty()) {
			userOpt = userRepository.findByEmail(pwdreset.getEmail());
		}else {
			return new RespStatus(2004,"Can not reset password, No username or email given");	
		}
		if (userOpt.isPresent()) {			
			return createValidationLink(userOpt.get(),EMAIL_TYPE.EMAIL_FORGOT_PWD);
		}
		
		return new RespStatus(2006,"Can not reset password, username or email not found");			
	}
	
	private boolean validate(FieldType ftype,String str) {
		return str.matches( validMap.get(ftype.name()).getKey() );
	}
			
	private RespStatus validateDetails(WebSignupDetails udetails) {
		if ( !validate(FieldType.email, udetails.getEmail()) ) return new RespStatus(2110,"Invalid email");
		if ( !validate(FieldType.phone, udetails.getPhone()) ) return new RespStatus(2111,"invalid phone number");
		if ( !validate(FieldType.country, udetails.getCountryISO()) ) return new RespStatus(2112,"Country not valid");
		return RespStatus.OK;
	}
	
	private RespStatus validateDetails(	WebChangeUserDetails udetails) {
		if ( !validate(FieldType.email, udetails.getEmail()) ) return new RespStatus(2110,"Invalid email");
		if ( !validate(FieldType.phone, udetails.getPhone()) ) return new RespStatus(2111,"invalid phone number");
		if ( !validate(FieldType.country, udetails.getCountryISO()) ) return new RespStatus(2112,"Country not valid");	
		return RespStatus.OK;
	}

	/**
	 * Logged in user, updates existing profile details	
	 * @param udetails
	 * @return
	 */
	public RespStatus updateUserWeb(WebChangeUserDetails udetails) {
		// check logged in user is same as this user
		Optional<UserAndRole> userOpt = securityService.getCurrentUser();
		if (userOpt.isPresent()) {
			UserAndRole user = userOpt.get();
			if (!user.isVerified()) return new RespStatus(198,"User, not verified scurity event reported");
		    if (udetails.getUsername()==null || !udetails.getUsername().equalsIgnoreCase(user.getUsername())) return new RespStatus(199,"Can not change username, security event reported");
		    if (udetails.getCid()!=user.getUserDetails().getCid()) return new RespStatus(199,"Incorrect CID, security event reported");	
		    if (!udetails.getEmail().equalsIgnoreCase(user.getEmail())) return new RespStatus(196,"Can not change email, security event reported");
			RespStatus res = validateDetails(udetails);
			if (res==RespStatus.OK) {
				// decide what updates to do			
				// can change phone;	 countryISO;  payDetails ,msg_updates; msg_mailshot;				
				user.setPhone( udetails.getPhone() );
				user.setMsg_updates( udetails.isMsg_updates() );
				user.setMsg_mailshot( udetails.isMsg_mailshot() );				
				UserInfo usrObj = user.getUserDetails();
				// validate country ISO
				usrObj.setCountryISO( udetails.getCountryISO() );
								
				userObjectRepository.save(usrObj);
				userRepository.save(user);
			}
			return res;
		}
		return new RespStatus(99,"user not logged in, security event reported");
	}
	
	public RespStatus createNewUserWeb(WebSignupDetails udetails) {		
		RespStatus res = validateDetails(udetails);
		if (res==RespStatus.OK) {		
			if (userRepository.findByUsername(udetails.getUsername()).isPresent()) return new RespStatus(2100,"Username exists already");		
			if (userRepository.findByEmail(udetails.getEmail()).isPresent()) return new RespStatus(2101,"User email exists already");
						
			UserAndRole uar = new UserAndRole(udetails.getUsername(), "", udetails.getEmail(), UserRole.ROLE_USER, false);  // cam not login until validation
			try {
				final UserInfo usrObj = userObjectRepository.save(new UserInfo( udetails.getUsername(),udetails.getCountryISO()));		
				uar.setUserDetails( usrObj );
				uar = userRepository.save(uar);
				log.info(String.format("Creating new user:%s  email: %s",uar.getUsername(),uar.getEmail()));
				createValidationLink(uar,EMAIL_TYPE.EMAIL_VALIDATE_EMAIL);
				
				return RespStatus.OK;
			}catch(Exception  e) {
				log.error(String.format("Error creating new user %s email %s", udetails.getUsername(), udetails.getEmail()));
				return new RespStatus(99,e.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * Initialy password is null, user can not login until validated
	 * @param udetails
	 * @return
	 */
	public UserAndRole createNewUser(WebSignupDetails udetails) {
		UserAndRole uar = new UserAndRole(udetails.getUsername(), "", udetails.getEmail(), UserRole.ROLE_USER, true);	
		final UserInfo usrObj = userObjectRepository.save(new UserInfo( udetails.getUsername(),udetails.getCountryISO()));		
		uar.setUserDetails( usrObj );
		return userRepository.save(uar);
	}
	
	public UserAndRole createAdminUser(WebSignupDetails udetails) {
		log.warn("Creating ADMIN user ..."+udetails.getUsername());
		UserAndRole uar = new UserAndRole(udetails.getUsername(), "", udetails.getEmail(), UserRole.ROLE_ADMIN, true);	
		final UserInfo usrObj = userObjectRepository.save(new UserInfo( udetails.getUsername(),udetails.getCountryISO()));		
		uar.setUserDetails( usrObj );
		return userRepository.save(uar);
	}
	
	public boolean setPassword(long uid,String pwd) {
		Optional<UserAndRole> uar = userRepository.findById(uid);
		if (uar.isPresent()) {
			uar.get().setValidationCode(null);  // reset any validation codes
			uar.get().setPassword( passwordEncoder.encode(pwd));
			userRepository.save(uar.get());
			return true;
		}
		return false;
	}
	
	
}
