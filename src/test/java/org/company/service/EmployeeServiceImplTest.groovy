package org.company.service

import org.company.dto.EmployeeDTO
import org.company.dto.GeneralResponseDTO
import org.company.dto.RegisteredEmployeeDTO
import org.company.entity.Employee
import org.company.repository.EmployeeRepository
import spock.lang.*
import spock.mock.DetachedMockFactory

import java.util.stream.Collectors

class EmployeeServiceImplTest extends Specification {

    EmployeeService employeeService
    EmployeeRepository employeeRepository

    def setup() {
        employeeRepository = Mock(EmployeeRepository)
        employeeService = new EmployeeServiceImpl(employeeRepository)
    }

    def "should return new employee data with id if successfully inserted"() {
        given:  "some new employee data"
        EmployeeDTO employeeDTO = new EmployeeDTO(
                name    : 'John',
                surname : 'Doe',
                grade   : 1,
                salary  : 2000
        )

        and:    "is successfully saved by repository"
        1 * employeeRepository.save(_) >> new Employee(
                id      : 1,
                name    : employeeDTO.name,
                surname : employeeDTO.surname,
                grade   : employeeDTO.grade,
                salary  : employeeDTO.salary
        )

        when:
        Optional<RegisteredEmployeeDTO> result = employeeService.insertEmployee(employeeDTO)

        then:
        result.isPresent()
        result.get() == new RegisteredEmployeeDTO(employeeDTO, 1)
    }

    def "should return empty if unsuccessfully inserted"() {
        given:  "some new employee data"
        EmployeeDTO employeeDTO = new EmployeeDTO(
                name    : 'John',
                surname : 'Doe',
                grade   : 1,
                salary  : 2000
        )

        and:    "employee repository thorws a runtime exception"
        1 * employeeRepository.save(_) >> { throw new RuntimeException() }

        when:
        Optional<RegisteredEmployeeDTO> result = employeeService.insertEmployee(employeeDTO)

        then:
        !result.isPresent()
    }
    def "should delete employee with id if exists and return positive message"() {
        given:  "employee with id 1 does exists"
        Employee employeeWithId1 = new Employee(
                name    : 'Norbert',
                surname : 'Norbertowski',
                grade   : 1,
                salary  : 2000,
                id      : 1
        )

        and:    "repository can find it"
        1 * employeeRepository.findById(1) >> Optional.of(employeeWithId1)

        and:    "delete it"
        1 * employeeRepository.delete(employeeWithId1)

        when:   "user calls employeeService::deleteEmployeeById"
        GeneralResponseDTO result = employeeService.deleteEmployeeById(1)

        then:   "should return confirmation message that employee has been deleted"
        result == GeneralResponseDTO.EMPLOYEE_HAS_BEEN_DELETED
    }

    def "should not delete employee with id if not exists and return negative message"() {
        given:  "employee with id 1 does not exist"
        Employee employeeWithId1 = null

        and:    "repository cannot find it"
        1 * employeeRepository.findById(1) >> Optional.empty()

        when:   "user calls employeeService::deleteEmployeeById"
        GeneralResponseDTO result = employeeService.deleteEmployeeById(1)

        then:   "should return negative message that employee has not been deleted"
        result == GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS
    }

    def "should not delete employee with id if exists but delete method has thrown an exception and return negative message"() {
        given:  "employee with id 1 exists"
        Employee employeeWithId1 = new Employee(
                name    : 'Norbert',
                surname : 'Norbertowski',
                grade   : 1,
                salary  : 2000,
                id      : 1
        )

        and:    "repository can find it"
        1 * employeeRepository.findById(1) >> Optional.of(employeeWithId1)

        and:    "but cannot delete it"
        1 * employeeRepository.delete(employeeWithId1) >> { throw new RuntimeException()}

        when:   "user calls employeeService::deleteEmployeeById"
        GeneralResponseDTO result = employeeService.deleteEmployeeById(1)

        then:   "should return negative message that server thrown error"
        result == GeneralResponseDTO.SERVER_ERROR
    }

    def "should return employee data if employee with id 1 exists"() {
        given:  "employee with id 1 exists"
        Employee employeeWithId1 = new Employee(
                name    : 'Norbert',
                surname : 'Norbertowski',
                grade   : 1,
                salary  : 2000,
                id      : 1
        )

        and:    "repository can find it"
        employeeRepository.findById(1) >> Optional.of(employeeWithId1)

        when:   "user calls employeeService::findById"
        Optional<RegisteredEmployeeDTO> result = employeeService.findById(1)

        then:   "should return employee data "
        result.isPresent()
        result.get() == RegisteredEmployeeDTO.fromEmployee(employeeWithId1)
    }

    def "should return empty optional if employee with id 1 does not exist"() {
        given:  "employee with id 1 not exists"
        Employee employeeWithId1 = null

        and:    "repository cannot find it"
        employeeRepository.findById(1) >> Optional.empty()

        when:   "user calls employeeService::findById"
        Optional<RegisteredEmployeeDTO> result = employeeService.findById(1)

        then:   "should return empty optional "
        !result.isPresent()
    }

    def "should update employee if exists and return positive message"() {
        given:  "employee with id 1 exists"
        Employee employeeWithId1 = new Employee(
                name    : 'Norbert',
                surname : 'Norbertowski',
                grade   : 1,
                salary  : 2000,
                id      : 1
        )

        and:    "repository can find it"
        employeeRepository.findById(1) >> Optional.of(employeeWithId1)

        and:    "update id"
        1 * employeeRepository.save(_) >> employeeWithId1

        when:   "user calls employeService::updateEmployeOfIdWithData method"
        GeneralResponseDTO result = employeeService.updateEmployeOfIdWithData(
                new EmployeeDTO (
                     surname    :   'Nowak'
                 ), 1
        )

        then:   "should return employee has been updated message"
        result == GeneralResponseDTO.EMPLOYEE_HAS_BEEN_UPDATED
    }

    def "should not update employee if does not exists and return negative message"() {
        given:  "employee with id 1 does not exist"
        Employee employeeWithId1 = null

        and:    "repository cannot find it"
        employeeRepository.findById(1) >> Optional.empty()

        when:   "user calls employeService::updateEmployeOfIdWithData method"
        GeneralResponseDTO result = employeeService.updateEmployeOfIdWithData(
                new EmployeeDTO (
                        surname    :   'Nowak'
                ), 1
        )

        then:   "should return employee does not exist message"
        result == GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS
    }

    def "should not update employee if exists but repository save method has thrown an exception"() {
        given:  "employee with id 1 exists"
        Employee employeeWithId1 = new Employee(
                name    : 'Norbert',
                surname : 'Norbertowski',
                grade   : 1,
                salary  : 2000,
                id      : 1
        )
        and:    "repository can find it"
        1 * employeeRepository.findById(1) >> Optional.of(employeeWithId1)

        and:    "repository save mthod throws an exception"
        1 * employeeRepository.save(_) >> {throw new RuntimeException()}

        when:   "user calls employeService::updateEmployeOfIdWithData method"
        GeneralResponseDTO result = employeeService.updateEmployeOfIdWithData(
                new EmployeeDTO (
                        surname    :   'Nowak'
                ), 1
        )

        then:   "should return server error message"
        result == GeneralResponseDTO.SERVER_ERROR
    }

    def "should return list of employees that matches a description provieded by user if employees like that exists"() {
        given:  "request from user for employees with name John"
        EmployeeDTO employeeDTO = new EmployeeDTO (
                name    : 'John'
        )

        and:    "employees with this name exists on the server"
        List employeesOnServer = [
                new Employee(
                        name    : 'John',
                        surname : 'Doe',
                        id      : 1
                ),
                new Employee(
                        name    : 'John',
                        surname : 'Smith',
                        id      : 2
                )
        ]

        and:    "repository can find them"
        1 * employeeRepository.findByEmployeeDTO(employeeDTO) >> employeesOnServer

        when:   "user calls employeeService::findBy method"
        List<RegisteredEmployeeDTO> result = employeeService.findBy(employeeDTO)

        then:
        !result.isEmpty()
        result == employeesOnServer.stream()
                        .map{employee -> RegisteredEmployeeDTO.fromEmployee(employee)}
                        .collect(Collectors.toList())
    }

    def "should return empty list of employees if no employees like provided in a request exists"() {
        given:  "request from user for employees with name John"
        EmployeeDTO employeeDTO = new EmployeeDTO (
                name    : 'John'
        )

        and:    "no with this name exists on the server"
        List employeesOnServer = []

        and:    "repository cannot find them"
        1 * employeeRepository.findByEmployeeDTO(employeeDTO) >> employeesOnServer

        when:   "user calls employeeService::findBy method"
        List<RegisteredEmployeeDTO> result = employeeService.findBy(employeeDTO)

        then:
        result.isEmpty()
    }
}
