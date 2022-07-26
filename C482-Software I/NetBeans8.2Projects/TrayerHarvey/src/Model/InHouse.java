/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author trayer
 */
public class InHouse extends Part{
    protected SimpleIntegerProperty machineId = new SimpleIntegerProperty(0);
    
    /**
     * Default constructor.
     */
    public InHouse() {
        this(0, "", (Double) 0.0, 0, 0, 0, 0);
    }
    
     /**
     * Constructor
     * 
     * @param int id, String name, Double price, int stock, int min, int max
     */
    public InHouse(int id, String name, Double price, int stock, int min, int max, int machineId) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setMachineId(machineId);
    }
    
    public void setMachineId(int machineId) {
        this.machineId.set(machineId);
    }
    
    public int getMachineId() {
        return machineId.get();
    }
}
