package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserListController implements Initializable {

    // table elements
    @FXML private TableView<Users> userTable;
    @FXML private TableColumn<Users, String> fullNameCol;
    @FXML private TableColumn<Users, String> userNameCol;
    @FXML private TableColumn<Users, String> phoneNumberCol;
    @FXML private TableColumn<Users, Integer> activeCol;
    
    @FXML private TextField userSearchTextField;
    
    // temporary list to hold existing users
    private static ObservableList<Users> tempUserList = FXCollections.observableArrayList();
    private ObservableList<Users> singleUserList = FXCollections.observableArrayList();
    private static Users transitionUser;
    
    
    // search for specific user on the table
    @FXML
    private void searchUser() {
        String searchText = userSearchTextField.getText();
        initializeSingleTable();
    }
    
    // remove specific user
    @FXML
    private void removeUser() {
        
    }
    
    // load Modify User screen with user data
    @FXML
    private void modUser(ActionEvent event) throws IOException {
        loadScene(event, "/View/ModUser.fxml");
    }
    
    // load New User screen
    @FXML
    private void addUser(ActionEvent event) throws IOException {
        loadScene(event, "/View/AddUser.fxml");
    }
    
    // return to main screen
    @FXML
    private void exitScreen(ActionEvent event) throws IOException {
        loadScene(event, "/View/MainScreen.fxml");
    }

    // getters & setters
    public static ObservableList<Users> getTempUserList() {
        return tempUserList;
    }

    public static void setTempUserList(ObservableList<Users> tempUserList) {
        UserListController.tempUserList = tempUserList;
    }

    public static Users getTransitionUser() {
        return transitionUser;
    }

    public static void setTransitionUser(Users transitionUser) {
        UserListController.transitionUser = transitionUser;
    }
    
    // populate user list and leave out test account
    private void populateTempUserList() throws SQLException {
        PreparedStatement statement = null;
        String query = "SELECT user.userId, user.userName, user.active, user.password FROM user "
                + "WHERE NOT user.userName = ? "
                + "ORDER BY user.userName";
        try {
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            statement.setString(1, "test");
            ResultSet results = statement.executeQuery();

            if (results.next() == false) {
                System.out.println("No users found.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No users were found. Please create a user.");
                alert.showAndWait();
            } else {
                while (results.next()) {
                    int userId = results.getInt("user.userId");
                    int active = results.getInt("user.active");
                    String userName = results.getString("user.userName");
                    String password = results.getString("user.password");
                    Users tmpUser = new Users(userName, password, userId, active);

                    tempUserList.add(tmpUser);
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
    
    // full table populate and/or re-initialize
    private void initializeFullTable() {
        
    }
    
    // display single user
    private void initializeSingleTable() {
        
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
        
        try {
            populateTempUserList();
        } catch (SQLException ex) {
            Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initializeFullTable();
    }    
    
}
