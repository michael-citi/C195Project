package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Locale;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;

public class LoginScreenController implements Initializable {

    // determining system locale and translation
    private Locale locale = Locale.getDefault();
    private ResourceBundle messages = ResourceBundle.getBundle("Translations.Bundle", locale);

    // declare scene variables
    @FXML private TextField uNameTextField;
    @FXML private PasswordField pWordTextField;
    @FXML private Button cancelBtn;
    @FXML private Button loginBtn;
    @FXML private Label uNameLabel;
    @FXML private Label pWordLabel;
    @FXML private Label welcomeLabel;

    // login credentials
    private static final String uName = "test";
    private static final String pWord = "test";

    // track login attempts
    private static int loginCounter = 0;

    @FXML
    private void checkCredentials() {
        String tempUserName = uNameTextField.getText();
        String tempPassword = pWordTextField.getText();

        if (loginCounter < 3) {
            // check username and password values against valid credentials
            if (uName.equals(tempUserName) && pWord.equals(tempPassword)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(messages.getString("successLoginTitle"));
                alert.setHeaderText(messages.getString("successLoginHeader"));
                alert.setContentText(messages.getString("successLoginContent"));
                alert.initModality(Modality.NONE);
                alert.showAndWait();
                // execute scene transition to main screen
                login();
            } else {
                // return error and increment brute force protection
                loginCounter += 1;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(messages.getString("failedLoginErrorHeader"));
                alert.setTitle(messages.getString("failedLoginErrorTitle"));
                alert.setContentText(messages.getString("failedLoginErrorContent"));
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
        } else if (loginCounter == 3) {
            // brute force protection
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(messages.getString("forceCloseErrorTitle"));
            alert.setHeaderText(messages.getString("forceCloseErrorHeader"));
            alert.setContentText(messages.getString("forceCloseErrorContent"));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            System.exit(0);
        }
    }

    private void login() {
        //TODO: Scene transition to main screen.
        System.out.println("Login successful");
    }

    @FXML
    private void cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(messages.getString("confirmExitTitle"));
        alert.setHeaderText(messages.getString("confirmExitHeader"));
        alert.setContentText(messages.getString("confirmExitContent"));
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    private void dbConnection() throws Exception {
        // error control
        try {
            // establish SQL database connection
            Connection dbConnection = null;
            String dbURL = "jdbc:mysql://52.206.157.109/";
            String dbUser = "U04V6U";
            String dbPass = "53688353202";
            dbConnection = DriverManager.getConnection(dbURL, dbUser, dbPass);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // assign translated text to buttons and labels
        cancelBtn.setText(messages.getString("cancelBtn"));
        loginBtn.setText(messages.getString("loginBtn"));
        uNameLabel.setText(messages.getString("username"));
        pWordLabel.setText(messages.getString("password"));
        welcomeLabel.setText(messages.getString("welcomeLabel"));
        // initialize database connection
        try {
            dbConnection();
        } catch (Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SQL Connection Error");
            alert.setContentText(e.getMessage());
        }
    }
}
