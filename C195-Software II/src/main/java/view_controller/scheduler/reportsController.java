package view_controller.scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.scheduler.appointment;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class for reports.FXML
 *
 * @author Trayer L Harvey
 */
public class reportsController  implements Initializable {
    ///////////////////////////////////////////////SETUP/////////////////////////////////////////////
    //Table----------------------------------------------------
    @FXML
    private TableView<appointment> appointmentsTable;
    @FXML
    private TableColumn<appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<appointment, String> titleCol;
    @FXML
    private TableColumn<appointment, String> descCol;
    @FXML
    private TableColumn<appointment, String> typeCol;
    @FXML
    private TableColumn<appointment, String> startCol;
    @FXML
    private TableColumn<appointment, String> endCol;
    @FXML
    private TableColumn<appointment, Integer> apptCustomerIdCol;
    //comboboxes-----------------------------------------------
    @FXML
    private ComboBox apptTypeComboBox;
    @FXML
    private ComboBox apptMonthComboBox;
    @FXML
    private ComboBox contactComboBox;
    @FXML
    private ComboBox customerComboBox;
    //nav buttons------------------------------------------------
    @FXML
    private Button back;
    @FXML
    private Button close;
    //Labels------------------------------------------------------
    @FXML
    private Label username;
    @FXML
    private Label apptTypeCountLabel;
    @FXML
    private Label apptMonthCountLabel;
    @FXML
    private Label contactActiveLabel;
    @FXML
    private Label customerActiveLabel;
    //local variables--------------------------------------
    String User_Name = "";
    Integer User_ID = 0;
    String selectedType = "";
    String selectedMonth = "";
    String selectedContact = "";
    String selectedCustomer = "";
    ObservableList<appointment> appointmentList = FXCollections.observableArrayList();
    public void setUser(String s) {
        username.setText(s);
        User_Name = s;
        String sql="";
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            //set ID
            sql = "SELECT User_ID FROM users WHERE User_Name='" + User_Name + "' LIMIT 1";
            ResultSet result = connectDB.createStatement().executeQuery(sql);
            while (result.next()) {
                User_ID = result.getInt(1);

            }
        } catch (Exception exception) {
            System.out.println("user id query failed in reports controller: "+sql);
            exception.printStackTrace();
        }
    }
    //////////////////////////////////////FUNCTIONS////////////////////////////////////////////
    //Main-------------------------------------------------------------------------------------
    /**
     * Initializes the  appointments controller class
     *
     */
    public void initialize(URL url, ResourceBundle rb) {
        //init table
        PropertyValueFactory<appointment, Integer> aIdFactory = new PropertyValueFactory<>("apptID");
        PropertyValueFactory<appointment, String> aTitleFactory = new PropertyValueFactory<>("apptTitle");
        PropertyValueFactory<appointment, String> aTypeFactory = new PropertyValueFactory<>("apptType");
        PropertyValueFactory<appointment, String> aDescFactory = new PropertyValueFactory<>("apptDesc");
        PropertyValueFactory<appointment, String> aStartTimeFactory = new PropertyValueFactory<>("apptStart");
        PropertyValueFactory<appointment, String> aEndTimeFactory = new PropertyValueFactory<>("apptEnd");
        PropertyValueFactory<appointment, Integer> aCustIDFactory = new PropertyValueFactory<>("apptCustomerID");
        apptIdCol.setCellValueFactory(aIdFactory);
        titleCol.setCellValueFactory(aTitleFactory);
        typeCol.setCellValueFactory(aTypeFactory);
        descCol.setCellValueFactory(aDescFactory);
        startCol.setCellValueFactory(aStartTimeFactory);
        endCol.setCellValueFactory(aEndTimeFactory);
        apptCustomerIdCol.setCellValueFactory(aCustIDFactory);
        //init comboboxes---------
            //1 init array size integers
        Integer numContacts=0;
        Integer numCustomers=0;
        Integer numTypes=0;
        Integer numMonths=0;
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

            ResultSet result = connectDB.createStatement().executeQuery("SELECT COUNT(Contact_ID) FROM contacts");
            result.next();
            //set num of contacts
            numContacts = result.getInt(1);

            result = connectDB.createStatement().executeQuery("SELECT COUNT(Customer_ID) FROM customers");
            result.next();
            //set num of customers
            numCustomers = result.getInt(1);

            result = connectDB.createStatement().executeQuery("SELECT COUNT(Type) FROM appointments");
            result.next();
            //set count of type

            result = connectDB.createStatement().executeQuery("SELECT COUNT(MONTH(Start)) FROM appointments");
            result.next();
            //set count of months
            numMonths = result.getInt(1);
        }catch(Exception exception){
            exception.printStackTrace();
        }
            //2 init string lists using counts
        List allContacts = new ArrayList(numContacts);
        List allCustomers = new ArrayList(numCustomers);
        List allTypes = new ArrayList(numTypes);
        List allMonths = new ArrayList(numMonths);
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

                //get and set a list of all contact names
            ResultSet results = connectDB.createStatement().executeQuery("SELECT * FROM contacts");
            while(results.next()){
                allContacts.add(results.getString("Contact_Name"));
            }
                //get and set a list of all customer names
            results = connectDB.createStatement().executeQuery("SELECT * FROM customers");
            while(results.next()){
                allCustomers.add(results.getString("Customer_Name"));
            }
                //get and set a list of all types
            results = connectDB.createStatement().executeQuery("SELECT DISTINCT Type FROM appointments");
            while(results.next()){
                allTypes.add(results.getString(1));
            }
                //get and set a list of all months
            results = connectDB.createStatement().executeQuery("SELECT DISTINCT (MONTH(Start)) FROM appointments");
            while(results.next()){
                allMonths.add(results.getString(1));
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
            //3 init comboboxes
        ObservableList<String> typeList = FXCollections.observableArrayList(allTypes);
        ObservableList<String> monthList = FXCollections.observableArrayList(allMonths);
        ObservableList<String> contactsList = FXCollections.observableArrayList(allContacts);
        ObservableList<String> customersList = FXCollections.observableArrayList(allCustomers);
        PropertyValueFactory<appointment, String> typeFactory = new PropertyValueFactory<>("apptTypeComboBox");
        PropertyValueFactory<appointment, String> monthFactory = new PropertyValueFactory<>("apptMonthComboBox");
        PropertyValueFactory<appointment, String> contactFactory = new PropertyValueFactory<>("contactComboBox");
        PropertyValueFactory<appointment, String> customerFactory = new PropertyValueFactory<>("customerComboBox");
        apptTypeComboBox.setItems(typeList);
        apptMonthComboBox.setItems(monthList);
        contactComboBox.setItems(contactsList);
        customerComboBox.setItems(customersList);
    }

    /**
     * submits a sql query to count all entries that match given type
     */
    public void getAppointmentsByType(){
        if(apptTypeComboBox.getValue()==null){
            selectedType="";
            apptTypeCountLabel.setText("");
        } else {
            selectedType=apptTypeComboBox.getValue().toString();
        }
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            ResultSet results = connectDB.createStatement().executeQuery("SELECT COUNT(Appointment_ID) FROM appointments WHERE Type='"+selectedType+"'");
            while (results.next()){
                apptTypeCountLabel.setText(String.valueOf(results.getInt(1)));
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * submits a sql query to count all entries that match given month
     */
    public void getAppointmentsByMonth(){
        if(apptMonthComboBox.getValue()==null){
            selectedMonth="";
            apptMonthCountLabel.setText("");
        } else {
            selectedMonth=apptMonthComboBox.getValue().toString();
        }
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            ResultSet results = connectDB.createStatement().executeQuery("SELECT COUNT(Appointment_ID) FROM appointments WHERE MONTH(START)='"+selectedMonth+"'");
            while (results.next()){
                apptMonthCountLabel.setText(String.valueOf(results.getInt(1)));
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * submits a sql query to get all entries that match a given contact
     */
    public void getAppointmentsByContact(){
        if(contactComboBox.getValue()!=null){
            try{
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();
                String sql = "SELECT * FROM appointments " +
                        "INNER JOIN contacts " +
                        "ON Appointments.Contact_ID = Contacts.Contact_ID " +
                        "WHERE Contacts.Contact_Name = '"+contactComboBox.getValue().toString()+"'";
                ResultSet results = connectDB.createStatement().executeQuery(sql);
                appointmentsTable.getItems().clear();
                while (results.next()) {
                    appointment a = new appointment();
                    a.setApptID(results.getInt("Appointment_ID"));
                    a.setApptTitle(results.getString("Title"));
                    a.setApptType(results.getString("Type"));
                    a.setApptDesc(results.getString("Description"));
                    a.setApptStart(results.getString("Start"));
                    a.setApptEnd(results.getString("End"));
                    a.setApptCustomerID(results.getInt("Customer_ID"));
                    appointmentList.addAll(a);
                }
                appointmentsTable.setItems(appointmentList);
                customerActiveLabel.setText("");
                contactActiveLabel.setText("Active");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            appointmentsTable.getItems().clear();
        }
    }

    /**
     * submits a sql query to get all entries that match a given customer
     */
    public void getAppointmentsByCustomer(){
        if (customerComboBox.getValue()!=null){
            try{
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();
                String sql = "SELECT * FROM appointments " +
                        "INNER JOIN customers " +
                        "ON Appointments.Customer_ID = customers.Customer_ID " +
                        "WHERE customers.Customer_Name = '"+customerComboBox.getValue().toString()+"'";
                ResultSet results = connectDB.createStatement().executeQuery(sql);
                appointmentsTable.getItems().clear();
                while (results.next()) {
                    appointment a = new appointment();
                    a.setApptID(results.getInt("Appointment_ID"));
                    a.setApptTitle(results.getString("Title"));
                    a.setApptType(results.getString("Type"));
                    a.setApptDesc(results.getString("Description"));
                    a.setApptStart(results.getString("Start"));
                    a.setApptEnd(results.getString("End"));
                    a.setApptCustomerID(results.getInt("Customer_ID"));
                    appointmentList.addAll(a);
                }
                appointmentsTable.setItems(appointmentList);
                customerActiveLabel.setText("Active");
                contactActiveLabel.setText("");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            appointmentsTable.getItems().clear();
        }
    }
    //General Functions------------------------------------------------------------------------
    /**
     * returns to home.FXML
     */
    public void backToHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            homeController homeController = loader.getController();
            homeController.setUser(User_Name);
            Stage stage = (Stage) back.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes Application
     */
    public void closeButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
