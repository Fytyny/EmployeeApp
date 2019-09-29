package org.company.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class GeneralResponseDTO {
    public static final GeneralResponseDTO EMPLOYEE_DOES_NOT_EXISTS = new GeneralResponseDTO ("Employee does not exist!",HttpStatus.NOT_FOUND);
    public static final GeneralResponseDTO EMPLOYEE_HAS_BEEN_DELETED = new GeneralResponseDTO ("Employee has been deleted",HttpStatus.OK);
    public static final GeneralResponseDTO EMPLOYEE_HAS_BEEN_UPDATED = new GeneralResponseDTO ("Employee has been updated", HttpStatus.OK);
    public static final GeneralResponseDTO SERVER_ERROR = new GeneralResponseDTO ("Server error!", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String response;

    @JsonIgnore
    private final HttpStatus status;

    private GeneralResponseDTO(String response, HttpStatus status){
        this.response = response;
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralResponseDTO that = (GeneralResponseDTO) o;
        return Objects.equals(response, that.response) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, status);
    }

    @Override
    public String toString() {
        return "GeneralResponseDTO{" +
                "response='" + response + '\'' +
                ", status=" + status +
                '}';
    }
}
