package Controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import Model.*;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController implements Initializable {
    
    private ObservableList<Appointment> tempApptList = FXCollections.observableArrayList();
    
    @FXML TableView<Appointment> mainTableView;
    @FXML TableColumn<Appointment, String> dateColumn;
    @FXML TableColumn<Appointment, String> userColumn;
    @FXML TableColumn<Appointment, String> timeColumn;
    @FXML TableColumn<Appointment, String> typeColumn;
    
    @FXML Label userTextLabel;
    @FXML DatePicker mainDatePicker;
    
    private RadioButton weeklyRadioBtn;
    private RadioButton monthlyRadioBtn;
    
    @FXML
    private void addUser(ActionEvent event) throws IOException {
        loadScene(event, "/View/AddUser.fxml");
    }
    
    @FXML
    private void modUser(ActionEvent event) throws IOException {
        loadScene(event, "/View/ModUser.fxml");
    }
    
    @FXML
    private void newAppt(ActionEvent event) throws IOException {
        loadScene(event, "/View/NewAppointment.fxml");
    }
    
    @FXML
    private void modAppt(ActionEvent event) throws IOException {
        loadScene(event, "/View/ModAppointment.fxml");
    }
    
    @FXML
    private void logOff(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Off");
        alert.setHeaderText("Log Off Current User");
        alert.setContentText("Are you sure you want to log off the current user?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            loadScene(event, "/View/LoginScreen.fxml");
        }
    }
    
    // generic loading scene method
    private void loadScene(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
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
        // apply username to welcome message
        userTextLabel.setText(LoginScreenController.transferUserName);
        // initialize the main tableview
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("fullUserName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
