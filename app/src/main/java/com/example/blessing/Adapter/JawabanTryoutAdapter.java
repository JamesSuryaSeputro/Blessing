package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.R;

import java.util.List;

public class JawabanTryoutAdapter extends RecyclerView.Adapter<JawabanTryoutAdapter.ViewHolder> {

    private List<DetailTryoutModel> mLearningModelArrayList;
    private Context mContext;

    public JawabanTryoutAdapter(List<DetailTryoutModel> mLearningModelArrayList, Context mContext) {
        this.mLearningModelArrayList = mLearningModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_jawaban, parent, false);
        return new JawabanTryoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailTryoutModel detailTryoutModel = mLearningModelArrayList.get(position);
        holder.noJawaban.setText(String.valueOf(position + 1));
        holder.jawaban.setText(detailTryoutModel.getJawabanTo());
    }

    @Override
    public int getItemCount() {
        if (mLearningModelArrayList.size() > 0) {
            return mLearningModelArrayList.size();
        } else {
            return 0;
        }
    }

    public void updateData(List<DetailTryoutModel> list) {
        mLearningModelArrayList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noJawaban, jawaban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noJawaban = itemView.findViewById(R.id.nojawaban);
            jawaban = itemView.findViewById(R.id.jawaban);
        }
    }
}
