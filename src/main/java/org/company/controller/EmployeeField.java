package org.company.controller;

public enum EmployeeField {
    NAME("name"),
    SURNAME("surname"),
    GRADE("grade"),
    SALARY("salary");

    String fieldName;
    EmployeeField(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName(){
        return fieldName;
    }
}
