package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.OrgViewItem;

@Repository("orgViewItemRepository")
public interface OrgViewItemRepository extends CrudRepository<OrgViewItem, Long>{

	@Query(value = "SELECT * FROM oviewitem d WHERE d.view_name = :viewname AND d.i_num = :iNum",nativeQuery = true) 
	Optional<OrgViewItem> findByviewNameAndiNum(@Param("viewname") String viewname,@Param("iNum") String iNum);
	List<OrgViewItem> findByViewName(String viewname);
}
