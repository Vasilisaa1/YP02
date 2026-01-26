package com.example.pr1.Topic;



import java.util.ArrayList;

public class TopicContext {
    public static ArrayList<TopicModel> All() {
        ArrayList<TopicModel> items = new ArrayList<>();
        items.add(new TopicModel(1, "Алгоритмы", "Изучение алгоритмов", "Подробное описание алгоритмов...", 1));

        return items;
    }
}
