package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class Users {
    // user properties
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    
    // constructor with basic user validation built-in
    public Users(String firstName, String lastName, String phoneNumber, String address) {
        switch (validateUserInfo(firstName, lastName, phoneNumber, address)) {
            case 0:
                failedAlert("First Name");
                break;
            case 1:
                failedAlert("Last Name");
                break;
            case 2:
                failedAlert("Phone Number");
                break;
            case 3:
                failedAlert("Address");
                break;
            case 4:
                this.firstName = firstName;
                this.lastName = lastName;
                this.phoneNumber = phoneNumber;
                this.address = address;
                successAlert();
            default:
                System.out.println("Unknown/Invalid User Creation.");
        }
    }

    // getters & setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    // validating user info before creating user
    // very basic validation. Regex comparisons will be used if time allows.
    private int validateUserInfo(String fName, String lName, String phoneNumber, String address) {
        if(fName.equals("")) {
            return 0;
        } else if(lName.equals("")) {
            return 1;
        } else if (phoneNumber.equals("") || (phoneNumber.length() != 7) ) {
            return 2;
        } else if (address.equals("")) {
            return 3;
        } else {
            return 4;
        }
    }
    
    // generated error message
    private void failedAlert(String property) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("User creation failed.");
        alert.setContentText("The user info: " + "\"" + property + "\"" + " is invalid.\n\n Please try again.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    // method created to reduce clutter in constructor
    private void successAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("User Created");
        alert.setHeaderText("Success!");
        alert.setContentText("User: " + lastName + ", " + firstName + " has been successfully created!");
        alert.initModality(Modality.NONE);
        alert.showAndWait();
    }
}
