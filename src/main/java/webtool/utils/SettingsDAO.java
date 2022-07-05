package webtool.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import webtool.mail.EmailSettings;
import webtool.pojo.Ccy;
import webtool.pojo.CcyType;
import webtool.pojo.PaymentType;
import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.pojo.WebSignupDetails;
import webtool.repository.CcyRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;

@Component
@Scope("singleton")
public class SettingsDAO {

	public final static String PATH_DELIM = "|";
	public final static String PWD_NOT_LISTED = "PWD_NOT_LISTED";
	final String MISC_ITEM_MANUAL = "misc item manual";
	static final String DEFAULT_ADMIN_NAME = "admin";
	static final String DEFAULT_ADMIN_PASSWORD = "admin";
	static final String DEFAULT_USER_NAME = "user1";
	static final String DEFAULT_USER_PASSWORD = "user1";
	
	public static final String WILDCARD = "%";
	static Logger log = Logger.getLogger(SettingsDAO.class);
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

	public static String SETTINGS_FILE = "settings.json";

	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	List<String> allowedCcy = List.of("AUD", "BCH", "BTC", "CAD", "CHF", "CNY", "CNHT", "ETC", "ETH", "EUR", "GBP",
			"HKD", "JPY", "LTC", "NZD", "RUB", "SAR", "SGD", "USD", "USDT", "VND", "XRP", "ZAR");

	@Autowired
	UserRepository userRepository;
	@Autowired
	CcyRepository fxcodeRepository;
	@Autowired
	PaymentTypeRepository paymentTypeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserObjectDAO userObjectDAO;

	@Autowired
	private SecurityUserDetailInterface securityService;


	private Settings settings;
	private Session mailSession = null;

	// @Value("${target.type}" )
	private String targetType = "dev";
	
	@PostConstruct
	public void init() {

		PaymentType BANK_TFR = null;
		
		if (settings == null) {
			settings = getSettings();
		}

		Long fxCodes = fxcodeRepository.count();
		if (fxCodes==0) {
			// create dummy entries
			fxcodeRepository.save( new Ccy("AUD","Australian Dollar",CcyType.Fiat,"",true) );		
			fxcodeRepository.save( new Ccy("CNY"," ",CcyType.Fiat,"",true) );									
			fxcodeRepository.save( new Ccy("BTC","Bitcoin",CcyType.Crypto,"",true) );
//			fxcodeRepository.save( new Ccy("ETC","Ether Classic",CcyType.Crypto,"",true) );
			fxcodeRepository.save( new Ccy("ETH","Ether",CcyType.Crypto,"",true) );
			fxcodeRepository.save( new Ccy("EUR","Euro",CcyType.Fiat,"",true) );
			//fxcodeRepository.save( new Ccy("GBP","British Pound Sterling",CcyType.Fiat,10,100000,true) );
			//fxcodeRepository.save( new Ccy("INR","Indian Rupee",CcyType.Fiat,10,100000,true) );
			fxcodeRepository.save( new Ccy("NZD","New Zealand Dollar",CcyType.Fiat,"",true) );
			fxcodeRepository.save( new Ccy("SGD","Singapore Dollar",CcyType.Fiat,"",true) );
			fxcodeRepository.save( new Ccy("USD","United States Dollar",CcyType.Fiat,"",true) );
			//fxcodeRepository.save( new Ccy("USDC","USD Coin",CcyType.Crypto,10,100000,true) );
			fxcodeRepository.save( new Ccy("USDT","Tether USD Anchor",CcyType.ERC20,"0xeEd7C9D32a6Db131c638C3E475F66908446D80eC",true) );
			fxcodeRepository.save( new Ccy("VND","Vietnamese Dong",CcyType.Fiat,"",true) );
//			fxcodeRepository.save( new Ccy("XMR","Monero",CcyType.Crypto,"",true) );			
		}	
		Long payCount = paymentTypeRepository.count();
		if (payCount==0) {
			BANK_TFR = paymentTypeRepository.save( new PaymentType("Bank Transfer","Bank Name","Bank Address","Swift code","Account name","Account number",true) );
			paymentTypeRepository.save( new PaymentType("Cash Deposit","Location","Time","Account Number","IBAN","Country",false) );
			paymentTypeRepository.save( new PaymentType("Pingit","line1","line2","line3","line4","line5",false) );
			paymentTypeRepository.save( new PaymentType("Western Union","line1","line2","line3","line4","line5",false) );
			paymentTypeRepository.save( new PaymentType("Money Gram","line1","line2","line3","line4","line5",false) );
			paymentTypeRepository.save( new PaymentType("International wire","line1","line2","line3","line4","line5",false) );
			paymentTypeRepository.save( new PaymentType("Ali Pay","line1","line2","line3","line4","line5",false) );
			paymentTypeRepository.save( new PaymentType("Other","line1","line2","line3","line4","line5",false) );
		}
		// check not first install
		Long userSz = userRepository.count();
		if (userSz == 0) {
			// if so, create default users and save them
			UserAndRole uar = userObjectDAO.createAdminUser(new WebSignupDetails(DEFAULT_ADMIN_NAME,"admin@email.com","01234567","GBR",true));			
//			userObjectDAO.setUpDefaultPayDetails(uar.getId(), new WebPaymentDetail(BANK_TFR.getId(),"admin bank","line 2","line 3","line 4","line 5","user comment"));
			userObjectDAO.setPassword(uar.getId(),"admin");						
			uar = userObjectDAO.createNewUser(new WebSignupDetails("user1","user1@email.com","87666555","GBR",true ));
//			userObjectDAO.setUpDefaultPayDetails(uar.getId(), new WebPaymentDetail(BANK_TFR.getId(),"user1 bank","line 2","line 3","line 4","line 5","user comment"));			
			userObjectDAO.setPassword(uar.getId(),"user1");


		}
	}
	
	public Session createMailSession() {
		
		Settings settings = getSettings();
		if (!settings.getEmailSettings().isEmpty()){
			EmailSettings emp = settings.getEmailSettings();
			Properties props = new Properties();
			props.put("mail.smtp.auth", emp.isAuth() );
			props.put("mail.smtp.starttls.enable", emp.isStarttls());
			props.put("mail.smtp.host", emp.getHost());
			props.put("mail.smtp.port", emp.getPort() );
			props.put("mail.smtp.ssl.protocols", "TLSv1.2" );
					
			mailSession = Session.getDefaultInstance( props , new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emp.getUsername(), emp.getPassword());
				}
			  });		
			return mailSession;
		}		
		return null;
	}
	
	@Transactional
	public RespStatus saveCurrencyOrder(Ccy vo) {

		String username = securityService.getUserName();
		if (username != null) {
			try {
				vo = fxcodeRepository.save(vo);
				log.info("Saved ccy id:" + vo);
				return RespStatus.OK;
			}catch(Exception e) {
				return new RespStatus(99,"Error adding CCY :"+e.getMessage());
			}
		}
		log.error("username is null");
		return new RespStatus(1, "username is null");
	}

	public RespStatus syncStaffList(List<UserAndRole> updatedList) {
		
		RespStatus resp = new RespStatus(1, "OK");
		List<Long> existingUid = new ArrayList<Long>();
		List<Long> updatedUid = new ArrayList<Long>();

		List<UserAndRole> existing = userRepository.findAll();
		for (UserAndRole eur : existing) {
			existingUid.add(eur.getId());
		}

		// check new entries by uid=""
		for (UserAndRole iar : updatedList) {

			if (iar.getId() == 0) {
				iar.setId(0);
				iar.setPassword(passwordEncoder.encode(iar.getPassword()));
				UserAndRole newuser = userRepository.save(iar);
			//	addNewUserAccount(newuser);

			} else {
				Optional<UserAndRole> optolduar = userRepository.findById(iar.getId());
				if (optolduar.isPresent()) {
					UserAndRole olduar = optolduar.get();
					updatedUid.add(iar.getId());
					// password has changed
					if (!iar.getPassword().equalsIgnoreCase(PWD_NOT_LISTED)) {
						olduar.setPassword(passwordEncoder.encode(iar.getPassword()));
					}
					if (!iar.getEmail().equalsIgnoreCase(olduar.getEmail())) {
						olduar.setEmail( iar.getEmail() );						
					}
					if (iar.isEnabled()!=olduar.isEnabled()) {
						olduar.setEnabled( iar.isEnabled() );						
					}
					if (iar.isVerified()!=olduar.isVerified()) {
						olduar.setVerified( iar.isVerified() );		
					}
					
					userRepository.save(olduar);
				}

			}
		}
		try {
			// userRepository.saveAll(updatedList);

			// now remove deleted entries
			for (Long eiar : existingUid) {
				if (!updatedUid.contains(eiar)) {
					userRepository.deleteById(eiar);
				}
			}
		} catch (Exception e) {
			log.error(e);
			resp = new RespStatus(0, "Failed");
		}
		return resp;
	}

	private File getSettingsFile() {
		// Creating the directory to store file
		final String rootPath;
		final File serverFile;

		if (targetType.equalsIgnoreCase("prod")) {
			rootPath = System.getProperty("catalina.base");
			serverFile = new File(rootPath + File.separator + "webapps" + File.separator + "configData" + File.separator
					+ SETTINGS_FILE);
		} else {
			rootPath = System.getProperty("user.dir");
			serverFile = new File(rootPath + File.separator + "configData" + File.separator + SETTINGS_FILE);
		}
		return serverFile;
	}

	private void setCryptoDefaults(CryptoSettings cset) {
		cset.setConfig("local", new CryptNetworkConfig()); //http://localhost:7545","0x19Ef82a5E30d8C01969b6629c8d3feAc96626753","0xf188e20ADBe88deC9B5CfCb5a518A69A4416581c","http://localhost/txn")
		cset.setSelectedProfile("local");		
	}
	
	private void setPrivateKeyDefaults(PrivateKeys pvt) {
		pvt.setKey("local","36bc12b5a33d43792985e95d56dd73d110259d6d952cbdfa7587fe2bae95c634");		
	}
	
	public Settings getSettings() {

		if (settings == null) {
			File file = getSettingsFile();
			try {
				if (file.exists()) {
					settings = gson.fromJson(new FileReader(file), Settings.class);
				} else {
					settings = new Settings();
					settings.setDefault();
					FileWriter fw = new FileWriter(file);
					fw.write(gson.toJson(settings));
					fw.close();
				}
			
				return settings;
			} catch (IOException e) {
				log.error(e);
			}
		}

		return settings;
	}

	public boolean saveSettings() {
		if (settings != null) {
			try {
				File file = getSettingsFile();
				FileWriter fw = new FileWriter(file);
				fw.write(gson.toJson(settings));
				fw.close();
			} catch (IOException e) {
				log.error(e);
			}
			return true;
		}
		return false;
	}

}
