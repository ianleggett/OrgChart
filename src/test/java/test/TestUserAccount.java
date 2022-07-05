package test;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import webtool.pojo.PaymentType;
import webtool.pojo.ProcStatus;
import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.WebChangeUserDetails;
import webtool.pojo.WebFilterObject;
import webtool.repository.UserRepository;
import webtool.service.ShimDataServices;
import webtool.service.ShimPublicData;
import webtool.utils.CoreDAO;
import webtool.utils.SettingsDAO;
import webtool.utils.UtilsWeb;

@ActiveProfiles("test")

@ContextConfiguration(classes = {   ApplicationTest.class })

//@AutoConfigureTestEntityManager
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJpaRepositories( basePackages = { "webtool.repository" })
@ComponentScan(basePackageClasses = {webtool.service.AuditService.class,  webtool.utils.CoreDAO.class,webtool.utils.SettingsDAO.class, webtool.pojo.SystemConfig.class} )

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(true)	
class TestUserAccount {
	Logger log = Logger.getLogger(TestUserAccount.class);
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SettingsDAO settingsDAO;
	@Autowired
	CoreDAO orderDAO;
	@Autowired
	ShimDataServices shimDataService;
	@Autowired
	ShimPublicData publicServices;
	
	//Optional<UserAndRole> userOpt;
	//AccountSummary accsum;
	
	
	static WebFilterObject NO_FILTER;
	static List<String> ALL_STATES;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		NO_FILTER = new WebFilterObject(true, true, 0, 0, 0, new ArrayList<Long>(), ALL_STATES);
		ALL_STATES = UtilsWeb.enumSetToStr( EnumSet.allOf(ProcStatus.class) );
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	

	
}
