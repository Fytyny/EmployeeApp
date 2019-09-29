package org.company.repository;

import org.company.dto.EmployeeDTO;
import org.company.entity.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    @Query("SELECT e FROM Employee e WHERE " +
            "(:#{#dto.name} is null or (:#{#dto.name} is not null and e.name = :#{#dto.name} )) and " +
            "(:#{#dto.surname} is null or (:#{#dto.surname} is not null and e.surname = :#{#dto.surname} )) and " +
            "(:#{#dto.grade} is null or (:#{#dto.grade} is not null and e.grade = :#{#dto.grade} )) and " +
            "(:#{#dto.salary} is null or (:#{#dto.salary} is not null and e.salary = :#{#dto.salary} ))")
    List<Employee> findByEmployeeDTO(@Param("dto") EmployeeDTO employeeDTO);
}
