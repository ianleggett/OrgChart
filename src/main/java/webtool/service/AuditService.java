package webtool.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import webtool.pojo.AuditAction;
import webtool.repository.AuditRepository;

@Service("auditService")
public class AuditService {
	
	
	@Autowired
	AuditRepository auditRepository;
	/**
	 * Create and store AuditAction
	 * 
	 * @param textDesc
	 * @return
	 */
	public AuditAction createAudit(String textDesc) {
		String currentUserName = "-user-not-logged-in-";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			currentUserName = authentication!= null ? authentication.getName() : "System";
		}

		AuditAction aact = new AuditAction(currentUserName, new Date(), textDesc);
		return auditRepository.save(aact);
	}
	
}
