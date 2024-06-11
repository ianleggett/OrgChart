package webtool.security;

import java.util.Optional;

import webtool.pojo.UserAndRole;


public interface SecurityUserDetailInterface {
	
	String getUserName();
	public Optional<UserAndRole> getCurrentUser();
	
	void setFakeUser(UserAndRole uname);// testing only
	
}
