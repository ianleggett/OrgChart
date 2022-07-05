package webtool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.EmailMsg;


@Repository("EmailRepository")
public interface EmailRepository extends CrudRepository<EmailMsg, Long>{

		//public final static String WILDCARD = "/";
	
		EmailMsg findById(@Param("id") long id);
		List<EmailMsg> findAll();
				
		@Query(value = "SELECT * FROM email d where d.status = 2 ORDER BY issue_dt asc limit 1",nativeQuery = true) 
		EmailMsg getOneReadyToSend();
		
		@Query(value = "SELECT * FROM email d where d.status in (:inList) ORDER BY issue_dt asc",nativeQuery = true) 
		List<EmailMsg> getMailQueue(@Param("inList") List<Integer> inList);
		
		@Query(value = "SELECT count(1) FROM email d where d.status in (:inList)",nativeQuery = true) 
		long getMailQueueCount(@Param("inList") List<Integer> inList);
		
		//@Transactional
	    //Long deleteById(@Param("id") String id);		
		
}
