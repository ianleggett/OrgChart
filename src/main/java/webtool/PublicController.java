package webtool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import webtool.pojo.Employee;
import webtool.pojo.FileDB;
import webtool.pojo.MermaidData;
import webtool.pojo.OrgContainer;
import webtool.pojo.OrgView;
import webtool.pojo.OrgViewItem;
import webtool.pojo.RespStatus;
import webtool.pojo.TableData;
import webtool.pojo.UserAndRole;
import webtool.pojo.WebEmployeeView;
import webtool.pojo.WebUpdateContainer;
import webtool.repository.EmployeeRepository;
import webtool.repository.OrgViewItemRepository;
import webtool.repository.OrgViewRepository;
import webtool.service.ShimPublicData;
import webtool.service.StorageService;
import webtool.utils.CoreDAO;

@RestController
public class PublicController {

	static Logger log = Logger.getLogger(PublicController.class);
	@Autowired
	ShimPublicData shimPublicData;
	@Autowired
	StorageService storageService;
	@Autowired
	CoreDAO coreDAO;;

	@RequestMapping(value = "/wstest", method = { RequestMethod.GET })
	public ModelAndView wstest() {
		ModelAndView model = new ModelAndView();
		model.setViewName("wstest");
		return model;
	}

	/**
	 * public server based call to validate email / reset pwd
	 * 
	 * @param valcode
	 * @return
	 */
	@RequestMapping(value = "/validate")
	public ModelAndView validate(@RequestParam(value = "v", required = true) String valcode,
			@RequestParam(value = "n", required = true) boolean newuser) {
		UserAndRole uar = shimPublicData.validateCode(valcode);
		log.info("validate code " + (uar != null));
		final ModelAndView mv;
		// ICL stubbed out for debug
		if (uar == null) {
			// ModelAndView modelAndView = new ModelAndView("redirect:/");
			mv = new ModelAndView("expiredlink");
			return mv;
		}
		mv = new ModelAndView("validate");
		// check validation code and allow new password set up
		String vcode = uar != null ? (uar.getValidationCode() != null ? uar.getValidationCode() : "-no-code-")
				: "-no-user-";
		mv.addObject("vc", vcode);
		mv.addObject("n", newuser); // this page deals with new user validation and user password reset
		return mv;
	}

	// public link holder can upload N files Odd is buyer, EVEN is seller
	/** can only upload N slots */
	@PostMapping("/uploadfile")
	public RespStatus uploadFile(@RequestParam(name = "caseguid") String caseguid,
			@RequestParam(name = "slot") int slotnum, @RequestParam(name = "file") MultipartFile file) {
		log.info("Uploading file");
		try {
			storageService.store(caseguid, slotnum, file);
			String message = "Uploaded the file successfully: " + file.getOriginalFilename();
			log.info(message);
			return RespStatus.OK;
		} catch (Exception e) {
			log.error("Could not upload the file: " + file.getOriginalFilename() + "!");
			return new RespStatus(13355, "Could not upload");
		}
	}

	// views media uploaded
	@RequestMapping(value = "/media", method = RequestMethod.GET)
	public void showMedia(@RequestParam(name = "caseguid") String caseguid, @RequestParam(name = "slot") int slotnum,
			HttpServletResponse response) throws ServletException, IOException {
		Optional<FileDB> file = storageService.getFile(caseguid, slotnum);
		if (file.isPresent()) {
			response.setContentType(file.get().getType());
			response.getOutputStream().write(file.get().getData());
		}
		response.getOutputStream().close();
	}

//	@RequestMapping(value = "/orgdata.json", method = RequestMethod.GET)
//	@ResponseBody
//	public MermaidData getMermaid() {
//		return new MermaidData("graph TD\n"
//				+ "A1209(Billy Blogs<br/>VP, Engineering)\n"
//				+ "A1209 --- A1067(Mike Magee<br/>Senior Engineering Manager)\n"
//				+ "A1209 --- A1331(Barry White<br/>Director of Engineering WMC)\n"
//				+ "A1209 --- A1102(Maddie Magee<br/>Software Engineer)\n"
//				+ "style A1102 fill:#ffdddd,stroke:#333,stroke-width:px\n"
//				+ "A1209 --- A1348(Bill Bailey<br/>Senior Engineering Manager)\n"
//				+ "");		
//	}

//	@RequestMapping(value = "/orgdata.json", method = RequestMethod.GET)
//	@ResponseBody
//	public OrgElements getOrgElements() {
//				
//		List<ONode> nodes = List.of(
//				new ONode("A1209","Billy Blogs"),
//				new ONode("A1067","Mike Magee<br/>Senior Engineering Manager"),
//				new ONode("A1331","Barry White<br/>Director of Engineering WMC"),
//				new ONode("A1348","Bill Bailey<br/>Senior Engineering Manager")
//				);
//		List<OEdge> edges = List.of();
//		
//		OElements oe = new OElements(nodes,edges);
//		
//		return new OrgElements(oe);
//	}

	@RequestMapping(value = "/containerEdit")
	public ModelAndView containers(@RequestParam(value = "v", required = false) String viewName) {
		return new ModelAndView("containerEdit");
	}

	@RequestMapping(value = "/containerdata.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<OrgContainer> getContainers() {
		return new TableData<OrgContainer>(coreDAO.getContainers(CoreDAO.DEFAULT_VIEW));
	}

	@RequestMapping(value = "/staffdata.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<WebEmployeeView> getStaffData() {
		return new TableData<WebEmployeeView>(coreDAO.getViewData(CoreDAO.DEFAULT_VIEW));
	}

	@RequestMapping(value = "/updateContainer.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus updateContainer(@RequestBody WebUpdateContainer postData) {
		log.info("updateContainer : " + postData);
		
		return coreDAO.updateContainer(postData);

	}

//	@RequestMapping(value = "/")
//	public ModelAndView home(HttpSession session, @RequestParam(value = "m", required = false) Integer basknum) throws IOException {
//		ModelAndView mv = new ModelAndView("main");
//		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		//String name = auth.getName();
//		return mv;
//	}

//	@RequestMapping(value = "/browse", method = { RequestMethod.GET })
//	public ModelAndView browse() {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("browse");
//		return model;
//	}
//	
//	@RequestMapping(value = "/ccycodes.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<Ccy> getccycodes() {		
//		return shimPublicData.getccycodes();		
//	}
//		
//	@RequestMapping(value = "/paymenttypes.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<PaymentType> paymenttypes() {		
//		return shimPublicData.paymenttypes();		
//	}
//	/**
//	 * Get Market Orders
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws JsonProcessingException 
//	 * @throws JsonMappingException 
//	 */   /// Browse Offers 
//	@RequestMapping(value = "/offers.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<CustomerOrder> allOffers( @RequestBody WebFilterObject filter) {
//		return shimPublicData.allOffers(filter);
//	}	
//	
//	/**
//	 * Get the user trading orders & history
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/userTrades.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<ContractMatch> userTrades(@RequestParam(value = "uid", required = false) Long uid) {
//		return shimPublicData.userTrades(null,uid);
//	}
//	
////	@RequestMapping(value = "/getprofilepublic.json", method = RequestMethod.GET)
////	@ResponseBody
////	public UserObject getprofilepublic( @RequestParam(value = "uid", required = true) long uid) {
////		return shimPublicData.getprofilepublic(uid);
////	}
}
