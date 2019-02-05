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

public class MainScreenController implements Initializable {
    
    @FXML Label userTextLabel;
    @FXML Button apptAlertBtn;
    
    @FXML
    private void manageUsers(ActionEvent event) throws IOException {
        loadScene(event, "/View/UserList.fxml");
    }
    
    @FXML
    private void manageAppt(ActionEvent event) throws IOException {
        loadScene(event, "/View/ScheduleScreen.fxml");
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
    
    @FXML
    private void showApptAlert() throws SQLException {
        // interesting information variables
        int rowCount = 0;
        String title = null;
        String location = null;
        String contact = null;
        String type = null;
        String start = null;
        // prepared statement query
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "SELECT title, location, contact, type, start FROM appointment WHERE "
                + "userId = ? AND "
                + "start BETWEEN ? AND ?";
        try {
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            statement.setInt(1, 00000);
            statement.setDate(2, x);
            statement.setDate(3, x);
            results = statement.executeQuery();
            
            if (results.next() == false) {
                System.out.println("No appointments within 15 minutes for the user.");
            } else {
                while (results.next()) {
                    
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
    
    // generic scene transition method
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
        try {
            // show alert for appointments within 15 minutes upon login
            showApptAlert();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
