/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author traye
 */
public class Screen_modPartController implements Initializable {

    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInv;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMin;
    @FXML
    private TextField partMax;
    @FXML
    private Label compLabel;
    @FXML
    private TextField companyName;
    @FXML
    private Label machLabel;
    @FXML
    private TextField machineId;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    @FXML
    private ToggleGroup partType;
    /** -------------------------------- **/
    public Inventory inventory = new Inventory();
    private int partIdToMod = 0;
        
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // FIX -------------------------------------------------------------------------------------------------------------------------------
    }    
    
    public void passInventoryAndId(Inventory inventory, int partIdToMod) {
        this.inventory = inventory;
        /** check type, init radio buttons and visibles **/
        Class tempClass = inventory.lookupPart(partIdToMod).getClass();
        String tempString = tempClass.toString();
        if (tempString.equals("class Model.InHouse")) {
            machLabel.setVisible(true);
            machineId.setVisible(true);
            compLabel.setVisible(false);
            companyName.setVisible(false);
            InHouse part = (InHouse) inventory.lookupPart(partIdToMod);
            machineId.setText(Integer.toString(part.getMachineId()));
            inHouseRadio.setSelected(true);
        } else if (tempString.equals("class Model.Outsourced")) {
            machLabel.setVisible(false);
            machineId.setVisible(false);
            compLabel.setVisible(true);
            companyName.setVisible(true);
            Outsourced part = (Outsourced) inventory.lookupPart(partIdToMod);
            companyName.setText(part.getCompanyName());
            outsourcedRadio.setSelected(true);
        }
        
        /** init textfields **/
        Part tempPart = inventory.lookupPart(partIdToMod);
        partId.setText(Integer.toString(partIdToMod) + " AUTO-GEN");
        partName.setText(tempPart.getName());
        partInv.setText(Integer.toString(tempPart.getStock()));
        partPrice.setText(Double.toString(tempPart.getPrice()));
        partMin.setText(Integer.toString(tempPart.getMin()));
        partMax.setText(Integer.toString(tempPart.getMax()));
    }

    @FXML
    private void saveMod(ActionEvent event) {
        /** init vars for new part **/
        int id = Integer.parseInt(partId.getText().replace(" AUTO-GEN", ""));
        String name = partName.getText();
        double price = (double) Double.parseDouble(partPrice.getText());
        int stock = Integer.parseInt(partInv.getText());  
        int max = Integer.parseInt(partMax.getText());
        int min = Integer.parseInt(partMin.getText());
        
        /**ALERT CHECK START**/
        /**if min is greater than max/max < min **/
        if (min>max) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Proceed");
            alert.setHeaderText("Min/Max Order Mismatch");
            alert.setContentText("Min must be smaller than Max in order to proceed");
            alert.showAndWait();
        /**if stock is not between min and max**/
        } else if ((stock<=max && stock>=min) == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Proceed");
            alert.setHeaderText("Inventory out of bounds!");
            alert.setContentText("Inventory must lie between Min and Max in order to proceed");
            alert.showAndWait();
        /**ALERT CHECK END**/
        } else {
            /** add inhouse or outsourced **/
            if (inHouseRadio.isSelected()) {
                int machId = Integer.parseInt(machineId.getText());
                Part part = new InHouse(id, name, price, stock, min, max, machId);
                int index = (int)(id - 1);
                inventory.updatePart(index, part);
            } else if (outsourcedRadio.isSelected()){
                String compName = (String) companyName.getText();
                Part part = new Outsourced(id, name, price, stock, min, max, compName);
                int index = (int)(id - 1);
                inventory.updatePart(index, part);
            }

            /** Open main screen **/
            try {
                Stage stage;
                Parent root;
                stage = (Stage) partId.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_main.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                Screen_mainController controller = loader.getController();
                if (inventory.allParts.size() > 0 || inventory.allProducts.size() > 0) {
                    controller.passInventory(inventory);
                }
            } catch (IOException ex) {
                Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }

    @FXML
    private void cancelMod(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Decision");
        String text = "Are you sure you want to Cancel?";
        alert.setHeaderText(text);
        alert.setContentText("You will lose all current inputs");
        ButtonType buttonCont = new ButtonType("Yes");
        ButtonType buttonCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonCont, buttonCancel);
            
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonCont){
            // ... user chose YES
            try {
                Stage stage;
                Parent root;
                stage = (Stage) outsourcedRadio.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_main.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                Screen_mainController controller = loader.getController();
                controller.passInventory(inventory);
            } catch (IOException ex) {
                Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
    
    @FXML
    private void inHouse(ActionEvent event) {
        machLabel.setVisible(true);
        machineId.setVisible(true);
        compLabel.setVisible(false);
        companyName.setVisible(false);
    }

    @FXML
    private void outsourced(ActionEvent event) {
        machLabel.setVisible(false);
        machineId.setVisible(false);
        compLabel.setVisible(true);
        companyName.setVisible(true);
    }
}
