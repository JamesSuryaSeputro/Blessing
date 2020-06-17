package com.example.blessing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.MainModel;
import com.example.blessing.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainModel> learningModelArrayList;
    private Context context;

    public MainAdapter(ArrayList<MainModel> learningModelArrayList, Context context) {
        this.learningModelArrayList = learningModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_img, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, final int position) {
        holder.imageview.setImageResource(learningModelArrayList.get(position).getLearnimage());
        holder.textview.setText(learningModelArrayList.get(position).getLearntext());
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  customRecyclerViewListener.onItemClick(learningModelArrayList.get(position).getLearntext());
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

            //imageview = (ImageView) view.findViewById(R.id.RV_Image);
            //textview = (TextView) view.findViewById(R.id.RV_Text);
        }
    }
}
