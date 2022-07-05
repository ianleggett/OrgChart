package webtool.service;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webtool.pojo.UserAndRole;
import webtool.pojo.WebChangeUserDetails;
import webtool.repository.CcyRepository;
import webtool.repository.PaymentTypeRepository;
import webtool.repository.UserRepository;
import webtool.security.SecurityUserDetailInterface;

@Service("shimDataService")
public class ShimDataServices {

	static Logger log = Logger.getLogger(ShimDataServices.class);
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	private SecurityUserDetailInterface securityService;
	@Autowired
	CcyRepository fxcodeRepository;
	@Autowired
	PaymentTypeRepository paymentTypeRepository;
	

	public WebChangeUserDetails getuserdetails() {
		Optional<UserAndRole> optusr = securityService.getCurrentUser();
		if (optusr.isPresent()) {
			return new WebChangeUserDetails(optusr.get());
		}
		return null;
	}
	
}
