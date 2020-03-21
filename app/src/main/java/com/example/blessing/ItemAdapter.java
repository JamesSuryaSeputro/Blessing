package com.example.blessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.LearningModel;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<LearningModel> learningModelArrayList;
    private Context context;
    private CustomRecyclerViewListener customRecyclerViewListener;

    public ItemAdapter(ArrayList<LearningModel> learningModelArrayList, Context context, CustomRecyclerViewListener customRecyclerViewListener) {
        this.learningModelArrayList = learningModelArrayList;
        this.context = context;
        this.customRecyclerViewListener = customRecyclerViewListener;
    }


    public void setCustomRecyclerViewListener(CustomRecyclerViewListener customRecyclerViewListener) {
        this.customRecyclerViewListener = customRecyclerViewListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, final int position) {
        holder.imageview.setImageResource(learningModelArrayList.get(position).getLearnimage());
        holder.textview.setText(learningModelArrayList.get(position).getLearntext());
        holder.imageview.setOnClickListener(new View.OnClickListener() {
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
        ImageView imageview;
        TextView textview;

        public ViewHolder(View view) {
            super(view);

            imageview = (ImageView) view.findViewById(R.id.RV_Image);
            textview = (TextView) view.findViewById(R.id.RV_Text);
        }
    }
}
