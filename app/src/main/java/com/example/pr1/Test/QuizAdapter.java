package com.example.pr1.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QuizModel> quizzes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvQuestion, tvQuestionNumber;
        public RadioGroup radioGroup;
        public RadioButton rbOption1, rbOption2, rbOption3, rbOption4, rbOption5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            radioGroup = itemView.findViewById(R.id.radio_group);
            rbOption1 = itemView.findViewById(R.id.rb_option1);
            rbOption2 = itemView.findViewById(R.id.rb_option2);
            rbOption3 = itemView.findViewById(R.id.rb_option3);
            rbOption4 = itemView.findViewById(R.id.rb_option4);
            rbOption5 = itemView.findViewById(R.id.rb_option5);
        }
    }

    public QuizAdapter(Context context, ArrayList<QuizModel> quizzes) {
        this.context = context;
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_test, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int currentPosition = position;
        QuizModel quiz = quizzes.get(currentPosition);

        holder.tvQuestion.setText(quiz.question_text);
        holder.tvQuestionNumber.setText("Вопрос " + (position + 1));

        // Сначала скрываем все варианты
        holder.rbOption1.setVisibility(View.GONE);
        holder.rbOption2.setVisibility(View.GONE);
        holder.rbOption3.setVisibility(View.GONE);
        holder.rbOption4.setVisibility(View.GONE);
        holder.rbOption5.setVisibility(View.GONE);

        try {
            JSONArray optionsArray = new JSONArray(quiz.options);

            // Показываем и заполняем только те варианты, которые есть в данных
            if (optionsArray.length() >= 1) {
                holder.rbOption1.setText(optionsArray.getString(0));
                holder.rbOption1.setVisibility(View.VISIBLE);
            }
            if (optionsArray.length() >= 2) {
                holder.rbOption2.setText(optionsArray.getString(1));
                holder.rbOption2.setVisibility(View.VISIBLE);
            }
            if (optionsArray.length() >= 3) {
                holder.rbOption3.setText(optionsArray.getString(2));
                holder.rbOption3.setVisibility(View.VISIBLE);
            }
            if (optionsArray.length() >= 4) {
                holder.rbOption4.setText(optionsArray.getString(3));
                holder.rbOption4.setVisibility(View.VISIBLE);
            }
            if (optionsArray.length() >= 5) {
                holder.rbOption5.setText(optionsArray.getString(4));
                holder.rbOption5.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Сбрасываем состояние
        holder.radioGroup.setOnCheckedChangeListener(null);
        holder.rbOption1.setOnClickListener(null);
        holder.rbOption2.setOnClickListener(null);
        holder.rbOption3.setOnClickListener(null);
        holder.rbOption4.setOnClickListener(null);
        holder.rbOption5.setOnClickListener(null);
        holder.radioGroup.clearCheck();

        // Восстанавливаем выбранный ответ
        if (quiz.userAnswer != null) {
            try {
                JSONArray optionsArray = new JSONArray(quiz.options);
                if (optionsArray.length() >= 1 && quiz.userAnswer.equals(optionsArray.getString(0))) {
                    holder.rbOption1.setChecked(true);
                } else if (optionsArray.length() >= 2 && quiz.userAnswer.equals(optionsArray.getString(1))) {
                    holder.rbOption2.setChecked(true);
                } else if (optionsArray.length() >= 3 && quiz.userAnswer.equals(optionsArray.getString(2))) {
                    holder.rbOption3.setChecked(true);
                } else if (optionsArray.length() >= 4 && quiz.userAnswer.equals(optionsArray.getString(3))) {
                    holder.rbOption4.setChecked(true);
                } else if (optionsArray.length() >= 5 && quiz.userAnswer.equals(optionsArray.getString(4))) {
                    holder.rbOption5.setChecked(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        View.OnClickListener radioClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizModel currentQuiz = quizzes.get(currentPosition);
                String selectedAnswer = "";

                try {
                    JSONArray optionsArray = new JSONArray(currentQuiz.options);

                    if (v.getId() == R.id.rb_option1) {
                        selectedAnswer = optionsArray.getString(0);
                        holder.radioGroup.check(R.id.rb_option1);
                    } else if (v.getId() == R.id.rb_option2) {
                        selectedAnswer = optionsArray.getString(1);
                        holder.radioGroup.check(R.id.rb_option2);
                    } else if (v.getId() == R.id.rb_option3) {
                        selectedAnswer = optionsArray.getString(2);
                        holder.radioGroup.check(R.id.rb_option3);
                    } else if (v.getId() == R.id.rb_option4) {
                        selectedAnswer = optionsArray.getString(3);
                        holder.radioGroup.check(R.id.rb_option4);
                    } else if (v.getId() == R.id.rb_option5) {
                        selectedAnswer = optionsArray.getString(4);
                        holder.radioGroup.check(R.id.rb_option5);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                currentQuiz.userAnswer = selectedAnswer;
                System.out.println("✅ SAVED! Question " + currentQuiz.id + " answer: '" + selectedAnswer + "'");
                System.out.println("📋 VERIFICATION - Q" + currentQuiz.id + " userAnswer: " + currentQuiz.userAnswer);
            }
        };

        // Устанавливаем обработчики для всех вариантов
        holder.rbOption1.setOnClickListener(radioClickListener);
        holder.rbOption2.setOnClickListener(radioClickListener);
        holder.rbOption3.setOnClickListener(radioClickListener);
        holder.rbOption4.setOnClickListener(radioClickListener);
        holder.rbOption5.setOnClickListener(radioClickListener);

        // Дублирующие слушатели для отладки (можно убрать если мешают)
        holder.rbOption1.setOnClickListener(v -> {
            System.out.println("🔘 DIRECT CLICK - Option 1 for question " + quiz.id);
            radioClickListener.onClick(v);
        });

        holder.rbOption2.setOnClickListener(v -> {
            System.out.println("🔘 DIRECT CLICK - Option 2 for question " + quiz.id);
            radioClickListener.onClick(v);
        });

        holder.rbOption3.setOnClickListener(v -> {
            System.out.println("🔘 DIRECT CLICK - Option 3 for question " + quiz.id);
            radioClickListener.onClick(v);
        });

        holder.rbOption4.setOnClickListener(v -> {
            System.out.println("🔘 DIRECT CLICK - Option 4 for question " + quiz.id);
            radioClickListener.onClick(v);
        });

        holder.rbOption5.setOnClickListener(v -> {
            System.out.println("🔘 DIRECT CLICK - Option 5 for question " + quiz.id);
            radioClickListener.onClick(v);
        });
    }
    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public Map<Integer, String> getUserAnswers() {
        Map<Integer, String> answers = new HashMap<>();
        for (QuizModel quiz : quizzes) {
            answers.put(quiz.id, quiz.userAnswer);
        }
        return answers;
    }

    public int calculateScore() {
        int score = 0;
        for (QuizModel quiz : quizzes) {
            String userAnswer = quiz.userAnswer;
            String correctAnswer = quiz.correct_answer;

            if (userAnswer != null && correctAnswer != null) {

                String normalizedUserAnswer = userAnswer.trim().toLowerCase();
                String normalizedCorrectAnswer = correctAnswer.trim().toLowerCase();

                if (normalizedUserAnswer.equals(normalizedCorrectAnswer)) {
                    score++;
                    System.out.println("Correct answer for question " + quiz.id);
                } else {
                    System.out.println("Wrong answer - User: '" + userAnswer + "', Correct: '" + correctAnswer + "'");
                }
            } else {
                System.out.println("Missing answer for question " + quiz.id + " - User: " + userAnswer + ", Correct: " + correctAnswer);
            }
        }
        System.out.println("Total score: " + score + "/" + quizzes.size());
        return score;
    }


}