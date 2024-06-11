package webtool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import webtool.pojo.EmailMsg;
import webtool.pojo.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

	
	@Query(value = "SELECT * FROM mediaevidence d where d.caseguid = :caseguid AND d.slot = :slot",nativeQuery = true) 		
	Optional<FileDB> findByCaseGuidAndSlot(@Param("caseguid") String caseguid,@Param("slot") int slot);
	
	@Query(value = "SELECT * FROM mediaevidence d where d.caseguid = :caseguid",nativeQuery = true) 
	List<FileDB> findByCaseGuid();
	
}
