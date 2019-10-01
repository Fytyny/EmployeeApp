package org.company.util;

import org.company.controller.EmployeeField;
import org.company.entity.Employee;
import org.company.repository.EmployeeSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class SpecificationUtils {

    private SpecificationUtils(){}

    public static Specification<Employee> getSpecificationFromParameters(Map<EmployeeField, String[]> arguments)
    {
        Specification<Employee> employeeSpecification = null;
        for (Map.Entry<EmployeeField,String[]> entry : arguments.entrySet()){
            Specification<Employee> employeeS = null;
            for (String string : entry.getValue()){
                EmployeeSpecification from = EmployeeSpecification.from(entry.getKey(), string);
                if (employeeS == null)
                {
                    employeeS = from;
                }
                else
                {
                    employeeS = employeeS.or(from);
                }
            }
            if (employeeSpecification == null)
            {
                employeeSpecification = employeeS;
            }
            else
            {
                employeeSpecification = employeeSpecification.and(employeeS);
            }
        }
        return employeeSpecification;
    }
}
