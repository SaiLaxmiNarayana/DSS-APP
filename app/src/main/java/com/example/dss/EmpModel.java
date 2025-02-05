package com.example.dss;

// DataModel.java
public class EmpModel {
    private String project;
    private String physicalLocation;
    private String state;
    private String noOfHoursWorked;
    private String meetingId;
    private String type;
    private String calendar;
    private String inTime;
    private String outTime;
    private String userRole;

    // Constructor
    public EmpModel(String project, String physicalLocation, String state,
                     String noOfHoursWorked, String meetingId, String type,
                     String calendar, String inTime, String outTime,String userRole) {
        this.project = project;
        this.physicalLocation = physicalLocation;
        this.state = state;
        this.noOfHoursWorked = noOfHoursWorked;
        this.meetingId = meetingId;
        this.type = type;
        this.calendar = calendar;
        this.inTime = inTime;
        this.outTime = outTime;
        this.userRole=userRole;
    }

    // Getters
    public String getProject() {
        return project;
    }

    public String getPhysicalLocation() {
        return physicalLocation;
    }

    public String getState() {
        return state;
    }

    public String getNoOfHoursWorked() {
        return noOfHoursWorked;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getType() {
        return type;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }
    public String getUserRole() {
        return userRole;
    }
}
