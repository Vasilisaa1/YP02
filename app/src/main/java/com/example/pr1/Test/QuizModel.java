package com.example.pr1.Test;

public class QuizModel {
    public int id;
    public int topic_id;
    public String question_text;
    public String options;
    public String correct_answer;
    public String userAnswer;

    public QuizModel(int id, int topic_id, String question_text, String options, String correct_answer) {
        this.id = id;
        this.topic_id = topic_id;
        this.question_text = question_text;
        this.options = options;
        this.correct_answer = correct_answer;
        this.userAnswer = null;
    }
}