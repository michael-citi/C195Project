package Model;

public class Address {
    
    private int addressId;
    private String address;
    private int cityId;
    private String zipCode;
    private String phoneNumber;
    
    // main address constructor
    public Address(int addressId, String address, int cityId, String zipCode, String phoneNumber) {
        this.addressId = addressId;
        this.address = address;
        this.cityId = cityId;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }
    
    // emtpy constructor
    public Address() {
        
    }

    // getters & setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
