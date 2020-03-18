package com.example.blessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.LearningModel;

import java.util.ArrayList;

public class LearningAdapter extends RecyclerView.Adapter<LearningAdapter.ViewHolder> {

    private ArrayList<LearningModel> learningModelArrayList;
    private Context context;
    private CustomRecyclerViewListener customRecyclerViewListener;

    public LearningAdapter(ArrayList<LearningModel> learningModelArrayList, Context context, CustomRecyclerViewListener customRecyclerViewListener) {
        this.learningModelArrayList = learningModelArrayList;
        this.context = context;
        this.customRecyclerViewListener = customRecyclerViewListener;
    }

    @NonNull
    @Override
    public LearningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cv_list, parent, false);
        LearningAdapter.ViewHolder holder = new LearningAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LearningAdapter.ViewHolder holder, final int position) {
        holder.textview.setText(learningModelArrayList.get(position).getLearntext());
        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customRecyclerViewListener.onClickCustomItem(learningModelArrayList.get(position).getLearntext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return learningModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        public ViewHolder(View view) {
            super(view);

            textview = (TextView) view.findViewById(R.id.nama_materi);
        }
    }
}
