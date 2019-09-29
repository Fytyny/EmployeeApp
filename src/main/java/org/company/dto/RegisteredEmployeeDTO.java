package org.company.dto;

import org.company.entity.Employee;

import java.util.Objects;

public class RegisteredEmployeeDTO {
    private EmployeeDTO employeeDTO;
    private Integer id;

    private RegisteredEmployeeDTO(){
    }

    public RegisteredEmployeeDTO(EmployeeDTO employeeDTO, Integer id)
    {
        this.employeeDTO = employeeDTO;
        this.id = id;
    }

    public static RegisteredEmployeeDTO fromEmployee(Employee employee)
    {
        RegisteredEmployeeDTO result = new RegisteredEmployeeDTO();
        if (employee != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setGrade(employee.getGrade());
            employeeDTO.setSalary(employee.getSalary());
            employeeDTO.setName(employee.getName());
            employeeDTO.setSurname(employee.getSurname());
            result.employeeDTO = employeeDTO;
            result.id = employee.getId();
        }
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return employeeDTO.getName();
    }

    public void setName(String name) {
        employeeDTO.setName(name);
    }

    public String getSurname() {
        return employeeDTO.getSurname();
    }

    public void setSurname(String surname) {
        employeeDTO.setSurname(surname);
    }

    public Integer getGrade() {
        return employeeDTO.getGrade();
    }

    public void setGrade(Integer grade) {
        employeeDTO.setGrade(grade);
    }

    public Integer getSalary() {
        return employeeDTO.getSalary();
    }

    public void setSalary(Integer salary) {
        employeeDTO.setSalary(salary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredEmployeeDTO that = (RegisteredEmployeeDTO) o;
        return Objects.equals(employeeDTO, that.employeeDTO) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeDTO, id);
    }

    @Override
    public String toString() {
        return "RegisteredEmployeeDTO{" +
                "employeeDTO=" + employeeDTO +
                ", id=" + id +
                '}';
    }
}
