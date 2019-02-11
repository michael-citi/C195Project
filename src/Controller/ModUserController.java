package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModUserController implements Initializable {

    @FXML private TextField userNameTextField;
    @FXML private TextField passwordTextField;
    @FXML private CheckBox activeCheckBox;
    
    // heavy duty update + validationg method
    @FXML
    private void saveChanges(ActionEvent event) throws IOException, SQLException {
        // confirmation before updating any values
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        alert.setContentText("Are you sure you want to save changes to this user? Click OK to have your changes "
                + "processed and return to the main User List screen.");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            if (validateData().equals("OK")) {
                String uName = userNameTextField.getText();
                String pWord = passwordTextField.getText();
                int userId = UserListController.getTransitionUser().getUserId();
                int active;
                if (activeCheckBox.selectedProperty().getValue() == true) {
                    active = 1;
                } else {
                    active = 0;
                }
                PreparedStatement statement = null;
                String query = "UPDATE user "
                        + "SET user.userName = ?, user.password = ?, user.active = ? "
                        + "WHERE user.userId = ?";
                try {
                    statement = LoginScreenController.dbConnect.prepareStatement(query);
                    statement.setString(1, uName);
                    statement.setString(2, pWord);
                    statement.setInt(3, active);
                    statement.setInt(4, userId);
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
                // return to User List screen after update is executed.
                loadScene(event, "/View/UserList.fxml");
            // error control alerts
            } else if (validateData().equals("empty user")) {
                loadError("Empty User Name", "User name cannot be empty.");
            } else if (validateData().equals("long user")) {
                loadError("Invalid User Name", "User name cannot be over 12 characters.");
            } else if (validateData().equals("empty password")) {
                loadError("Empty Password", "The password cannot be empty.");
            } else if (validateData().equals("long password")) {
                loadError("Invalid Password", "The password cannot be over 14 characters.");
            } else {
                loadError("Unknown Error", "An unknown error occurred. Changes will not be processed and the program "
                        + "will return to the main User List screen.");
                loadScene(event, "/View/Userlist.fxml");
            }
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Changes?");
        alert.setContentText("Are you sure you want to cancel changes to this user?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadScene(event, "/View/UserList.fxml");
        }
    }
    
    // basic validation method form fields.
    private String validateData() {
        String errorMsg;
        String uNameCheck = userNameTextField.getText();
        String pWordCheck = passwordTextField.getText();
        if (uNameCheck.equals("")) {
            errorMsg = "empty user";
        } else if (uNameCheck.length() > 12) {
            errorMsg = "long user";
        } else if (pWordCheck.equals("")) {
            errorMsg = "empty password";
        } else if (pWordCheck.length() > 14) {
            errorMsg = "long password";
        } else {
            errorMsg = "OK";
        }
        return errorMsg;
    }
    
    // generic error alert frame
    private void loadError(String title, String content) {
        Alert newAlert = new Alert(AlertType.ERROR);
        newAlert.setTitle(title);
        newAlert.setContentText(content);
        newAlert.initModality(Modality.APPLICATION_MODAL);
        newAlert.showAndWait();
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
        // populate fields with tranisiton user properties
        userNameTextField.setText(UserListController.getTransitionUser().getUserName());
        passwordTextField.setText(UserListController.getTransitionUser().getPassword());
        if(UserListController.getTransitionUser().getActive() == 1) {
            activeCheckBox.setSelected(true);
        } else {
            activeCheckBox.setSelected(false);
        }
    }
}
