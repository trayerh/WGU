/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author trayer
 */
public class Outsourced extends Part{
    protected SimpleStringProperty companyName = new SimpleStringProperty();
    
     /**
     * Default constructor.
     */
    public Outsourced() {
        this(0, "", (Double) 0.0, 0, 0, 0, null);
    }
    
    /**
     * Constructor
     * 
     * @param int id, String name, Double price, int stock, int min, int max
     */
    public Outsourced(int id, String name, Double price, int stock, int min, int max, String companyName) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setCompanyname(companyName);
    }
    
    public void setCompanyname(String companyName) {
        this.companyName.set(companyName);
    }
    
    public String getCompanyName() {
        return this.companyName.get();
    }
}
