//package test;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Properties;
//
//import org.apache.log4j.Logger;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//
//import webtool.pojo.ArbitrationCase;
//import webtool.pojo.Ccy;
//import webtool.pojo.ContractMatch;
//import webtool.pojo.ContractStatus;
//import webtool.pojo.EmailMsg;
//import webtool.pojo.UserAndRole;
//import webtool.repository.CcyRepository;
//import webtool.repository.CustomerOrderRepository;
//import webtool.repository.PaymentTypeRepository;
//import webtool.repository.UserPayDetailRepository;
//import webtool.repository.UserRepository;
//import webtool.security.SecurityUserDetailInterface;
//import webtool.service.ShimDataServices;
//import webtool.service.ShimPublicData;
//import webtool.utils.MailDAO;
//import webtool.utils.MailDAO.EMAIL_TYPE;
//import webtool.utils.OrdersDAO;
//import webtool.utils.SettingsDAO;
//import webtool.utils.UserObjectDAO;
//
//@ActiveProfiles("test")
//
//@ContextConfiguration(classes = {   ApplicationTest.class })
//
////@AutoConfigureTestEntityManager
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@EnableJpaRepositories( basePackages = { "webtool.repository" })
//@ComponentScan(basePackageClasses = {test.TestHelperService.class, webtool.pojo.SystemConfig.class, webtool.service.FakeCryptoService.class,  webtool.utils.MailDAO.class, webtool.service.ShimDataServices.class} )
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Rollback(true)	
//class TestEmailSending {
//	Logger log = Logger.getLogger(TestEmailSending.class);
//	
//	final static String EMAIL_DIR = "testfiles/"; 
//	@Autowired
//	UserRepository userRepository;
//	@Autowired
//	CcyRepository ccyRepository;
//	@Autowired
//	PaymentTypeRepository paymentTypeRepository;	
//	@Autowired
//	UserPayDetailRepository userPayDetailRepository;
//	@Autowired
//	SecurityUserDetailInterface fakeSecurityDetails;
//	@Autowired
//	CustomerOrderRepository custOrderRepository;
//	
//	@Autowired
//	TestHelperService testHelper;
//	
//	@Autowired
//	ShimDataServices shimDataServices;
//	@Autowired
//	ShimPublicData publicServices;
//	@Autowired
//	private UserObjectDAO userObjectDAO;
//	@Autowired
//	private MailDAO mailDAO;
//	@Autowired
//	private OrdersDAO ordersDAO;
//	@Autowired
//	private SettingsDAO settingsDAO;
//	
//	
//	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
//	@BeforeEach
//	void setUp() throws Exception {		
//		//mailDAO.init();
//	}
//
//	@AfterEach
//	void tearDown() throws Exception {
//	}	
//	
//	void writeFile(String filename, String contents) {
//		
//	    try {
//	    	FileWriter myWriter = new FileWriter(EMAIL_DIR+filename);
//			myWriter.write(contents);
//			myWriter.close();	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	    	    
//	}
//	
////	@Test
////	void testWait() throws InterruptedException {
////		log.info("waiting for email timer...");
////		Thread.sleep(120_000);
////		log.info("Done.");
////	}
//	
//	@Test
//	void testOfferAccept() {		
//		UserAndRole seller = testHelper.setUser("user2");
//		UserAndRole buyer = testHelper.setUser("user1");
//		testHelper.setWallet(seller.getUsername(),"01234567-user2-wallet");
//		testHelper.setUserDefaultPayType(seller.getUsername());
//		
//		testHelper.setWallet(buyer.getUsername(),"01234567-user1-wallet");
//		testHelper.setUserDefaultPayType(buyer.getUsername());
//		
//		Ccy fromccy = testHelper.getCCY("USDT");
//		Ccy toccy = testHelper.getCCY("AUD");
//		ContractMatch trade = new ContractMatch("OrderId-12345", buyer.getUserDetails(), seller.getUserDetails(), seller.getPayDetails().get(0),"0x122buyer1111", "0x1333-seller-6666", fromccy, toccy, 1234, 3333, 10, 8);
//		EmailMsg email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_OFFERACPT);
//		log.info(email.getMsg());
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_OFFERACPT);		
//		writeFile("tradeaccept_seller.html",email.getMsg());	
//		email = mailDAO.createCustomerEmail(buyer,trade,EMAIL_TYPE.EMAIL_OFFERACPT);		
//		writeFile("tradeaccept_buyer.html",email.getMsg());	
//
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_OFFERCANCEL);
//		writeFile("offercancel_seller.html",email.getMsg());
//		email = mailDAO.createCustomerEmail(buyer,trade,EMAIL_TYPE.EMAIL_OFFERCANCEL);
//		writeFile("offercancel_buyer.html",email.getMsg());
//		
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_BANKSENT);
//		writeFile("banksent.html",email.getMsg());
//		
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_BANKREC);
//		writeFile("bankrec.html",email.getMsg());
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_TFRCOMPLETE);		
//		writeFile("tfrcomplete.html",email.getMsg());
//		email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_TRADEREFUND);		
//		writeFile("traderefund.html",email.getMsg());	
//		
//	}
//
//	@Test
//	void testOfferCancel() {		
//		UserAndRole seller = testHelper.setUser("user2");
//		UserAndRole buyer = testHelper.setUser("user1");
//		testHelper.setWallet(seller.getUsername(),"01234567-user2-wallet");
//		testHelper.setUserDefaultPayType(seller.getUsername());
//		
//		testHelper.setWallet(buyer.getUsername(),"01234567-user1-wallet");
//		testHelper.setUserDefaultPayType(buyer.getUsername());
//		
//		Ccy fromccy = testHelper.getCCY("USDT");
//		Ccy toccy = testHelper.getCCY("AUD");
//		ContractMatch trade = new ContractMatch("OrderId-12345", buyer.getUserDetails(), seller.getUserDetails(), seller.getPayDetails().get(0),"0x122buyer1111", "0x1333-seller-6666", fromccy, toccy, 1234, 3333, 10, 8);
//		EmailMsg email = mailDAO.createCustomerEmail(seller,trade,EMAIL_TYPE.EMAIL_OFFERCANCEL);
//		log.info(email.getMsg());
//	}
//
//	@Test
//	void testNewUser() {
//		UserAndRole seller = testHelper.setUser("user2");
//		UserAndRole buyer = testHelper.setUser("user1");
//		testHelper.setWallet(seller.getUsername(),"01234567-user2-wallet");
//		testHelper.setUserDefaultPayType(seller.getUsername());
//		
//		testHelper.setWallet(buyer.getUsername(),"01234567-user1-wallet");
//		testHelper.setUserDefaultPayType(buyer.getUsername());
//		
//		writeFile("newuser_email.html",mailDAO.createCustomerEmail(seller,EMAIL_TYPE.EMAIL_NEWUSER).getMsg());
//		Properties extraParams = new Properties();
//		extraParams.put("newuser", seller );
//		writeFile("staff_newuser_email.html",mailDAO.createSingleAdminEmail("staffmember@email.com",EMAIL_TYPE.EMAIL_STAFF_NEWUSER,extraParams).getMsg());
//	}
//	
//	@Test
//	void testArbitration() {		
//		UserAndRole seller = testHelper.setUser("user2");
//		UserAndRole buyer = testHelper.setUser("user1");
//		testHelper.setWallet(seller.getUsername(),"01234567-user2-wallet");
//		testHelper.setUserDefaultPayType(seller.getUsername());
//		
//		testHelper.setWallet(buyer.getUsername(),"01234567-user1-wallet");
//		testHelper.setUserDefaultPayType(buyer.getUsername());
//		
//		Ccy fromccy = testHelper.getCCY("USDT");
//		Ccy toccy = testHelper.getCCY("AUD");
//		ContractMatch trade = new ContractMatch("OrderId-12345", buyer.getUserDetails(), seller.getUserDetails(), seller.getPayDetails().get(0),"0x122buyer1111", "0x1333-seller-6666", fromccy, toccy, 1234, 3333, 10, 8);
//		
//		ArbitrationCase arb = new ArbitrationCase(trade,"Test arb state for emails");
//		
//		writeFile("seller_testArbitration.html",mailDAO.createCustomerEmail(seller,arb,EMAIL_TYPE.EMAIL_ARBITRATE).getMsg());
//		writeFile("buyer_testArbitration.html",mailDAO.createCustomerEmail(buyer,arb,EMAIL_TYPE.EMAIL_ARBITRATE).getMsg());
//		
//		Properties extraParams = new Properties();
//		extraParams.put("arb", arb);		
//		writeFile("staff_testArbitration.html",mailDAO.createSingleAdminEmail("Test@user", EMAIL_TYPE.EMAIL_STAFF_ARB, extraParams).getMsg());
//		
//	}
//	
////	@Test
////	@Rollback(false)
////	@Order(1)
////	void testNewUserSignup() {
////		
////		log.info("Test 1");
////		RespStatus res = publicServices.usersignup(new WebSignupDetails("userXXX","userXXX@email.com","012345678977","GBR",false) );  // ALL good
////		assertThat(res.getStatusCode()).isEqualTo(0);
////		
////		UserAndRole user = testHelper.setUser("userXXX");			
////		assertThat(user.isVerified()).isFalse();
////		testHelper.userLogout();  // need to be logged out
////	}
////		
////	
////	@Test
////	@Rollback(false)
////	@Order(2) 
////	void testValidatedSignup() {
////		log.info("Test 2");
////		UserAndRole user = testHelper.setUser("userXXX");	
////		testHelper.userLogout();  // need to be logged out
////				
////		// checks validation code, if good creates a new code for reset password
////		UserAndRole uar = publicServices.validateCode(user.getValidationCode());
////				
////		publicServices.publicsetpwd(user.getValidationCode(),"newpwd","true");  // new user email	
////	}
////	
////	
////	/**
////	 * Testing user change details
////	 */	
////	@Test
////	@Order(3) 	
////	@Rollback(false)
////	void testSendValidationLink() {	
////		log.info("Test 3");
////		UserAndRole user = testHelper.setUser("user2");	
////		testHelper.userLogout();  // need to be logged out
////		
////		RespStatus res = publicServices.forgotPwd(new PwdResetObject("user2", null));
////		assertThat(res.getStatusCode()).isEqualTo(0);
////		
////		// CHECK forgot password email				
////	}	
////	
////	
////	@Test
////	@Order(3) 	
////	@Rollback(false)
////	void testCreateNewOrder() {			
////		UserAndRole user = testHelper.setUser("user1");	
////		// create an order
////		WebOfferObject wo = testHelper.createOrder("CNY","USDT",100,100,10,null);
////		RespStatus res = shimDataServices.addupdateorder(wo);
////		assertThat(res.getStatusCode()).isEqualTo(0);
////		
////		
////	}	
////
////	@Test
////	@Order(3) 	
////	@Rollback(false)
////	void testAcceptOrder() {			
////		UserAndRole user = testHelper.setUser("user2");	
////		
////	}	
//
//	
//	
//}
