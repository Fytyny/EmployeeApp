package org.company.repository

import org.apache.commons.lang3.RandomStringUtils
import org.company.dto.EmployeeDTO
import org.company.entity.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom

@DataJpaTest
class EmployeeRepositoryIT extends Specification {

    @Autowired
    private EmployeeRepository employeeRepository;

    def setup()
    {
        insertTestData()
    }

    def '''should get all employees with name John'''()
    {
        given:
        EmployeeDTO testEmployee = new EmployeeDTO(
                name: 'John'
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.size() == 3
    }

    def '''should get all employees if empty DTO passed'''()
    {
        given:
        EmployeeDTO testEmployee = new EmployeeDTO(
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.size() ==  4
    }

    def '''should get all employees with surname Doel'''()
    {
        given:
        EmployeeDTO testEmployee = new EmployeeDTO(
                surname: 'Doel'
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.size() ==  1
    }

    def '''should get all employees with name John, salary 500 and surname Doe'''()
    {
        given:
        EmployeeDTO testEmployee = new EmployeeDTO(
                name: 'John',
                surname: 'Doe',
                salary: 500
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.size() ==  2
    }

    def '''should get all employees with name John, salary 500, surname Doe and grade 0'''()
    {
        given:
        EmployeeDTO testEmployee = new EmployeeDTO(
                name: 'John',
                surname: 'Doe',
                salary: 500,
                grade: 0
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.size() ==  1
    }

    def '''should get no employees with name Jane'''()
    {
        given: "employee name that does not exist in db"
        EmployeeDTO testEmployee = new EmployeeDTO(
                name: 'Jane',
                surname: 'Doe',
                salary: 500,
                grade: 0
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.isEmpty()
    }

    def '''should get no employees with grade 2'''()
    {
        given: "employee name that does not exist in db"
        EmployeeDTO testEmployee = new EmployeeDTO(
                grade: 2
        )

        when:
        List<Employee> employeeList = employeeRepository.findByEmployeeDTO(testEmployee)

        then:
        employeeList.isEmpty()
    }

    def '''should generate id'''()
    {
        given:
        Employee employee = new Employee(
                name: 'Han'
        )
        when:
        Employee save = employeeRepository.save(employee)

        then:
        save.getId() != null
    }

    def insertTestData()
    {
        employeeRepository.save(
                new Employee(
                        name: 'John',
                        surname: 'Doe',
                        salary: 500,
                        grade: 1
                )
        )

        employeeRepository.save(
                new Employee(
                        name: 'John',
                        surname: 'Doel',
                        salary: 500,
                        grade: 1
                )
        )

        employeeRepository.save(
                new Employee(
                        name: 'John',
                        surname: 'Doe',
                        salary: 500,
                        grade: 0
                )
        )

        employeeRepository.save(
                new Employee(
                        name: 'Johnny',
                        surname: 'Doe',
                        salary: 459,
                        grade: 1
                )
        )
    }

}