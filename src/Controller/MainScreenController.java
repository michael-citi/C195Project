package Controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainScreenController implements Initializable {
    
    private ObservableList<Users> tempUserList = FXCollections.observableArrayList();
    private ObservableList<Appointment> tempApptList = FXCollections.observableArrayList();
    
    @FXML TableView<Appointment> mainTableView;
    @FXML TableColumn<Appointment, String> dateColumn;
    @FXML TableColumn<Appointment, String> userColumn;
    @FXML TableColumn<Appointment, String> timeColumn;
    @FXML TableColumn<Appointment, String> typeColumn;
    
    @FXML Label userTextLabel;
    @FXML DatePicker mainDatePicker;
    
    private RadioButton weeklyRadioBtn;
    private RadioButton monthlyRadioBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
