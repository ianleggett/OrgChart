package webtool.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.Ccy;


@Repository("ccyRepository")
public interface CcyRepository extends CrudRepository<Ccy, Long>{

		//public final static String WILDCARD = "/";
	
		Optional<Ccy> findById(@Param("id") long id);	
				
		Optional<Ccy> findByName(@Param("name") String name);
		
		@Query(value = "SELECT * FROM ccy d WHERE d.enable > 0",nativeQuery = true) 
		List<Ccy> findEnabledAll();
		
		List<Ccy> findAll();	
		
		@Transactional
	    Long deleteById(@Param("id") long id);
	
}
