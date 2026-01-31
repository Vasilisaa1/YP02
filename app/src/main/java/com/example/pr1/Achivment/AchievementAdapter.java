package com.example.pr1.Achivment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Achievement> achievements;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_achivement);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }

    public AchievementAdapter(Context context, ArrayList<Achievement> achievements) {
        this.inflater = LayoutInflater.from(context);
        this.achievements = achievements;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_achivement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);

        // Устанавливаем название достижения
        holder.tvName.setText(achievement.achievementType);

        // Форматируем дату
        if (achievement.unlockedAt != null && !achievement.unlockedAt.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

                Date date = inputFormat.parse(achievement.unlockedAt);
                String formattedDate = outputFormat.format(date);
                holder.tvDate.setText("Получено: " + formattedDate);
            } catch (ParseException e) {
                holder.tvDate.setText("Получено: " + achievement.unlockedAt);
            }
        } else {
            holder.tvDate.setText("Дата не указана");
        }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public void updateData(ArrayList<Achievement> newAchievements) {
        this.achievements.clear();
        this.achievements.addAll(newAchievements);
        notifyDataSetChanged();
    }



}