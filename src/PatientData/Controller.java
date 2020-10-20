package PatientData;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.net.PortUnreachableException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class Controller implements Initializable
{
    final String hostname = "employee-db.cazbck0mpzss.us-east-1.rds.amazonaws.com";
    final String dbname = "testdb";
    final String port = "3306";
    final String username = "martha";
    final String password = "martha1105";

    final String AWS_URL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;

    @FXML
    private JFXButton LoadPatientsBtn;
    @FXML
    private JFXButton ExistingPatientsBtn;
    @FXML
    private JFXButton FemaleBtn;
    @FXML
    private JFXButton BloodTypeBtn;
    @FXML
    private JFXButton MinorsBtn;
    @FXML
    private JFXButton DeleteBtn;
    @FXML
    private Label loadingLabel;
    @FXML
    private ListView<String> patientList;
    private ListView<Patient>selectedPatientList;
    private ObservableList<String> items2;
    private ObservableList<Patient> items;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //items = selectedPatientList.getItems();
        //items2 = patientList.getItems();
        /*items2 = patientList2.getItems();
        Patient p1 = new Patient(UUID.randomUUID(), "Ana Lopez", "Female", "A-POS", 21, 123.2, 5.4);
        Patient p2 = new Patient(UUID.randomUUID(), "John Smith", "Male", "B-NEG", 19, 185.0, 5.11);
        Patient p3 = new Patient(UUID.randomUUID(), "Michael Johnson", "Male", "A-POS", 13, 135.4, 5.9);
        Patient p4 = new Patient(UUID.randomUUID(), "Crystal Evans", "Female", "O-POS", 18, 119.1, 5.8);
        Patient p5 = new Patient(UUID.randomUUID(), "David Hernandez", "Male", "O-POS", 15, 168.3, 5.10);
        items.addAll(p1,p2,p3,p4,p5);*/

        GeneratePatient(5);

        LoadPatientsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loadingLabel.setPrefSize(100,50);
                loadingLabel.setText("Loading...");
                loadingLabel.setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                FillTable();
                try{
                    Statement stmt = GetDBStatement();
                    if(stmt != null){
                        String sqlStmt = "SELECT * FROM PatientData";
                        ResultSet result = stmt.executeQuery(sqlStmt);
                        while(result.next()){
                            items2.add(result.getString("name"));
                        }
                    }
                }catch (Exception ex){
                    System.out.println("ERROR LOADING PATIENT DATA TO LIST: "+ ex.getMessage());
                }
            }
        });

        ExistingPatientsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoadPatients();
                loadingLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,CornerRadii.EMPTY,Insets.EMPTY)));
            }
        });
        DeleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try
                {
                   Connection conn = DriverManager.getConnection(AWS_URL);
                   Statement stmt = conn.createStatement();
                   System.out.println("CONNECTION ESTABLISHED");

                   String sql = "DROP TABLE PatientData";
                   stmt.executeUpdate(sql);
                   System.out.println("Table PatientData DELETED");
                }
                catch (Exception ex)
                {
                    System.out.println("ERROR AT DELETE: "+ ex.getMessage());
                }
            }
        });
        FemaleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loadingLabel.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
                try
                {
                    Connection conn = DriverManager.getConnection(AWS_URL);
                    Statement stmt = conn.createStatement();
                    System.out.println("CONNECTION ESTABLISHED");
                    System.out.println("Female Patients: ");
                    ResultSet result = stmt.executeQuery("SELECT id,name,age FROM PatientData WHERE gender = 'female'");
                    //String sql = "SELECT id, name, age FROM PatientData WHERE gender = 'Female'";
                    //stmt.executeQuery(sql);
                    //ResultSet  result = stmt.executeQuery(sql);
                    while (result.next())
                    {
                    System.out.print(result.getString("id"));
                    System.out.print("\t");
                    System.out.print(result.getString("name"));
                    System.out.print("\t");
                    System.out.print(result.getString("age"));
                    System.out.print("\t");
                    System.out.println();
                    }
                }
                catch (Exception ex)
                {
                    System.out.println("ERROR RETRIEVING GENDER DATA: "+ ex.getMessage());
                }
            }
        });
        BloodTypeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadingLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                try {
                    Connection conn = DriverManager.getConnection(AWS_URL);
                    Statement stmt = conn.createStatement();
                    System.out.println("CONNECTION ESTABLISHED");
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PatientData ORDER BY bloodType");
                    while (rs.next()) {
                        System.out.print(rs.getString("id"));
                        System.out.print("\t");
                        System.out.print(rs.getString("name"));
                        System.out.print("\t");
                        System.out.print(rs.getString("age"));
                        System.out.print("\t");
                        System.out.print(rs.getString("gender"));
                        System.out.print("\t");
                        System.out.print(rs.getString("bloodType"));
                        System.out.print("\t");
                        System.out.print(rs.getString("weight"));
                        System.out.print("\t");
                        System.out.print(rs.getString("height"));
                        System.out.print("\t");
                        System.out.println();
                    }
                } catch (Exception ex) {
                    System.out.println(" Error obtaining blood type: "+ex.getMessage());
                }
            }
        });
        MinorsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadingLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                try{
                    Connection conn = DriverManager.getConnection(AWS_URL);
                    Statement stmt = conn.createStatement();
                    System.out.println("CONNECTION ESTABLISHED");
                    ResultSet minorRS = stmt.executeQuery("SELECT * FROM PatientData WHERE age < 18");
                    while(minorRS.next()){
                        System.out.print(minorRS.getString("id"));
                        System.out.print("\t");
                        System.out.print(minorRS.getString("name"));
                    }
                }catch (Exception ex){
                    System.out.println("ERROR retrieving results for patients under 81: "+ex.getMessage());
                }
            }
        });

        /*selectedPatientList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
            @Override
            public void changed(ObservableValue<? extends Patient> observableValue, Patient patients, Patient patients1) {
                loadingLabel.setText("Selected Patients: "+patients1);
            }
        });*/
    }
    private Statement GetDBStatement(){
        try{
            Connection conn = DriverManager.getConnection(AWS_URL);
            Statement stmt = conn.createStatement();
            System.out.println("CONNECTION ESTABLISHED");

            return stmt;
        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }

        private void FillTable ()
        {
            try
            {
                Connection conn = DriverManager.getConnection(AWS_URL);
                Statement stmt = conn.createStatement();
                System.out.println("CONNECTION ESTABLISHED");

                //ADD PATIENT DATA
                System.out.println("ADDING PATIENTS TO TABLE");
                UUID id1 = UUID.randomUUID();
                String name1 = "Ana Lopez";
                Integer age1 = 17;
                String gender1 = "Female";
                String bloodType1 = "AB-Neg";
                Double weight1 = 123.0;
                Double height1 = 5.8;
                String sql = "INSERT INTO PatientData VALUES"
                        + "('" + id1.toString() + "', '" + name1 + "','" + age1.toString() + "','" + gender1 + "', '" + bloodType1 + "','" + weight1.toString() + "','" + height1.toString() + "')";
                stmt.executeUpdate(sql);

                UUID id2 = UUID.randomUUID();
                String name2 = "Eric Smith";
                Integer age2 = 29;
                String gender2 = "Male";
                String bloodType2 = "O-Pos";
                Double weight2 = 148.0;
                Double height2 = 5.10;
                sql = "INSERT INTO PatientData VALUES"
                        + "('" + id2.toString() + "', '" + name2 + "','" + age2.toString() + "','" + gender2 + "', '" + bloodType2 + "','" + weight2.toString() + "','" + height2.toString() + "')";
                stmt.executeUpdate(sql);

                UUID id3 = UUID.randomUUID();
                String name3 = "David Johnson";
                Integer age3 = 23;
                String gender3 = "Male";
                String bloodType3 = "AB-Pos";
                Double weight3 = 148.0;
                Double height3 = 6.0;
                sql = "INSERT INTO PatientData VALUES"
                        + "('" + id3.toString() + "', '" + name3 + "','" + age3.toString() + "','" + gender3 + "', '" + bloodType3 + "','" + weight3.toString() + "','" + height3.toString() + "')";
                stmt.executeUpdate(sql);

                UUID id4 = UUID.randomUUID();
                String name4 = "Elliot Johnson";
                Integer age4 = 23;
                String gender4 = "Male";
                String bloodType4 = "A-Pos";
                Double weight4 = 159.2;
                Double height4 = 5.11;
                sql = "INSERT INTO PatientData VALUES"
                        + "('" + id4.toString() + "', '" + name4 + "','" + age4.toString() + "','" + gender4 + "', '" + bloodType4 + "','" + weight4.toString() + "','" + height4.toString() + "')";
                stmt.executeUpdate(sql);

                System.out.println("PATIENT DATA ADDED TO TABLE");
            }
            catch (Exception ex)
            {
                System.out.println("ERROR ADDING PATIENTS TO TABLE: " + ex.getMessage());
            }
        }

        private void LoadPatients()
        {
            try
            {
                Connection conn = DriverManager.getConnection(AWS_URL);
                Statement stmt = conn.createStatement();
                System.out.println("CONNECTION ESTABLISHED");
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM PatientData");

                //String sqlStmt = "SELECT * FROM PatientData";
                //ResultSet result = stmt.executeQuery(sqlStmt);
                while(resultSet.next())
                {
                    System.out.print(resultSet.getString("id"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("name"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("age"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("gender"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("bloodType"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("weight"));
                    System.out.print("\t");
                    System.out.print(resultSet.getString("height"));
                    System.out.print("\t");
                    System.out.println();
                }
            }
            catch (Exception ex)
            {
                System.out.println("ERROR IN SELECT STATEMENT: "+ex.getMessage());
            }
        }

        private void GeneratePatient ( int x){

            try {
                Connection conn = DriverManager.getConnection(AWS_URL);
                Statement stmt = conn.createStatement();
                System.out.println("CONNECTION ESTABLISHED");
                try
                {
                    stmt.execute("CREATE TABLE PatientData (" +
                        "id VARCHAR(50), " +
                        "name VARCHAR(200), " +
                        "age VARCHAR(50), " +
                        "gender VARCHAR(50), " +
                        "bloodType VARCHAR(50), " +
                        "weight VARCHAR(25), " +
                        "Height VARCHAR(25))");
                    System.out.println("TABLE CREATED");
                }
                catch (Exception ex)
                {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
            catch (Exception ex)
            {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
