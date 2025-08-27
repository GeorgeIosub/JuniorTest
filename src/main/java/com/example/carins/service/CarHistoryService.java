package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryDTO;
import com.example.carins.web.dto.CarHistoryEventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CarHistoryService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    public CarHistoryService(
            CarRepository carRepository,
            InsurancePolicyRepository policyRepository,
            ClaimRepository claimRepository
    ) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
    }

    public CarHistoryDTO getCarHistory(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        List<CarHistoryEventDTO> events = new ArrayList<>();

        // Create a policy
        policyRepository.findByCarId(carId).forEach(policy -> {
            Map<String, Object> details = Map.of(
                    "provider", policy.getProvider(),
                    "startDate", policy.getStartDate(),
                    "endDate", policy.getEndDate()
            );
            events.add(new CarHistoryEventDTO("POLICY", policy.getStartDate(), details));
        });

        // Create a claim
        claimRepository.findByCarId(carId).forEach(claim -> {
            Map<String, Object> details = Map.of(
                    "description", claim.getDescription(),
                    "amount", claim.getAmount()
            );
            events.add(new CarHistoryEventDTO("CLAIM", claim.getClaimDate(), details));
        });

        // Sort events chronologically
        events.sort(Comparator.comparing(CarHistoryEventDTO::date));

        return new CarHistoryDTO(car.getId(), car.getVin(), events);
    }
}
