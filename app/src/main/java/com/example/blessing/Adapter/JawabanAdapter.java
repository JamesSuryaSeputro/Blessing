package com.example.blessing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.KuisModel;
import com.example.blessing.R;

import java.util.List;

public class JawabanAdapter extends RecyclerView.Adapter<JawabanAdapter.ViewHolder> {

    private List<KuisModel> mLearningModelArrayList;
    private Context mContext;

    public JawabanAdapter(List<KuisModel> mLearningModelArrayList, Context mContext) {
        this.mLearningModelArrayList = mLearningModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_jawaban, parent, false);
        return new JawabanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KuisModel kuisModel = mLearningModelArrayList.get(position);
        holder.noJawaban.setText(String.valueOf(position + 1));
        holder.jawaban.setText(kuisModel.getJawaban());
    }

    @Override
    public int getItemCount() {
        if (mLearningModelArrayList.size() > 0) {
            return mLearningModelArrayList.size();
        } else {
            return 0;
        }
    }

    public void updateData(List<KuisModel> list) {
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
