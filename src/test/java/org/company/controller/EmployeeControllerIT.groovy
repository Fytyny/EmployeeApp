package org.company.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.company.dto.EmployeeDTO
import org.company.dto.GeneralResponseDTO
import org.company.dto.RegisteredEmployeeDTO
import org.company.entity.Employee
import org.company.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [EmployeeController])
class EmployeeControllerIT extends Specification {
    public static final String GENERATED_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
    @Autowired
    MockMvc mvc

    @Autowired
    EmployeeService employeeService

    @Autowired
    ObjectMapper objectMapper

    def '''should pass request body from Employee/insert endpoint to employeeService::insertEmployee method
     and return created status'''()
    {
        given: "request body sent by user"
        EmployeeDTO employeeParameter = new EmployeeDTO(
                name          : 'Jhon',
                surname       : 'Doe',
                grade         : 1,
                salary        : 2000
        )

        and: "employeeService::insertEmployee expected response"
        RegisteredEmployeeDTO insertEmployeeResponse = new RegisteredEmployeeDTO(employeeParameter, 1)

        1 * employeeService.insertEmployee(employeeParameter) >> Optional.of(insertEmployeeResponse)

        when: "endpoint Employee/insert meets user's request body"
        def results = mvc.perform(post("/Employee/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeParameter))
        )

        then:"user gets a response in json which should be his request plus generated id"
        results.andExpect(status().isCreated())
        .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
        .andExpect(content().json(objectMapper.writeValueAsString(insertEmployeeResponse)))
    }

    def '''should pass request body from Employee/insert endpoint to employeeService::insertEmployee method
     and return error message if repository's save method failed'''()
    {
        given: "request body sent by user"
        EmployeeDTO employeeParameter = new EmployeeDTO(
                name          : 'Jhon',
                surname       : 'Doe',
                grade         : 1,
                salary        : 2000
        )

        and: "employeeService::insertEmployee failed"
        1 * employeeService.insertEmployee(employeeParameter) >> Optional.empty()

        when: "endpoint Employee/insert meets user's request body"
        def results = mvc.perform(post("/Employee/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeParameter))
        )

        then:"user gets a response in json which should be his request plus generated id"
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.SERVER_ERROR)))
    }

    def '''should pass id from Employee/delete/{id} endpoint to employeeServide::deleteEmployee
            and return ok status if employee exists'''()
    {
        given: "id of existing user passed by user"
        Integer idToBeDeleted = 1

        and: "employeeService::deleteEmployee expected answer"
        1 * employeeService.deleteEmployeeById(idToBeDeleted) >> GeneralResponseDTO.EMPLOYEE_HAS_BEEN_DELETED

        when: "endpoint Employee/delete/{id} meets user's request id"
        def results = mvc.perform(delete("/Employee/delete/1"))

        then: "user gets ok status"
        results.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_HAS_BEEN_DELETED)))

    }

    def '''should pass id from Employee/delete/{id} endpoint to employeeServide::deleteEmployee
            and return not modified status if employee dos not exists'''()
    {
        given: "id of not existing employee passed by user"
        Integer idToBeDeleted = 1

        and: "employeeService::deleteEmployee expected answer"
        1 * employeeService.deleteEmployeeById(idToBeDeleted) >> GeneralResponseDTO.EMPLOYEE_NOT_DELETED

        when: "endpoint Employee/delete/{id} meets user's request id"
        def results = mvc.perform(delete("/Employee/delete/1"))

        then: "user gets not modified status"
        results.andExpect(status().isNotModified())
            .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_NOT_DELETED)))
    }

    def '''should pass id from Employee/{id} get endpoint to employeeService::findById method and return its data in json if exists''' ()
    {
        given: "id of employee passed by user"
        Integer idPassedByUser = 1
        and: "user with that id exists"
        RegisteredEmployeeDTO registeredEmployeeDTO = new RegisteredEmployeeDTO(
                new EmployeeDTO(
                        name    : 'John',
                        surname : 'Doe',
                        grade   : 2,
                        salary  : 3000
                ),1
        )
        and:"its data is returned by employeeService::findById method"
        1 * employeeService.findById(idPassedByUser) >> Optional.of(registeredEmployeeDTO)

        when: "endpoint Employee/{id} meets user request's id"
        def results = mvc.perform(get("/Employee/1"))

        then: "existing employee's info is returned to user"
        results.andExpect(status().isOk())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(registeredEmployeeDTO)))
    }


    def '''should pass id from Employee/{id} get endpoint to employeeService::findById method
             and return error message and status not found if not exists''' ()
    {
        given: "id of employee passed by user"
        Integer idPassedByUser = 1

        and: "user with that id not exists"
        and: "method employeeService::findById returns null"
        1 * employeeService.findById(idPassedByUser) >> Optional.empty()

        when: "endpoint Employee/{id} meets user request's id"
        def results = mvc.perform(get("/Employee/" + idPassedByUser))

        then: "error response with status not found is returned to user "
        results.andExpect(status().isNotFound())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS)))
    }

    def '''should pass employee data from Employee/update/{id} put endpoint to employeeService::updateEmployee
            and return status ok with response message if employee exists'''()
    {
        given: "id of employee passed by user"
        Integer idPassedByUser = 1

        and: "new employee data passed by user"
        EmployeeDTO employeeDTO = new EmployeeDTO(
                name     : "Jane"
        )

        and: "employee exists"
        1 * employeeService.updateEmployeOfIdWithData(employeeDTO, idPassedByUser) >> GeneralResponseDTO.EMPLOYEE_HAS_BEEN_UPDATED

        when: "endpoint Employee/update/{id} meets user's request"
        def result = mvc.perform(
                put("/Employee/update/" + idPassedByUser)
                .content(objectMapper.writeValueAsString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON)
        )

        then: "return positive response message with ok status"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_HAS_BEEN_UPDATED))
        )
    }

    def '''should pass employee data from Employee/update/{id} put endpoint to employeeService::updateEmployee
            and return status not found with response message if employee does not exists'''()
    {
        given: "id of employee passed by user"
        Integer idPassedByUser = 1

        and: "new employee data passed by user"
        EmployeeDTO employeeDTO = new EmployeeDTO(
                name     : "Jaine"
        )

        and: "employee does not exists"
        1 * employeeService.updateEmployeOfIdWithData(employeeDTO, idPassedByUser) >> GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS

        when: "endpoint Employee/update/{id} meets user's request"
        def result = mvc.perform(
                put("/Employee/update/" + idPassedByUser)
                        .content(objectMapper.writeValueAsString(employeeDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "return negative response message with not found status"
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS))
        )
    }

    def '''should pass searched employee data from /Employee/find post endpoint to employeeService::findBy method
             and return list of employees from that method'''()
    {
        given: "employee data passed to endpoint"
        EmployeeDTO employeeRequestData = new EmployeeDTO(
                name        : 'John'
        )

        and: "some data that meets that criteria exists on the server"
        List<RegisteredEmployeeDTO> data = Arrays.asList(
                new RegisteredEmployeeDTO(
                        new EmployeeDTO(
                                name    : 'John',
                                surname : 'Doe',
                                grade   : 1,
                                salary  : 2000
                        ),3
                ),
                new RegisteredEmployeeDTO(
                        new EmployeeDTO(
                                name    : 'Jane',
                                surname : 'Doe',
                                grade   : 2,
                                salary  : 3000
                        ),5
                )
        )
        1 * employeeService.findBy(employeeRequestData) >> data

        when: "enpoint /Employee/find meets user's request"
        def result = mvc.perform(
                post("/Employee/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestData))
        )

        then: "return list of employees that meets request's criteria"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(data)))
    }

    def '''should pass searched employee data from /Employee/find post endpoint to employeeService::findBy method
             and return empty list of employees if no employee meets requested criteria'''()
    {
        given: "employee data passed to endpoint"
        EmployeeDTO employeeRequestData = new EmployeeDTO(
                name        : 'John'
        )

        and: "no data that meets that criteria exists on the server"
        1 * employeeService.findBy(employeeRequestData) >> Collections.emptyList()

        when: "enpoint /Employee/find meets user's request"
        def result = mvc.perform(
                post("/Employee/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestData))
        )

        then: "return empty list in json"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(GENERATED_CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())))
    }

    @Configuration
    @ComponentScan(basePackages = ["org.company.controller"])
    static class McokConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        EmployeeService employeeService() {
            return detachedMockFactory.Mock(EmployeeService)
        }
    }
}
