package com.example.vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String title;
    private String hotel;
    private String start_date;
    private String end_date;
    private Transportation transport;

    public Vacation(int vacationID, String title, String hotel, String start_date, String end_date, Transportation transport) {
        this.vacationID = vacationID;
        this.title = title;
        this.hotel = hotel;
        this.start_date = start_date;
        this.end_date = end_date;
        this.transport = transport;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Transportation getTransport() {
        return transport;
    }

    public void setTransport(Transportation transport) {
        this.transport = transport;
    }
}
