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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    // track login attempts
    private static int loginCounter = 0;
    private Connection dbConnect = null;

    
    private void checkCredentials() {
        String tempUserName = uNameTextField.getText();
        String tempPassword = pWordTextField.getText();
        
        try {
            Statement statement = dbConnect.createStatement();
            ResultSet result = statement.executeQuery("SELECT userId, userName, password, active from user");
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void login() {
        checkCredentials();
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
            String dbURL = "jdbc:mysql://52.206.157.109/";
            String dbUser = "U04V6U";
            String dbPass = "53688353202";
            dbConnect = DriverManager.getConnection(dbURL, dbUser, dbPass);
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
