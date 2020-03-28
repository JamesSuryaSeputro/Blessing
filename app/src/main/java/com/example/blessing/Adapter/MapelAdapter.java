package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.MapelActivity;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.R;

import java.util.ArrayList;
import java.util.List;

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.ViewHolder> {

    private ArrayList<MapelModel> mLearningModelArrayList;
    private Context mContext;
    private OnItemClickListener mListener;

    public MapelAdapter(Context context, ArrayList<MapelModel> learningModelArrayList) {
        this.mContext = context;
        this.mLearningModelArrayList = learningModelArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

//    public MapelAdapter(ArrayList<MapelModel> learningModelArrayList, Context context, OnItemClickListener listener) {
//        this.learningModelArrayList = learningModelArrayList;
//        this.context = context;
//        mListener = listener;
//    }

    @NonNull
    @Override
    public MapelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cv_list, parent, false);
        MapelAdapter.ViewHolder holder = new MapelAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MapelAdapter.ViewHolder holder, final int position) {
        MapelModel mapelModel = mLearningModelArrayList.get(position);
        String mapelList = mapelModel.getNamaMapel();

        holder.textview.setText(mapelList);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClickListener(position);
            }
        });
//        holder.textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OnItemClickListener.onItemClickListener(learningModelArrayList.get(position).getNamaMapel());
//            }
//            });
    }

    public void updatedata(List<MapelModel> list) {
        mLearningModelArrayList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mLearningModelArrayList.size() > 0) {
            return mLearningModelArrayList.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        //ingat ini di kasih sebagai deklarasi dari inherintancenya dan pendeklarasian tentang alamat dari element
        public ViewHolder(@NonNull View view) {
            super(view);
            textview = view.findViewById(R.id.nama_materi);
        }
    }
}
