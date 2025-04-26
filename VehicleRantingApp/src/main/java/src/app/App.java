package src.app;

import src.services.AuthService;
import src.services.RentalService;
import src.services.VehicleService;

import java.util.*;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private static String userId;

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {

    }

    static void loginRegisterMenu() {
        System.out.println("Choice option:");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("0 - Exit");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                loginRegisterMenu();
        }
    }

    private static void login() {
        Scanner scanner = new Scanner(System.in);
        boolean loginS = false;
        String login = "";
        while (!loginS) {
            if(!Objects.equals(login, "")) System.out.println("Not found login: "+login);
            System.out.print("Enter login: ");
            login = scanner.nextLine();
            loginS = tryToLogin(login);
        }
        boolean passwordS = false;
        String password = "";
        while (!passwordS) {
            if (!Objects.equals(password, "")) System.out.println("Password is incorrect");
            System.out.println("Enter password: ");
            password = scanner.nextLine();
            passwordS = tryToPassword(login, password);
        }
        System.out.println("Login successful");
        userId = getUserId(login, password);
    }

    private static boolean tryToLogin(String login) {
        //jeżeli znalezione - true
        //jeżeli nieznalezione - false
        return true;
    }

    private static boolean tryToPassword(String login, String password) {
        //jeżeli dobre - true
        //jeżeli złe - false
        return true;
    }

    public static String getUserId(String login, String password) {
        return "!ID!";
    }

    private static void register() {
        Scanner scanner = new Scanner(System.in);
        boolean loginS = false;
        String login = "";
        while(!loginS) {
            if(!Objects.equals(login, "")) System.out.println("Login "+login+" already exists");
            System.out.print("Create new login: ");
            login = scanner.nextLine();
            loginS = !tryToLogin(login);
        }
        boolean passwordS = false;
        String password = "";
        while(!passwordS) {
            System.out.print("Create your password: ");
            password = scanner.nextLine();
            if(!Objects.equals(password, "")) {
                System.out.println("Password can't be empty");
                continue;
            }
            if(password.length() < 8) {
                System.out.println("Password must be at least 8 characters");
                continue;
            }
            boolean hasUpperCase = false;
            boolean hasLowerCase = false;
            boolean hasSpecialChar = false;
            boolean hasNumber = false;
            for(int i = 0; i < password.length(); i++) {
                char ch = password.charAt(i);
                if(Character.isUpperCase(ch)) {
                    hasUpperCase = true;
                } else if(Character.isLowerCase(ch)) {
                    hasLowerCase = true;
                } else if(Character.isDigit(ch)) {
                    hasNumber = true;
                } else if(!Character.isLetterOrDigit(ch)) {
                    hasSpecialChar = true;
                }
            }
            if (!hasUpperCase) {
                System.out.println("Password must have at least one uppercase letter");
                continue;
            }
            if (!hasLowerCase) {
                System.out.println("Password must have at least one lowercase letter");
                continue;
            }
            if (!hasNumber) {
                System.out.println("Password must have at least one number");
                continue;
            }
            if (!hasSpecialChar) {
                System.out.println("Password must have at least one special character");
                continue;
            }
            passwordS = true;
        }
        createNewUser(login, password);
        System.out.println("Register successful");
    }

    private static void createNewUser(String login, String password) {
        //tworzenie nowego user
    }


    static void normalMenu() {
        System.out.println("Choice option:");
        System.out.println("1 - List of vehicles");
        System.out.println("2 - Rent vehicle");
        System.out.println("3 - Return vehicle");
        System.out.println("0 - Logout");
    }

    static void adminMenu() {
        System.out.println("Choice option:");
        System.out.println("1 - List of vehicles");
        System.out.println("2 - List of users");
        System.out.println("3 - Rent vehicle");
        System.out.println("4 - Return vehicle");
        System.out.println("5 - Add new vehicle");
        System.out.println("6 - Delete vehicle");
        System.out.println("0 - Logout");
    }
}
