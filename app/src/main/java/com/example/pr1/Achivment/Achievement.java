package com.example.pr1.Achivment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Achievement {
    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("achievement_type")
    public String achievementType;

    @SerializedName("unlocked_at")
    public String unlockedAt;

    public Achievement(int id, int userId, String achievementType, String unlockedAt) {
        this.id = id;
        this.userId = userId;
        this.achievementType = achievementType;
        this.unlockedAt = unlockedAt;
    }
}
