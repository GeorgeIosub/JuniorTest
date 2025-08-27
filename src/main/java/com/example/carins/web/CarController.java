package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final CarRepository carRepository;

    public CarController(CarService service, CarRepository carRepository) {
        this.service = service;
        this.carRepository = carRepository;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        LocalDate parsedDate;
        // Reject invalid format dates
        try {
            parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid date format. Use ISO format YYYY-MM-DD."));
        }

        // Reject dates outside a reasonable range
        if (parsedDate.isBefore(LocalDate.of(1980, 1, 1)) || parsedDate.isAfter(LocalDate.of(2050, 12, 31))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Date out of supported range (1980-01-01 to 2050-12-31)."));
        }

        // Check car existence
        if (!carRepository.existsById(carId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error","Car with id " + carId + " not found."));
        }
        boolean valid = service.isInsuranceValid(carId, parsedDate);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, date.toString(), valid));
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
