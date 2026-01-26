package com.example.pr1.Topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<TopicModel> Topic;
    private OnTestClickListener onTestClickListener;
    private OnBackClickListener onBackClickListener;
    private Context context;

    public interface OnTestClickListener {
        void onTestClick(int topicId);
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public void setOnTestClickListener(OnTestClickListener listener) {
        this.onTestClickListener = listener;
    }

    public void setOnBackClickListener(OnBackClickListener listener) {
        this.onBackClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvShortDesc, tvFullDesc;
        public MaterialButton btnTest, btnCollapse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.topicHeader);
            tvShortDesc = itemView.findViewById(R.id.topicSubtitle);
            tvFullDesc = itemView.findViewById(R.id.topicDescription);
            btnTest = itemView.findViewById(R.id.btnTest);
            btnCollapse = itemView.findViewById(R.id.btnCollapse);
        }
    }

    public TopicAdapter(Context context, ArrayList<TopicModel> topics){
        this.inflater = LayoutInflater.from(context);
        this.Topic = topics;
        this.context = context;
    }

    @NonNull
    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_topic, parent, false);
        return new TopicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.ViewHolder holder, int position) {
        TopicModel topic = Topic.get(position);
        holder.tvTitle.setText(topic.title);
        holder.tvShortDesc.setText(topic.short_description);
        holder.tvFullDesc.setText(topic.full_description);

        holder.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnTest.setBackgroundColor(context.getResources().getColor(R.color.cyan));

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && onTestClickListener != null) {
                    onTestClickListener.onTestClick(Topic.get(adapterPosition).id);
                }
                v.postDelayed(() -> {
                    holder.btnTest.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                }, 300);
            }
        });

        holder.btnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnCollapse.setBackgroundColor(context.getResources().getColor(R.color.cyan));

                if (onBackClickListener != null) {
                    onBackClickListener.onBackClick();
                }
                v.postDelayed(() -> {
                    holder.btnCollapse.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                }, 300);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Topic.size();
    }
}