package com.example.carins.web.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimRequestDto(
        @NotNull(message = "Claim date is required") LocalDate claimDate,
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive") BigDecimal amount
) {}
