package webtool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.UpdateLog;

@Repository("updateLogRepository")
public interface UpdateLogRepository extends CrudRepository<UpdateLog, Long>{

	@Query(value = "select * from updatelog d WHERE d.update_type in (:updatetypes) AND timestamp > CURRENT_TIMESTAMP - INTERVAL 31 DAY",nativeQuery = true)   // AND timestamp > CURRENT_TIMESTAMP - INTERVAL 31 DAY
	List<UpdateLog> findAllByType(@Param("updatetypes") List<Integer> updatetypes);
}
