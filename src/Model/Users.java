package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import Controller.*;

public class Users extends Appointment{
    private String userName;
    private String password;
    private int active;
       
    // contructor with bulit-in validation
    public Users(String userName, String password, int active) {
        super();
        this.userName = userName;
        this.password = password;
        this.active = active;
    }
    
    // getters & setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
    // generated error message
    private void failedAlert(String property) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("User creation failed.");
        alert.setContentText("The user info: " + "\"" + property + "\"" + " is invalid or too many characters.\n\n Please try again.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    // success method created to reduce clutter in constructor
    private void successAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("User Created");
        alert.setHeaderText("Success!");
        alert.setContentText("User: " + this.userName + " has been successfully created!");
        alert.initModality(Modality.NONE);
        alert.showAndWait();
    }
    
    public static Users returnUser(String userName) throws SQLException {
        String query = "SELECT userId, userName, active, createDate, createdBy, lastUpdate, lastUpdateBy FROM "
                + "user WHERE userName = ?";
        PreparedStatement statement = null;
        ResultSet results = null;
        Users user = null;
        try {
            statement = LoginScreenController.dbConnect.prepareStatement(query);
            statement.setString(1, userName);
            results = statement.executeQuery();
            
            if (results.next() == false) {
                return null;
            } else {
                user.setUserId(results.getInt("userId"));
                user.setUserName(results.getString("userName"));
                user.setActive(results.getInt("active"));
                user.setCreateDate(results.getDate("createDate"));
                user.setCreatedBy(results.getString("createdBy"));
                user.setLastUpdate(results.getTime("lastUpdate"));
                user.setLastUpdateBy(results.getString("lastUpdateBy"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return user;
    }
}
