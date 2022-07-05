package webtool.mail;

import java.io.Serializable;

public class EmailSettings implements Serializable {

	private static final long serialVersionUID = 1L;
	String _id; 
	String host="";
	int port=0;
	String username="";
	String password="";
	String mail_transport_protocol="";
	boolean auth; // mail_smtp_auth
	boolean starttls; // mail_smtp_starttls_enable
	String fromEmail=""; // optional replyto
	
	boolean notifyStaff = true;
	String staffList="";  // list of csv emails to notify
	
	int emailCheckMins = 0; // check and send emails every 15 mins, 0 for disable	
	
	boolean simulateSending = false; 	
	
	boolean emailOnUserRegistration = true; 	
	boolean emailOnNewOrder = true;  // is live
	boolean emailOnDeposit = true;  // someone took part or all of the order
	boolean emailOnCancel = true;   // cancel invoked
	boolean emailOnBankDeposit = true;
	boolean emailOnBankRec = true;
	boolean emailOnComplete = true;

	public EmailSettings() {
		super();
	}

	public EmailSettings(String host, int port, String username, String password, String mail_transport_protocol,
			boolean mail_smtp_auth, boolean mail_smtp_starttls_enable,String fromEmail,String staffList) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.mail_transport_protocol = mail_transport_protocol;
		this.auth = mail_smtp_auth;
		this.starttls = mail_smtp_starttls_enable;
		this.fromEmail = fromEmail;
		this.staffList = staffList;
	}

	public boolean isEmpty() {
		return this.host.isEmpty();
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail_transport_protocol() {
		return mail_transport_protocol;
	}

	public void setMail_transport_protocol(String mail_transport_protocol) {
		this.mail_transport_protocol = mail_transport_protocol;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public boolean isStarttls() {
		return starttls;
	}

	public void setStarttls(boolean starttls) {
		this.starttls = starttls;
	}

	public int getEmailCheckMins() {
		return emailCheckMins;
	}

	public void setEmailCheckMins(int emailCheckMins) {
		this.emailCheckMins = emailCheckMins;
	}

	public boolean isEmailOnUserRegistration() {
		return emailOnUserRegistration;
	}

	public void setEmailOnUserRegistration(boolean emailOnUserRegistration) {
		this.emailOnUserRegistration = emailOnUserRegistration;
	}

	public boolean isEmailOnNewOrder() {
		return emailOnNewOrder;
	}

	public void setEmailOnNewOrder(boolean emailOnNewOrder) {
		this.emailOnNewOrder = emailOnNewOrder;
	}

	public boolean isEmailOnDeposit() {
		return emailOnDeposit;
	}

	public void setEmailOnDeposit(boolean emailOnDeposit) {
		this.emailOnDeposit = emailOnDeposit;
	}

	public boolean isEmailOnCancel() {
		return emailOnCancel;
	}

	public void setEmailOnCancel(boolean emailOnCancel) {
		this.emailOnCancel = emailOnCancel;
	}

	public boolean isEmailOnBankDeposit() {
		return emailOnBankDeposit;
	}

	public void setEmailOnBankDeposit(boolean emailOnBankDeposit) {
		this.emailOnBankDeposit = emailOnBankDeposit;
	}

	public boolean isEmailOnBankRec() {
		return emailOnBankRec;
	}

	public void setEmailOnBankRec(boolean emailOnBankRec) {
		this.emailOnBankRec = emailOnBankRec;
	}

	public boolean isEmailOnComplete() {
		return emailOnComplete;
	}

	public void setEmailOnComplete(boolean emailOnComplete) {
		this.emailOnComplete = emailOnComplete;
	}

	public boolean isSimulateSending() {
		return simulateSending;
	}

	public void setSimulateSending(boolean simulateSending) {
		this.simulateSending = simulateSending;
	}

	public String getStaffList() {
		return staffList;
	}

	public void setStaffList(String staffList) {
		this.staffList = staffList;
	}

	public boolean isNotifyStaff() {
		return notifyStaff;
	}

	public void setNotifyStaff(boolean notifyStaff) {
		this.notifyStaff = notifyStaff;
	}

}
