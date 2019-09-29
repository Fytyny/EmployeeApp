package org.company.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class GeneralResponseDTOTest {
    @Test
    public void toJsonTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(GeneralResponseDTO.EMPLOYEE_DOES_NOT_EXISTS);
        assertFalse(json.contains("status"));
    }
}

