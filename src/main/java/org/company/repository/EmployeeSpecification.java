package org.company.repository;

import org.company.controller.EmployeeField;
import org.company.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EmployeeSpecification implements Specification<Employee> {
    private EmployeeField employeeField;
    private String value;

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (value == null) {
            return criteriaBuilder.isNull(
                    root.get(employeeField.getFieldName()));
        } else {
            return criteriaBuilder.equal(
                    root.get(employeeField.getFieldName()), value);
        }
    }

    public static EmployeeSpecification from (EmployeeField field, String value)
    {
        EmployeeSpecification specification = new EmployeeSpecification();
        specification.employeeField = field;
        specification.value = value;
        return specification;
    }
}
