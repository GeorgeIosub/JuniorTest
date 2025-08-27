package com.example.carins.web.dto;

import java.time.LocalDate;
import java.util.Map;

public record CarHistoryEventDTO(
        String type,           // See if it is an insurance or a claim
        LocalDate date,        // Insurance start date or claim date
        Map<String, Object> details
) {}
