package com.bhoomik.Vardaan.model;

/**
 * Created by Shreyash on 26-03-2018.
 */

public class PersonDetails {
    public String name;
    public String branch;
    public String year;
    public String room;
    public String hostel;
    public String mobile;
    public String du;
    public String email;
    public String registrationDate;
    public String password;
    public String isactive;
    public PersonDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public PersonDetails( String name, String mobile, String isactive ){
        //this.registrationDate = registrationDate;
        this.name = name;
        this.mobile = mobile;
        /*this.year = year;
        this.room = room;
        this.hostel = hostel;
        this.mobile = mobile;
        this.email = email;
        this.password= password;
        this.du = du;*/
        this.isactive= isactive;
    }
}


