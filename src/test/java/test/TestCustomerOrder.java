package test;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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

import webtool.pojo.Ccy;
import webtool.pojo.UserInfo;
import webtool.repository.CcyRepository;
@ActiveProfiles("test")

@ContextConfiguration(classes = {   ApplicationTest.class })

//@AutoConfigureTestEntityManager
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJpaRepositories( basePackages = { "webtool.repository" })
@ComponentScan(basePackageClasses = {} )

//webtool.service.ShimDataServices.class,

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(true)	
class TestCustomerOrder {
	@Autowired
	CcyRepository ccyRepository;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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

	@Test
	void test() {
//		Optional<Ccy> usdtOpt = ccyRepository.findByName("USDT");
//		assertThat(usdtOpt.isPresent()).isTrue();
//		
//		Optional<Ccy> cnyOpt = ccyRepository.findByName("CNY");
//		assertThat(cnyOpt.isPresent()).isTrue();
//		final double fee = 10;
//		LocalDateTime exp = LocalDateTime.now().plusDays(10);
//		
//		UserInfo fakeUser = new UserInfo();		
//		
//		CustomerOrder co = new CustomerOrder( fakeUser, usdtOpt.get(), 100, cnyOpt.get(), 50, fee, exp, "custref" , null);
//		
//		assertThat(co.isBuyer()).isFalse();
//		assertThat(co.getFromccy()==usdtOpt.get()).isTrue();
//		assertThat(co.getToccy()==cnyOpt.get()).isTrue();
//		assertThat(co.getExchRate()).isBetween(0.5, 0.5);
//		
//		co = new CustomerOrder( fakeUser, cnyOpt.get(), 50, usdtOpt.get(), 100, fee, exp, "custref" , null);
//		
//		assertThat(co.isBuyer()).isTrue();
//		assertThat(co.getFromccy()==usdtOpt.get()).isTrue();
//		assertThat(co.getToccy()==cnyOpt.get()).isTrue();
//		assertThat(co.getExchRate()).isBetween(0.5, 0.5);
				
	}

}
