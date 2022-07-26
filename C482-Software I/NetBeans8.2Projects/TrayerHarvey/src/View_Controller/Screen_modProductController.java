/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.*;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trayer
 */
public class Screen_modProductController implements Initializable {

    @FXML
    private TableView<Part> searchResultsTable;
    @FXML
    private TableView<Part> savedDataTable;
    @FXML
    private TextField productId;
    @FXML
    private TextField productName;
    @FXML
    private TextField productInv;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMin;
    @FXML
    private TextField productMax;
    @FXML
    private TextField searchProductField;
    @FXML
    private TableColumn<Part, Integer> partIdTop;
    @FXML
    private TableColumn<Part, String> partNameTop;
    @FXML
    private TableColumn<Part, Integer> partInvTop;
    @FXML
    private TableColumn<Part, Double> partPriceTop;
    @FXML
    private TableColumn<Part, Integer> partIdBottom;
    @FXML
    private TableColumn<Part, String> partNameBottom;
    @FXML
    private TableColumn<Part, Integer> partInvBottom;
    @FXML
    private TableColumn<Part, Double> partPriceBottom;
     /** -------------------------------- **/
    public Inventory inventory = new Inventory();
    private int productIdToMod = 0;
    private Product tempProduct = new Product();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIdTop.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTop.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvTop.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceTop.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // display Double in Currency format
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        partPriceTop.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }   
        });
        
        partIdBottom.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameBottom.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvBottom.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceBottom.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // display Double in Currency format
        partPriceBottom.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }   
        });
        
        searchResultsTable.setItems(inventory.getAllParts());
        savedDataTable.setItems(tempProduct.getAllAssociatedParts());
    }    

    @FXML
    private void searchParts(ActionEvent event) {
        /**get string/int to be searched**/
        String search = searchProductField.getText();
        if (Pattern.compile("[0-9]").matcher(search).find()) {
            /**if int**/
            int searchInt = Integer.parseInt(search);
            if (inventory.lookupPart(searchInt) != null) {
                /**System.out.printf("Part found-id#:%d", searchInt);**/
                /**add too assoc part**/
                Part tempPart = inventory.lookupPart(searchInt);
                tempProduct.addAssociatedPart(tempPart);
                savedDataTable.setItems(tempProduct.getAllAssociatedParts());
            } else {
                //System.out.printf("Part not found-id#:%d", search);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("search error");
                alert.setHeaderText("Part not found");
                alert.setContentText("Records have no match for that ID");
                ButtonType buttonOk = new ButtonType("OK");
                alert.getButtonTypes().setAll(buttonOk);
                alert.showAndWait();
            }
        } else if (Pattern.compile("[a-zA-z]").matcher(search).find()) {
            /**if string**/
            String searchString = search.replaceAll("[^A-Za-z]+", "");;
            if (inventory.lookupPart(searchString) != null) {
                /**System.out.printf("Part found-string:%s", searchString);**/
                /**open assoc part**/
                Part tempPart = inventory.lookupPart(inventory.lookupPart(searchString).getId());
                tempProduct.addAssociatedPart(tempPart);
                savedDataTable.setItems(tempProduct.getAllAssociatedParts());
            } else {
                //System.out.printf("Part not found-id#:%d", search);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("search error");
                alert.setHeaderText("Part not found");
                alert.setContentText("Records have no match for that Name");
                ButtonType buttonOk = new ButtonType("OK");
                alert.getButtonTypes().setAll(buttonOk);
                alert.showAndWait();
            }
        } else {
            //System.out.printf(" rules for part search broken by string:%s ", search);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("search error");
            alert.setHeaderText("Part not found");
            alert.setContentText("Please only search Name and ID Fields");
            ButtonType buttonOk = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonOk);
            alert.showAndWait();
        }
    }

    public void passInventoryAndId(Inventory inventory, int productIdToMod) {
        /**set product info**/
        this.inventory = inventory;
        Product product = inventory.lookupProduct(productIdToMod);
        productId.setText((Integer.toString(productIdToMod) + " AUTO-GEN"));
        productName.setText(product.getName());
        productInv.setText(Integer.toString(product.getStock()));
        productPrice.setText(Double.toString(product.getPrice()));
        productMin.setText(Integer.toString(product.getMin()));
        productMax.setText(Integer.toString(product.getMax()));
        
        /**set assoc. part info**/
        if(product.getAllAssociatedParts() != null) {
            savedDataTable.setItems(product.getAllAssociatedParts());
            tempProduct = product;
        }
    }
    
    @FXML
    private void cancelChanges(ActionEvent event) {
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
                stage = (Stage) productName.getScene().getWindow();
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
    private void saveChanges(ActionEvent event) {
        /** mod product **//**bad name, change? **/

        /** init vars for updated prod **/
        int id = Integer.parseInt(productId.getText().replace(" AUTO-GEN", ""));
        String name = productName.getText();
        int stock = 0;
        if (!productInv.getText().isEmpty()) {
            stock = Integer.parseInt(productInv.getText());
        }
        double price = 0.00;
        if (!productPrice.getText().isEmpty()) {
            price = (double) Double.parseDouble(productPrice.getText());
        }
        int max = Integer.parseInt(productMax.getText());
        int min = Integer.parseInt(productMin.getText());
        double partsSum = 0.00;
        for (Part part : tempProduct.getAllAssociatedParts()) {
            partsSum = partsSum + part.getPrice();
        }
        
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
        /**if product does not have at least one part**/
        } else if (tempProduct.getAllAssociatedParts().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Proceed");
            alert.setHeaderText("Product has no Parts!");
            alert.setContentText("Product must have at least one Associated Part in order to proceed");
            alert.showAndWait();
        /**if product cost is less than sum part cost**/
        } else if (price<partsSum) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Proceed");
            alert.setHeaderText("Parts cost exceeds Product price");
            alert.setContentText("Product must have a higher price than the sum of its Parts to proceed");
            alert.showAndWait();
        /**if name or price are missing**/
        } else if (name.isEmpty() || price == 0.00) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Proceed");
            alert.setHeaderText("Fields are empty");
            alert.setContentText("Name is a required field in order to proceed");
            alert.showAndWait();
        /**ALERT CHECK END**/
        } else {
            Product product = new Product(id, name, price, stock, min, max);

            for ( Part part : tempProduct.getAllAssociatedParts()) {
                product.addAssociatedPart(part);
            }

            /**add to curr inventory**/
            int index = (int)(id-1);
            inventory.updateProduct(index, product);

            /** Open main screen **/
            try {
                Stage stage;
                Parent root;
                stage = (Stage) productId.getScene().getWindow();
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
    private void deleteAsPart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Decision");
        String text = "Are you sure you want to remove this Part from Association?";
        alert.setHeaderText(text);
        ButtonType buttonCont = new ButtonType("Yes");
        ButtonType buttonCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonCont, buttonCancel);
            
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonCont){
            // ... user chose YES
            tempProduct.deleteAssociatedPart(savedDataTable.getSelectionModel().getSelectedItem());
            savedDataTable.setItems(tempProduct.getAllAssociatedParts());
        }
    }

    @FXML
    private void addAsPart(ActionEvent event) {
        if (searchResultsTable.getSelectionModel().getSelectedItem() != null) {
            Part tempPart = searchResultsTable.getSelectionModel().getSelectedItem();
            tempProduct.addAssociatedPart(tempPart);
            savedDataTable.setItems(tempProduct.getAllAssociatedParts());
        }
    }
    
}
