package com.example.FirstAppSpringStripe.repository;

import com.example.FirstAppSpringStripe.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByIsActiveTrue();
    Optional<Vehicle> findByIdAndIsActiveTrue(String id);
    List<Vehicle> findByIsActiveTrueAndIdNotIn(Set<String> rentedVehicleIds);
}