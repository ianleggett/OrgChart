package webtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtool.service.ShimPrimaryData;

@Secured({ "ROLE_SUPERUSER","ROLE_ADMIN", "ROLE_USER"})
@RestController
@RequestMapping("/system")
public class ControllerPrimaryData {

	@Autowired
	ShimPrimaryData shimPrimaryData;	
	

}
