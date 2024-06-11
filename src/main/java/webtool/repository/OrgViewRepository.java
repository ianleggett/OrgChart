package webtool.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import webtool.pojo.OrgView;


@Repository("orgViewRepository")
public interface OrgViewRepository extends CrudRepository<OrgView, String>{


}
