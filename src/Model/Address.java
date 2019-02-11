package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Address {
    
    private SimpleIntegerProperty addressId;
    private SimpleStringProperty address;
    private SimpleIntegerProperty cityId;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty phoneNumber;
    
    // main address constructor
    public Address(int addressId, String address, int cityId, String zipCode, String phoneNumber) {
        this.addressId = new SimpleIntegerProperty(addressId);
        this.address = new SimpleStringProperty(address);
        this.cityId = new SimpleIntegerProperty(cityId);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }
    
    // emtpy constructor
    public Address() {
        
    }

    // getters & setters
    public int getAddressId() {
        return addressId.get();
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getCityId() {
        return cityId.get();
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }
}
