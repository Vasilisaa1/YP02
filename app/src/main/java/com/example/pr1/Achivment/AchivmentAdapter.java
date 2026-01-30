package com.example.pr1.Achivment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1.R;

import java.util.ArrayList;

public class AchivmentAdapter extends RecyclerView.Adapter<AchivmentAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Achivment> Achivments;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName, tvDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_achivement);
            tvDate = itemView.findViewById(R.id.tv_date1);
        }
    }

    public AchivmentAdapter(Context context, ArrayList<Achivment> achivments){
        this.inflater = LayoutInflater.from(context);
        this.Achivments = achivments;
        this.context = context;
    }
    @NonNull
    @Override
    public AchivmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AchivmentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
