package webtool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.AuditAction;

@Repository("auditRepository")
public interface AuditRepository extends CrudRepository<AuditAction, Long>{

		//public final static String WILDCARD = "/";
	
		AuditAction findById(@Param("id") long id);
		List<AuditAction> findByStaffName(@Param("staffName") String staffName);
		List<AuditAction> findByWhenDt(@Param("when") Date when);
				
		List<AuditAction> findAll();	
		
		@Transactional
	    Long deleteById(@Param("id") long id);
	
}
