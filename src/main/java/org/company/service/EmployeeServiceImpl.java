package org.company.service;

import org.company.controller.EmployeeField;
import org.company.dto.EmployeeDTO;
import org.company.dto.GeneralResponseDTO;
import org.company.dto.RegisteredEmployeeDTO;
import org.company.entity.Employee;
import org.company.repository.EmployeeRepository;
import org.company.repository.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository)
    {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Optional<RegisteredEmployeeDTO> insertEmployee(EmployeeDTO employeeDTO)
    {
        return save(Employee.fromEmployeeDTO(employeeDTO))
                .map(RegisteredEmployeeDTO::fromEmployee);
    }

    @Override
    public GeneralResponseDTO deleteEmployeeById(Integer id)
    {
        GeneralResponseDTO result;
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null)
        {
            result =  GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS;
        }
        else
        {
            if (delete(employee))
            {
                result = GeneralResponseDTO.EMPLOYEE_HAS_BEEN_DELETED;
            }
            else
            {
                result = GeneralResponseDTO.SERVER_ERROR;
            }
        }
        return result;
    }

    @Override
    public Optional<RegisteredEmployeeDTO> findById(Integer id)
    {
        return employeeRepository.findById(id)
                .map(RegisteredEmployeeDTO::fromEmployee);
    }

    @Override
    public GeneralResponseDTO updateEmployeOfIdWithData(EmployeeDTO employeeDTO, Integer id)
    {
        GeneralResponseDTO generalResponseDTO;
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null)
        {
            generalResponseDTO = GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS;
        }
        else
        {
            Employee newEmployeeData = Employee.fromEmployeeDTO(employeeDTO);
            newEmployeeData.setId(id);

            Employee employeeAfterUpdate = save(newEmployeeData).orElse(null);
            if (employeeAfterUpdate != null)
            {
                generalResponseDTO = GeneralResponseDTO.EMPLOYEE_HAS_BEEN_UPDATED;
            }
            else
            {
                generalResponseDTO = GeneralResponseDTO.SERVER_ERROR;
            }
        }
        return generalResponseDTO;
    }

    @Override
    public List<RegisteredEmployeeDTO> findBy(EmployeeDTO employeeDTO)
    {
        return employeeRepository.findByEmployeeDTO(employeeDTO).stream()
                .map(RegisteredEmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisteredEmployeeDTO> findBySpecification(Specification<Employee> employeeSpecification) {
        return employeeRepository.findAll(employeeSpecification).stream()
                .map(RegisteredEmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    private Optional<Employee> save(Employee employee)
    {
        try {
            return Optional.of(
                    employeeRepository.save(employee)
            );
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private boolean delete(Employee employee)
    {
        try{
            employeeRepository.delete(employee);
            return true;
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
