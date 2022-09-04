package webtool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import webtool.pojo.AggContList;
import webtool.pojo.FileDB;
import webtool.pojo.GoJSData;
import webtool.pojo.MermaidData;
import webtool.pojo.OrgContainer;
import webtool.pojo.RespStatus;
import webtool.pojo.TableData;
import webtool.pojo.UserAndRole;
import webtool.pojo.ViewByType;
import webtool.pojo.WebEmployeeView;
import webtool.pojo.WebStatusUpdate;
import webtool.pojo.WebUpdateContainer;
import webtool.pojo.WebViewUpdate;
import webtool.service.DataLoaderService;
import webtool.service.ShimPublicData;
import webtool.service.StorageService;
import webtool.utils.CoreDAO;

@RestController
public class PublicController {

	public static String SESS_VIEW = "session_view";
	public static String SESS_DEPT = "session_dept";
	
	
	static Logger log = Logger.getLogger(PublicController.class);
	@Autowired
	ShimPublicData shimPublicData;
	@Autowired
	StorageService storageService;
	@Autowired
	CoreDAO coreDAO;

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
	@PostMapping("/importstaff")
	public ModelAndView importStaff(@RequestParam(name = "file") MultipartFile file) {
		log.info("Importing Staff");
		final ModelAndView mv = new ModelAndView("uploadstatus");
		try {
			Path destFile = storageService.store(file);
			String message = "Uploaded the file successfully: " + file.getOriginalFilename();
			log.info(message);
			coreDAO.getDataService().uploadStaff(destFile);
		} catch (Exception e) {
			log.error("Could not upload the file: " + file.getOriginalFilename() + "!");			
		}
		return mv;
	}
	
	@PostMapping("/importgroup")
	public ModelAndView importGroup(@RequestParam(name = "file") MultipartFile file,@RequestParam(name = "view",required = true) String vname) {
		log.info("Importing group");
		final ModelAndView mv = new ModelAndView("uploadstatus");
		try {
			Path destFile = storageService.store(file);
			String message = "Uploaded the file successfully: " + file.getOriginalFilename();
			log.info(message);
			coreDAO.getDataService().uploadGroup(destFile,vname);
		} catch (Exception e) {
			log.error("Could not upload the file: " + file.getOriginalFilename() + "!");			
		}
		return mv;
	}
	
//	@PostMapping("/startImport")
//	public RespStatus startImport() {
//		log.info("Start Import");
//		return coreDAO.startImport();
//	}
	
	@RequestMapping(value = "/updateStatus.json", method = RequestMethod.GET)
	@ResponseBody
	public WebStatusUpdate updateStatus() {
		WebStatusUpdate wsu = new WebStatusUpdate(coreDAO.getDataService().getStatus(), !coreDAO.getDataService().isInProgress());		
		return wsu;		
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

	
	private String getStrSetting(final String vStr,final Object sess, final String defaultStr) {
		final String res;
		if  ((vStr!=null)&&(!vStr.isBlank())) {
			res = vStr;			 
		}else if (sess != null) {
			res = sess.toString();
		}else {
			res = defaultStr;
		}
		return res;
	}
	
	private void setSessionDetails(HttpServletRequest request,ModelAndView mv,String vStr, String vDept) {
		final Object sessview = request.getSession().getAttribute(SESS_VIEW);		
		final String view = getStrSetting(vStr,sessview,CoreDAO.DEFAULT_VIEW);
		request.getSession().setAttribute(SESS_VIEW, view);
		final Object sessdep = request.getSession().getAttribute(SESS_DEPT);
		final String dept = getStrSetting(vDept,sessdep,CoreDAO.DEFAULT_DEPT);
		request.getSession().setAttribute(SESS_DEPT, dept);
		mv.addObject("view",view);
		mv.addObject("dept",dept);		
	}
	
	@RequestMapping(value = "/staff")
	public ModelAndView staff( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("staff");
		setSessionDetails(request,mv,viewName,null);		
		return mv;
	}
	@RequestMapping(value = "/containerEdit")
	public ModelAndView containers( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("containerEdit");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}
	@RequestMapping(value = "/viewEdit")
	public ModelAndView views( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("viewEdit");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}
	
	@RequestMapping(value = "/diag")
	public ModelAndView diag( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName,
			@RequestParam(value = "d", required = false) String dept) {		
		ModelAndView mv = new ModelAndView("diag");		
		setSessionDetails(request,mv,viewName,dept);		
		return mv;
	}
	
	@RequestMapping(value = "/uploadstaff")
	public ModelAndView uploadstaff( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("uploadstaff");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}
	
	@RequestMapping(value = "/uploadgroup")
	public ModelAndView uploadgroup( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("uploadgroup");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}
	
	@RequestMapping(value = "/uploadstatus")
	public ModelAndView uploadstatus( HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new  ModelAndView("uploadstatus");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}
	
	@RequestMapping(value = "/orgdata_gojs.json", method = RequestMethod.GET)
	@ResponseBody
	public GoJSData getGoJSData(@RequestParam(name = "v",required = true) String viewName, 
								@RequestParam(name = "d",required = false) String dept,
								@RequestParam(name = "l",required = false) String linksStr) {
		final boolean links = linksStr!=null ? true : false;
		final boolean leavers = false;
		return coreDAO.genModelGoJS(viewName,dept, links,leavers, ViewByType.ViewByTeam);	
	}
	
	@RequestMapping(value = "/containerdata.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<WebUpdateContainer> getContainers(@RequestParam(name = "v",required = true) String viewName) {
		return new TableData<WebUpdateContainer>(coreDAO.getContainers(viewName,ViewByType.ViewByTeam));
	}
	
	@RequestMapping(value = "/containerAggData.json", method = RequestMethod.GET)
	@ResponseBody
	public AggContList getAggContainerData(HttpServletRequest request, @RequestParam(name = "v",required = true) String viewName,@RequestParam(name = "viewType",required = true) String viewtype) {
		
		return coreDAO.getAggContainerData(viewName,ViewByType.valueOf(viewtype)/*ViewByType.ViewByTeam*/);
	}
	
	
	@RequestMapping(value = "/viewdata.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<WebViewUpdate> getViews() {
		return new TableData<WebViewUpdate>(coreDAO.getViews());
	}

	@RequestMapping(value = "/staffdata.json", method = RequestMethod.GET)
	@ResponseBody
	public TableData<WebEmployeeView> getStaffData(@RequestParam(name = "v",required = true) String viewName, @RequestParam(name = "d",required = false) String dept) {
		final boolean leavers = false;
		return new TableData<WebEmployeeView>(coreDAO.getViewData(viewName,dept,leavers,ViewByType.ViewByTeam));
	}

	@RequestMapping(value = "/updateStaff.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus updateStaff(@RequestBody WebEmployeeView postData) {
		log.info("updateStaff : " + postData);
		return coreDAO.updateStaffContainer(postData);
	}

	@RequestMapping(value = "/updateContainer.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus updateContainer(@RequestBody WebUpdateContainer postData) {
		log.info("updateContainer : " + postData);
		return coreDAO.updateContainer(postData);
	}

	@RequestMapping(value = "/addContainer.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus addContainer(@RequestBody WebUpdateContainer postData) {
		log.info("addContainer : " + postData);
		return coreDAO.addContainer(postData);
	}

	@RequestMapping(value = "/updateView.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus updateView(@RequestBody WebViewUpdate postData) {
		log.info("updateContainer : " + postData);
		return coreDAO.updateView(postData);
	}

	@RequestMapping(value = "/addView.json", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public RespStatus addView(@RequestBody WebViewUpdate postData) {
		log.info("addContainer : " + postData);
		return coreDAO.addView(postData);
	}

	@RequestMapping(value = "/")
	public ModelAndView home(HttpServletRequest request, @RequestParam(value = "v", required = false) String viewName) {
		ModelAndView mv = new ModelAndView("home");
		setSessionDetails(request,mv,viewName,null);
		return mv;
	}

}
