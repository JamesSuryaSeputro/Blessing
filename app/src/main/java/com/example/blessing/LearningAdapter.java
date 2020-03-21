package com.example.blessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.blessing.Model.LearningModel;
import com.example.blessing.Model.MapelModel;

import java.util.ArrayList;
import java.util.List;

public class LearningAdapter extends RecyclerView.Adapter<LearningAdapter.ViewHolder> {

    private ArrayList<MapelModel> learningModelArrayList;
    private Context context;
    private CustomRecyclerViewListener customRecyclerViewListener;

    public LearningAdapter(ArrayList<MapelModel> learningModelArrayList, Context context, CustomRecyclerViewListener customRecyclerViewListener) {
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
        holder.textview.setText(learningModelArrayList.get(position).getNamaMapel());
        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customRecyclerViewListener.onClickCustomItem(learningModelArrayList.get(position).getNamaMapel());
            }
        });
    }

    public void updatedata(List<MapelModel> list){
        learningModelArrayList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       if (learningModelArrayList.size()>0){
           return learningModelArrayList.size();
       }else {
           return 0;
       }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        public ViewHolder(View view) {
            super(view);
            textview = (TextView) view.findViewById(R.id.nama_materi);
        }
    }
}
