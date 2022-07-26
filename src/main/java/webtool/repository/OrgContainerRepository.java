package webtool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.OrgContainer;

@Repository("orgContainerRepository")
public interface OrgContainerRepository extends CrudRepository<OrgContainer, Long>{

	List<OrgContainer> findByViewName(String name);
	
	@Query(value = "SELECT * FROM ocontainer d WHERE d.view_name = :viewname ORDER BY d.dept_name,d.group_name,d.team_name",nativeQuery = true) 
	List<OrgContainer> findByViewNameSorted(@Param("viewname") String viewname);
}
