package model.scheduler;

import java.sql.Date;

/**
 * Class for entries from the customers database
 *
 * @author Trayer L Harvey
 */
public class customer {
    protected int customerID; //Auto incremented in database
    protected String customerName;
    protected String customerPhone;

    protected String customerAddress;
    protected String customerPostal;
    protected String customerCountry;
    protected String customerDivision;

    protected Date createDate;
    protected String createdBy;
    protected Date lastUpdate;
    protected String lastUpdateBy;

    //default constructor
    public customer() {this(0,"","","","","",new Date(0,0,0),"","",new Date(0,0,0));}

    //constructor
    public customer(int customerID, String customerName, String address,  String postalCode, String phone, String country, Date lastUpdate, String lastUpdateBy, String createdBy, Date createDate) {
        setCustomerID(customerID);
        setCustomerName(customerName);
        setCustomerPhone(phone);

        setCustomerAddress(address);
        setCustomerPostalCode(postalCode);
        setCustomerCountry(country);

        setCustomerCreateDate(createDate);
        setCustomerCreatedBy(createdBy);
        setCustomerLastUpdate(lastUpdate);
        setCustomerLastUpdateBy(lastUpdateBy);
    }

    //GETTERS--------------------------------------------------------------------------------------|
    public Integer getCustomerID() {return customerID;}
    public String getCustomerName() {return customerName;}
    public String getCustomerAddress() {
        return customerAddress;
    }
    public String getCustomerPostalCode() {return customerPostal;}
    public String getCustomerPhone() {
        return customerPhone;
    }
    public String getCustomerDivision() {return customerDivision;}
    public String getCustomerCountry() {return customerCountry;}
    public Date getCustomerLastUpdate() {return lastUpdate;}
    public String getCustomerLastUpdateBy() {return lastUpdateBy;}

    //SETTERS------------------------------------------------------------------------------------|
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public void setCustomerPhone(String phone) {this.customerPhone = phone;}

    public void setCustomerAddress(String address) {this.customerAddress = address;}
    public void setCustomerPostalCode(String postalCode) {this.customerPostal = postalCode;}
    public void setCustomerDivision(String division) {this.customerDivision = division;}
    public void setCustomerCountry(String country) {this.customerCountry = country;}

    private void setCustomerCreateDate(Date createDate) {this.createDate = createDate;}
    private void setCustomerCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setCustomerLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setCustomerLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
}
