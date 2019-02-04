package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class Users {
    // user properties    
    private SimpleStringProperty fullUserName;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty address;
    private SimpleStringProperty country;
    private SimpleStringProperty city;
    private SimpleStringProperty zipCode;
    // both ID variables will be the same value for each user, used in separate tables
    // separate variables used for clarity
    private SimpleIntegerProperty customerID;
    private SimpleIntegerProperty userID;
    
    // collection of users
    private static ObservableList<Users> userList = FXCollections.observableArrayList();

    // contructor with bulit-in validation
    public Users(String userName, String phone, String address, String country, String city, String zipCode, int tableID) {
        switch (validateUserInfo(userName, phone, address, country, city, zipCode)){
            case 0:
                this.fullUserName = new SimpleStringProperty(userName);
                this.phoneNumber = new SimpleStringProperty(phone);
                this.address = new SimpleStringProperty(address);
                this.country = new SimpleStringProperty(country);
                this.city = new SimpleStringProperty(city);
                this.zipCode = new SimpleStringProperty(zipCode);
                this.customerID = new SimpleIntegerProperty(tableID);
                this.userID = new SimpleIntegerProperty(tableID);
                successAlert();
                break;
            case 1:
                failedAlert(userName);
                break;
            case 2:
                failedAlert(phone);
                break;
            case 3:
                failedAlert(address);
                break;
            case 4:
                failedAlert(country);
                break;
            case 5:
                failedAlert(city);
                break;
            case 6:
                failedAlert(zipCode);
                break;
            default:
                System.out.println("Error occurred. User was not created.");
        }
    }
    
    // getters & setters

    public static ObservableList<Users> getUserList() {
        return userList;
    }

    public static void setUserList(ObservableList<Users> userList) {
        Users.userList = userList;
    }
    
    
    public int getUserID() {
        return userID.get();
    }
    
    public void setUserID(int userID) {
        this.userID.set(userID);
    }
    
    public int getCustomerID() {
        return customerID.get();
    }
    
    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getUserName() {
        return fullUserName.get();
    }

    public void setUserName(String userName) {
        this.fullUserName.set(userName);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }
    
    // basic user data validation
    private int validateUserInfo(String userName, String phone, String address, String country, String city, String zipCode) {
        if (userName.equals("") || userName.length() > 45) {
            return 1;
        } else if (phone.equals("") || phone.length() > 8) {
            return 2;
        } else if (address.equals("") || address.length() > 50) {
            return 3;
        } else if (country.equals("")) {
            return 4;
        } else if (city.equals("")) {
            return 5;
        } else if (zipCode.equals("") || zipCode.length() > 5){
            return 6;
        } else {
            return 0;
        } 
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
        alert.setContentText("User: " + this.fullUserName + " has been successfully created!");
        alert.initModality(Modality.NONE);
        alert.showAndWait();
    }
}
