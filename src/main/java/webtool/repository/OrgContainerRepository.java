package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.OrgContainer;

@Repository("orgContainerRepository")
public interface OrgContainerRepository extends CrudRepository<OrgContainer, Long>{

	List<OrgContainer> findByViewName(String name);

	@Query(value = "SELECT * FROM ocontainer d WHERE d.view_name = :viewname ORDER BY d.team_name",nativeQuery = true) 
	List<OrgContainer> findByViewTeamSorted(@Param("viewname") String viewname);

	
	@Query(value = "SELECT * FROM ocontainer d WHERE d.view_name = :viewname AND d.team_name = :teamname",nativeQuery = true) 
	Optional<OrgContainer> findByAll(@Param("viewname") String viewname, @Param("teamname") String teamname);
	
	@Query(value = "SELECT * FROM ocontainer d WHERE d.view_name = :viewname AND d.id = :cid",nativeQuery = true) 
	Optional<OrgContainer> findById(@Param("viewname") String viewname, @Param("cid") Long cid);
		
//	@Query(value = "SELECT new WebUpdateContainer(d.view_name,d.dept_name,d.group_name,d.team_name,COUNT(c.)) FROM ocontainer AS d GROUP BY c.year ORDER BY c.year DESC")
//	List<CommentCount> countTotalCommentsByYearClass();

}
