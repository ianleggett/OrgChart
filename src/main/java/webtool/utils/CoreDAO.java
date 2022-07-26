package webtool.utils;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import webtool.pojo.Employee;
import webtool.pojo.OrgContainer;
import webtool.pojo.OrgView;
import webtool.pojo.OrgViewItem;
import webtool.pojo.Person;
import webtool.pojo.ProcStatus;
import webtool.pojo.RespStatus;
import webtool.pojo.WebEmployeeView;
import webtool.pojo.WebUpdateContainer;
import webtool.repository.EmployeeRepository;
import webtool.repository.OrgContainerRepository;
import webtool.repository.OrgViewItemRepository;
import webtool.repository.OrgViewRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;
import webtool.service.DataLoaderService;
import webtool.service.SystemStatusService;

@Component
@Scope("singleton")
public class CoreDAO {
	
	public static final String DEFAULT_VIEW = "default";
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
	
	@Autowired
	private DataLoaderService dataloaderService;
	
	@Autowired
	private OrgViewItemRepository orgViewItemRepository;
	
	@Autowired
	EmployeeRepository employeeRepositiory;
	@Autowired
	OrgContainerRepository orgContainerRepository;	
	
	//private ThreadedQueueProcessor<TradeCmd> qProc;	
	final ExecutorService executorService =  Executors.newFixedThreadPool(1); 
			
	@PostConstruct
	private void init() {
		initProc();
	}
	
	private void initProc() {
		systemStatusService.setWorking(true);		
		systemStatusService.setNewMessage("System OK");
		dataloaderService.initDefaultView();
		dataloaderService.load();
		
	}

	public List<WebEmployeeView> getViewData(String viewName) {
		
		List<WebEmployeeView> result = new ArrayList<WebEmployeeView>();
		List<Employee> emps =  ImmutableList.copyOf( employeeRepositiory.findAll() );
		Map<String, Employee> empMap = emps.stream().collect( Collectors.toMap( Employee::getInum, it->it) );				
		
		List<OrgContainer> orgContainers = orgContainerRepository.findByViewName(DEFAULT_VIEW);
		Map<Long,OrgContainer> contMap = orgContainers.stream().collect(Collectors.toMap( OrgContainer::getId, it->it) );
		
		List<OrgViewItem> ovOpt = orgViewItemRepository.findByViewName("default");
		
		for(OrgViewItem ovi : ovOpt) {		
			Employee emp = empMap.get(ovi.getiNum());
			OrgContainer oc = contMap.get(ovi.getContainerId());
			if ((emp!=null) && (oc!=null)) {
				WebEmployeeView wev = new WebEmployeeView();					
				wev.setEmployee( emp );
				wev.setContainer(oc);
				result.add(wev);
			}
		}	
		return result;
	}
	
	public List<OrgContainer> getContainers(String viewName){
		return orgContainerRepository.findByViewNameSorted(viewName);
	}
	
	public RespStatus updateContainer(WebUpdateContainer upd) {
		Optional<OrgContainer> orgContOpt = orgContainerRepository.findById(upd.getId());
		if (orgContOpt.isPresent()) {
			if (orgContOpt.get().updateDetails(upd)) {
				orgContainerRepository.save(orgContOpt.get());
			}
			return RespStatus.OK;
		}
		return new RespStatus(987,"container id not found");
	}
	
	public boolean validateEmail(String email) {
		return true;
	}
	
	public boolean validateUser(String user) {
		return true;
	}
	



}
