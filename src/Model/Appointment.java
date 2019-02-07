package Model;

public class Appointment {
    
    private String startDate;
    private String endDate;
    private String type;
    private String userName;
    private int appointmentId;
    private String description;
    
    // main appointment constructor
    public Appointment(int appointmentId, String startDate, String endDate, String type, String userName, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.userName = userName;
        this.appointmentId = appointmentId;
        this.description = description;
    }
    
    // partial appointment constructor
    public Appointment(String startDate, String endDate, String userName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.userName = userName;
    }
    
    // empty constructor
    public Appointment() {
        
    }
    
    // getters & setters
    public String getDescription() {
        return description;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
