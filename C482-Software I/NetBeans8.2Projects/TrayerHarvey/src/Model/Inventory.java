/* DONE
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author trayer
 */
public class Inventory {
    public static ObservableList<Partst> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    /** ADD v----------------------------------------------------------------**/
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }
   
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    /** LOOKUP v-------------------------------------------------------------**/
    public Part lookupPart(int partId) {
        for (Part part : allParts) {
                if (part.getId() == partId) {
                    return part;
                }
            }
        return null;
    }
    
    public Product lookupProduct(int productId) {
        for (Product product : allProducts) {
                if (product.getId() == productId) {
                    return product;
                }
            }
        return null;
    }
    
    public Part lookupPart(String partName) {
        for (Part part : allParts) {
                if (partName.equals(part.getName())) {
                    return part;
                }
            }
        return null;
    }
    
    public Product lookupProduct(String productName) {
        for (Product product : allProducts) {
                if (productName.equals(product.getName())) {
                    return product;
                }
            }
        return null;
    }
    
    /** UPDATE v-------------------------------------------------------------**/
    public void updatePart(int index, Part selectedPart) {
        /**allParts.get(index).setId(selectedPart.getId());**/
        allParts.set(index, selectedPart);
    }
    
    public void updateProduct(int index, Product newProduct) {
        Product updatedProduct = new Product();
        updatedProduct = newProduct;
        allProducts.get(index).setId(updatedProduct.getId());
        allProducts.get(index).setName(updatedProduct.getName());
        allProducts.get(index).setPrice(updatedProduct.getPrice());
        allProducts.get(index).setStock(updatedProduct.getStock());
        allProducts.get(index).setMin(updatedProduct.getMin());
        allProducts.get(index).setMax(updatedProduct.getMax());
    }
    
    /** DELETE v-------------------------------------------------------------**/
    public boolean deletePart(Part selectedPart) {
        Boolean wasDeleted = null;
            if (allParts.removeAll(selectedPart)) {
                wasDeleted = true;
            } else {
                wasDeleted = null;
            }
        return wasDeleted;
    }
    
     public boolean deleteProduct(Product selectedProduct) {
        Boolean wasDeleted = null;
            if (allProducts.removeAll(selectedProduct)) {
                wasDeleted = true;
            } else {
                wasDeleted = null;
            }
        return wasDeleted;
    }
     
    /** GET ALL v------------------------------------------------------------**/
    public ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
