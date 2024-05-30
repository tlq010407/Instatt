package com.example.instatt.Class;
public class ClassItem {
    private String Date;
    private String EndTime;
    private String Room;
    private String StartTime;
    private String Status;
    private String Type;
    public ClassItem() {

    }

    public String getDate() {return Date;}

    public void setDate(String date) {
        Date = date;
    }
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {this.Type = Type;}

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
}
