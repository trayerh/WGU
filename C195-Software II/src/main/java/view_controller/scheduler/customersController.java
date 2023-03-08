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

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import model.scheduler.customer;

/**
 * FXML Controller class for customers.FXML
 *
 * @author Trayer L Harvey
 */
public class customersController implements Initializable {
    @FXML
    private Button add;
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerPhoneNumber;
    @FXML
    private TextField customerAddress;
    @FXML
    private TextField customerPostalCode;
    @FXML
    private ComboBox customerCountry;
    @FXML
    private ComboBox customerDivision;
    @FXML
    private Label username;
    @FXML
    private Label customerID;
    @FXML
    private Button close;
    @FXML
    private Label customerAlert;
    @FXML
    private Button back;
    @FXML
    private TableView<customer> customersTable;
    @FXML
    private TableColumn<customer, Integer> idCol;
    @FXML
    private TableColumn<customer, String> nameCol;
    @FXML
    private TableColumn<customer, String> phoneCol;
    @FXML
    private TableColumn<customer, String> addressCol;
    @FXML
    private TableColumn<customer, String> divisionCol;
    @FXML
    private TableColumn<customer, String> postalCol;
    @FXML
    private TableColumn<customer, String> countryCol;

    //SETUP-------------------------------------------------------|
    ObservableList<customer> customerList = FXCollections.observableArrayList();
    Integer selectedCustomerID;
    String selectedCountry = "";
    String selectedDivision;
    String User_Name = "";

    public void setUser(String s) {
        username.setText(s);
        User_Name = s;
    }

    /**
     * Initializes the  customers controller class
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init fields
        PropertyValueFactory<customer, Integer>  idFactory = new PropertyValueFactory<>("customerID");
        PropertyValueFactory<customer, String> nameFactory = new PropertyValueFactory<>("customerName");
        PropertyValueFactory<customer, String> phoneFactory = new PropertyValueFactory<>("customerPhone");
        PropertyValueFactory<customer, String> addressFactory = new PropertyValueFactory<>("customerAddress");
        PropertyValueFactory<customer, String> postalFactory = new PropertyValueFactory<>("customerPostalCode");
        PropertyValueFactory<customer, String> countryFactory = new PropertyValueFactory<>("customerCountry");
        PropertyValueFactory<customer, String> divisionFactory = new PropertyValueFactory<>("customerDivision");
        //init table
        idCol.setCellValueFactory(idFactory);
        nameCol.setCellValueFactory(nameFactory);
        phoneCol.setCellValueFactory(phoneFactory);
        addressCol.setCellValueFactory(addressFactory);
        postalCol.setCellValueFactory(postalFactory);
        countryCol.setCellValueFactory(countryFactory);
        divisionCol.setCellValueFactory(divisionFactory);

        //init list
        ObservableList<String> countryList = FXCollections.observableArrayList();
            //query database for countries
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

            ResultSet result = connectDB.createStatement().executeQuery("SELECT * FROM countries");
            while(result.next()){
                countryList.add(result.getString("Country"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        customerCountry.setItems(countryList);
        customerDivision.setEditable(false);

        //populate table
        this.populateTable();
    }
    //PART 2 - Customers------------------------------------------------------------------------------|
    //     2.2(of7): When adding and updating a customer, text fields are used to collect the
    //               following data: customer name, address, postal code, and phone number.

    /**
     * activated by button click, adds or updates a customers row in MySQL
     */
    public void modifyCustomer(ActionEvent e) {

        //add or update? if no id exists
        if (customerID.getText()=="") {
            //add new
            try {
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();

                //Countries Table----------------------------------|
                //country id: ai
                String country = (customerCountry.getValue()==null)?"":customerCountry.getValue().toString();
                String Created_By = User_Name;
                String Last_Updated_By = User_Name;
                String countriesSql = "INSERT INTO countries (Country, Create_Date, Created_By, Last_Update, Last_Updated_By)" +
                        "               VALUES ('"+country+"',CURRENT_TIMESTAMP,'"+Created_By+"',CURRENT_TIMESTAMP,'"+Last_Updated_By+"')";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(countriesSql);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                //customers table----------------------------------|
                //Cust ID: AI
                String Customer_Name = customerName.getText();
                String Address = customerAddress.getText();
                String Postal_Code = customerPostalCode.getText();
                String Phone = customerPhoneNumber.getText();
                Integer Division_ID = null;
                ResultSet result = connectDB.createStatement().executeQuery("SELECT Division_ID from `first_level_divisions` WHERE Division='"+selectedDivision+"'");
                while(result.next()){
                    Division_ID = result.getInt(1);
                }

                String customersSql = "INSERT INTO customers (Customer_Name,Address,Postal_code,Phone,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                        "VALUES ('"+Customer_Name+"','"+Address+"','"+Postal_Code+"','"+Phone+"',CURRENT_TIMESTAMP,'"+Created_By+"',CURRENT_TIMESTAMP,'"+Last_Updated_By+"',"+Division_ID+")";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(customersSql);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            //update
            try {
                //DBQuery1of4 connect
                databaseConnect connectNow = new databaseConnect();
                Connection connectDB = connectNow.getConnection();
                //DBQuery2of4 vars
                Integer Customer_ID = selectedCustomerID;
                String Customer_Name = customerName.getText();
                String Address = customerAddress.getText();
                String Postal_Code= customerPostalCode.getText();
                String Phone = customerPhoneNumber.getText();
                String Last_Updated_By = this.User_Name;
                //Get div info--------------------------------------------
                ResultSet divisionResult = connectDB.createStatement().executeQuery("SELECT Division_ID from `first_level_divisions` WHERE Division='"+selectedDivision+"'");
                divisionResult.next();
                Integer Division_ID = divisionResult.getInt(1);
                //DBQuery3of4 write script
                String updateCustomersSQL = "" +
                        "UPDATE customers " +
                        "SET " +
                            "Customer_Name='"+Customer_Name+"', " +
                            "Address='"+Address+"', " +
                            "Postal_Code='"+Postal_Code+"', " +
                            "Phone='"+Phone+"', " +
                            "Last_Update=CURRENT_TIMESTAMP, " +
                            "Last_Updated_By='"+Last_Updated_By+"', " +
                            "Division_ID="+Division_ID+" " +
                        "WHERE Customer_ID="+Customer_ID;
                //DBQuery4of4 run script
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(updateCustomersSQL);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                clearForm();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.populateTable();
    }

    //     2.4(of7):  All of the original customer information is displayed on the update form.
    //                Customer_ID must be disabled.
    /**
     * loads selected customer values into form
     */
    public void loadCustomer (ActionEvent e) {

        customer c = customersTable.getSelectionModel().getSelectedItem();
        selectedCustomerID = c.getCustomerID();
        customerID.setText("ID: "+selectedCustomerID);
        customerName.setText(c.getCustomerName());
        customerPhoneNumber.setText(c.getCustomerPhone());
        customerAddress.setText(c.getCustomerAddress());
        customerDivision.setValue(c.getCustomerDivision());
        customerPostalCode.setText(c.getCustomerPostalCode());
        customerCountry.setValue(c.getCustomerCountry());

        //update the add button
        add.setText("Update");
    }

    /**
     * clears form and sets promptTexts
     */
    public void clearForm(){

        selectedCustomerID = null;
        customerID.setText("");
        customerName.setPromptText("Name");
        customerPhoneNumber.setPromptText("Phone Number");
        customerAddress.setPromptText("Address");
        customerDivision.setValue("Division");
        customerPostalCode.setPromptText("Postal Code");
        customerCountry.setValue("Country");

        add.setText("Add");
    }

    /**
     * fills table with Customers from MySQL Database
     */
    public void populateTable(){
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

            ResultSet results = connectDB.createStatement().executeQuery(
                    "SELECT customers.Customer_ID, customers.Customer_Name, customers.Phone, customers.Address, customers.Postal_Code, `first_level_divisions`.`Division`, countries.Country "+
                            "FROM customers "+
                            "INNER JOIN `first_level_divisions` ON  `first_level_divisions`.Division_ID = customers.Division_ID "+
                            "INNER JOIN countries ON countries.Country_ID =  `first_level_divisions`.`Country_ID` "
            );
            customersTable.getItems().clear();
            while (results.next()) {
                customer c = new customer();
                c.setCustomerID(results.getInt("Customer_ID"));
                c.setCustomerName(results.getString("Customer_Name"));
                c.setCustomerAddress(results.getString("Address"));
                c.setCustomerPhone(results.getString("Phone"));
                c.setCustomerPostalCode(results.getString("Postal_Code"));
                c.setCustomerDivision(results.getString("Division"));
                c.setCustomerCountry(results.getString("Country"));
                customerList.addAll(c);
            }
            customersTable.setItems(customerList);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void selectCountry(ActionEvent e) {
        //save country
        selectedCountry=customerCountry.getValue().toString();
        //use country id to get divisions
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            String sql = "SELECT `first_level_divisions`.*, countries.Country FROM  `first_level_divisions` " +
                         "INNER JOIN countries " +
                         "ON countries.Country_ID = `first_level_divisions`.Country_ID " +
                         "WHERE countries.Country='"+selectedCountry+"'";
            ResultSet results = connectDB.createStatement().executeQuery(sql);
            while (results.next()){
                divisionList.add(results.getString("Division"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        customerDivision.setEditable(true);
        customerDivision.setItems(divisionList);
    }

    public void selectDivision(ActionEvent e) {
        if (customerDivision.getValue()==null) {
            selectedDivision="";
        } else {
            selectedDivision=customerDivision.getValue().toString();
        }
    }

    /**
     * deletes a customer if no appointments reference said customer
     */
    public void deleteCustomer (ActionEvent e) {
        //get selected customer id
        customer c = customersTable.getSelectionModel().getSelectedItem();
        selectedCustomerID = c.getCustomerID();
        Integer apptCount = 0;
        //get all appointments from selected customer
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            ResultSet result = connectDB.createStatement().executeQuery("SELECT COUNT(Appointment_ID) FROM appointments WHERE Customer_ID="+selectedCustomerID);
            result.next();
            apptCount = result.getInt(1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //if none, delete
        if(apptCount==0) {
            // delete
            //alert to check
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Customer #" + selectedCustomerID + "?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                try {
                    databaseConnect connectNow = new databaseConnect();
                    Connection connectDB = connectNow.getConnection();
                    String sql = "DELETE FROM customers WHERE Customer_ID=" + selectedCustomerID;
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(sql);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                populateTable();
            }
            //or show error about existing apointments
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Customer #" + selectedCustomerID + " has "+apptCount+"or more appointments which must be removed before this record can be deleted", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //GENERAL FUNCTIONS---------------------------------------------------------
    /**
     * closes application
     */
    public void closeButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    /**
     * called by button action, returns to home.fxml
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
}
