package com.example.pr1.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;
import com.example.pr1.TopicActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MainModel> items;
    public Context context;
    private OnStudyClickListener onStudyClickListener;
    private OnTestClickListener onTestClickListener;

    public interface OnStudyClickListener {
        void onStudyClick(int topicId);
    }

    public interface OnTestClickListener {
        void onTestClick(int topicId);
    }

    public void setOnStudyClickListener(OnStudyClickListener listener) {
        this.onStudyClickListener = listener;
    }

    public void setOnTestClickListener(OnTestClickListener listener) {
        this.onTestClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, shortDesc;
        MaterialButton btnExpand, btnTest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            shortDesc = itemView.findViewById(R.id.description);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            btnTest = itemView.findViewById(R.id.btnTest);
        }
    }

    public MainAdapter(Context context, ArrayList<MainModel> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        MainModel item = items.get(position);
        holder.title.setText(item.title);
        holder.shortDesc.setText(item.short_description);

        holder.btnExpand.setOnClickListener(v -> {

            Intent intent = new Intent(context, TopicActivity.class);
            intent.putExtra("Topic", item.id);
            context.startActivity(intent);
        });
        holder.btnTest.setOnClickListener(v -> {
            if (onTestClickListener != null) {
                onTestClickListener.onTestClick(item.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}