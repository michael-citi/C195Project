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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

public class UserListController implements Initializable {

    // table elements
    @FXML private TableView<Users> userTable;
    @FXML private TableColumn<Users, String> userNameCol;
    @FXML private TableColumn<Users, Integer> activeCol;
    
    @FXML private TextField userSearchTextField;
    
    // temporary list to hold existing users
    private static ObservableList<Users> userList = FXCollections.observableArrayList();
    private ObservableList<Users> tempUserList = FXCollections.observableArrayList();
    private static Users transitionUser;
    
    
    // search for specific user on the table
    @FXML
    private void searchUser() {
        boolean userFound = false;
        String searchText = userSearchTextField.getText();
        for (int i = 0; i < userList.size(); ++i) {
            String temp = userList.get(i).getUserName();
            if (temp.equals(searchText)) {
                userFound = true;
                tempUserList.setAll(userList.get(i));
            }
        }
        if (userFound == false) {
            // reset user list table
            initializeUserTable();
        }
    }
    
    // remove specific user
    @FXML
    private void removeUser() throws SQLException {
        // prevent action if no user is selected
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            selectError();
        } else {
            // confirmation alert
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setContentText("Please confirm deleting user: " + userTable.getSelectionModel().getSelectedItem().getUserName());
            alert.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                // sql delete statement
                PreparedStatement statement = null;
                String query = "DELETE FROM user, appointment "
                        + "WHERE user.userId = ?";
                try {
                    statement = LoginScreenController.dbConnect.prepareStatement(query);
                    statement.setInt(1, userTable.getSelectionModel().getSelectedItem().getUserId());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
                // update table with current user list
                initializeUserTable();
            }
        }
    }
    
    // load Modify User screen with user data
    @FXML
    private void modUser(ActionEvent event) throws IOException {
        // prevent action if no user is selected
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            selectError();
        } else {
            // assign user variable to be transferred to modify user screen
            transitionUser = userTable.getSelectionModel().getSelectedItem();
            loadScene(event, "/View/ModUser.fxml");
        }
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
    public static ObservableList<Users> getUserList() {
        return userList;
    }

    public static void setUserList(ObservableList<Users> tempUserList) {
        UserListController.userList = tempUserList;
    }

    public static Users getTransitionUser() {
        return transitionUser;
    }

    public static void setTransitionUser(Users transitionUser) {
        UserListController.transitionUser = transitionUser;
    }
    
    // populate user list while leaving out test account
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
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("User List Empty");
                alert.setContentText("No users were found. Please create a new user.");
                alert.showAndWait();
            } else {
                while (results.next()) {
                    int userId = results.getInt("user.userId");
                    int active = results.getInt("user.active");
                    String userName = results.getString("user.userName");
                    String password = results.getString("user.password");
                    Users tmpUser = new Users(userName, password, userId, active);

                    userList.add(tmpUser);
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
    private void initializeUserTable() {
        userTable.setItems(userList);
    }
    
    // generic scene transition method
    private void loadScene(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    // error control alert for user selection
    private void selectError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("User Selection Error");
        alert.setContentText("You have not selected a user to modify.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        initializeUserTable();
        
        try {
            populateTempUserList();
        } catch (SQLException ex) {
            Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
}
