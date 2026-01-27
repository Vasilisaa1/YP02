package com.example.pr1.Result;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ResultsModel> results;
    private Context context;
    private Map<Integer, Integer> topicQuestionCounts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTopic, tvScore, tvDate, statusText;
        public CardView statusCard;
        public CircularProgressIndicator progressCircle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopic = itemView.findViewById(R.id.tv_topic);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvDate = itemView.findViewById(R.id.tv_date);
            statusCard = itemView.findViewById(R.id.status_card);
            statusText = itemView.findViewById(R.id.status_text);
            progressCircle = itemView.findViewById(R.id.progress_circle);
        }
    }

    public ResultsAdapter(Context context, ArrayList<ResultsModel> results) {
        this.inflater = LayoutInflater.from(context);
        this.results = results;
        this.context = context;
        this.topicQuestionCounts = new HashMap<>();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultsModel result = results.get(position);

        holder.tvTopic.setText("Тема: " + result.topic_title);

        int totalQuestions = getTotalQuestionsForTopic(result.topic_id);

        holder.tvScore.setText(result.score + "/" + totalQuestions);

        if (result.completed_at != null && !result.completed_at.isEmpty()) {
            String date = result.completed_at.split("T")[0];
            String[] dateParts = date.split("-");
            String formattedDate = dateParts[2] + "." + dateParts[1] + "." + dateParts[0];
            holder.tvDate.setText("Дата: " + formattedDate);
        } else {
            holder.tvDate.setText("Дата: не завершено");
        }

        double percentage = calculatePercentage(result.score, totalQuestions);


        setStatus(holder, percentage);


        int progress = (int) percentage;
        animateProgress(holder.progressCircle, progress);
    }


    private int getTotalQuestionsForTopic(int topicId) {

        for (ResultsModel result : results) {
            if (result.topic_id == topicId && result.total_questions > 0) {
                System.out.println("📊 Using server data for topic " + topicId + ": " + result.total_questions + " questions");
                return result.total_questions;
            }
        }

        if (topicQuestionCounts.containsKey(topicId)) {
            int count = topicQuestionCounts.get(topicId);
            System.out.println("📚 Using local data for topic " + topicId + ": " + count + " questions");
            return count;
        } else {

            System.out.println("⚠️ Using default for topic " + topicId + ": 5 questions");
            return 5;
        }
    }

    private double calculatePercentage(int score, int totalQuestions) {
        if (totalQuestions > 0) {
            return ((double) score / totalQuestions) * 100;
        }
        return 0;
    }

    private void setStatus(ViewHolder holder, double percentage) {
        if (percentage >= 70) {
            holder.statusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green));
            holder.statusText.setText("Отлично");
        } else if (percentage >= 50) {
            holder.statusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cyan));
            holder.statusText.setText("Удовлетворительно");
        } else {
            holder.statusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
            holder.statusText.setText("Неудовлетворительно");
        }
    }

    private void animateProgress(CircularProgressIndicator progressIndicator, int progress) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressIndicator, "progress", 0, progress);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
    }

    @Override
    public int getItemCount() {
        return results.size();
    }



    public Map<Integer, Integer> getTopicQuestionCounts() {
        return new HashMap<>(topicQuestionCounts);
    }
}