package src.services;

import src.repositories.RentalRepository;
import src.repositories.VehicleRepository;

public class VehicleService {

    VehicleRepository vehicleRepo;
    RentalRepository rentalRepo;

    public VehicleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }
}
