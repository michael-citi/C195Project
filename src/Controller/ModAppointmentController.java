package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Model.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModAppointmentController implements Initializable {

    @FXML private DatePicker apptDatePicker;
    @FXML private TextField titleTextField;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> endTimeComboBox;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private TextArea descripTextArea;
    @FXML private ComboBox<String> apptTypeComboBox;
    
    private static Appointment tempAppt;
    
    // time zone setup
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    
    @FXML
    private void saveChanges() throws IOException, SQLException {
        String errorMsg = validateData();
        if (errorMsg.equals("None")) {
            PreparedStatement statement = null;
            String insert = "UPDATE appointment "
                    + "SET customerId = ?, title = ?, type = ?, description = ?, start = ?, end = ?, "
                    + "lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? "
                    + "WHERE appointmentId = ?";
            try {
                // building & formatting timestamp to insert into SQL
                LocalDate date = apptDatePicker.getValue();
                LocalTime tmpStart = LocalTime.parse(startTimeComboBox.getValue(), timeFormat);
                LocalTime tmpEnd = LocalTime.parse(endTimeComboBox.getValue(), timeFormat);
                LocalDateTime startDate = LocalDateTime.of(date, tmpStart);
                LocalDateTime endDate = LocalDateTime.of(date, tmpEnd);
                ZonedDateTime startTZone = startDate.atZone(zoneId).withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime endTZone = endDate.atZone(zoneId).withZoneSameInstant(ZoneId.of("UTC"));
                Timestamp startDateInsert = Timestamp.valueOf(startTZone.toLocalDateTime());
                Timestamp endDateInsert = Timestamp.valueOf(endTZone.toLocalDateTime());

                statement = LoginScreenController.dbConnect.prepareStatement(insert);
                statement.setInt(1, customerComboBox.getValue().getCustomerId());
                statement.setString(2, titleTextField.getText());
                statement.setString(3, "");
                statement.setString(4, descripTextArea.getText());
                statement.setTimestamp(5, startDateInsert);
                statement.setTimestamp(6, endDateInsert);
                statement.setString(7, LoginScreenController.getUser().getUserName());
                statement.setInt(8, tempAppt.getAppointmentId());
                int result = statement.executeUpdate();
                if (result == 1) {
                    System.out.println("Appointment was updated.");
                    
                } else {
                    System.out.println("Appointment was NOT updated");
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } else {
            generateError(errorMsg);
        }
    }
    
    // validate form data before submitting new appointment
    private String validateData() {
        String error;
        if (apptDatePicker.getValue() == null) {
            error = "Start Date field cannot be empty.";
        } else if (startTimeComboBox.getValue() == null) {
            error = "Appointment must have a start time.";
        } else if (endTimeComboBox.getValue() == null) {
            error = "Appointment must have an end time.";
        } else if (titleTextField.getText().equals("")) {
            error = "Title field cannot be empty.";
        } else if (apptTypeComboBox.getValue() == null) {
            error = "You must choose an appointment type.";
        } else if (customerComboBox.getValue() == null){
            error = "You must choose a customer for this appointment. If there are none "
                    + "to choose from, please create a customer by returning to the main menu "
                    + "and selecting the \"Manage Customers\" button.";
        } else if (descripTextArea.getText().equals("")) {
            error = "Please enter a description for this appointment.";
        } else {
            error = "None";
        }
        return error;
    }
    
    // generate error message if appointment validation returns error
    private void generateError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Submitting Appointment");
        alert.setContentText("Please correct the following error: " + errorMessage);
        alert.showAndWait();
    }
    
    // method used to build list of available times to use for appointments
    // user should not be able to pick times outside of normal 8-5 business hours
    private void buildApptTimeValues() {
        LocalTime opHours = LocalTime.of(8, 0);
        ObservableList<String> startTimeList = FXCollections.observableArrayList();
        ObservableList<String> endTimeList = FXCollections.observableArrayList();
        do {
            startTimeList.add(opHours.format(timeFormat));
            endTimeList.add(opHours.format(timeFormat));
            // increment by 15 minute intervals
            opHours = opHours.plusMinutes(15);
        } while (!opHours.equals(LocalTime.of(17, 15)));
        // remove last available time in start time list
        startTimeList.remove(startTimeList.size() - 1);
        // remove first available time in end time list
        endTimeList.remove(0);
        
        //populate comboboxes
        apptDatePicker.setValue(LocalDate.now());
        startTimeComboBox.setItems(startTimeList);
        endTimeComboBox.setItems(endTimeList);
    }
    
    // method needed to populate Customer combobox
    private void populateCustomers() throws SQLException {
        PreparedStatement statement = null;
        String query = "SELECT customer.customerId, customer.customerName FROM customer "
                + "ORDER BY customer.customerId";
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            
            while (results.next()) {
                int custId = results.getInt("customer.customerId");
                String custName = results.getString("customer.customerName");
                System.out.println("Customer Name: " + custName + " Customer ID: " + custId);
                
                Customer tmpCustomer = new Customer(custId, custName);
                customerList.add(tmpCustomer);
            }
            customerComboBox.setItems(customerList);
        } catch (SQLException ex) {
            System.out.println("Error retrieving customers: " + ex.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Changes?");
        alert.setContentText("Are you sure you want to cancel changes?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        // return with no changes
        if (result.get() == ButtonType.OK) {
            loadScene(event, "/View/ScheduleScreen.fxml");
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tempAppt = ScheduleScreenController.getTransitionAppt();
        buildApptTimeValues();
        apptTypeComboBox.setItems(ScheduleScreenController.getTypeList());
        
        try {
            populateCustomers();
        } catch (SQLException ex) {
            Logger.getLogger(ModAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        apptTypeComboBox.setValue(tempAppt.getType());
        titleTextField.setText(tempAppt.getTitle());
        descripTextArea.setText(tempAppt.getDescription());
        customerComboBox.setValue(tempAppt.getCustomer());
        
    }    
    
}
