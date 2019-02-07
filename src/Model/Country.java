package Model;

public class Country {
    
    private int countryId;
    private String countryName;
    
    // main country constructor
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    // empty constructor
    public Country() {
        
    }
    
    // getters & setters
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
