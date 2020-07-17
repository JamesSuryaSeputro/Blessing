package com.example.blessing.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.R;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.github.chrisbanes.photoview.PhotoView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImagePembahasanAdapter extends RecyclerView.Adapter<ImagePembahasanAdapter.ViewHolder> {

    private List<KuisModel> mLearningModelArrayList;
    private Context mContext;

    public ImagePembahasanAdapter(ArrayList<KuisModel> learningModelArrayList, Context context) {
        this.mLearningModelArrayList = learningModelArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ImagePembahasanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePembahasanAdapter.ViewHolder holder, int position) {
        KuisModel kuisModel = mLearningModelArrayList.get(position);
        String imgList = String.valueOf(kuisModel.getImgPertanyaan());

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate();

        String path = RetrofitBuildCustom.BASE_URL + "uploads/";
        Glide.with(mContext)
                .load(path + imgList)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.solutionProgressBar.setVisibility(View.GONE);
                        holder.photoView.setScaleType(ImageView.ScaleType.CENTER);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.solutionProgressBar.setVisibility(View.GONE);
                        holder.photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .apply(options)
                .into(holder.photoView);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;
        ProgressBar solutionProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.img_pembahasan);
            solutionProgressBar = itemView.findViewById(R.id.solutionProgressBar);
        }
    }
}
