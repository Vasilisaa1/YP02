package com.example.pr1;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.Main.MainAdapter;
import com.example.pr1.Main.MainModel;
import com.example.pr1.Result.ResultsAdapter;
import com.example.pr1.Result.ResultsModel;
import com.example.pr1.Users.UsersModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String Login, Password;
    public String Name;
    private RecyclerView recyclerView;
    private ArrayList<MainModel> topicList = new ArrayList<>();
    private MainAdapter adapter;

    private int currentUserId = 0;
    private String currentUserName = "";
    private String currenUserEmail = "";
    private String authToken = "";
    private RecyclerView resultsRecyclerView;
    private ResultsAdapter resultsAdapter;
    private ArrayList<ResultsModel> resultsList = new ArrayList<>();
    private int IdTopic;


    private ImageView[] triangles;
    private boolean animationsStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        setupPasswordToggle();
    }



    private void initializeTriangleAnimations() {
        triangles = new ImageView[]{
                findViewById(R.id.imageView2),
                findViewById(R.id.imageView3),
                findViewById(R.id.imageView5),
                findViewById(R.id.decorative_triangle_top)
        };

        startTriangleAnimations();
    }

    private void startTriangleAnimations() {
        if (animationsStarted) return;

        animateTriangle(triangles[0], 1000, 2000, 30f, 40f);
        animateTriangle(triangles[1], 1500, 2500, -25f, 35f);
        animateTriangle(triangles[2], 1200, 2200, 35f, -30f);
        animateTriangle(triangles[3], 800, 1800, -40f, 25f);

        animationsStarted = true;
    }

    private void animateTriangle(View triangle, long duration, long startDelay, float xRange, float yRange) {
        if (triangle == null) return;

        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(triangle, "translationY", -yRange, yRange, -yRange);
        yAnimator.setDuration(duration);
        yAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        yAnimator.setStartDelay(startDelay);

        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(triangle, "translationX", -xRange, xRange, -xRange);
        xAnimator.setDuration(duration + 500);
        xAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        xAnimator.setStartDelay(startDelay + 200);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(triangle, "alpha", 0.3f, 0.7f, 0.3f);
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



    private void setupPasswordToggle() {

        TextInputLayout passwordLayoutAuth = findViewById(R.id.textInputLayoutPasswordAuth);
        TextInputEditText passwordEditTextAuth = findViewById(R.id.editTextTextPassword);

        if (passwordLayoutAuth != null && passwordEditTextAuth != null) {
            passwordLayoutAuth.setEndIconOnClickListener(v -> togglePasswordVisibility(passwordEditTextAuth));
        }


        setupRegistrationPasswordToggles();
    }

    private void setupRegistrationPasswordToggles() {

        TextInputLayout passwordLayoutReg = findViewById(R.id.textInputLayoutPasswordReg);
        TextInputEditText passwordEditTextReg = findViewById(R.id.editTextPassword);

        if (passwordLayoutReg != null && passwordEditTextReg != null) {
            passwordLayoutReg.setEndIconOnClickListener(v -> togglePasswordVisibility(passwordEditTextReg));
        }

        TextInputLayout confirmPasswordLayout = findViewById(R.id.textInputLayoutConfirmPassword);
        TextInputEditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        if (confirmPasswordLayout != null && confirmPasswordEditText != null) {
            confirmPasswordLayout.setEndIconOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText));
        }
    }

    private void togglePasswordVisibility(TextInputEditText passwordEditText) {
        if (passwordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }



    private void initializeResults() {
        resultsRecyclerView = findViewById(R.id.listresult);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsAdapter = new ResultsAdapter(this, resultsList);
        resultsRecyclerView.setAdapter(resultsAdapter);
    }

    private void initializeTopics() {
        recyclerView = findViewById(R.id.listCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this, topicList);

        adapter.setOnStudyClickListener(topicId -> {
            IdTopic = topicId;

        });

        adapter.setOnTestClickListener(this::startTestActivity);
        recyclerView.setAdapter(adapter);
    }

    private void showTopicDetails(int topicId) {
        Toast.makeText(this, "Открываем тему: " + topicId, Toast.LENGTH_SHORT).show();
    }

    private void startTestActivity(int topicId) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        intent.putExtra("TOPIC_ID", topicId);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }



    public void OnResult(View view) {
        setContentView(R.layout.activity_results);
        stopTriangleAnimations();
        initializeResults();

        if (currentUserId != 0) {
            new GetUserResults().execute();
        } else {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogin(View view) {
        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText etPassword = findViewById(R.id.editTextTextPassword);

        Login = etEmail.getText().toString();
        Password = etPassword.getText().toString();

        if (Login.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        new GetUser().execute();
    }

    public void onRegistration(View view) {
        EditText tv_email = findViewById(R.id.editTextEmail);
        EditText tv_name = findViewById(R.id.editTextName);
        EditText tv_password = findViewById(R.id.editTextPassword);
        EditText tv_password2 = findViewById(R.id.editTextConfirmPassword);

        String email = tv_email.getText().toString();
        String name = tv_name.getText().toString();
        String password = tv_password.getText().toString();
        String confirmPassword = tv_password2.getText().toString();

        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        Login = email;
        Password = password;
        Name = name;

        new SetUser().execute();
    }

    public void ReigesterPage(View view) {
        setContentView(R.layout.registr);

    }
    public void goToLoginPage(View view) {
        setContentView(R.layout.auth);
        setupPasswordToggle();
    }
    public void OnProfil(View view) {
        setContentView(R.layout.profile_activity);
        stopTriangleAnimations();
        new LoadProfileData().execute();
    }

    public void Back(View view) {
        stopTriangleAnimations();
        finishAffinity();
    }

    public void OnMain(View view) {
        setContentView(R.layout.activity_main);
        stopTriangleAnimations();
        initializeTopics();
        new GetTopic().execute();
    }


    class GetUserResults extends AsyncTask<Void, Void, ArrayList<ResultsModel>> {
        Connection.Response response;

        @Override
        protected ArrayList<ResultsModel> doInBackground(Void... voids) {
            ArrayList<ResultsModel> results = new ArrayList<>();
            try {
                response = Jsoup.connect("http://10.0.2.2:5184/api/UserProgress/List")
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    Map<Integer, String> topicTitles = loadTopicTitles();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resultJson = jsonArray.getJSONObject(i);
                        int userId = resultJson.getInt("user_id");

                        if (userId == currentUserId) {
                            int topicId = resultJson.getInt("topic_id");
                            String topicTitle = topicTitles.getOrDefault(topicId, "Тема #" + topicId);

                            // Парсим total_questions, если есть, иначе используем 0
                            int totalQuestions = 0;
                            if (resultJson.has("total_questions")) {
                                totalQuestions = resultJson.getInt("total_questions");
                            }

                            ResultsModel result = new ResultsModel(
                                    resultJson.getInt("id"),
                                    userId,
                                    topicId,
                                    resultJson.getBoolean("is_completed"),
                                    resultJson.getInt("score"),
                                    resultJson.optString("completed_at", null),
                                    topicTitle,
                                    totalQuestions // передаем total_questions
                            );
                            results.add(result);

                            System.out.println("📋 Loaded result - Topic: " + topicId +
                                    ", Score: " + resultJson.getInt("score") +
                                    ", Total Questions: " + totalQuestions);
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return results;
        }


        private Map<Integer, String> loadTopicTitles() {
            Map<Integer, String> topics = new HashMap<>();
            try {
                Connection.Response topicResponse = Jsoup.connect("http://10.0.2.2:5184/api/Topics/List")
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (topicResponse.statusCode() == 200) {
                    String jsonResponse = topicResponse.body();
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject topic = jsonArray.getJSONObject(i);
                        topics.put(topic.getInt("id"), topic.getString("title"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return topics;
        }

        @Override
        protected void onPostExecute(ArrayList<ResultsModel> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                resultsList.clear();
                resultsList.addAll(result);
                resultsAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Загружено результатов: " + result.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Результаты не найдены", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SetUser extends AsyncTask<Void, Void, Void> {
        Connection.Response response;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection conn = Jsoup.connect("http://10.0.2.2:5184/api/Users/Register");
                Map<String, String> myMap = new HashMap<>();
                myMap.put("username", Name);
                myMap.put("email", Login);
                myMap.put("password", Password);
                conn.data(myMap);

                response = conn.ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .timeout(60 * 1000)
                        .execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (response == null) {
                Toast.makeText(MainActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                return;
            }

            if (response.statusCode() == 200) {
                runOnUiThread(() -> {
                    setContentView(R.layout.auth);
                    stopTriangleAnimations();


                    Toast.makeText(MainActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(MainActivity.this, "Ошибка: " + response.statusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetTopic extends AsyncTask<Void, Void, ArrayList<MainModel>> {
        Connection.Response response;

        @Override
        protected ArrayList<MainModel> doInBackground(Void... voids) {
            ArrayList<MainModel> topics = new ArrayList<>();
            try {
                response = Jsoup.connect("http://10.0.2.2:5184/api/Topics/List")
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject topic = jsonArray.getJSONObject(i);
                        MainModel mainModel = new MainModel(
                                topic.getInt("id"),
                                topic.getString("title"),
                                topic.getString("short_description"),
                                topic.getString("full_description"),
                                topic.getInt("order_index")
                        );
                        topics.add(mainModel);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return topics;
        }

        @Override
        protected void onPostExecute(ArrayList<MainModel> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                topicList.clear();
                topicList.addAll(result);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Темы загружены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Не удалось загрузить темы", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoadProfileData extends AsyncTask<Void, Void, UsersModel> {
        @Override
        protected UsersModel doInBackground(Void... voids) {
            try {
                Log.d("Profile", "Attempting to load profile with token: " + (authToken.isEmpty() ? "empty" : "present"));
                Connection.Response response = Jsoup.connect("http://10.0.2.2:5184/api/Users/GetCurrentUser")
                        .header("Authorization", "Bearer " + authToken)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                Log.d("Profile", "Response status: " + response.statusCode());

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    Log.d("Profile", "Response body: " + jsonResponse);
                    JSONObject userJson = new JSONObject(jsonResponse);

                    return new UsersModel(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email")
                    );
                } else {
                    Log.d("Profile", "Failed to get current user, status: " + response.statusCode());
                    if (currentUserId != 0) {
                        return getUserById(currentUserId);
                    }
                }
            } catch (IOException | JSONException e) {
                Log.e("Profile", "Error loading profile: " + e.getMessage());
                e.printStackTrace();
            }

            if (currentUserId != 0 && !currentUserName.isEmpty()) {
                Log.d("Profile", "Using cached user data");
                return new UsersModel(currentUserId, currentUserName, currenUserEmail);
            }

            return null;
        }

        private UsersModel getUserById(int userId) {
            try {
                Connection.Response response = Jsoup.connect("http://10.0.2.2:5184/api/Users/" + userId)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONObject userJson = new JSONObject(jsonResponse);
                    return new UsersModel(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email")
                    );
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UsersModel user) {
            super.onPostExecute(user);
            if (user != null) {
                updateProfileUI(user);
                Toast.makeText(MainActivity.this, "Данные профиля загружены", Toast.LENGTH_SHORT).show();
            } else {
                showDemoProfile();
                Toast.makeText(MainActivity.this, "Не удалось загрузить данные профиля", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetUser extends AsyncTask<Void, Void, Void> {
        Connection.Response response;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                response = Jsoup.connect("http://10.0.2.2:5184/api/Users/Login?email=" + Login + "&password=" + Password)
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    authToken = jsonObject.getString("token");

                    if (jsonObject.has("userId")) {
                        currentUserId = jsonObject.getInt("userId");
                    } else if (jsonObject.has("id")) {
                        currentUserId = jsonObject.getInt("id");
                    }

                    currentUserName = Login;
                    currenUserEmail = Login;
                    Log.d("GetUser", "User ID set to: " + currentUserId);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (response == null) {
                Toast.makeText(MainActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                return;
            } else if (response.statusCode() != 200) {
                Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                return;
            } else {
                runOnUiThread(() -> {
                    setContentView(R.layout.activity_main);
                    stopTriangleAnimations();
                    initializeTopics();
                    new GetTopic().execute();

                });
            }
        }
    }


    private void updateProfileUI(UsersModel user) {
        TextView profileName = findViewById(R.id.profile_name);
        TextView profileEmail = findViewById(R.id.profile_email);
        TextView profileId = findViewById(R.id.profile_id);

        if (profileName != null) profileName.setText(user.username);
        if (profileEmail != null) profileEmail.setText(user.email);
        if (profileId != null) profileId.setText("ID: " + user.id);

        currentUserId = user.id;
        currentUserName = user.username;
        currenUserEmail = user.email;
    }

    private void showDemoProfile() {
        TextView profileName = findViewById(R.id.profile_name);
        TextView profileEmail = findViewById(R.id.profile_email);

        if (profileName != null) profileName.setText("Демо пользователь");
        if (profileEmail != null) profileEmail.setText("demo@example.com");

        Toast.makeText(this, "Загружены демо-данные", Toast.LENGTH_SHORT).show();
    }
}