package webtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import webtool.security.SecurityUserDetailService;


@Configuration
@Order(2)
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfigurationWeb extends WebSecurityConfigurerAdapter {

	@Autowired
	AuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	SecurityUserDetailService userDetailsService;
	
	
	   @Autowired
	    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService);
	        auth.authenticationProvider(authenticationProvider());
	    }
	   
	    @Bean
	    public PasswordEncoder pwdEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	        authenticationProvider.setUserDetailsService(userDetailsService);
	        authenticationProvider.setPasswordEncoder(pwdEncoder());
	        return authenticationProvider;
	    }
	
	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }  
	    
	  @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	    }
	  	  
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		
		//http.headers().httpStrictTransportSecurity().disable();
		
		// TODO: ICL this was added to allow access for GWT testing
		original(http);
	}
	
	/**
	 * Original code for login
	 * @param http
	 * @throws Exception
	 */
	protected void original(HttpSecurity http) throws Exception {		
 		http 		 		
 		 .csrf().disable()
// 		.requestMatchers()
 		.authorizeRequests() 		
 		.antMatchers("/system/login","/system/logout","/validate","/resources/**","/react/**").permitAll() 	
 		.antMatchers("/**").permitAll()
 		.anyRequest().authenticated() 
        .and()
 		.formLogin().loginPage("/system/login").permitAll() ///.failureHandler(customAuthenticationFailureHandler)  // needed for retry hacking
 		.defaultSuccessUrl("/system/", true)   
 		.and()
 		.logout().permitAll()
 		.and()
	     .sessionManagement()
	     .maximumSessions(1)	    
	     ;
	}


}
