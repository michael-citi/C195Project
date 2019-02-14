package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Model.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScheduleScreenController implements Initializable {
    
    // time zone setup and human-readable date formatter
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    // configure both tableviews
    @FXML private TableView<Appointment> scheduleTableView;
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateCol;
    @FXML private TableColumn<Appointment, LocalDateTime> endDateCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, Customer> customerCol;
    @FXML private TableColumn<Appointment, String> descripCol;
    @FXML private TableColumn<Appointment, String> userCol;
    
    // observable lists for their respective tableviews
    private static ObservableList<Appointment> apptsList = FXCollections.observableArrayList();
    @FXML private RadioButton weeklyRadioBtn;
    @FXML private RadioButton monthlyRadioBtn;
    private static Appointment transitionAppt;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField titleTextField;
    @FXML private ComboBox customerComboBox;
    @FXML private TextArea descripTextArea;
    
    // getter for transitional object
    public static Appointment getTransitionAppt() {
        return transitionAppt;
    }
    
    @FXML
    private void removeAppt() throws SQLException {
        // prevent action if no appointment is selected
        if (scheduleTableView.getSelectionModel().getSelectedItem() == null) {
            selectError();
        } else {
            // confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Please confirm deleting appointment: " + scheduleTableView.getSelectionModel().getSelectedItem().getTitle());
            alert.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                // sql delete statement
                PreparedStatement statement = null;
                String query = "DELETE * FROM appointment "
                        + "WHERE appointment.appointmentId = ?";
                try {
                    statement = LoginScreenController.dbConnect.prepareStatement(query);
                    statement.setInt(1, scheduleTableView.getSelectionModel().getSelectedItem().getAppointmentId());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
            // refresh tableview
            initializeTableView();
        }
    }
    
    // method used to initialize & refresh tableview
    @FXML
    private void initializeTableView() {
        try {
            populateApptsList();
        } catch (SQLException ex) {
            Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (weeklyRadioBtn.selectedProperty().getValue() == true) {
            weeklySchedule();
        } else if (monthlyRadioBtn.selectedProperty().getValue() == true) {
            monthlySchedule();
        } else {
            System.out.println("Error detecting which schedule radio button was chosen.");
        }
    }
    
    // error control alert for appointment selection
    private void selectError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Appointment Selection Error");
        alert.setContentText("You have not selected an appointment to modify.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    // generate monthly schedule and update table view
    private void monthlySchedule() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlusMonth = now.plusMonths(1);
        FilteredList<Appointment> filteredAppts = new FilteredList<>(apptsList);
        // multi-line lambda to parse datetime format and return sortable date values
        filteredAppts.setPredicate(date -> {
            LocalDate apptDate = LocalDate.parse(date.getStartDate(), dateFormat);
            return apptDate.isAfter(now) && apptDate.isBefore(nowPlusMonth);
        });
        scheduleTableView.setItems(filteredAppts);
    }
    
    // generate weekly schedule and update table view
    private void weeklySchedule() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlusDays = now.plusDays(7);
        FilteredList<Appointment> filteredAppts = new FilteredList<>(apptsList);
        filteredAppts.setPredicate(date -> {
            LocalDate apptDate = LocalDate.parse(date.getStartDate(), dateFormat);
            return apptDate.isAfter(now) && apptDate.isBefore(nowPlusDays);
        });
        scheduleTableView.setItems(filteredAppts);
    }
    
    @FXML
    private void newAppt(ActionEvent event) throws SQLException {
        PreparedStatement statement = null;
        String insert = "INSERT INTO appointment (customerId, title, description, location, contact, "
                + "url, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
        String errorMsg = validateData();
        switch (errorMsg) {
            case "None":
                try {
                    Customer tempCustomer = (Customer) customerComboBox.getSelectionModel().getSelectedItem();
                    statement = LoginScreenController.dbConnect.prepareStatement(insert);
                    statement.setInt(1, tempCustomer.getCustomerId());
                    statement.setString(2, titleTextField.getText());
                    statement.setString(3, descripTextArea.getText());
                    statement.setString(4, "");
                    statement.setString(5, "");
                    statement.setString(6, "");
                    
                } catch (SQLException ex) {
                    Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
                initializeTableView();
                break;
            default:
                generateError(errorMsg);
        }
    }
    
    @FXML
    private void modAppt(ActionEvent event) throws IOException {
        // reset transitionAppt object to null prior to assinging new object value, if exists
        transitionAppt = null;
        transitionAppt = scheduleTableView.getSelectionModel().getSelectedItem();
        // if no object was assigned, return error
        if (transitionAppt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("You have not selected an appointment to modify.");
            alert.showAndWait();
        } else {
            // load modify appointment screen after successful assignment of object
            loadScene(event, "View/ModAppointment.fxml");
        }
    }
    
    // validate form data before submitting new appointment
    private String validateData() {
        String error;
        if (startDatePicker.getValue() == null) {
            error = "Start Date field cannot be empty.";
        } else if (endDatePicker.getValue() == null) {
            error = "End Date field cannot be empty.";
        } else if (titleTextField.getText().equals("")) {
            error = "Title field cannot be empty.";
        } else if (customerComboBox.getSelectionModel().getSelectedItem() == null){
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
    
    @FXML
    private void exit(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Exit Appointment Scheduler?");
        alert.setContentText("Would you like to leave the appointment scheduler screen?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadScene(event, "/View/MainScreen.fxml");
        }
    }
    
    // populate apptsList with all appointments for currently logged in user
    private void populateApptsList() throws SQLException {
        PreparedStatement statement = null;
        String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                + "appointment.start, appointment.end, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId "
                + "ORDER BY appointment.start";
        try {
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            // catch error if results are empty
            if (results.next() == false) {
                System.out.println("No appointments found.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment List Empty");
                alert.setContentText("No appointments were found. Please schedule a new appointment.");
                alert.showAndWait();
            } else {
                while (results.next()) {
                    int apptId = results.getInt("appointment.appointmentId");
                    Timestamp timeApptStart = results.getTimestamp("appointment.start");
                    Timestamp timeApptEnd = results.getTimestamp("appointment.end");
                    String apptTitle = results.getString("appointment.title");
                    String apptUser = results.getString("appointment.createdBy");
                    String apptDescrip = results.getString("appointment.description");
                    Customer apptCustomer = new Customer(results.getInt("appointment.customerId"), results.getString("customer.customerName"));
                    // fix time & dates to readable format
                    ZonedDateTime zoneStart = timeApptStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime apptStart = zoneStart.withZoneSameInstant(zoneId);
                    
                    ZonedDateTime zoneEnd = timeApptEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime apptEnd = zoneEnd.withZoneSameInstant(zoneId);
                    // add appointments to list
                    Appointment appt = new Appointment(apptId, apptStart.format(dateFormat), apptEnd.format(dateFormat), apptTitle, apptDescrip, apptCustomer, apptUser);
                    apptsList.add(appt);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    
    // generate error message if appointment validation returns error
    private void generateError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Submitting Appointment");
        alert.setContentText("Please correct the following error: " + errorMessage);
        alert.showAndWait();
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
        // configure schedule table columns
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        // default schedule is weekly
        weeklyRadioBtn.setSelected(true);
        // initialize the table data
        initializeTableView();
        
    }    
}
