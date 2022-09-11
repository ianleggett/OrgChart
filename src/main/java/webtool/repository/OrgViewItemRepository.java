package webtool.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.NamedNativeQuery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.OrgViewItem;
import webtool.pojo.UtilAggCount;

@Repository("orgViewItemRepository")
public interface OrgViewItemRepository extends CrudRepository<OrgViewItem, Long>{

	@Query(value = "SELECT * FROM oviewitem d WHERE d.view_name = :viewname AND d.i_num = :iNum",nativeQuery = true) 
	List<OrgViewItem> findByviewNameAndiNum(@Param("viewname") String viewname,@Param("iNum") String iNum);
	List<OrgViewItem> findByViewName(String viewname);
	
	@Query(value = "select d.container_id AS cid, COUNT(1) as cnt from oviewitem d WHERE d.view_name=:viewname GROUP BY d.container_id",nativeQuery = true)
	List<Object[]> countContainerUsage(@Param("viewname") String viewname);
}
