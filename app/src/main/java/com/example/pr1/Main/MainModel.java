package com.example.pr1.Main;


public class MainModel {
    public int id;
    public String title;
    public String short_description;
    public String full_description;
    public int order_index;

    public MainModel(int id, String title, String short_description, String full_description, int order_index) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.full_description = full_description;
        this.order_index = order_index;
    }
}