package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.web.dto.ClaimRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/cars/{carId}/claims")
public class ClaimController {

    private final ClaimRepository claimRepository;
    private final CarRepository carRepository;

    public ClaimController(ClaimRepository claimRepository, CarRepository carRepository) {
        this.claimRepository = claimRepository;
        this.carRepository = carRepository;
    }

    @PostMapping
    public ResponseEntity<Claim> createClaim(
            @PathVariable Long carId,
            @Valid @RequestBody ClaimRequestDto request
    ) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        Claim claim = new Claim();
        claim.setCar(car);
        claim.setClaimDate(request.claimDate());
        claim.setDescription(request.description());
        claim.setAmount(request.amount());

        Claim saved = claimRepository.save(claim);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }
}

