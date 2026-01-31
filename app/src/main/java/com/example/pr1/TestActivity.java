package com.example.pr1;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.Test.QuizAdapter;
import com.example.pr1.Test.QuizModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuiz;
    private QuizAdapter quizAdapter;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvProgress, tvQuestionCount, testTitle;
    private MaterialButton btnBack, btnCheck;
    private int topicId;
    private int currentUserId;
    private ImageView[] triangles;
    private boolean animationsStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        topicId = getIntent().getIntExtra("TOPIC_ID", -1);
        currentUserId = getIntent().getIntExtra("USER_ID", 0);

        if (topicId == -1) {
            Toast.makeText(this, "Ошибка: не передан ID темы", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        initializeTriangleAnimations();
        loadQuizData();
    }
    private void initializeTriangleAnimations() {
        triangles = new ImageView[]{
                findViewById(R.id.imageView),
                findViewById(R.id.imageView4),
                findViewById(R.id.imageView5)
        };
        for (ImageView triangle : triangles) {
            if (triangle == null) {
                return;
            }
        }

        startTriangleAnimations();
    }

    private void startTriangleAnimations() {
        if (animationsStarted) return;

        animateTriangle(triangles[0], 1000, 2000, 30f, 40f);
        animateTriangle(triangles[1], 1500, 2500, -25f, 35f);
        animateTriangle(triangles[2], 1200, 2200, 35f, -30f);

        animationsStarted = true;
    }

    private void animateTriangle(View triangle, long duration, long startDelay, float xRange, float yRange) {
        if (triangle == null) return;
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(triangle, "translationY",
                -yRange, yRange, -yRange);
        yAnimator.setDuration(duration);
        yAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        yAnimator.setStartDelay(startDelay);
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(triangle, "translationX",
                -xRange, xRange, -xRange);
        xAnimator.setDuration(duration + 500);
        xAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        xAnimator.setStartDelay(startDelay + 200);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(triangle, "alpha",
                0.3f, 0.7f, 0.3f);
        alphaAnimator.setDuration(duration + 1000);
        alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        alphaAnimator.setStartDelay(startDelay);

        yAnimator.start();
        xAnimator.start();
        alphaAnimator.start();
    }

    private void stopTriangleAnimations() {
        if (triangles == null) return;

        for (ImageView triangle : triangles) {
            if (triangle != null) {
                triangle.clearAnimation();
                triangle.animate().cancel();
            }
        }
        animationsStarted = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationsStarted) {
            startTriangleAnimations();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTriangleAnimations();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTriangleAnimations();
    }

    private void initializeViews() {
        recyclerViewQuiz = findViewById(R.id.recyclerViewQuiz);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        testTitle = findViewById(R.id.testTitle);
        btnBack = findViewById(R.id.btnBack);
        btnCheck = findViewById(R.id.btnCheck);

        recyclerViewQuiz.setLayoutManager(new LinearLayoutManager(this));

        quizAdapter = new QuizAdapter(this, quizList);
        recyclerViewQuiz.setAdapter(quizAdapter);
        btnBack.setOnClickListener(v -> {
            btnBack.setBackgroundColor(getResources().getColor(R.color.cyan));

            finish();
            v.postDelayed(() -> {
                btnBack.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }, 300);
        });
        btnCheck.setOnClickListener(v -> {
            btnCheck.setBackgroundColor(getResources().getColor(R.color.cyan));

            submitTest();
            v.postDelayed(() -> {
                btnCheck.setBackgroundColor(getResources().getColor(R.color.purple_500));
            }, 300);
        });

        testTitle.setText("Тест по теме #" + topicId);
        btnCheck.setEnabled(false);
    }

    private void loadQuizData() {
        GetQuizTask getQuizTask = new GetQuizTask();
        getQuizTask.execute(topicId);
    }

    class GetQuizTask extends AsyncTask<Integer, Void, ArrayList<QuizModel>> {
        Connection.Response response;

        @Override
        protected ArrayList<QuizModel> doInBackground(Integer... params) {
            int topicId = params[0];
            ArrayList<QuizModel> quizzes = new ArrayList<>();

            try {
                response = Jsoup.connect("http://10.0.2.2:5184/api/Quiz/ByTopic?topicId=" + topicId)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONArray jsonArray = new JSONArray(jsonResponse);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject quizJson = jsonArray.getJSONObject(i);
                        System.out.println("Quiz JSON: " + quizJson.toString());

                        QuizModel quiz = new QuizModel(
                                quizJson.getInt("id"),
                                quizJson.getInt("topic_id"),
                                quizJson.getString("question_text"),
                                quizJson.getString("options"),
                                quizJson.getString("correct_answer")
                        );
                        quizzes.add(quiz);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                System.out.println("Error parsing quiz: " + e.getMessage());
            }
            return quizzes;
        }

        @Override
        protected void onPostExecute(ArrayList<QuizModel> result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                quizList.clear();
                quizList.addAll(result);
                quizAdapter.notifyDataSetChanged();

                updateQuizUI(result.size());
                btnCheck.setEnabled(true);

                Toast.makeText(TestActivity.this,
                        "Загружено вопросов: " + result.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestActivity.this,
                        "Тест для этой темы не найден", Toast.LENGTH_SHORT).show();
                btnCheck.setEnabled(false);
            }
        }
    }

    private void updateQuizUI(int questionCount) {
        tvQuestionCount.setText("Вопросов: " + questionCount);
        progressBar.setMax(questionCount);
        tvProgress.setText("0/" + questionCount);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, questionCount);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
    }

    private void submitTest() {
        if (quizList.isEmpty()) {
            Toast.makeText(this, "Нет вопросов для проверки", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("=== DEBUG: Checking user answers ===");
        for (int i = 0; i < quizList.size(); i++) {
            QuizModel quiz = quizList.get(i);
            System.out.println("Question " + (i + 1) + " (ID: " + quiz.id + "): " + quiz.userAnswer);
        }
        System.out.println("=== END DEBUG ===");

        int score = quizAdapter.calculateScore();
        int totalQuestions = quizList.size();
        SaveResultTask saveResultTask = new SaveResultTask();
        saveResultTask.execute(score, totalQuestions);
        showTestResultAndTriggerAchievements(score, totalQuestions);
    }

    private void showTestResultAndTriggerAchievements(int score, int totalQuestions) {
        double percentage = (double) score / totalQuestions * 100;
        String message = String.format("Результат: %d/%d (%.1f%%)", score, totalQuestions, percentage);
        int statusColor;
        String statusText;
        if (score >= totalQuestions * 0.7) {
            statusColor = getResources().getColor(R.color.green);
            statusText = "Отлично!";
        } else if (score >= totalQuestions * 0.5) {
            statusColor = getResources().getColor(R.color.cyan);
            statusText = "Удовлетворительно";
        } else {
            statusColor = getResources().getColor(R.color.red);
            statusText = "Неудовлетворительно";
        }

        StringBuilder details = new StringBuilder();
        details.append(message).append("\n");
        details.append("Статус: ").append(statusText).append("\n\n");

        Map<Integer, String> userAnswers = quizAdapter.getUserAnswers();

        for (int i = 0; i < quizList.size(); i++) {
            QuizModel quiz = quizList.get(i);
            String userAnswer = userAnswers.get(quiz.id);
            boolean isCorrect = userAnswer != null && userAnswer.equals(quiz.correct_answer);
            String status = isCorrect ? "✓ Верно" : "✗ Неверно";

            details.append("Вопрос ").append(i + 1).append(": ").append(status).append("\n");
            if (!isCorrect) {
                details.append("Ваш ответ: ").append(userAnswer != null ? userAnswer : "Нет ответа").append("\n");
                details.append("Правильный ответ: ").append(quiz.correct_answer).append("\n");
            }
            details.append("\n");
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Результат теста")
                .setMessage(details.toString())
                .setPositiveButton("OK", (dialog, which) -> {
                    saveCheckAchievementsFlag();
                    Intent intent = new Intent(TestActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("CHECK_ACHIEVEMENTS", true);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void triggerAchievementsCheck() {
        saveCheckAchievementsFlag();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("CHECK_ACHIEVEMENTS", true);
        startActivity(intent);
    }

    private void showTestResult(int score, int totalQuestions) {
        double percentage = (double) score / totalQuestions * 100;
        String message = String.format("Результат: %d/%d (%.1f%%)", score, totalQuestions, percentage);
        int statusColor;
        String statusText;
        if (score >= totalQuestions * 0.7) {
            statusColor = getResources().getColor(R.color.green);
            statusText = "Отлично!";
        } else if (score >= totalQuestions * 0.5) {
            statusColor = getResources().getColor(R.color.cyan);
            statusText = "Удовлетворительно";
        } else {
            statusColor = getResources().getColor(R.color.red);
            statusText = "Неудовлетворительно";
        }
        StringBuilder details = new StringBuilder();
        details.append(message).append("\n");
        details.append("Статус: ").append(statusText).append("\n\n");

        Map<Integer, String> userAnswers = quizAdapter.getUserAnswers();

        for (int i = 0; i < quizList.size(); i++) {
            QuizModel quiz = quizList.get(i);
            String userAnswer = userAnswers.get(quiz.id);
            boolean isCorrect = userAnswer != null && userAnswer.equals(quiz.correct_answer);
            String status = isCorrect ? "✓ Верно" : "✗ Неверно";

            details.append("Вопрос ").append(i + 1).append(": ").append(status).append("\n");
            if (!isCorrect) {
                details.append("Ваш ответ: ").append(userAnswer != null ? userAnswer : "Нет ответа").append("\n");
                details.append("Правильный ответ: ").append(quiz.correct_answer).append("\n");
            }
            details.append("\n");
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Результат теста")
                .setMessage(details.toString())
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(TestActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();


    }
    private void saveCheckAchievementsFlag() {
        SharedPreferences prefs = getSharedPreferences("TestPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("check_achievements_after_test", true);
        editor.putLong("last_test_time", System.currentTimeMillis());
        editor.apply();
        Log.d("TestActivity", "Saved check_achievements_after_test flag");
    }
    class SaveResultTask extends AsyncTask<Integer, Void, Boolean> {
        Connection.Response response;
        String errorMessage = "";
        int totalQuestions;

        @Override
        protected Boolean doInBackground(Integer... params) {
            int score = params[0];
            totalQuestions = params[1];

            try {
                System.out.println("🔧 Saving result - User: " + currentUserId + ", Topic: " + topicId +
                        ", Score: " + score + ", Total Questions: " + totalQuestions);

                response = Jsoup.connect("http://10.0.2.2:5184/api/UserProgress/Add")
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .timeout(30000)
                        .data("user_id", String.valueOf(currentUserId))
                        .data("topic_id", String.valueOf(topicId))
                        .data("score", String.valueOf(score))
                        .data("total_questions", String.valueOf(totalQuestions)) // ДОБАВЬТЕ ЭТУ СТРОКУ
                        .data("is_completed", "true")
                        .execute();

                System.out.println("✅ Response status: " + response.statusCode());
                System.out.println("✅ Response body: " + response.body());

                return response.statusCode() == 200;

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                System.out.println("❌ Error: " + errorMessage);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(TestActivity.this, "Результат сохранен", Toast.LENGTH_SHORT).show();
                saveQuestionCountLocally(topicId, totalQuestions);


            } else {
                String message = "Ошибка сохранения результата";
                if (response != null) {
                    message += ". Статус: " + response.statusCode();
                    try {
                        message += ". Ответ: " + response.body();
                    } catch (Exception e) {
                        message += ". Не удалось прочитать ответ";
                    }
                }
                if (!errorMessage.isEmpty()) {
                    message += ". Ошибка: " + errorMessage;
                }
                Toast.makeText(TestActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }


        private void saveQuestionCountLocally(int topicId, int questionCount) {
            android.content.SharedPreferences prefs = getSharedPreferences("QuestionCounts", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("topic_" + topicId, questionCount);
            editor.apply();

            System.out.println("💾 Saved locally: Topic " + topicId + " has " + questionCount + " questions");
        }
    }
}