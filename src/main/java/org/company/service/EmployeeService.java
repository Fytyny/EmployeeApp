package org.company.service;

import org.company.controller.EmployeeField;
import org.company.dto.EmployeeDTO;
import org.company.dto.GeneralResponseDTO;
import org.company.dto.RegisteredEmployeeDTO;
import org.company.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
    Optional<RegisteredEmployeeDTO> insertEmployee(EmployeeDTO employeeDTO);
    GeneralResponseDTO deleteEmployeeById(Integer id);
    Optional<RegisteredEmployeeDTO> findById(Integer id);
    GeneralResponseDTO updateEmployeOfIdWithData(EmployeeDTO employeeDTO, Integer id);
    List<RegisteredEmployeeDTO> findBy(EmployeeDTO employeeDTO);
    List<RegisteredEmployeeDTO> findBySpecification(Specification<Employee> employeeSpecification);
}
