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
class CarHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCarHistory_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/cars/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.events").isArray());
    }

    @Test
    void getCarHistory_CarNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/cars/999/history"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Car not found"));
    }
}
