package test;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import webtool.security.SecurityUserDetailInterface;

@ExtendWith(SpringExtension.class)
@Configuration
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.NONE)
//@ComponentScan(basePackageClasses = {webtool.service.OmsService.class})
@EntityScan(basePackages={"webtool.pojo"})
//@ComponentScan(basePackages = {"webtool.service"})
@EnableTransactionManagement
@PropertySources({ @PropertySource("classpath:exch-config.properties"), @PropertySource("classpath:sys-config.properties") })
//@ComponentScan(basePackageClasses = {webtool.service.OmsService.class,webtool.utils.SettingsDAO.class,webtool.pojo.SystemConfig.class} )
//public class TestApplication implements CommandLineRunner{
public class ApplicationTest {
	static Logger log = Logger.getLogger(ApplicationTest.class);
	
	private SimpMessagingTemplate simpMessTemplate;
	
	
	@Bean
	public SimpMessagingTemplate getChannel() {	
		if (simpMessTemplate==null) 
			simpMessTemplate = new SimpMessagingTemplate( new StubMessageChannel());
		return simpMessTemplate;
	}
	
    @Bean
    public PasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	public ApplicationTest() throws KeyManagementException, NoSuchAlgorithmException {
		super();
		
	}

}
