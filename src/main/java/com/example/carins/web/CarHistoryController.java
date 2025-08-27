package com.example.carins.web;

import com.example.carins.service.CarHistoryService;
import com.example.carins.web.dto.CarHistoryDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars/{carId}/history")
public class CarHistoryController {

    private final CarHistoryService carHistoryService;

    public CarHistoryController(CarHistoryService carHistoryService) {
        this.carHistoryService = carHistoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarHistoryDTO> getHistory(@PathVariable Long carId) {
        CarHistoryDTO history = carHistoryService.getCarHistory(carId);
        return ResponseEntity.ok(history);
    }
}