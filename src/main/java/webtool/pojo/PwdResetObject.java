package webtool.pojo;

public class PwdResetObject {

	String username;
	String email;
	
	public PwdResetObject() {
		super();
	}
	public PwdResetObject(String username, String email) {
		super();
		this.username = username;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
