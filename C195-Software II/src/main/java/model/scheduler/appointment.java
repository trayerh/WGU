package model.scheduler;

import javafx.util.converter.LocalDateStringConverter;

import java.sql.Date;
import java.time.LocalDate;

/**
 * class for entries from the appointments database
 *
 * @author Trayer L Harvey
 */
public class appointment {
    protected Integer apptID;
    protected String apptTitle;
    protected String apptDesc;
    protected String apptLocation;
    protected String apptType;
    protected LocalDate apptDate;
    protected String apptStart;
    protected String apptEnd;
    protected String apptCreatedBy;
    protected LocalDate apptLastUpdate;
    protected String apptLastUpdateBy;
    protected Integer apptCustomerID;
    protected Integer apptUserID;
    protected Integer apptContactID;

    public appointment() {
        this(0,"","","","",LocalDate.of(1, 1, 1),"","","",LocalDate.of(1, 1, 1),"",0,0,0);
    }

    public appointment(int aid, String ati, String ad, String al, String aty, LocalDate ada, String as, String ae, String acb, LocalDate alu, String lub, int cuid, int auid, int acid){
        setApptID(aid);
        setApptTitle(ati);
        setApptDesc(ad);
        setApptLocation(al);
        setApptType(aty);
        setApptDate(ada);
        setApptStart(as);
        setApptEnd(ae);
        setApptCreatedBy(acb);
        setApptLastUpdate(alu);
        setApptLastUpdateBy(lub);
        setApptCustomerID(cuid);
        setApptUserID(auid);
        setApptContactID(acid);
    }

    //SETTERS
    public void setApptID(int aid) {this.apptID = aid;}
    public void setApptTitle(String at) {this.apptTitle = at;}
    public void setApptDesc(String ad) {this.apptDesc = ad;}
    public void setApptLocation(String al) {this.apptLocation = al;}
    public void setApptType(String at) {this.apptType = at;}
    public void setApptDate(LocalDate ad) {this.apptDate = ad;}
    public void setApptStart(String start) {this.apptStart = start;}
    public void setApptEnd(String end) {this.apptEnd = end;}
    public void setApptCreatedBy(String acb) {this.apptCreatedBy = acb;}
    public void setApptLastUpdate(LocalDate lu) {this.apptLastUpdate = lu;}
    public void setApptLastUpdateBy(String lub) {this.apptLastUpdateBy = lub;}
    public void setApptCustomerID(int cid) {this.apptCustomerID = cid;}
    public void setApptUserID(int uid) {this.apptUserID = uid;}
    public void setApptContactID(int cid) {this.apptContactID = cid;}

    //GETTERS
    public Integer getApptID() {return apptID;}
    public String getApptTitle() {return apptTitle;}
    public String getApptDesc() {return apptDesc;}
    public String getApptLocation() {return apptLocation;}
    public String getApptType() {return apptType;}
    public LocalDate getApptDate() {return apptDate;}
    public String getApptStart() {return apptStart;}
    public String getApptEnd() {return apptEnd;}
    public String getApptCreatedBy() {return apptCreatedBy;}
    public LocalDate getApptLastUpdate() {return apptLastUpdate;}
    public String getApptLastUpdateBy() {return apptLastUpdateBy;}
    public Integer getApptCustomerID() {return apptCustomerID;}
    public Integer getApptUserID() {return apptUserID;}
    public Integer getApptContactID() {return apptContactID;}
}
