package src.repositories.impl.json;

import src.models.Vehicle;
import src.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleJsonRepository implements VehicleRepository {
    @Override
    public List<Vehicle> findAll() {
        return List.of();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
