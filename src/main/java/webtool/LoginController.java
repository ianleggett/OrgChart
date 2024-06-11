package webtool;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/system")
public class LoginController {

	@RequestMapping(value = "/logout2", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}		
		return "redirect:/";// You can redirect wherever you want, but generally it's a good practice to
										// show login screen again.
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpSession session, String error, String logout) {
		HashMap<String, Object> model = new HashMap<String, Object>();
		if (error != null) {
			// WebUser exist = userRepository.findByEmail(email);
			if (session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
				model.put("error", session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
			} else {
				model.put("error", "Your username or password is invalid.");
			}
		} else if (logout != null) {
			model.put("message", "You have been logged out successfully.");
		}
		return new ModelAndView("login", model);
	}
	
}
