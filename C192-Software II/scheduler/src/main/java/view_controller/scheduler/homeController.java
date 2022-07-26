package view_controller.scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;

/**
 * FXML Controller class for home.FXML
 *
 * @author Trayer L Harvey
 */
public class homeController {
    @FXML
    private Button close;
    @FXML
    private Button customers;
    @FXML
    private Button appointments;
    @FXML
    private Button reporting;
    @FXML
    private Label username;
    String User_Name;
    Integer User_ID;
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

    /**
     * closes application
     */
    public void closeButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    /**
     * loads customers.FXML
     */
    public void loadCustomers(ActionEvent e) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customers.fxml"));
            Parent root = loader.load();

            customersController customersController = loader.getController();
            customersController.setUser(User_Name);
            Stage stage = (Stage) customers.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * loads appointments.fxml
     */
    public void loadAppointments(ActionEvent e) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointments.fxml"));
            Parent root = loader.load();

            appointmentsController appointmentsController = loader.getController();
            appointmentsController.setUser(User_Name);
            appointmentInFifteen();
            Stage stage = (Stage) appointments.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * loads reports.FXML
     */
    public void loadReporting(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reports.fxml"));
            Parent root = loader.load();

            reportsController reportsController = loader.getController();
            reportsController.setUser(User_Name);
            Stage stage = (Stage) reporting.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * checks to see if any appointments happen in the next 15 min and alerts on load to appoinments.FXML
     */
    public void appointmentInFifteen() {
        //get current timestamp + 15minutes
        LocalDateTime nowPlusFifteenLDT = LocalDateTime.now().plusMinutes(15);
        LocalDateTime nowLDT = LocalDateTime.now();

        //if start before curr+15, show alert
            //default message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "No immediate appointments",ButtonType.OK);
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();

            ResultSet results = connectDB.createStatement().executeQuery("SELECT * FROM appointments WHERE User_ID="+User_ID);
            while (results.next()) {
                //get timestamp from DB, convert to local
                //temp init assignment
                LocalDateTime startLDT=LocalDateTime.now();
                boolean anyUpcoming = false;
                if (results.getString("Start")!=null) {
                    //convert string to timestamp
                    startLDT = Timestamp.valueOf(results.getString("Start")).toLocalDateTime();
                    //timestamp to zoneddatetime
                    ZonedDateTime startZDT = ZonedDateTime.of(startLDT, ZoneId.of("UTC"));
                    //zdt from utc to local
                    OffsetDateTime startOffset = startZDT.toOffsetDateTime().withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
                    //offset to ZDT
                    startZDT = startOffset.atZoneSameInstant(ZoneId.systemDefault());
                    //ZDT to Local
                    startLDT = startZDT.toLocalDateTime();
                }
                //loop and compare to results
                if (nowLDT.isBefore(startLDT) && nowPlusFifteenLDT.isAfter(startLDT)) {
                    //if start before curr+15 & the start time hasn't passed now,
                    // update alert message to show appt id and date and time
                    alert.setContentText("Appointment#" + results.getInt("Appointment_ID")
                                       + " is coming up within 15 minutes! Start: "+startLDT);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //show alert
        alert.showAndWait();
    }

}
