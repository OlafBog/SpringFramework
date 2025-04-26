package src.app;

import src.repositories.RentalRepository;
import src.repositories.UserRepository;
import src.repositories.VehicleRepository;
import src.repositories.impl.jdbc.RentalJdbcRepository;
import src.repositories.impl.jdbc.UserJdbcRepository;
import src.repositories.impl.jdbc.VehicleJdbcRepository;
import src.repositories.impl.json.RentalJsonRepository;
import src.repositories.impl.json.UserJsonRepository;
import src.repositories.impl.json.VehicleJsonRepository;
import src.services.AuthService;
import src.services.RentalService;
import src.services.VehicleService;

public class Main {
    public static void main(String[] args) {
        String storageType = "json";

        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu

        //TODO: Dorzucenie do projektu swoich jsonrepo.

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        //TODO:Przerzucenie logiki interakcji z userem do App
        App app = new App(authService, vehicleService, rentalService);
        app.run();
    }
}