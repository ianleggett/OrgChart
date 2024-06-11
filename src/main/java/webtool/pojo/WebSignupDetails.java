package webtool.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Passed at the start of the session for reference
 * Also used for new user signup
 * @author ian
 *
 */
public class WebSignupDetails {

	String username;
//	long userid;
//	String token;
//	String blurb;
	String email;			
	String phone;	
	
	String countryISO;  //https://www.iso.org/obp/ui/#search 3 digit ISO 3166
	       
//	List<PaymentDetail> payDetails = new ArrayList<PaymentDetail>();

//    UserCoin mywallet;	    	    		
//	boolean verified;	    
//	boolean msg_updates;
//	boolean msg_mailshot;		
//	Date timestamp;  // last update
	
	public WebSignupDetails() {
		super();
	}
		
	/**
	 *  Testing and set up calls ONLY 
	 * @param username
	 * @param email
	 * @param phone
	 * @param countryISO
	 * @param verified
	 */
	public WebSignupDetails(String username, String email, String phone, String countryISO, boolean verified) {
		super();
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.countryISO = countryISO;
//		this.verified = verified;
	}


//
//	public String getBlurb() {
//		return blurb;
//	}
//
//	public void setBlurb(String blurb) {
//		this.blurb = blurb;
//	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountryISO() {
		return countryISO;
	}

	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

//	public List<PaymentDetail> getPayDetails() {
//		return payDetails;
//	}
//
//	public void setPayDetails(List<PaymentDetail> payDetails) {
//		this.payDetails = payDetails;
//	}
//
//	public UserCoin getMywallet() {
//		return mywallet;
//	}
//
//	public void setMywallet(UserCoin mywallet) {
//		this.mywallet = mywallet;
//	}

//	public boolean isVerified() {
//		return verified;
//	}
//
//	public void setVerified(boolean verified) {
//		this.verified = verified;
//	}
//
//	public boolean isMsg_updates() {
//		return msg_updates;
//	}
//
//	public void setMsg_updates(boolean msg_updates) {
//		this.msg_updates = msg_updates;
//	}
//
//	public boolean isMsg_mailshot() {
//		return msg_mailshot;
//	}
//
//	public void setMsg_mailshot(boolean msg_mailshot) {
//		this.msg_mailshot = msg_mailshot;
//	}
//
//	public Date getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(Date timestamp) {
//		this.timestamp = timestamp;
//	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
//	public long getUserid() {
//		return userid;
//	}
//	public void setUserid(long userid) {
//		this.userid = userid;
//	}
//	public String getToken() {
//		return token;
//	}
//	public void setToken(String token) {
//		this.token = token;
//	}

	@Override
	public String toString() {
		return "WebSignupDetails [username=" + username + ", email=" + email + ", phone=" + phone + ", countryISO="
				+ countryISO + "]";
	}

	
	
}
