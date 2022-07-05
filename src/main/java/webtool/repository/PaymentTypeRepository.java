package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.PaymentType;

@Repository("paymentTypeRepository")
public interface PaymentTypeRepository extends CrudRepository<PaymentType, Long>{

		//public final static String WILDCARD = "/";
	
		Optional<PaymentType> findById(@Param("id") long id);	
				
		Optional<PaymentType> findByName(@Param("name") String name);
		
		@Query(value = "SELECT * FROM paymenttype d WHERE d.enabled = true ",nativeQuery = true) 
		List<PaymentType> findEnabledAll();
		
		List<PaymentType> findAll();	
		
		@Transactional
	    Long deleteById(@Param("id") long id);
	

}
