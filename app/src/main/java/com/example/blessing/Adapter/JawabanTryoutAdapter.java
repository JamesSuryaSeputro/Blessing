package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.R;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class JawabanTryoutAdapter extends RecyclerView.Adapter<JawabanTryoutAdapter.ViewHolder> {

    private List<DetailTryoutModel> mLearningModelArrayList;
    private Context mContext;
    public String userid;
    private Boolean isClicked = true;
    private OnClickImageChooser mListener;

    public JawabanTryoutAdapter(List<DetailTryoutModel> mLearningModelArrayList, Context mContext) {
        this.mLearningModelArrayList = mLearningModelArrayList;
        this.mContext = mContext;
        this.userid = Preferences.getKeyUser(mContext);
    }

    public void setmListener(OnClickImageChooser mListener) {
        this.mListener = mListener;
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
        holder.imgChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(detailTryoutModel.getIdDetailtryout());
            }
        });

        String imgAns = String.valueOf(detailTryoutModel.getImgJawaban());
        String path = RetrofitBuildCustom.BASE_URL + "uploads/";
        Glide.with(mContext)
                .load(path + imgAns)
                .into(holder.imgJawaban);

        holder.imgDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClicked){
                    isClicked = false;
                    holder.imgJawaban.setVisibility(View.VISIBLE);
                } else {
                    isClicked = true;
                    holder.imgJawaban.setVisibility(View.GONE);
                }
            }
        });
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
        ImageView imgChooser, imgDropdown;
        PhotoView imgJawaban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noJawaban = itemView.findViewById(R.id.nojawaban);
            jawaban = itemView.findViewById(R.id.jawaban);
            imgChooser = itemView.findViewById(R.id.pick_image);
            imgDropdown = itemView.findViewById(R.id.dropdown_arrow);
            imgJawaban = itemView.findViewById(R.id.img_jawaban);

            if (userid.equals("3")) {
                imgChooser.setVisibility(View.GONE);
            }
        }
    }
}
