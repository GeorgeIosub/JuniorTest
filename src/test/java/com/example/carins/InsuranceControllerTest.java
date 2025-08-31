package com.example.carins;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void isInsuranceValid_ShouldReturn400_WhenDateFormatInvalid() throws Exception {
        mockMvc.perform(get("/cars/1/insurance-valid").param("date", "2024-99-99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid date format. Use ISO format YYYY-MM-DD."));
    }

    @Test
    void isInsuranceValid_ShouldReturn400_WhenDateOutOfRange() throws Exception {
        mockMvc.perform(get("/cars/1/insurance-valid").param("date", "1800-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Date out of supported range (1980-01-01 to 2050-12-31)."));
    }

    @Test
    void isInsuranceValid_ShouldReturn404_WhenCarNotFound() throws Exception {
        mockMvc.perform(get("/cars/400/insurance-valid").param("date", "2024-01-01"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Car with id 400 not found."));
    }

    @Test
    void isInsuranceValid_ShouldReturn200_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/cars/1/insurance-valid").param("date", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.date").value("2024-01-01"))
                .andExpect(jsonPath("$.valid").isBoolean());
    }
}
