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
public class Product {
    protected ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    protected SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    protected SimpleStringProperty name = new SimpleStringProperty("");
    protected SimpleDoubleProperty price = new SimpleDoubleProperty(0.0);
    protected SimpleIntegerProperty stock =  new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty min =  new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty max =  new SimpleIntegerProperty(0);
            
    /**
     * Default constructor.
     */
    public Product() {
        this(0, "", 0.0, 0, 0, 0);
    }
    
     /**
     * Constructor
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, Double price, int stock, int min, int max) {
        this.id.set(id);
        this.name.set(name);
        this.price.set(price);
        this.stock.set(stock);
        this.min.set(min);
        this.max.set(max);
    }
    
    /** SET v----------------------------------------------------------------**/
    public void setId(int id) {
        this.id.set(id);
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public void setPrice(Double price) {
        this.price.set(price);
    }
    
    public void setStock(int stock) {
        this.stock.set(stock);
    }
    
    public void setMin(int min) {
        this.min.set(min);
    }
    
    public void setMax(int max) {
        this.max.set(max);
    }
    
    /** GET v----------------------------------------------------------------**/
    public int getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    public Double getPrice() {
        return price.get();
    }
    
    public int getStock() {
        return stock.get();
    }
    
    public int getMin() {
        return min.get();
    }
    
    public int getMax() {
        return max.get();
    }
    /** MISC v---------------------------------------------------------------**/
   
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    public boolean deleteAssociatedPart(Part selectedAspart) {
        return associatedParts.remove(selectedAspart);
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
