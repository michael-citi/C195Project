package Model;

public class Appointment {
    
    private String startDate;
    private String endDate;
    private String type;
    private int appointmentId;
    private String description;
    private Customer customer;
    
    // main appointment constructor
    public Appointment(int appointmentId, String startDate, String endDate, String type, String description, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.appointmentId = appointmentId;
        this.description = description;
        this.customer = customer;
    }
    
    // partial appointment constructor
    public Appointment(String startDate, String endDate, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
    }
    
    // empty constructor
    public Appointment() {
        
    }
    
    // getters & setters
    public String getDescription() {
        return description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
        
    public void setDescription(String description) {    
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}
