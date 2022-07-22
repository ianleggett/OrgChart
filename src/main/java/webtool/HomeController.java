package webtool;
//
//import java.io.IOException;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//@Controller
//public class HomeController {
//
//	static Logger log = Logger.getLogger(HomeController.class);
//	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
//	
//	
//	@RequestMapping(value = "/")
//	public ModelAndView profile() throws IOException {
//		ModelAndView mv = new ModelAndView("main");
//		return mv;
//	}
//
//}