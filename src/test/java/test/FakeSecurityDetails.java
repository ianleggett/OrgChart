package test;

import java.util.Optional;
import org.springframework.stereotype.Service;

import webtool.pojo.UserAndRole;
import webtool.security.SecurityUserDetailInterface;


@Service
public class FakeSecurityDetails implements SecurityUserDetailInterface {

	Optional<UserAndRole> currentUser;
	
	
	@Override
	public String getUserName() {		
		return (currentUser != null) ? currentUser.get().getUsername() : null;
	}

	@Override
	public Optional<UserAndRole> getCurrentUser() {
		return currentUser;
	}

	@Override
	public void setFakeUser(UserAndRole uname) {
		
		currentUser = (uname!=null) ? Optional.of(uname) : null;		
	}
	
	

}
