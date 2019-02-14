package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    
    private SimpleIntegerProperty customerId;
    private SimpleStringProperty customerName;
    private Address address;
    private SimpleStringProperty countryName;
    
    // main customer constructor
    public Customer(int customerId, String customerName, Address address, String country) {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.address = address;
        this.countryName = new SimpleStringProperty(country);
    }
    
    // partial customer constructor
    public Customer(int customerId, String customerName) {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
    }
    
    // empty constructor
    public Customer() {
        
    }

    // getters & setters
    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCountryName() {
        return countryName.get();
    }

    public void setCountryName(String countryName) {
        this.countryName.set(countryName);
    }
}
