package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import webtool.pojo.UserInfo;

@Repository("UserInfoRepository")
public interface UserInfoRepository extends CrudRepository<UserInfo, Long>{
		
			Optional<UserInfo> findByCid(@Param("cid") Long id);
			
			@Transactional
		    Long deleteByCid(@Param("cid") long id);

	
}
