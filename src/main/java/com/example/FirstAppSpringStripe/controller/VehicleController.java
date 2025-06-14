package com.example.FirstAppSpringStripe.controller;

import com.example.FirstAppSpringStripe.model.Rental;
import com.example.FirstAppSpringStripe.model.User;
import com.example.FirstAppSpringStripe.model.Vehicle;
import com.example.FirstAppSpringStripe.service.RentalService;
import com.example.FirstAppSpringStripe.service.UserService;
import com.example.FirstAppSpringStripe.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    public VehicleController(VehicleService vehicleService, RentalService rentalService, UserService userService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {
        logger.info("Request received for vehicle with ID: {}", id);
        return vehicleService.findById(id)
                .map(vehicle -> {
                    logger.info("Found vehicle with ID: {}", id);
                    return ResponseEntity.ok(vehicle);
                })
                .orElseGet(() -> {
                    logger.info("Vehicle with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.findAvailableVehicles();
    }

    @GetMapping("/rented")
    public List<Vehicle> getRentedVehicles(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        List<Rental> rentals = rentalService.findActiveRentalByUserId(user.getId());
        return rentals.stream().map(Rental::getVehicle).toList();
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String id) {
        try {
            vehicleService.deleteById(id);
            return ResponseEntity.ok("Vehicle with ID: " + id + " deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the vehicle.");
        }
    }
}