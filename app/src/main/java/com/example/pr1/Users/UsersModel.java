package com.example.pr1.Users;

import android.graphics.drawable.Drawable;

import java.sql.Blob;
import java.time.LocalDateTime;

public class UsersModel {
    public int id;
    public String username;
    public String email;
    public String password;
    public  String ProfileIconFileName;


    public UsersModel(int id, String username, String email, String ProfileIconFileName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.ProfileIconFileName = ProfileIconFileName;
    }


    public void setId(int Id) {this.id = Id;}
    public void setEmail(String Email) {this.email = Email;}

    public void setPassword(String Password) {this.password = Password;}



    public int getId() {return this.id;}
    public String getEmail() {return  this.email;}

    public String getPassword() {return this.password;}


}