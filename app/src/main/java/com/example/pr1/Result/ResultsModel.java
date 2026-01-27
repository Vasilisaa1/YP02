package com.example.pr1.Result;

public class ResultsModel {
    public int id;
    public int user_id;
    public int topic_id;
    public boolean is_completed;
    public int score;
    public String completed_at;
    public String topic_title;
    public int total_questions;

    public ResultsModel(int id, int user_id, int topic_id, boolean is_completed,
                        int score, String completed_at, String topic_title) {
        this.id = id;
        this.user_id = user_id;
        this.topic_id = topic_id;
        this.is_completed = is_completed;
        this.score = score;
        this.completed_at = completed_at;
        this.topic_title = topic_title;
        this.total_questions = 0;
    }

    public ResultsModel(int id, int user_id, int topic_id, boolean is_completed,
                        int score, String completed_at, String topic_title, int total_questions) {
        this.id = id;
        this.user_id = user_id;
        this.topic_id = topic_id;
        this.is_completed = is_completed;
        this.score = score;
        this.completed_at = completed_at;
        this.topic_title = topic_title;
        this.total_questions = total_questions;
    }
}