package com.example.pr1.Topic;

public class TopicModel {
    public int id;
    public String title;
    public String short_description;
    public String full_description;
    public int order_index;

    public TopicModel(int id, String title, String short_description, String full_description, int order_index) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.full_description = full_description;
        this.order_index = order_index;
    }

}
