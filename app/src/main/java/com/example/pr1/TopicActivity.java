package com.example.pr1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.Topic.TopicAdapter;
import com.example.pr1.Topic.TopicModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {
    private static final String TAG = "TopicActivity";

    RecyclerView recyclerView;
    TopicAdapter adapter;
    private ArrayList<TopicModel> topicList = new ArrayList<>();

    private int topicId;
    private String topicTitle;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);

        recyclerView = findViewById(R.id.recycleviewtopic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(this, topicList);
        adapter.setOnTestClickListener(new TopicAdapter.OnTestClickListener() {
            @Override
            public void onTestClick(int topicId) {
                startTestActivity(topicId);
            }
        });
        adapter.setOnBackClickListener(new TopicAdapter.OnBackClickListener() {
            @Override
            public void onBackClick() {
                finish();
            }
        });

        recyclerView.setAdapter(adapter);

        topicId = getIntent().getIntExtra("Topic", -1);

        if (topicId != -1) {
            GetTopic getTopic = new GetTopic();
            getTopic.execute(topicId);
        } else {
            Toast.makeText(this, "Ошибка: не передан ID темы", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startTestActivity(int topicId) {
        Intent intent = new Intent(TopicActivity.this, TestActivity.class);
        intent.putExtra("TOPIC_ID", topicId);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    class GetTopic extends AsyncTask<Integer, Void, TopicModel> {
        Connection.Response response;

        @Override
        protected TopicModel doInBackground(Integer... params) {
            int topicId = params[0];
            try {
                response = Jsoup.connect("http://10.0.2.2:5184/api/Topics/Item?id=" + topicId)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    JSONObject topic = new JSONObject(jsonResponse);

                    return new TopicModel(
                            topic.getInt("id"),
                            topic.getString("title"),
                            topic.getString("short_description"),
                            topic.getString("full_description"),
                            topic.getInt("order_index")
                    );
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TopicModel result) {
            super.onPostExecute(result);

            if (result != null) {
                topicList.clear();
                topicList.add(result);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(TopicActivity.this, "Не удалось загрузить тему", Toast.LENGTH_SHORT).show();
            }
        }
    }
}