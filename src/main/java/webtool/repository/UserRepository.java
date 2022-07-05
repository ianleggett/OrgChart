package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.UserAndRole;


@Repository("UserRepository")
public interface UserRepository extends CrudRepository<UserAndRole, Long>{
	
		Optional<UserAndRole> findById(@Param("id") Long id);
		Optional<UserAndRole> findByUsername(@Param("username") String username);
		Optional<UserAndRole> findByEmail(@Param("email") String email);		
		@Query(value = "SELECT * FROM user d where d.validation_code = :validationCode AND d.validation_code IS NOT NULL",nativeQuery = true) 
		Optional<UserAndRole> findByValidationCode(@Param("validationCode") String validationCode);
				
		@Query(value = "SELECT * FROM user d where d.user_details_cid = :cid ",nativeQuery = true)
		Optional<UserAndRole> findByCid(@Param("cid") Long cid);
		
		List<UserAndRole> findAll();	
				
		@Transactional
	    Long deleteById(@Param("id") long id);
	
}
