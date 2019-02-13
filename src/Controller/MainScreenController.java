package Controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class MainScreenController implements Initializable {
    
    // time zone setup and human-readable date formatter
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    
    // appointment containers
    private ObservableList<Appointment> nowAppts = FXCollections.observableArrayList();
    private static Users mainUser = LoginScreenController.getUser();
    
    // useable javafx elements
    @FXML private Label userTextLabel;
    @FXML private Button apptAlertBtn;
    
    // boolean to hold found appointments value
    private static boolean apptsFound;
    
    @FXML
    private void manageUsers(ActionEvent event) throws IOException {
        loadScene(event, "/View/UserList.fxml");
    }
    
    @FXML
    private void manageAppt(ActionEvent event) throws IOException {
        loadScene(event, "/View/ScheduleScreen.fxml");
    }
    
    @FXML
    private void apptReports(ActionEvent event) throws IOException {
        loadScene(event, "/View/ReportsScreen.fxml");
    }
    
    @FXML
    private void logOff(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Off");
        alert.setHeaderText("Log Off " + mainUser.getUserName());
        alert.setContentText("Are you sure you want to log off the current user?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            loadScene(event, "/View/LoginScreen.fxml");
        }
    }
    
    // create appointment objects and add to nowAppts
    private void addAppointments() throws SQLException{
        PreparedStatement statement = null;
        try {
            // query data from appointment and customer tables
            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.title"
                    + "appointment.description, appointment.start, appointment.end, customer.customerId, "
                    + "customer.customerName, user.userId, user.userName "
                    + "FROM appointment, customer, user "
                    + "WHERE appointment.customerId = customer.customerId AND user.userId = ? "
                    + "ORDER BY appointment.start";
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            statement.setInt(1, mainUser.getUserId());
            ResultSet results = statement.executeQuery();
            
            if (results.next() == false) {
                // do nothing if no values are retrieved
                apptsFound = false;
            } else {
                // populate Appointment properties
                apptsFound = true;
                while (results.next()) {
                    int apptId = results.getInt("appointment.appointmentId");
                    String apptDescrip = results.getString("appointment.description");
                    String apptTitle = results.getString("appointment.title");
                    Customer customer = new Customer(results.getInt("appointment.customerId"), results.getString("customer.customerName"));
                    Timestamp start = results.getTimestamp("appointment.start");
                    Timestamp end = results.getTimestamp("appointment.end");
                    String apptUser = results.getString(mainUser.getUserName());
                    // format timestamps to use for new Appointment object
                    ZonedDateTime zoneApptStart = start.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime zoneApptEnd = end.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    // more formatting
                    ZonedDateTime apptStart = zoneApptStart.withZoneSameInstant(zoneId);
                    ZonedDateTime apptEnd = zoneApptEnd.withZoneSameInstant(zoneId);
                    // add new Appointment object to the list
                    nowAppts.add(new Appointment(apptId, apptStart.format(dateFormat), apptEnd.format(dateFormat), apptTitle, apptDescrip, customer, apptUser));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    
    @FXML
    private void showApptAlert() {
        // update appointment list
        try {
            addAppointments();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // setup alert, if appointments found
        if (apptsFound == false) {
            // do nothing
            apptAlertBtn.setDisable(true);
            System.out.println("No immediate appointments to display.");
        } else {
            apptAlertBtn.setDisable(false);
            apptAlertBtn.setText("Upcomming Appointments (" + nowAppts.size() + ")");
            // creating local time variables for alert
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime later = current.plusMinutes(15);
            // filtered list multi-line lambda to sort appointment date values
            FilteredList<Appointment> filterApptList = new FilteredList<>(nowAppts);
            filterApptList.setPredicate(date -> {
                LocalDateTime apptDate = LocalDateTime.parse(date.getStartDate(), dateFormat);
                return apptDate.isAfter(current.minusMinutes(1)) && apptDate.isBefore(later);
            });
        }
    }
    
    // generic scene transition method
    private void loadScene(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    // exit method with confirmation prompt
    @FXML
    private void exitProgram() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Exit Program?");
        alert.setContentText("Would you like to exit the program?");
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize alert button
        apptAlertBtn.setDisable(false);
        // apply username to welcome message
        userTextLabel.setText(mainUser.getUserName());
        // show alert if appointments exist
        showApptAlert();
        
    }
}
