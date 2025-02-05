package com.example.dss;

import java.sql.Timestamp;

public class Statusmodel {
    private String reason;
    private String fromd ;
    private String todate ;

    private Timestamp timestamp ;
    private int status;
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public int getStatus(){return status;}
    public void setStatus(int status){this.status=status;}

    public  void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp ;
    }
    public Timestamp getTimestamp() {
        return timestamp ;
    }

    public void setfromdate(String fromd) {
        this.fromd = fromd ;
    }

    public String getfromdate() {
        return  fromd;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTodatedate() {
        return  todate;
    }


}
