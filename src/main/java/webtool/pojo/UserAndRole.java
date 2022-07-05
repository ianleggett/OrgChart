package webtool.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "user")
public class UserAndRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_generator")
    @SequenceGenerator(name="usr_generator", sequenceName = "usr_seq", initialValue = 1)
    @Column(name = "id", updatable = false, nullable = false)          
    private long id;
    
    @Column(unique=true)
    String username;  // cant change this    
    String password;
	String email;		
    String phone; 
    
    // public facing details
    @OneToOne(fetch = FetchType.EAGER)
    UserInfo userDetails; // just to show browse users
            
//    @OneToMany(targetEntity=PaymentDetail.class, fetch = FetchType.EAGER)   
//    private List<PaymentDetail> payDetails = new ArrayList<PaymentDetail>();
//
//    @OneToOne
//    private UserCoin mywallet;
    
    UserRole role;    
    boolean enabled;    
    
	@Column(columnDefinition="tinyint(1) default 1")
	boolean verified=false;
    
	String validationCode;
	
	@Column(columnDefinition="tinyint(1) default 1")
	boolean msg_updates=true;
	@Column(columnDefinition="tinyint(1) default 1")
	boolean msg_mailshot=true;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") 
	@Column(name = "timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date timestamp;
	@Lob
	String notes;
	
	public UserAndRole() {
		super();
	}
	
	public UserAndRole(WebSignupDetails newuser) {
		this.username = newuser.getUsername();
		this.email = newuser.getEmail();
		this.phone = newuser.getPhone();		
	//	this.payDetails = newuser.getPayDetails();
		this.enabled = true;
	//	this.msg_mailshot = newuser.msg_mailshot;
		this.password = "";				
		/// user ubject
		this.userDetails.countryISO = newuser.getCountryISO();
	//	this.userDetails.blurb = newuser.getBlurb();
		this.userDetails.username = this.username;
		this.userDetails.lastseen = LocalDateTime.now();
		this.userDetails.feedback = -1;  // no ratings
		this.userDetails.tradeCount = 0;		
		role = UserRole.ROLE_USER;
	}
	
	/**
	 * For test purposes ONLY
	 * @param username
	 * @param password
	 * @param email
	 * @param role
	 * @param enabled
	 */
	public UserAndRole(String username, String password, String email, UserRole role, boolean enabled) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;		
		this.enabled = enabled;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

//	public List<UserCoin> getMycoins() {
//		return mycoins;
//	}
//
//	public void setMycoins(List<UserCoin> mycoins) {
//		this.mycoins = mycoins;
//	}

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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public UserInfo getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserInfo userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAndRole other = (UserAndRole) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;		
		return true;
	}
}
