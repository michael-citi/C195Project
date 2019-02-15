package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Model.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class CustomerListController implements Initializable {

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TableColumn<Customer, Address> addressCol;
    
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<City> masterCityList = FXCollections.observableArrayList();
    private static ObservableList<Country> countryList = FXCollections.observableArrayList();
    private static ObservableList<City> tempCityList = FXCollections.observableArrayList();
    private static int generatedId = -1;
    
    @FXML private TextField nameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField zipTextField;
    
    @FXML private ComboBox<Country> countryComboBox;
    @FXML private ComboBox<City> cityComboBox;
    
    
    @FXML
    private void newCustomer() throws SQLException {
        String errorMsg = validateCustomer();
        if (errorMsg.equals("None")) {
            // insert new address
            PreparedStatement addressStatement = null;
            String insertAddress = "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createDate, "
                    + "createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            try {
                // temp values to insert
                City tempCity = cityComboBox.getValue();                
                
                addressStatement = LoginScreenController.dbConnect.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS);
                addressStatement.setInt(1, generatedId);
                addressStatement.setString(2, streetTextField.getText());
                addressStatement.setString(3, "");
                addressStatement.setInt(4, tempCity.getCityId());
                addressStatement.setString(5, zipTextField.getText());
                addressStatement.setString(6, phoneTextField.getText());
                addressStatement.setString(7, LoginScreenController.getUser().getUserName());
                addressStatement.setString(8, LoginScreenController.getUser().getUserName());
                addressStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (addressStatement != null ) {
                    addressStatement.close();
                }
            }
            // insert new customer
            PreparedStatement custStatement = null;
            String insertCustomer = "INSERT INTO customer (customerId, customerName, addressId, active, createDate, "
                + "createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            try {
                // temp values to insert
                Country tempCountry = countryComboBox.getValue();
                
                custStatement = LoginScreenController.dbConnect.prepareStatement(insertCustomer);
                custStatement.setInt(1, generatedId);
                custStatement.setString(2, nameTextField.getText());
                custStatement.setInt(3, generatedId);
                custStatement.setInt(4, 1);
                custStatement.setString(5, LoginScreenController.getUser().getUserName());
                custStatement.setString(6, LoginScreenController.getUser().getUserName());
                custStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(UserListController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (custStatement != null) {
                    custStatement.close();
                }
            }
            // reset disabled state on city combobox
            cityComboBox.setDisable(true);
            // increment generated address ID
            generatedId++;
        } else {
            generateError(errorMsg);
        }
    }
    
    @FXML
    private void modCustomer() {
        //TODO: do stuff.
    }
    
    // enable city combobox selection once country is chosen
    @FXML
    private void enableCityBox() {
        // clear temp city list to populate it only with relevant cities
        tempCityList.removeAll();
        // create temporary country object from selection
        Country tempCountry = countryComboBox.getValue();
        // if selection is null/empty, reset disabled state on city combobox and do nothing
        if(tempCountry == null) {
            if (cityComboBox.disableProperty().getValue() == false) {
                cityComboBox.setDisable(true);
            }
        // if selection is id of 1 (United States), populate city combobox with relevant cities
        } else if (tempCountry.getCountryId() == 1) {
            cityComboBox.setDisable(false);
             for (int i = 0; i < masterCityList.size(); ++i) {
                City tempCity = masterCityList.get(i);
                if (tempCity.getCountryId() == 1) {
                    tempCityList.add(tempCity);
                }
            }
            cityComboBox.setItems(tempCityList);
        // if selection is id of 2 (England), populate city combobox with relevant cities   
        } else if (tempCountry.getCountryId() == 2) {
            cityComboBox.setDisable(false);
            for (int i = 0; i < masterCityList.size(); ++i) {
                City tempCity = masterCityList.get(i);
                if (tempCity.getCountryId() == 2) {
                    tempCityList.add(tempCity);
                }
            }
            cityComboBox.setItems(tempCityList);
        // unknown or syntax error catch
        } else {
            System.out.println("Something went wrong with the comboboxes. Check your code.");
        }
    }
    
    
    // validating customer data and generating error message
    private String validateCustomer() {
        String error;
        City validCity = cityComboBox.getValue();
        Country validCountry = countryComboBox.getValue();
        if (nameTextField.getText().equals("")) {
            error = "Customer name cannot be blank.";
        } else if (phoneTextField.getText().equals("")) {
            error = "Phone number cannot be blank.";
        } else if (phoneTextField.getText().length() > 8) {
            error = "Phone number is invalid. The correct format is: 555-1234.";
        } else if (streetTextField.getText().equals("")) {
            error = "Street address cannot be blank.";
        } else if (zipTextField.getText().equals("")) {
            error = "ZipCode cannot be empty.";
        } else if (zipTextField.getText().length() > 5) {
            error = "ZipCode is invalid. The correct format is: 12345.";
        } else if (validCity == null) {
            error = "A City must be chosen from the drop down box.";
        } else if (validCountry == null) {
            error = "A Country must be chosen from the drop down box.";
        } else {
            error = "None";
        }
        return error;
    }
    
    // method used to populate country list
    private void populateCountryList() throws SQLException {
        countryList.removeAll();
        PreparedStatement countryStm = null;
        String query = "SELECT countryId, country FROM country "
                + "ORDER BY countryId";
        try {
            countryStm = LoginScreenController.dbConnect.prepareStatement(query);
            ResultSet results = countryStm.executeQuery();
            // confirm country table return
            while (results.next()) {
                String countryName = results.getString("country");
                int countryId = results.getInt("countryId");
                System.out.println("Country Name: " + countryName + " Country ID: " + countryId);
            }
            // reset db cursor
            results.beforeFirst();
            if (results.next() == false) {
                System.out.println("No country information to retrieve. Something went wrong, check SQL syntax.");
            } else {
                while (results.next()) {
                    int countryId = results.getInt("countryId");
                    String countryName = results.getString("country");
                    
                    Country country = new Country(countryId, countryName);
                    countryList.add(country);
                }
                countryComboBox.setItems(countryList);
            }
        } catch (SQLException ex) {
            System.out.println("Failed to query City table: " + ex.getMessage());
        } finally {
            if (countryStm != null) {
                countryStm.close();
            }
        }
    }
    
    // method used to populate city list
    private void populateCityList() throws SQLException {
        masterCityList.removeAll();
        PreparedStatement cityStm = null;
        String query = "SELECT city, cityId, city.countryId FROM city "
                + "ORDER BY cityId";
        try {
            cityStm = LoginScreenController.dbConnect.prepareStatement(query);
            ResultSet results = cityStm.executeQuery();
            // confirm city table return
            while (results.next()) {
                String cityName = results.getString("city");
                int cityId = results.getInt("cityId");
                int countryId = results.getInt("city.countryId");
                System.out.println("City: " + cityName + " City ID: " + cityId + " Country ID: " + countryId);
            }
            // reset db cursor
            results.beforeFirst();
            if (results.next() == false) {
                System.out.println("No city information to retrieve. Something went wrong, check SQL syntax.");
            } else {
                while (results.next()) {
                    String cityName = results.getString("city");
                    int cityId = results.getInt("cityId");
                    int countryId = results.getInt("city.countryId");
                    
                    City city = new City(cityId, cityName, countryId);
                    masterCityList.add(city);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Failed to query City table: " + ex.getMessage());
        } finally {
            if (cityStm != null) {
                cityStm.close();
            }
        }
    }
    
    @FXML
    private void back(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Return?");
        alert.setContentText("Are you sure you would like to return to the main screen?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    
    // error message template for validation
    private void generateError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Submitting Customer");
        alert.setContentText("Please correct the following error: " + errorMessage);
        alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        // set generated address ID initial value
        try {
            PreparedStatement stm = LoginScreenController.dbConnect.prepareStatement("SELECT MAX(addressId) AS maxId FROM address");
            ResultSet results = stm.executeQuery();
            if (results.next() == false) {
                generatedId = 0;
            } else {
                generatedId = results.getInt("maxId") + 1;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Current generated address/customer ID: " + generatedId);
        
        
        // override toString method for comboboxes to clear visual error
        countryComboBox.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country object) {
                return object.getCountryName();
            }
            
            @Override
            public Country fromString(String string) {
                // lambda utilized to efficiently code the override string method
                return countryComboBox.getItems().stream().filter(ap -> 
                ap.getCountryName().equals(string)).findFirst().orElse(null);
            }
        });
        
        // override toString method for comboboxes to clear visual error
        cityComboBox.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City object) {
                return object.getCityName();
            }
            
            @Override
            public City fromString(String string) {
                // lambda utilized to efficiently code the override string method
                return cityComboBox.getItems().stream().filter(ap -> 
                ap.getCityName().equals(string)).findFirst().orElse(null);
            }
        });
        
        if (customerList.isEmpty()) {
            System.out.println("No customers to populate table yet.");
        } else {
            customerTableView.setItems(customerList);
        }
        
        try {
            populateCityList();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            populateCountryList();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // populate city and country comboboxes and start city combobox in disabled state
        cityComboBox.setDisable(true);
    }    
}
