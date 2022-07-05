package webtool.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import webtool.pojo.ProcStatus;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.service.SystemStatusService;

@Component
@Scope("singleton")
public class CoreDAO {
	

	public static final String WILDCARD = "%";
	static Logger log = Logger.getLogger(CoreDAO.class);
	static public int RATING_NOT_SET = -1;

	public static final EnumSet<ProcStatus> canEditUpdate = EnumSet.of(ProcStatus.CREATED,ProcStatus.EXPIRED); // orders in

	final SimpleDateFormat ordIdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	final SimpleDateFormat webDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
	final SimpleDateFormat webDateFormat = new SimpleDateFormat("dd MMM yyyy");
	final BigDecimal WEI = new BigDecimal("1000000000000000000");
	
	int ACCEPT_TO_EXPIRE = 120; // minute
	int DEPOSIT_TO_EXPIRE = 120; // minute	
	
	int INVITED_MAX = 50; // max invited users per order


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SettingsDAO settingsDAO;

	@Autowired
	PaymentTypeRepository paymentTypeRepository;
	@Autowired
	MailDAO emailDAO;
	@Autowired
	UserObjectDAO userDAO;
	@Autowired
	private SecurityUserDetailInterface securityService;
	
	@Autowired
	private SystemStatusService systemStatusService;
	
	//private ThreadedQueueProcessor<TradeCmd> qProc;	
	final ExecutorService executorService =  Executors.newFixedThreadPool(1); 
			
	@PostConstruct
	private void init() {
		initProc();
	}
	
	private void initProc() {
		systemStatusService.setWorking(true);		
		systemStatusService.setNewMessage("System OK");
	}


	public boolean validateEmail(String email) {
		return true;
	}
	
	public boolean validateUser(String user) {
		return true;
	}
	



}
