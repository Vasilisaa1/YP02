package com.example.pr1.Achivment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<Achievement> data;

    @SerializedName("count")
    public int count;

    @SerializedName("new_achievements")
    public List<Achievement> newAchievements; // Новое поле для свежих достижений

    public boolean hasNewAchievements() {
        return newAchievements != null && !newAchievements.isEmpty();
    }
}