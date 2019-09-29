package org.company.service;

import org.company.dto.EmployeeDTO;
import org.company.dto.GeneralResponseDTO;
import org.company.dto.RegisteredEmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<RegisteredEmployeeDTO> insertEmployee(EmployeeDTO employeeDTO);
    GeneralResponseDTO deleteEmployeeById(Integer id);
    Optional<RegisteredEmployeeDTO> findById(Integer id);
    GeneralResponseDTO updateEmployeOfIdWithData(EmployeeDTO employeeDTO, Integer id);
    List<RegisteredEmployeeDTO> findBy(EmployeeDTO employeeDTO);
}
