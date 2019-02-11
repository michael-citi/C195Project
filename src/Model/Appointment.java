package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Appointment {
    
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty type;
    private SimpleIntegerProperty appointmentId;
    private SimpleStringProperty description;
    private Customer customer;
    
    // main appointment constructor
    public Appointment(int appointmentId, String startDate, String endDate, String type, String description, Customer customer) {
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.type = new SimpleStringProperty(type);
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.description = new SimpleStringProperty(description);
        this.customer = customer;
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

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }
}
