package webtool;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import webtool.pojo.FileDB;
import webtool.pojo.MermaidData;
import webtool.pojo.RespStatus;
import webtool.pojo.UserAndRole;
import webtool.service.ShimPublicData;
import webtool.service.StorageService;

@RestController
public class PublicController {

	static Logger log = Logger.getLogger(PublicController.class);
	@Autowired
	ShimPublicData shimPublicData;
	@Autowired
	StorageService storageService;
	
	
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
	public void showMedia(@RequestParam(name = "caseguid") String caseguid,
			@RequestParam(name = "slot") int slotnum, HttpServletResponse response)
			throws ServletException, IOException {		
		Optional<FileDB> file = storageService.getFile(caseguid,slotnum);
		if (file.isPresent()) {
			response.setContentType(file.get().getType());
			response.getOutputStream().write(file.get().getData());
		}
		response.getOutputStream().close();
	}

	@RequestMapping(value = "/orgdata.json", method = RequestMethod.GET)
	@ResponseBody
	public MermaidData getMermaid() {		
		return new MermaidData("graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]");		
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
