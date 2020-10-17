package PatientData;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.UUID;

public class NewPatients extends Patient {

    public NewPatients(UUID id, String name, String gender, String bloodType, int age, float weight, float height) {
        super(id, name, gender, bloodType, age, weight, height);
    }
}
