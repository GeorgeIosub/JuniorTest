package com.example.carins.web.dto;

import java.util.List;

public record CarHistoryDTO(
        Long carId,
        String vin,
        List<CarHistoryEventDTO> events
) {}