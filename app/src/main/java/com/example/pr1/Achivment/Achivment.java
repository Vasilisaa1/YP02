package com.example.pr1.Achivment;

import java.util.Date;

public class Achivment {

    public int Id;
    public int UserId;
    public String achivement_type;
    public Date unlocked_at;

    public Achivment(int Id, int UserId, String achivement_type,Date unlocked_at ){
        this.Id = Id;
        this.UserId = UserId;
        this.achivement_type = achivement_type;
        this.unlocked_at = unlocked_at;

    }
}
