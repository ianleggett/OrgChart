package webtool.pojo;

public class WebChgPwd {

	long userid;
	String oldpwd;
	String newpwd;
	
	public WebChgPwd() {
		super();
	}
	public WebChgPwd(long userid, String oldpwd, String newpwd) {
		super();
		this.userid = userid;
		this.oldpwd = oldpwd;
		this.newpwd = newpwd;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getOldpwd() {
		return oldpwd;
	}
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}
	public String getNewpwd() {
		return newpwd;
	}
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}
	
	
}
