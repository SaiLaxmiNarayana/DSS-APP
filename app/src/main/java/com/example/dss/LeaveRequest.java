package com.example.dss;

public class LeaveRequest {
    private int userId;
    private String startDate;
    private String endDate;
    private String status;

    public LeaveRequest(int userId, String startDate, String endDate, String status) {

        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }



    public int getUserId() {
        return userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}