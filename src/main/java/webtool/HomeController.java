package webtool;
//package webtool;
//
//import java.io.IOException;
//import javax.servlet.http.HttpSession;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import webtool.utils.SettingsDAO;
//
//@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//@Controller
//@RequestMapping("/user")
//public class HomeController {
//
//	static Logger log = Logger.getLogger(HomeController.class);
//	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
//	
//	@Autowired
//	private SettingsDAO settingsDAO;
//	
//	
//	@RequestMapping(value = "/profile")
//	public ModelAndView profile(HttpSession session, @RequestParam(value = "m", required = false) Integer basknum) throws IOException {
//		ModelAndView mv = new ModelAndView("userprofile");
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String name = auth.getName();
//		return mv;
//	}
//	
//	@RequestMapping(value = "/ctr_pre", method = { RequestMethod.GET })
//	public ModelAndView contractpre() {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("ctr_pre");
//		return model;
//	}
//	
//	@RequestMapping(value = "/ctr_own", method = { RequestMethod.GET })
//	public ModelAndView contracttester() {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("ctr_own");
//		return model;
//	}
//
//	@RequestMapping(value = "/pretrade", method = { RequestMethod.GET })
//	public ModelAndView pretrade(@RequestParam(value = "oid", required = true) String oid) {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("pretrade");
//		// string back to long
//		model.addObject("oid", oid );
//		return model;
//	}
//	@RequestMapping(value = "/opentrade", method = { RequestMethod.GET })
//	public ModelAndView opentrade() {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("opentrade");
//		return model;
//	}
//	
//	@RequestMapping(value = "/wallets", method = { RequestMethod.GET })
//	public ModelAndView wallets() {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("wallets");
//		return model;
//	}
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/trades")
//	public ModelAndView trades() throws IOException {
//		ModelAndView mv = new ModelAndView("trades");
//		return mv;
//	}
//	
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/offers")
//	public ModelAndView offers() throws IOException {
//		ModelAndView mv = new ModelAndView("offers");	
//		return mv;
//	}
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/newoffer")
//	public ModelAndView newoffer() throws IOException {
//		ModelAndView mv = new ModelAndView("newoffer");			
//		double feepct = settingsDAO.getSettings().getCryptoSettings().getCurrent().getSellerfeePct();
//		log.info("Setting seller fees: "+feepct);
//		mv.addObject("feepct", feepct);		
//		return mv;
//	}
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/algosummary")
//	public ModelAndView algosummary(@RequestParam(value = "algoid", required = true) String algoid, @RequestParam(value = "prod", required = false) Boolean prod) throws IOException {
//		ModelAndView mv = new ModelAndView("algosummary");	
//		mv.addObject("algoid", algoid);	
//		if (prod!=null)
//			mv.addObject("prod", prod);
//		else
//			mv.addObject("prod", true);
//		return mv;
//	}	
//
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/fundsadd")
//	public ModelAndView fundsadd() throws IOException {
//		ModelAndView mv = new ModelAndView("fundsadd");	
//		return mv;
//	}
//	
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/fundswithdraw")
//	public ModelAndView fundswithdraw() throws IOException {
//		ModelAndView mv = new ModelAndView("fundswithdraw");	
//		return mv;
//	}
//	
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/algos")
//	public ModelAndView algos() throws IOException {
//		ModelAndView mv = new ModelAndView("algos");	
//		return mv;
//	}	
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/backtest")
//	public ModelAndView backtest() throws IOException {
//		ModelAndView mv = new ModelAndView("backtest");	
//		return mv;
//	}	
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@RequestMapping(value = "/bookview")
//	public ModelAndView bookview() throws IOException {
//		ModelAndView mv = new ModelAndView("bookview");	
//		return mv;
//	}	
//}
