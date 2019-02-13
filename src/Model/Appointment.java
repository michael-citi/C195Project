package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Appointment {
    
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleIntegerProperty appointmentId;
    private SimpleStringProperty description;
    private Customer customer;
    private SimpleStringProperty title;
    private SimpleStringProperty user;
    
    // main appointment constructor
    public Appointment(int appointmentId, String startDate, String endDate, String title, String description, Customer customer, String user) {
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.description = new SimpleStringProperty(description);
        this.title = new SimpleStringProperty(title);
        this.customer = customer;
        this.user = new SimpleStringProperty(user);
    }
    
    // partial appointment constructor
    public Appointment(String startDate, String endDate, Customer customer) {
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.customer = customer;
    }
    
    // empty constructor
    public Appointment() {
        
    }
    
    // getters & setters
    public String getDescription() {
        return description.get();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
        
    public void setDescription(String description) {    
        this.description.set(description);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }
    
    public String getUser() {
        return user.get();
    }
    
    public void setUser(String user) {
        this.user.set(user);
    }
}
