package Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import Car.*; //import Car fra car package


/*

TO DO:
- Jeg ville gerne have rykket listCar, addCar, deleteCar ud i Car package i stedet.
    Det var dog lidt irriterende at få til at virke..
- Lav metoder til af oprette renter profil
- Lav metoder til at oprette kontrakter til profilerne.
- Lave packages med renter class, contract class og tilhørende metoder

Og jeg er klar over at jeg har skrevet mange kommentarer, som ikke skal stå der i en færdig kode.
De står der for min egen skyld, så jeg kan gå tilbage og huske hvad linjerne gør og kan bruge det i fremtiden.
- Olga

 */

public class Main {

    public static final String DATABASE_URL
            = "jdbc:mysql://localhost:3306/kailua_car_rental";
    //JDBC URL, bruger SQL driveren, kører på min computer(localhost), standardporten 3306, med kailua_car_rental databasen

    public static Connection con; //Variabel der holder DB-forbindelsen


    final static ArrayList<Car> cars = new ArrayList<>();


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        try {
            //opretter forbindelse til databasen
            //databaseURL, mySQL brugernavn og adgangskode.
            con = DriverManager.getConnection(DATABASE_URL, user, password);
            System.out.println("Connected as " + user);

            //Statement objekt, "talerør" for SQL-teksten
            Statement statement = con.createStatement();

            //MENU
            menu(scanner, statement);

            statement.close();
            con.close();
            scanner.close();

        } catch (SQLException sqlex) {
            //hvis noget går galt (password, server nede, SQL fejl)
            //fejlbesked der forklarer hvorfor
            System.out.println("Database error: " + sqlex.getMessage());
            System.exit(1); //afslut program med fejlkode 1.
        }
    }

    public static void menu(Scanner scanner, Statement statement) {
        while (true) {
            System.out.println("\n================== CAR MENU ==================");
            System.out.println("1. List cars");
            System.out.println("2. Add new car");
            System.out.println("3. Delete car");
            System.out.println("0. Exit");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        listCars(statement);
                        break;
                    case 2:
                        addCar(scanner);
                        break;
                    case 3:
                        deleteCar(statement, scanner);
                        break;
                    case 0:
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println("You have to pick 1, 2, 3 or 0.");
                        break;
                }
            } else {
                System.out.println("That is not a number! Try again!");
                scanner.next(); //rydder det forjerte input.
            }

        }
    }

    public static void listCars(Statement statement) {
        //listCars() = Kører SELECT i databasen, opretter Car objekter ud fra rækkkerne, printer dem ud og fanger fejl.


        String SQLdata = "SELECT * FROM cars"; //Giv mig alle kolonner fra cars

        //Jeg forstår ResultSet som en form for beholder, for den data, jeg henter fra databasen. Bruges når man har lavet SELECT
        try (ResultSet rs = statement.executeQuery(SQLdata)) { //Bruger statement som jeg har taget med fra Main, gennem resultSet?? Med SQLdata defineret ovenfor

            cars.clear(); //sørger for, at der ikke allerede er fyldt med biler i cars ArrayList, før vi fylder op igen.
            while (rs.next()) { //"Så længe der stadig er rækker, vil while loop køre og lave car objekter"
                Car car = new Car(
                        rs.getInt("car_id"), //bruger rs(ResultSet) til at hente bilens ID, brand, model osv....
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getString("registration_number_plate"),
                        rs.getDate("first_reg_year_and_month").toLocalDate(), //KOnverterer til java LocalDate!
                        rs.getInt("km_driven"),
                        rs.getInt("car_type_id")
                );
                cars.add(car); //tilføjer til ArrayList
            }

            //Printer resultater
            if (cars.isEmpty()) {
                System.out.println("(no cars found)");
            } else {
                for (Car car : cars) { //Printer arraylist
                    System.out.println(car);
                }
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }


    public static void addCar(Scanner scanner) {

        String sql = "INSERT INTO cars SET " +
                "brand = ?, model = ?, fuel_type = ?, registration_number_plate = ?, " +
                "first_reg_year_and_month = ?, km_driven = ?, car_type_id = ?";

        //Prepared statement bruges til INSERT, UPDATE, DELETE, bruges med setString(), setINT() osv. = SENDE KOMMANDOER TIL DATABASEN
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            //BRAND
            System.out.println("\nEnter brand: ");
            String brand = scanner.nextLine();

            //MODEL
            System.out.println("\nEnter model: ");
            String model = scanner.nextLine();

            //FUEL
            System.out.println("\nEnter fuel type:\n1. Electric\n2. Petrol\n3. Diesel\n4. Hybrid");
            String fuelType;
            while (true) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1:
                            System.out.println("Electric selected.");
                            fuelType = "Electric";
                            break;
                        case 2:
                            System.out.println("Petrol selected.");
                            fuelType = "Petrol";
                            break;
                        case 3:
                            System.out.println("Diesel selected.");
                            fuelType = "Diesel";
                            break;
                        case 4:
                            System.out.println("Hybrid selected.");
                            fuelType = "Hybrid";
                            break;
                        default:
                            System.out.println("You have to choose a number from 1-4.");
                            continue; //tilbage til starten af while
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That’s not a number! Try again, please.");
                }
            }

            //REG NR
            System.out.println("\nEnter registration number (plate): ");
            String regNumber = scanner.nextLine();

            //REG DATE
            System.out.println("\nEnter first registration date (yyyy-mm-dd): ");
            String firstRegStr = scanner.nextLine();
            java.sql.Date firstReg = java.sql.Date.valueOf(firstRegStr);

            //KM DRIVEN
            System.out.println("\nEnter km driven: ");
            int kmDriven = Integer.parseInt(scanner.nextLine());

            //CAR TYPE
            System.out.println("\nEnter car type:\n1. Luxury\n2. Family\n3. Sport");
            int carTypeId = 0;
            while (true) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1:
                            System.out.println("Luxury selected.");
                            carTypeId = 1;
                            break;
                        case 2:
                            System.out.println("Family selected.");
                            carTypeId = 2;
                            break;
                        case 3:
                            System.out.println("Sport selected.");
                            carTypeId = 3;
                            break;
                        default:
                            System.out.println("You have to choose 1, 2 or 3");
                            continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That’s not a number! Try again, please.");
                }
            }

            //her skal vi binde værdierne til de values, som ikke blev defineret tidligere! De er pt sat som ?
            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setString(3, fuelType);
            ps.setString(4, regNumber);
            ps.setDate(5, firstReg); //java.sql.Date
            ps.setInt(6, kmDriven);
            ps.setInt(7, carTypeId);

            int rows = ps.executeUpdate(); //Opdater de 7 .

            //hvis rows bliver over 0, så har den lykkedes i at opdatere en bil
            if (rows > 0) {
                System.out.println("\n---------------- CAR ADDED ----------------");
                System.out.println("Brand: " + brand
                        +"\nModel: " + model
                        + "\nFuel: " + fuelType
                        + "\nRegistration number(plate): " + regNumber
                        + "\nFirst registration year and month: " + firstReg
                        + "\nKM driven: " + kmDriven
                        + "\nCar type: " + carTypeId);
            } else {
                System.out.println("Failed to add car :-(");
            }

        } catch (
                SQLException e) { //Ærligt forstår ikke helt hvad de her exeptions præcist betyder, og hvordan jeg skal vide hvad jeg skal bruge.
            System.out.println("Database error while adding car: " + e.getMessage());
        }

    }

    public static void deleteCar(Statement statement, Scanner scanner) {
        listCars(statement); //Bruger list cars metoden, som allerede er lavet

        int carIdChoice;
        System.out.println("___________________________________________\n\n" +
                "Enter car ID on the car you wish to delete: ");

        try {
            carIdChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("That’s not a valid number.");
            return;
        }

        String sqlDelete = "DELETE FROM cars WHERE car_id = " + carIdChoice;

        try {
            int rows = statement.executeUpdate(sqlDelete);

            if (rows > 0) {
                System.out.println("Car with ID " + carIdChoice + " deleted successfully.");
            } else {
                System.out.println("No car with ID " + carIdChoice + " found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error while deleting car: " + e.getMessage());
        }
    }



}