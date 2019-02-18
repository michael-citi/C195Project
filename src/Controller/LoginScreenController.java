package Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Model.*;
import java.sql.Statement;

public class LoginScreenController implements Initializable {

    // determining system locale, time zone, and translation
    private final Locale locale = Locale.getDefault();
    private final ResourceBundle messages = ResourceBundle.getBundle("Translations.Bundle", locale);
    // boolean check for successful login attempts
    private static boolean userLoggedIn = false;
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
    // initialize database connection variable
    public static Connection dbConnect = null;
    // user object to pass through to main screen if login successfull
    private static Users user = new Users();

    // check login credentials
    private int checkCredentials() throws SQLException {
        // first check to prevent brute force login attempts        
        if (loginCounter >= 5) {
            System.out.println("Brute force protection enabled. Program shutting down.");
            return 4;
        }
        // if function is not ended immediately, proceed to generate variables and queries
        String tempUserName = uNameTextField.getText();
        String tempPassword = pWordTextField.getText();
        // reset userFound to false state
        userLoggedIn = false;
        // prepared statement to verify login credentials
        String querySQL = "SELECT user.userId, user.userName, user.password, user.active FROM user WHERE user.userName = ?";
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = dbConnect.prepareStatement(querySQL);
            statement.setString(1, tempUserName);
            result = statement.executeQuery();
            // comparing results if they exist
            if (result.next() == false) {
                System.out.println("No user found with that username and password.");
                return 3;
            } else {
                do {
                    String pWord = result.getString("user.password");
                    int active = result.getInt("user.active");
                    if(pWord.equals(tempPassword)) {
                        if (active == 1) {
                            userLoggedIn = true;
                            user = new Users(result.getString("user.userName"), result.getString("user.password"), result.getInt("user.userId"), result.getInt("user.active"));
                            System.out.println("Username and Password match. User is active. Successful login.");
                            return 0;
                        } else if (active == 0) {
                            System.out.println("Username and Password match. User is NOT active. Login failed.");
                            return 1;
                        }
                    } else {
                        System.out.println("Username and Password do NOT match. Login Failed.");
                        return 2;
                    }
                } while (result.next());
            }
        } catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        // return value to filter out unknown errors
        return 5;
    }
    
    @FXML
    private void login(ActionEvent event) throws Exception {
        // login with switch/case validation
        switch (checkCredentials()) {
            // successful login. reset loginCounter
            case 0:
                loginCounter = 0;
                // record login event
                recordLogin();
                // display visual element showing successful login
                Alert zeroAlert = new Alert(AlertType.INFORMATION);
                zeroAlert.setTitle(messages.getString("successLoginTitle"));
                zeroAlert.setHeaderText(messages.getString("successLoginHeader"));
                zeroAlert.setContentText(messages.getString("successLoginContent"));
                zeroAlert.showAndWait();
                // transition to main scene
                Parent root = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
                Scene scene = new Scene(root);
                Stage mainScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScene.setScene(scene);
                mainScene.show();
                break;
                
            // user found but not active
            case 1:
                recordLogin();
                Alert oneAlert = new Alert(AlertType.ERROR);
                oneAlert.setTitle(messages.getString("userFoundInactiveTitle"));
                oneAlert.setHeaderText(messages.getString("userFoundInactiveHeader"));
                oneAlert.setContentText(messages.getString("userFoundInactiveContent"));
                oneAlert.showAndWait();
                clearTextFields();
                break;
                
            // username and password do not match. increment logincounter
            case 2:
                ++loginCounter;
                recordLogin();
                Alert twoAlert = new Alert(AlertType.ERROR);
                twoAlert.setTitle(messages.getString("failedLoginErrorTitle"));
                twoAlert.setHeaderText(messages.getString("failedLoginErrorHeader"));
                twoAlert.setContentText(messages.getString("failedLoginErrorContent"));
                twoAlert.showAndWait();
                clearTextFields();
                break;
                
            // user not found. increment loginCounter   
            case 3:
                ++loginCounter;
                Alert threeAlert = new Alert(AlertType.ERROR);
                threeAlert.setTitle(messages.getString("userNotFoundTitle"));
                threeAlert.setHeaderText(messages.getString("userNotFoundHeader"));
                threeAlert.setContentText(messages.getString("userNotFoundContent"));
                threeAlert.showAndWait();
                clearTextFields();
                break;
                
            // brute force protection    
            case 4:
                Alert fourAlert = new Alert(AlertType.WARNING);
                fourAlert.setTitle(messages.getString("forceCloseErrorTitle"));
                fourAlert.setHeaderText(messages.getString("forceCloseErrorHeader"));
                fourAlert.setContentText(messages.getString("forceCloseErrorContent"));
                fourAlert.initModality(Modality.APPLICATION_MODAL);
                fourAlert.showAndWait();
                System.exit(0);
                break;
                
            // checkCredentials() failed to return a value 0-4. 
            // something went wrong. give warning and close program
            case 5:
                Alert fiveAlert = new Alert(AlertType.WARNING);
                fiveAlert.setTitle(messages.getString("loginErrorTitle"));
                fiveAlert.setHeaderText(messages.getString("loginErrorHeader"));
                fiveAlert.setContentText(messages.getString("loginErrorContent"));
                fiveAlert.showAndWait();
                System.exit(0);
                break;
                
            default:
                // do something...maybe. this shouldn't be reachable.
                System.out.println("Something odd happened with checkCredentials().");
        }
    }

    // user getter
    public static Users getUser() {
        return user;
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
            String dbURL = "jdbc:mysql://52.206.157.109/U04V6U";
            String dbUser = "U04V6U";
            String dbPass = "53688353202";
            dbConnect = DriverManager.getConnection(dbURL, dbUser, dbPass);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    private void clearTextFields() {
        uNameTextField.setText("");
        pWordTextField.setText("");
    }
    
    // write login events to log file
    private void recordLogin() throws IOException {
        // temp variables
        String logUserName = uNameTextField.getText();
        String userIsFound;
        // determine user logged in event
        if (userLoggedIn == false) {
            userIsFound = "Failed Login";
        } else {
            userIsFound = "Successful Login";
        }
        // open file to append at end of log
        try (FileWriter writer = new FileWriter("user_event_log.txt", true)) {
            BufferedWriter buffWriter = new BufferedWriter(writer);
            buffWriter.write("User: " + logUserName + "\nTimestamp: " + Instant.now().toString() 
                    + "\nEvent: " + userIsFound + "\n---------\n");
            buffWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
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
