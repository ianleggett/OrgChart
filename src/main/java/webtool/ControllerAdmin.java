package webtool;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import webtool.mail.EmailSettings;
import webtool.pojo.Ccy;
import webtool.pojo.EmailMsg;
import webtool.pojo.RespStatus;
import webtool.pojo.TableData;
import webtool.pojo.UserAndRole;
import webtool.repository.CcyRepository;
import webtool.repository.UserRepository;
import webtool.utils.MailDAO;
import webtool.utils.Settings;
import webtool.utils.SettingsDAO;
import webtool.utils.SystemMessage;
import webtool.utils.SystemStatus;

@Secured({ "ROLE_ADMIN" })
@RestController
@RequestMapping("/system")
public class ControllerAdmin {
	
	static Logger log = Logger.getLogger(ControllerAdmin.class);

	@Autowired
	private SettingsDAO settingsDAO;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CcyRepository ccyRepository;
	
	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private MailDAO emailDAO;	
//	@Autowired
//	private SystemStatusService systemStatusService;
	
	@PostConstruct
	private void init() {
		// settingsDAO.startStreaming();
	}
	
	
	@RequestMapping(value = "/")
	public ModelAndView adminpanel(){
		ModelAndView mv = new ModelAndView("adminpanel");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return mv;
	}
	
	@RequestMapping(value = "/showemail.json", method = RequestMethod.GET)
	@ResponseBody
	public EmailMsg showemail(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") long id) {

		return emailDAO.getEmail(id);
	}
	
	@RequestMapping(value = "/emails.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<EmailMsg> emaildata(@RequestParam(value = "t", required = false) String type) {

		if (type == null)
			type = "";
		final List<EmailMsg> emaildata;
		switch (type) {
		case "sent":
			emaildata = emailDAO.getSent();
			break;
		case "hold":
			emaildata = emailDAO.getHeld();
			break;
		case "error":
			emaildata = emailDAO.getError();
			break;
		default:
			emaildata = emailDAO.getPending();
			break;
		}

		TableData<EmailMsg> td = new TableData<EmailMsg>();

		// we dont need to push the email data since it is bulky,!!!!!
		for (EmailMsg mh : emaildata) {
			EmailMsg cp = new EmailMsg();
			mh.shallowCopy(cp);
			td.data.add(cp);
		}
		return td;
	}
	
	@RequestMapping(value = "/setemailstate.json", method = RequestMethod.POST)
	@ResponseBody
	public RespStatus changeemailstate(@RequestParam("id") long id,
			@RequestParam("state") String nextState) {
		return emailDAO.setEmailState(id,nextState);
		
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_STAFF" })
	@RequestMapping(value = "/ctr_pre")
	public ModelAndView ctr_pre() {
		return new ModelAndView("ctr_pre");
	}
	

	@RequestMapping(value = "/fact_emails")
	public ModelAndView fact_emails()  {
		ModelAndView mv = new ModelAndView("fact_emails");		
		return mv;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_STAFF" })
	@RequestMapping(value = "/arbconsole")
	public ModelAndView arbconsole()  {
		ModelAndView mv = new ModelAndView("arbconsole");		
		return mv;
	}
	
	
	@RequestMapping(value = "/admin_main")
	public ModelAndView admin_main() throws IOException {
		ModelAndView mv = new ModelAndView("admin_main");
		Settings settings = settingsDAO.getSettings();		
		mv.addObject("settings", settings);
		return mv;
	}
	
	@RequestMapping(value = "/adminsavestaff.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public RespStatus getSaveStaffUsers(HttpSession session, HttpServletResponse response,
			@RequestBody List<UserAndRole> jsonData, Model model) {

		return settingsDAO.syncStaffList(jsonData);
	}


	@RequestMapping(value = "/adminstaffusers.json", method = RequestMethod.GET)
	public List<UserAndRole> getStaffUsers() {
		List<UserAndRole> users = userRepository.findAll();
		// set all passwords to 'PWD_NOT_LISTED'
		for (UserAndRole uar : users) {
			uar.setPassword(SettingsDAO.PWD_NOT_LISTED);
		}
		return users;
	}

	
	@RequestMapping(value = "/adminemailsettings.json", method = RequestMethod.GET)
	public EmailSettings getadminemailsettings() {
		Settings settings = settingsDAO.getSettings();
		return settings.getEmailSettings();
	}
	@RequestMapping(value = "/adminemailsave.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public RespStatus adminemailsave(@RequestBody EmailSettings jsonData) {
		log.info(jsonData);
		Settings settings = settingsDAO.getSettings();
		settings.setEmailSettings(jsonData);
		settingsDAO.saveSettings();
		log.info("Email settings updated");
		return RespStatus.OK;
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/currencycodetable.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<Ccy> currencycodetable() {
		return new TableData<Ccy>(ccyRepository.findAll());
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addupdatecurrency.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus addupdatecurrency(@RequestBody Ccy postData) {

		log.info(postData);
		return settingsDAO.saveCurrencyOrder(postData);
	}
	
	@RequestMapping(value = "/shutdown", method = RequestMethod.POST)
	public void shutdown(HttpSession session, HttpServletResponse response) throws Exception {
		// send msg to all login pages and working pages
		
		log.info("Shutdown recieved");
		if ((appContext instanceof ConfigurableWebApplicationContext)) {
		    ((ConfigurableWebApplicationContext)appContext).close();
		    log.warn("**** user invoked shutdown ... Shutting down");
		  }	
	}
	
}
