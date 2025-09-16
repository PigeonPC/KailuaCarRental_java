package Car;

import java.time.LocalDate;

public class Car {


    private int car_id;
    private String brand;
    private String model;
    private String fuel;
    private String registration_number_plate;
    private LocalDate first_reg_year_and_month;
    private int km_driven;
    private int car_type_id;

    public Car(int car_id, String brand, String model, String fuel,
               String registration_number_plate, LocalDate first_reg_year_and_month,
               int km_driven, int car_type_id){

        this.car_id = car_id;
        this.brand = brand;
        this.model = model;
        this.fuel = fuel;
        this.registration_number_plate = registration_number_plate;
        this.first_reg_year_and_month = first_reg_year_and_month;
        this.km_driven = km_driven;
        this.car_type_id = car_type_id;
    }


    @Override
    public String toString() {

        String carType; //Jeg vil gerne have den til at printe bil typen i stedet for id tallet.
        if (car_type_id == 1) {
            carType = "Luxury";
        } else if (car_type_id == 2){
            carType = "Family";
        } else if (car_type_id == 3) {
            carType = "Sport";
        } else {
            carType = "Unknown";
        }

        return "___________________________________________\n\nCar ID: " + car_id
                +"\nBrand: " + brand
                +"\nModel: " + model
                + "\nFuel: " + fuel
                + "\nRegistration number(plate): " + registration_number_plate
                + "\nFirst registration year and month: " + first_reg_year_and_month
                + "\nKM driven: " + km_driven
                + "\nCar type: " + carType;
    }


    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getRegistration_number_plate() {
        return registration_number_plate;
    }

    public void setRegistration_number_plate(String registration_number_plate) {
        this.registration_number_plate = registration_number_plate;
    }

    public LocalDate getFirst_reg_year_and_month() {
        return first_reg_year_and_month;
    }

    public void setFirst_reg_year_and_month(LocalDate first_reg_year_and_month) {
        this.first_reg_year_and_month = first_reg_year_and_month;
    }

    public int getKm_driven() {
        return km_driven;
    }

    public void setKm_driven(int km_driven) {
        this.km_driven = km_driven;
    }

    public int getCar_type_id() {
        return car_type_id;
    }

    public void setCar_type_id(int car_type_id) {
        this.car_type_id = car_type_id;
    }
}
