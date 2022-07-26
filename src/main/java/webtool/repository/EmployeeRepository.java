package webtool.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import webtool.pojo.Employee;

@Repository("employeeRepository")
public interface EmployeeRepository extends CrudRepository<Employee, String>{

	
	
	
}
