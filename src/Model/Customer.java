package Model;

public class Customer {
    
    private int customerId;
    private String customerName;
    private Address address;
    private String countryName;
    
    // main customer constructor
    public Customer(int customerId, String customerName, Address address, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.countryName = country;
    }
    
    // partial customer constructor
    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }
    
    // empty constructor
    public Customer() {
        
    }
    
    // getters & setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
}
