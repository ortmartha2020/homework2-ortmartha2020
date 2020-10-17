package PatientData;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.UUID;

public class Patient {
    private UUID id;
    private String name;
    private String gender;
    private String bloodType;
    private int age;
    private double weight;
    private double height;

    public Patient(UUID id, String name, String gender, String bloodType, int age, double weight, double height) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.bloodType = bloodType;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }
}
