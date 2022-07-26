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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
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
public class Screen_mainController implements Initializable {
    
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productInv;
    @FXML
    private TableColumn<Product, Double> productPrice;
    @FXML
    private TextField partSearchField;
    @FXML
    private TextField searchProductsField;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    
    public static Inventory inventory = new Inventory();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Init Parts Table
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // display Double in Currency format
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        partPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
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
        
        partsTable.setItems(inventory.allParts);
        
        /**init products table**/
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // display Double in Currency format
        productPrice.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        
        productsTable.setItems(inventory.allProducts);
    }    
    
    public void passInventory(Inventory inventory) {
        this.inventory = inventory;
        partsTable.setItems(inventory.allParts); 
    }
    
    @FXML
    private void addPart(ActionEvent event) throws IOException {
        /** Open add part **/
        try {
            Stage stage;
            Parent root;
            stage = (Stage) partsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getResource("screen_addPart.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Screen_addPartController controller = loader.getController(); 
            if (inventory.allParts.size() > 0 || inventory.allProducts.size() > 0) {
                controller.passInventory(inventory);
            }
        } catch (IOException ex) {
            Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @FXML
    private void modifyPart(ActionEvent event) throws IOException {
         /** get part to be modified **/
        Part tempPart = partsTable.getSelectionModel().getSelectedItem();
        int partIdToMod = tempPart.getId();
        
        /** Open mod part **/
        try {
            Stage stage;
            Parent root;
            stage = (Stage) partsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modPart.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Screen_modPartController controller = loader.getController(); 
            if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                controller.passInventoryAndId(inventory, partIdToMod);
            }
        } catch (IOException ex) {
            Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @FXML
    private void deletePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Decision");
        String text = "Are you sure you want to delete this Part?";
        alert.setHeaderText(text);
        ButtonType buttonCont = new ButtonType("Yes");
        ButtonType buttonCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonCont, buttonCancel);
            
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonCont){
            // ... user chose YES
            inventory.deletePart(partsTable.getSelectionModel().getSelectedItem());
            partsTable.setItems(inventory.getAllParts());
        }
    }
    
    @FXML
    private void searchParts(ActionEvent event) {
        /**get string/int to be searched**/
        String search = partSearchField.getText();
        if (Pattern.compile("[0-9]").matcher(search).find()) {
            /**if int**/
            int searchInt = Integer.parseInt(search);
            if (inventory.lookupPart(searchInt) != null) {
                /**System.out.printf("Part found-id#:%d", searchInt);**/
                /**open mod part**/
                try {
                    int partIdToMod = searchInt;
                    Stage stage;
                    Parent root;
                    stage = (Stage) partsTable.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modPart.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Screen_modPartController controller = loader.getController(); 
                    if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                        /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                        controller.passInventoryAndId(inventory, partIdToMod);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
                }      
            } else {
                //System.out.printf("Part (id) not found#:%s", search);
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
                /**open mod part**/
                try {
                    int partIdToMod = inventory.lookupPart(searchString).getId();
                    Stage stage;
                    Parent root;
                    stage = (Stage) partsTable.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modPart.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Screen_modPartController controller = loader.getController(); 
                    if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                        /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                        controller.passInventoryAndId(inventory, partIdToMod);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
                }      
            } else {
                //System.out.printf("Part (name) not found#:%d", search);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("search error");
                alert.setHeaderText("Part not found");
                alert.setContentText("Records have no match for that Part Name");
                ButtonType buttonOk = new ButtonType("OK");
                alert.getButtonTypes().setAll(buttonOk);
                alert.showAndWait();
            }
        } else {
            //System.out.printf("Part string error#:%d", search);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("search error");
            alert.setHeaderText("Part not found");
            alert.setContentText("please only search for Name or ID fields");
            ButtonType buttonOk = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonOk);
            alert.showAndWait();
        }
    }
    
    @FXML
    private void addProducts(ActionEvent event) {
        /** Open add product **/
        try {
            Stage stage;
            Parent root;
            stage = (Stage) productsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_addProduct.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Screen_addProductController controller = loader.getController(); 
            if (inventory.allParts.size() > 0 || inventory.allProducts.size() > 0) {
                controller.passInventory(inventory);
            }
        } catch (IOException ex) {
            Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @FXML
    private void modifyProducts(ActionEvent event) {
        /** go to ModifyProduct? **/
         /** get prod to be modified **/
        Product tempProduct = productsTable.getSelectionModel().getSelectedItem();
        int productIdToMod = tempProduct.getId();
        
        /** Open mod prod **/
        try {
            Stage stage;
            Parent root;
            stage = (Stage) productsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modProduct.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Screen_modProductController controller = loader.getController(); 
            if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                controller.passInventoryAndId(inventory, productIdToMod);
            }
        } catch (IOException ex) {
            Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @FXML
    private void deleteProducts(ActionEvent event) {
        Product tempProduct = productsTable.getSelectionModel().getSelectedItem();
        int asPartsSize = tempProduct.getAllAssociatedParts().size();
        /**ALERT CHECK START**/
        /**warning if product has any associated parts**/
        if (asPartsSize > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Proceed with Caution");
            String text = "Product has " + asPartsSize + " Associated Parts";
            alert.setHeaderText(text);
            alert.setContentText("Do you still wish to delete this product?");
            ButtonType buttonCont = new ButtonType("Yes");
            ButtonType buttonCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonCont, buttonCancel);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonCont){
                // ... user chose YES
                inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
                productsTable.setItems(inventory.getAllProducts());
            } 
        /**ALERT CHECK END**/
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Confirm Decision");
            String text = "Are you sure you want to delete this Product?";
            alert.setHeaderText(text);
            ButtonType buttonCont = new ButtonType("Yes");
            ButtonType buttonCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonCont, buttonCancel);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonCont){
                // ... user chose YES
                inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
                productsTable.setItems(inventory.getAllProducts());
            } 
        }
    }

    @FXML
    private void searchProducts(ActionEvent event) {
        /**get string/int to be searched**/
        String search = searchProductsField.getText();
        if (Pattern.compile("[0-9]").matcher(search).find()) {
            /**if int**/
            int searchInt = Integer.parseInt(search);
            if (inventory.lookupProduct(searchInt) != null) {
                /**System.out.printf("Product found-id#:%d", searchInt);**/
                /**open mod prod**/
                try {
                    int productIdToMod = searchInt;
                    Stage stage;
                    Parent root;
                    stage = (Stage) productsTable.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modProduct.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Screen_modProductController controller = loader.getController(); 
                    if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                        /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                        controller.passInventoryAndId(inventory, productIdToMod);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
                }      
            } else {
                //System.out.printf("Product (id) not found#:%s", search);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("search error");
                alert.setHeaderText("Product not found");
                alert.setContentText("Records have no match for that ID");
                ButtonType buttonOk = new ButtonType("OK");
                alert.getButtonTypes().setAll(buttonOk);
                alert.showAndWait();
            }
        } else if (Pattern.compile("[a-zA-z]").matcher(search).find()) {
            /**if string**/
            String searchString = search.replaceAll("[^A-Za-z]+", "");;
            if (inventory.lookupProduct(searchString) != null) {
                /**System.out.printf("Product found-string:%s", searchString);**/
                /**open mod part**/
                try {
                    int productIdToMod = inventory.lookupProduct(searchString).getId();
                    Stage stage;
                    Parent root;
                    stage = (Stage) productsTable.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("screen_modProduct.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Screen_modProductController controller = loader.getController(); 
                    if ((inventory.allParts.size() > 0) || (inventory.allProducts.size() > 0)) {
                        /**System.out.printf("Inventory part:%s", inventory.allParts.get((inventory.allParts.size()-1)).getName());**/
                        controller.passInventoryAndId(inventory, productIdToMod);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Screen_mainController.class.getName()).log(Level.SEVERE, null, ex);
                }      
            } else {
                //System.out.printf("Part (Name) not found#:%s", search);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("search error");
                alert.setHeaderText("Product not found");
                alert.setContentText("Records have no match for that Name");
                ButtonType buttonOk = new ButtonType("OK");
                alert.getButtonTypes().setAll(buttonOk);
                alert.showAndWait();
            }
        } else {
            //System.out.printf("Product string wrong format#:%s", search);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("search error");
            alert.setHeaderText("Product not found");
            alert.setContentText("Please only search for Name or ID fields");
            ButtonType buttonOk = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonOk);
            alert.showAndWait();
        }
    }
    
    
    @FXML
    private void ExitApp(ActionEvent event) {
        Stage stage = (Stage) partsTable.getScene().getWindow();
        stage.close();
    }
    
}
