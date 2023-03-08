package view_controller.scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * FXML Controller class for login.FXML
 *
 * @author Trayer L Harvey
 */
public class loginController {
    @FXML
    private Label loginAlert;
    @FXML
    private Button close;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submit;
    String User_Name;
    String Language_ID;
    @FXML
    private Label welcomeText;
    @FXML
    public Label locationId;
    @FXML
    public Label locationIdLabel;
    @FXML
    public Label titleLabel;

    //     1.2(of4): determines the user’s location (i.e., ZoneId) and displays it in a label on the log-in form
    /**
     * Initializes the  appointments controller class
     *
     */
    public void initialize() {
        try {
            Language_ID = Locale.getDefault().getLanguage().toString();
            switch (Language_ID) {
                case "en":
                    username.setPromptText("Username");
                    password.setPromptText("Password");
                    submit.setText("Login");
                    close.setText("Exit");
                    locationIdLabel.setText("Location ID: ");
                    locationId.setText(Locale.getDefault().getCountry());
                    titleLabel.setText("TLH Scheduler Login");
                    break;
                case "fr":
                    username.setPromptText("Nom d'utilisateur");
                    password.setPromptText("Le mot de passe");
                    submit.setText("Connexion");
                    close.setText("Sortie");
                    locationIdLabel.setText("ID d'emplacement: ");
                    locationId.setText(Locale.getDefault().getCountry());
                    titleLabel.setText("Planificateur d'TLH Login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes application
     */
    public void closeButtonOnAction(ActionEvent e) {

        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    //PART 1 - LOG IN--------------------------------------------------------------------------------------------------
    //     1.1(of4): accepts a user ID and password and provides an appropriate error message
    /**
     * gathers user-inputted data from login form for validation
     */
    public void submitButtonOnAction(ActionEvent e) {
        if(username.getText().isBlank() && password.getText().isBlank()) {
            switch (Language_ID) {
                case "en":
                    loginAlert.setText("Fields left blank");
                    break;
                case "fr":
                    loginAlert.setText("champs laissés vides");
                    break;
            }

        } else {
            validateLogin();
        }
    }

    /**
     * checks database for valid match and redirects to home.FXML after recording attempt
     */
    public void validateLogin() {
        try {
            databaseConnect connectNow = new databaseConnect();
            Connection connectDB = connectNow.getConnection();
            String sql = "SELECT COUNT(1)" +
                    "FROM users " +
                    "WHERE User_Name = '"+username.getText()+"'" +
                    " && Password = '"+password.getText()+"'";
            User_Name = username.getText();
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(sql);
            //create or read file for login tracking
            getFile();
            while(result.next()){
                Path of = Path.of("login_activity.txt");
                if (result.getInt(1)==1){
                    //login validated
                        //note it
                    try {
                        String note = "Login Attempt on "+LocalDateTime.now().toString()+" RESULT: SUCCESS\n";
                        Files.write(of, note.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                    }
                        //redirect
                    login();
                } else {
                    //login failed
                    switch (Language_ID) {
                        case "en":
                            loginAlert.setText("Invalid username or password");
                            break;
                        case "fr":
                            loginAlert.setText("Nom d'utilisateur ou mot de passe invalide");
                            break;
                    }
                        //note it
                    try {
                        String note = "Login Attempt on "+LocalDateTime.now().toString()+" RESULT: FAILURE\n";
                        Files.write(of, note.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * creates or finds the loging activity file for recording
     */
    public void getFile(){

        try {
            File myObj = new File("login_activity.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();}
        }

    /**
     * loads home screen
     */
    public void login() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            homeController homeController = loader.getController();
            homeController.setUser(User_Name);

            Stage stage = (Stage) submit.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}