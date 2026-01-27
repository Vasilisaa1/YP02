package com.example.pr1.Main;

import java.util.ArrayList;

public class MainContext {
    public static ArrayList<MainModel> All() {
        ArrayList<MainModel> items = new ArrayList<>();
        items.add(new MainModel(1, "Алгоритмы", "Изучение алгоритмов", "Подробное описание алгоритмов...", 1));
        return items;
    }
}