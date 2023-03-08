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
import model.scheduler.customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * FXML Controller class for appointments.FXML
 *
 * @author Trayer L Harvey
 */
public class appointmentsController implements Initializable {

    //FXML START--------------------------------------------------------------
    //form----------------------------------------
    @FXML
    private TextField apptTitleTextField;
    @FXML
    private TextField apptDescriptionTextField;
    @FXML
    private TextField apptLocationTextField;
    @FXML
    private ComboBox apptContactComboBox;
    @FXML
    private TextField apptTypeTextField;
    @FXML
    private DatePicker apptDatePicker;

    @FXML
    private TextField apptStartTextField;
    @FXML
    private TextField apptEndTextField;
    @FXML
    private ComboBox apptCustomerComboBox;
    //action buttons---------------------------------------
    @FXML
    private Button addButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private RadioButton allRadio;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private RadioButton monthRadio;
    //Table------------------------------------------------
    @FXML
    private TableView<appointment> appointmentsTable;
    @FXML
    private TableColumn<appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<appointment, String> titleCol;
    @FXML
    private TableColumn<appointment, String> descCol;
    @FXML
    private TableColumn<appointment, String> locationCol;
    @FXML
    private TableColumn<appointment, String> apptContactIdCol;
    @FXML
    private TableColumn<appointment, String> typeCol;
    @FXML
    private TableColumn<appointment, String> startCol;
    @FXML
    private TableColumn<appointment, String> endCol;
    @FXML
    private TableColumn<appointment, Integer> apptCustomerIdCol;

    //nav buttons------------------------------------------
    @FXML
    private Button back;
    @FXML
    private Button close;

    //labels + messages------------------------------------
    @FXML
    private Label username;
    @FXML
    private Label apptIdLabel;
    //FXML END---------------------------------------------------------------------------------------
    //setup--------------------------
    String User_Name = "";
    Integer User_ID = 0;
    public void setUser(String s) {
        username.setText(s);
        User_Name = s;
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            //set ID
            String sql = "SELECT User_ID FROM users WHERE User_Name='" + User_Name + "' LIMIT 1";
            ResultSet result = connectDB.createStatement().executeQuery(sql);
            while (result.next()) {
                User_ID = result.getInt(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    //Vars
    Integer selectedApptID;
    String selectedContactName="";
    Integer selectedContactID=0;
    String selectedCustomerName="";
    Integer selectedCustomerID=0;

    //main-----------------------------------------------------------------------------------
    /**
     * Initializes the  appointments controller class
     *
     */
    public void initialize(URL url, ResourceBundle rb) {
        //init textfields
        PropertyValueFactory<appointment, Integer> aIdFactory = new PropertyValueFactory<>("apptID");
        PropertyValueFactory<appointment, String> aTitleFactory = new PropertyValueFactory<>("apptTitle");
        PropertyValueFactory<appointment, String> aDescFactory = new PropertyValueFactory<>("apptDesc");
        PropertyValueFactory<appointment, String> aLocationFactory = new PropertyValueFactory<>("apptLocation");
        PropertyValueFactory<appointment, String> aContactIDFactory = new PropertyValueFactory<>("apptContactID");
        PropertyValueFactory<appointment, String> aTypeFactory = new PropertyValueFactory<>("apptType");
        PropertyValueFactory<appointment, String> aDateFactory = new PropertyValueFactory<>("apptDate");
        PropertyValueFactory<appointment, String> aStartTimeFactory = new PropertyValueFactory<>("apptStart");
        PropertyValueFactory<appointment, String> aEndTimeFactory = new PropertyValueFactory<>("apptEnd");
        PropertyValueFactory<appointment, Integer> aCustIDFactory = new PropertyValueFactory<>("apptCustomerID");
        //LAMBDA: prevent user from picking dtaes prior to today or on the weekend
        /**
         * LAMBDA updateItem function to disable days we're closed (weekends)
         *
         */
        apptDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty
                        || date.compareTo(today) < 0
                        || date.getDayOfWeek() == DayOfWeek.SATURDAY
                        || date.getDayOfWeek() == DayOfWeek.SUNDAY
                );
            }
        });
        apptIdCol.setCellValueFactory(aIdFactory);
        titleCol.setCellValueFactory(aTitleFactory);
        descCol.setCellValueFactory(aDescFactory);
        locationCol.setCellValueFactory(aLocationFactory);
        apptContactIdCol.setCellValueFactory(aContactIDFactory);
        typeCol.setCellValueFactory(aTypeFactory);
        startCol.setCellValueFactory(aStartTimeFactory);
        endCol.setCellValueFactory(aEndTimeFactory);
        apptCustomerIdCol.setCellValueFactory(aCustIDFactory);
        //query for all contacts + countries----------------------------------------------------------------------------
            //1 init array size integers
        Integer numContacts=0;
        Integer numCustomers=0;
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
        }catch(Exception exception){
            exception.printStackTrace();
        }
            //2 init arrays for name using sizes found in 1
        List allContacts = new ArrayList(numContacts);
        List allCustomers = new ArrayList(numCustomers);
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
        }catch(Exception exception){
            exception.printStackTrace();
        }
            //3 init comboboxes/dropdowns
        ObservableList<String> contactsList = FXCollections.observableArrayList(allContacts);
        ObservableList<String> customersList = FXCollections.observableArrayList(allCustomers);
        PropertyValueFactory<appointment, String> contactFactory = new PropertyValueFactory<>("apptContactComboBox");
        PropertyValueFactory<appointment, String> customerFactory = new PropertyValueFactory<>("apptCustomerComboBox");
        apptContactComboBox.setItems(contactsList);
        apptCustomerComboBox.setItems(customersList);

        //populate table
        populateTable();
    }

    /**
     * called by button action, adds or updates a record in appointments
     */
    public void modifyAppt(ActionEvent e) {
        //Time setup----------------------------------------------------
            //get field inputs, store as strings
        String selectedContactName = (apptContactComboBox.getValue() == null)?"":apptContactComboBox.getValue().toString();
        selectContactID(selectedContactName);
        String selectedCustomerName = (apptCustomerComboBox.getValue()==null)?"":apptCustomerComboBox.getValue().toString();
        selectCustomerID(selectedCustomerName);
        LocalDate date = apptDatePicker.getValue()==null?LocalDate.parse("0001-01-01"):apptDatePicker.getValue();
        LocalTime startTime = LocalTime.parse(apptStartTextField.getText().isEmpty() ? "00:00" : apptStartTextField.getText());
        LocalTime endTime = LocalTime.parse(apptEndTextField.getText().isEmpty() ? "00:00" : apptEndTextField.getText());
            //process time, merge to zonedatetimes
                //get local zoneid
        ZoneId localZoneID = ZoneId.systemDefault();
            //convert local datetime to utc datetime before inserting
        ZonedDateTime startZDT = ZonedDateTime.of(date, startTime, localZoneID);
        ZonedDateTime endZDT = ZonedDateTime.of(date, endTime, localZoneID);
        ZonedDateTime startUTC = startZDT.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = endZDT.withZoneSameInstant(ZoneOffset.UTC);
        //END TIME SETUP----------------------------------------------
        //only add if time is valid
        if (timeIsValid(startUTC, endUTC)){
            String startString = Timestamp.valueOf(startUTC.toLocalDateTime()).toString();
            String endString = Timestamp.valueOf(endUTC.toLocalDateTime()).toString();
            //setup + get vars---------------------------------------------------------------------
            //Integer Appointment_ID
            String Title = apptTitleTextField.getText();
            String Description = apptDescriptionTextField.getText();
            String Location = apptLocationTextField.getText();
            String Type = apptTypeTextField.getText();

            String Created_By = User_Name;
            String Last_Updated_By = User_Name;

            //add and/or modify-------------------------------------------------------------------------
            if (apptIdLabel.getText()==""){
                //add new-------------------------------
                try {
                    //connect
                    databaseConnect connectNow = new databaseConnect();
                    Connection connectDB = connectNow.getConnection();
                    //Write Query-------------------------------------------------------------
                    String sql = "INSERT INTO `appointments`(`Title`, " +
                            "`Description`, " +
                            "`Location`, " +
                            "`Type`, " +
                            "`Start`, " +
                            "`End`, " +
                            "`Created_By`, " +
                            "`Last_Update`, " +
                            "`Last_Updated_By`, " +
                            "`Customer_ID`, " +
                            "`User_ID`, " +
                            "`Contact_ID`) " +
                            "VALUES ('"+Title+"', " +
                            "'"+Description+"', " +
                            "'"+Location+"', " +
                            "'"+Type+"', " +
                            "'"+startString+"', " +
                            "'"+endString+"', " +
                            "'"+Created_By+"', " +
                            "CURRENT_TIMESTAMP, " +
                            "'"+Last_Updated_By+"', " +
                            ""+selectedCustomerID+", " +
                            ""+User_ID+", " +
                            ""+selectedContactID+")";
                    //submit query
                    try {
                        Statement statement = connectDB.createStatement();
                        statement.executeUpdate(sql);
                    } catch (Exception exception) {
                        System.out.println("Insert Query Failed: "+sql);
                        exception.printStackTrace();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                //update--------------------------------
                try {
                    //DBQuery1of4 connect
                    databaseConnect connectNow = new databaseConnect();
                    Connection connectDB = connectNow.getConnection();

                    //DBQuery3of4 write script
                    String sql = "UPDATE `appointments` " +
                            "SET `Title`='"+Title+"'," +
                            "`Description`='"+Description+"'," +
                            "`Location`='"+Location+"'," +
                            "`Type`='"+Type+"'," +
                            "`Start`='"+startString+"'," +
                            "`End`='"+endString+"'," +
                            "`Last_Update`=CURRENT_TIMESTAMP," +
                            "`Last_Updated_By`='"+Last_Updated_By+"'," +
                            "`Customer_ID`='"+selectedCustomerID+"'," +
                            "`User_ID`='"+User_ID+"'," +
                            "`Contact_ID`='"+selectedContactID+"' " +
                            "WHERE `Appointment_ID`="+apptIdLabel.getText().substring(3);
                    //DBQuery4of4 run script
                    try {
                        Statement statement = connectDB.createStatement();
                        statement.executeUpdate(sql);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    //clear form after either----------------------------
                    clearForm();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            //populate table-----------------------------------------------------------------------------
            this.populateTable();
        }
    }
    /**
     * populates form with appointment data from selected table row
     * Uses Lambda functions to get selected name from combobox dynamically
     */
    public void loadAppt(ActionEvent e) {
        //init
        appointment a = appointmentsTable.getSelectionModel().getSelectedItem();
        //if selected, load, else do nothing
        if (a!=null){
            //query for Names
            selectedContactID = a.getApptContactID();
            selectedCustomerID = a.getApptCustomerID();

            try {
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();
                ResultSet result = connectDB.createStatement().executeQuery("SELECT Contact_Name FROM contacts WHERE Contact_ID='"+selectedContactID+"'");
                while(result.next()){
                    selectedContactName = result.getString(1);
                }
                selectedCustomerID = a.getApptCustomerID();
                result = connectDB.createStatement().executeQuery("SELECT Customer_Name FROM customers WHERE Customer_ID='"+selectedCustomerID+"'");
                while(result.next()){
                    selectedCustomerName = result.getString(1);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //load form values
            selectedApptID = a.getApptID();
            apptIdLabel.setText("ID: "+selectedApptID);
            apptTitleTextField.setText(a.getApptTitle());
            apptDescriptionTextField.setText(a.getApptDesc());
            apptLocationTextField.setText(a.getApptLocation());
            apptContactComboBox.setValue(selectedContactName);
            apptCustomerComboBox.setValue(selectedCustomerName);
            apptTypeTextField.setText(a.getApptType());
            if (a.getApptStart()==null || a.getApptStart().length()==0) {
                apptDatePicker.setValue(null);
                apptStartTextField.setText("");
            } else {
                apptStartTextField.setText(a.getApptStart().substring(11,16));
                if(a.getApptStart().substring(0, 4).equals("0000") || a.getApptStart().substring(5, 7).equals("00") || a.getApptStart().substring(8, 10).equals("00")){
                    apptDatePicker.setValue(null);
                } else {
                    apptDatePicker.setValue(LocalDate.parse(a.getApptStart().substring(0, 10)));
                }
            }
            if (a.getApptEnd()==null || a.getApptEnd().length()==0) {
                apptDatePicker.setValue(null);
                apptEndTextField.setText("");
            } else {
                apptEndTextField.setText(a.getApptEnd()==null?"":a.getApptEnd().substring(11,16));
            }
            //update add button
            addButton.setText("Update");
        }
        //LAMBDA to easily save contactID from contactName/customerID from customerName
        apptContactComboBox.setOnKeyReleased(event -> {selectContactID(selectedContactName);});
        apptCustomerComboBox.setOnKeyReleased(event -> {selectCustomerID(selectedCustomerName);});
    }

    /**
     * deletes an appointment entry on user confirm
     */
    public void deleteAppt(ActionEvent e) {
        //get selected appointment
        appointment a = appointmentsTable.getSelectionModel().getSelectedItem();
        selectedApptID = a.getApptID();

        //alert to check
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Appointment #" + selectedApptID + "?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            //delete
            try {
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();

                String sql = "DELETE FROM appointments WHERE Appointment_ID="+selectedApptID;

                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        //refresh table
        populateTable();
    }

    /**
     * loads weekly or monthly views of appointment data into Table
     */
    public void alterView(ActionEvent e) {
        ToggleGroup radioButtons = new ToggleGroup();
        allRadio.setToggleGroup(radioButtons);
        weekRadio.setToggleGroup(radioButtons);
        monthRadio.setToggleGroup(radioButtons);
        RadioButton selected = (RadioButton) radioButtons.getSelectedToggle();
        switch (selected.getText()) {
            case "View All" :
                populateTable();
                break;
            case "View This Week" :
                //init vars
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, 7);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datePlusWeek = formatter.format(c.getTime());
                try {
                    databaseConnect connectNow = new databaseConnect();
                    Connection connectDB = connectNow.getConnection();
                    String sql = "SELECT * FROM appointments " +
                                 "WHERE Start > CURRENT_TIMESTAMP " +
                                 "AND End < '"+datePlusWeek+"'";
                    ResultSet results = connectDB.createStatement().executeQuery(sql);
                    appointmentsTable.getItems().clear();
                    while (results.next()) {
                        appointment a = new appointment();
                        a.setApptID(results.getInt("Appointment_ID"));
                        a.setApptTitle(results.getString("Title"));
                        a.setApptDesc(results.getString("Description"));
                        a.setApptLocation(results.getString("Location"));
                        a.setApptContactID(results.getInt("Contact_ID"));
                        a.setApptType(results.getString("Type"));
                        a.setApptStart(results.getString("Start"));
                        a.setApptEnd(results.getString("End"));
                        a.setApptCustomerID(results.getInt("Customer_ID"));
                        appointmentList.addAll(a);
                    }
                    appointmentsTable.setItems(appointmentList);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case "View This Month" :
                //init vars
                c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, 30);
                formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datePlusMonth = formatter.format(c.getTime());
                try {
                    databaseConnect connectNow = new databaseConnect();
                    Connection connectDB = connectNow.getConnection();
                    String sql = "SELECT * FROM appointments " +
                            "WHERE Start > CURRENT_TIMESTAMP " +
                            "AND End < '"+datePlusMonth+"'";
                    ResultSet results = connectDB.createStatement().executeQuery(sql);
                    appointmentsTable.getItems().clear();
                    while (results.next()) {
                        appointment a = new appointment();
                        a.setApptID(results.getInt("Appointment_ID"));
                        a.setApptTitle(results.getString("Title"));
                        a.setApptDesc(results.getString("Description"));
                        a.setApptLocation(results.getString("Location"));
                        a.setApptContactID(results.getInt("Contact_ID"));
                        a.setApptType(results.getString("Type"));
                        a.setApptStart(results.getString("Start"));
                        a.setApptEnd(results.getString("End"));
                        a.setApptCustomerID(results.getInt("Customer_ID"));
                        appointmentList.addAll(a);
                    }
                    appointmentsTable.setItems(appointmentList);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
        }
    }

    //General-----------------------------------------------------------------------------
    /**
     * checks to ensure entered time is between business hours EST
     */
    public boolean timeIsValid(ZonedDateTime startUTC, ZonedDateTime endUTC) {
        boolean isValid;
        //assure start is not before 8amEST
            //convert startUTC to EST
        OffsetDateTime startEST = startUTC.toOffsetDateTime().withOffsetSameInstant(ZoneId.of("America/New_York").getRules().getOffset(Instant.now()));
        ZonedDateTime startZDT = startEST.atZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime startESTlocal = startZDT.toLocalDateTime();
        LocalDateTime eightAMeastern = LocalDateTime.of(startEST.getYear(),startEST.getMonth(),startEST.getDayOfMonth(),8,0);
        //assure end is not after 10pmEST
        OffsetDateTime endEST = endUTC.toOffsetDateTime().withOffsetSameInstant(ZoneId.of("America/New_York").getRules().getOffset(Instant.now()));
        ZonedDateTime endZDT = endEST.atZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime endESTlocal = startZDT.toLocalDateTime();
        LocalDateTime tenPMeastern = LocalDateTime.of(startEST.getYear(),startEST.getMonth(),startEST.getDayOfMonth(),22,0);
        //compare. if start before 8AMest or end after 10PMest
        if (startESTlocal.isBefore(eightAMeastern) || endESTlocal.isAfter(tenPMeastern)) {
            //invalid
            isValid=false;
            if (endESTlocal.isAfter(tenPMeastern)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment End Time in EST: " + endESTlocal + "is after our operating hours(EST) that day, of "+tenPMeastern, ButtonType.OK);
                alert.showAndWait();
            }
            if (startESTlocal.isBefore(eightAMeastern)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment Start Time in EST: " + startESTlocal + "is before our operating hours(EST) that day, of "+eightAMeastern, ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            isValid=true;
        }
        return isValid;
    }
    ObservableList<appointment> appointmentList = FXCollections.observableArrayList();

    /**
     * populates table with appointment data from MySQL Database
     */
    public void populateTable(){
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

            ResultSet results = connectDB.createStatement().executeQuery("SELECT * FROM appointments");
            appointmentsTable.getItems().clear();
            while (results.next()) {
                appointment a = new appointment();
                a.setApptID(results.getInt("Appointment_ID"));
                a.setApptTitle(results.getString("Title"));
                a.setApptDesc(results.getString("Description"));
                a.setApptLocation(results.getString("Location"));
                a.setApptContactID(results.getInt("Contact_ID"));
                a.setApptType(results.getString("Type"));
                //time processing---------------------------------------------
                String start="";
                String end="";
                if (results.getString("Start")!=null) {
                    //convert string to timestamp
                    LocalDateTime startLDT = Timestamp.valueOf(results.getString("Start")).toLocalDateTime();
                    LocalDateTime endLDT = Timestamp.valueOf(results.getString("End")).toLocalDateTime();
                    //timestamp to zoneddatetime
                    ZonedDateTime startZDT = ZonedDateTime.of(startLDT, ZoneId.of("UTC"));
                    ZonedDateTime endZDT = ZonedDateTime.of(endLDT, ZoneId.of("UTC"));
                    //zdt from utc to local
                    OffsetDateTime startLocal = startZDT.toOffsetDateTime().withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
                    OffsetDateTime endLocal = endZDT.toOffsetDateTime().withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
                    //set strings
                    start = startLocal.toString().substring(0, startLocal.toString().length()-6);
                    end = endLocal.toString().substring(0, endLocal.toString().length()-6);
                }
                a.setApptStart(start);
                a.setApptEnd(end);
                //END time processing-----------------------------------------
                a.setApptCustomerID(results.getInt("Customer_ID"));
                appointmentList.addAll(a);
            }
            appointmentsTable.setItems(appointmentList);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * clears form and populates default prompt data
     */
    public void clearForm() {
        selectedApptID = null;
        apptTitleTextField.setPromptText("Title");
        apptDescriptionTextField.setPromptText("Description");
        apptLocationTextField.setPromptText("Location");
        apptContactComboBox.setValue("Contact");
        apptTypeTextField.setPromptText("Type");
        apptDatePicker.setValue(null);
        apptStartTextField.setPromptText("Start Time");
        apptEndTextField.setPromptText("End Time");
        apptCustomerComboBox.setValue("Customer");

        addButton.setText("add");
    }

    public void selectContactID(String Contact_Name){
        Integer Contact_ID = 0;
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            //set ID-------------------
            ResultSet result = connectDB.createStatement().executeQuery("SELECT Contact_ID FROM contacts WHERE Contact_Name='" + Contact_Name + "'");
            while (result.next()) {
                Contact_ID = result.getInt(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        selectedContactID=Contact_ID;
    }

    public void selectCustomerID(String Customer_Name) {
        Integer Customer_ID = 0;
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            //set ID-------------------
            String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + Customer_Name + "'";
            ResultSet result = connectDB.createStatement().executeQuery(sql);
            while (result.next()) {
                Customer_ID = result.getInt(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        selectedCustomerID=Customer_ID;
    }

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
