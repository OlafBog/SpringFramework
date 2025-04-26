package src.repositories.impl.jdbc;

import src.models.Rental;
import src.repositories.RentalRepository;

import java.util.List;
import java.util.Optional;

public class RentalJdbcRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        return List.of();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return Optional.empty();
    }
}
