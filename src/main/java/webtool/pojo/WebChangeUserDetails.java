package webtool.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Passed at the start of the session for reference
 * Also used for new user signup
 * @author ian
 *
 */
public class WebChangeUserDetails {

	String username;   // can not change username
	long cid;       // can not change username	
	String email;	  // can not change email
	
	//String blurb;
	String phone;		
	String countryISO;  //https://www.iso.org/obp/ui/#search 3 digit ISO 3166
	    
	boolean msg_updates;    // can send email updates
	boolean msg_mailshot;	// can send email offers
	
	public WebChangeUserDetails() {
		super();
	}
		
	public WebChangeUserDetails(UserAndRole uar){
		this(uar.getUserDetails().getCid(),uar.username,uar.email,uar.phone,uar.userDetails.countryISO,uar.verified);
	}
	/**
	 *  Testing and set up calls ONLY 
	 * @param username
	 * @param email
	 * @param phone
	 * @param countryISO
	 * @param verified
	 */
	public WebChangeUserDetails(long cid,String username, String email, String phone, String countryISO, boolean verified) {
		super();
		this.cid = cid;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.countryISO = countryISO;
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
	public boolean isMsg_updates() {
		return msg_updates;
	}

	public void setMsg_updates(boolean msg_updates) {
		this.msg_updates = msg_updates;
	}

	public boolean isMsg_mailshot() {
		return msg_mailshot;
	}

	public void setMsg_mailshot(boolean msg_mailshot) {
		this.msg_mailshot = msg_mailshot;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "WebChangeUserDetails [username=" + username + ", cid=" + cid + ", email=" + email + ", phone=" + phone
				+ ", countryISO=" + countryISO + ", msg_updates=" + msg_updates + ", msg_mailshot=" + msg_mailshot
				+ "]";
	}


	
}
