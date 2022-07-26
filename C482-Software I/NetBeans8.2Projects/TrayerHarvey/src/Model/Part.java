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

/**
 *
 * @author trayer
 */
public abstract class Part {
    protected SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    protected SimpleStringProperty name = new SimpleStringProperty("");
    protected SimpleDoubleProperty price = new SimpleDoubleProperty(0.0);
    protected SimpleIntegerProperty stock =  new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty min =  new SimpleIntegerProperty(0);
    protected SimpleIntegerProperty max =  new SimpleIntegerProperty(0);
    
    /**
     * Default constructor.
     */
    public Part() {
        this(0, "", 0.0, 0, 0, 0);
    }
    
     /**
     * Constructor
     * 
     * @param int id, String name, Double price, int stock, int min, int max
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
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
    
    public void setPrice(double price) {
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
    
    public double getPrice() {
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
}
