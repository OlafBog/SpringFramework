package com.example.FirstAppSpringStripe.service.impl;

import com.example.FirstAppSpringStripe.model.Vehicle;
import com.example.FirstAppSpringStripe.repository.RentalRepository;
import com.example.FirstAppSpringStripe.repository.VehicleRepository;
import com.example.FirstAppSpringStripe.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return savedVehicle;
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        Set<String> ids = rentalRepository.findRentedVehicleIds();
        if(ids.isEmpty()) {
            return vehicleRepository.findByIsActiveTrue();
        }
        return vehicleRepository.findByIsActiveTrueAndIdNotIn(ids);
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        List<Vehicle> activeVehicles = findAllActive();
        Set<String> rentedIds = rentalRepository.findRentedVehicleIds();
        return activeVehicles.stream()
                .filter(vehicle -> rentedIds.contains(vehicle.getId()))
                .toList();
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return findAvailableVehicles().stream()
                .anyMatch(vehicle -> vehicle.getId().equals(vehicleId));
    }

    @Override
    public void deleteById(String id) {
        if(rentalRepository.existsByVehicleIdAndReturnDateIsNull(id)) {
            throw new IllegalStateException("Vehicle " + id + " is rented.");
        }
        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndIsActiveTrue(id);
        if(vehicle.isEmpty()) {
            throw new IllegalStateException("Vehicle " + id + " not found.");
        } else {
            vehicle.get().setActive(false);
            vehicleRepository.save(vehicle.get());
        }
        //vehicleRepository.deleteById(id);
    }
}
