package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointment {
    
    private Users user;
    private String title;
    private String description;
    private SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleStringProperty type;
    
    private static ObservableList<Appointment> apptList = FXCollections.observableArrayList();
    
    public Appointment(Users user, String title, String description, String date, String time, String type) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.type = new SimpleStringProperty(type);
    }

    // getters & setters
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type.get();
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public ObservableList<Appointment> getApptList() {
        return apptList;
    }
}
