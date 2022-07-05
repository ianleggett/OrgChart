package webtool.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextListener;

import webtool.pojo.UserAndRole;
import webtool.repository.UserRepository;


@Service("userDetailsService")
@Transactional
@WebListener
public class SecurityUserDetailService extends RequestContextListener implements SecurityUserDetailInterface,UserDetailsService {

	Logger log = Logger.getLogger(SecurityUserDetailService.class);
	
	//@Autowired
	//DataSource dataSource;
	@Autowired
	UserRepository userRepository;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private HttpServletRequest request;
        
	private UserAndRole testUser = null;
	
	private String getClientIP() {
	    String xfHeader = request.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return request.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		  String ip = getClientIP();
	        if (loginAttemptService.isBlocked(ip)) {
	            throw new BadCredentialsException("Account & IP address is blocked - contact customer support");
	        }
		
       
//            Employee loggedInEmployee = employeeService.findEmployeeByUsername(username);
//            List<GrantedAuthority> authorities = getAuthorities(loggedInEmployee);
        	Optional<UserAndRole> usr = userRepository.findByUsername(username);
        	if (usr.isPresent()) {
        		UserAndRole usrrole = usr.get();
	        	boolean enabled = true;
	            boolean accountNonExpired = true;
	            boolean credentialsNonExpired = true;
	            boolean accountNonLocked = true;
	            return new User(
	                   usrrole.getUsername(),usrrole.getPassword(),usrrole.isEnabled(),accountNonExpired, credentialsNonExpired,accountNonLocked, setAuth( usrrole.getRole().toString()  ));
        	}
        	log.error("User "+username+" not found");
        	
        	throw new UsernameNotFoundException("User "+username+" is not found");       
    }

	/**
	 * 
	 * @return
	 */
	public String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication!=null) {
			if (!(authentication instanceof AnonymousAuthenticationToken)) {			
				return authentication.getName();
			}
		}
		if (testUser!=null) return testUser.getUsername();
		return null;		
	}
	
	/**
	 * 
	 * @return
	 */
	public Optional<UserAndRole> getCurrentUser() {
		
		Optional<UserAndRole> usrrole = userRepository.findByUsername(this.getUserName());
		if(usrrole.isPresent()) {
			return usrrole;
		}
		
		return Optional.empty();		
	}
	
	/**
	 * Part of fake user, only for testing
	 */
	public void setFakeUser(UserAndRole name) {
		testUser = name;
	}
	
	private List<GrantedAuthority> setAuth(String auths) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	//	for (String auths : authList) {
			GrantedAuthority ga = new GrantedAuthority() {
				@Override
				public String getAuthority() {
					return auths;
				}
				
				public String toString() {
					return auths;
				}
			};
			authorities.add(ga);
	//	}
		return authorities;
	}

	private static List<GrantedAuthority> getAuthorities(String user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority ga = new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "ROLE_ADMIN";
			}
		};
		authorities.add(ga);
		// for (Role role : employee.getRoles()) {
		// for (Permission permission : role.getPermissions()) {
		// authorities.add(new SimpleGrantedAuthority(permission.getName()));
		// }
		// }
		return authorities;
	}

}