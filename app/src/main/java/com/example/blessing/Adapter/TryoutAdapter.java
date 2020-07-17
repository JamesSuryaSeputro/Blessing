package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.TryoutModel;
import com.example.blessing.R;
import com.example.blessing.Utils.Preferences;

import java.util.List;

public class TryoutAdapter extends RecyclerView.Adapter<TryoutAdapter.ViewHolder> {
    private static final String TAG = "TryoutAdapter";
    private List<TryoutModel> mLearningModelArrayList;
    private Context mContext;
    private String userid;
    private OnClickItemContextMenuTryout mListener;

    public TryoutAdapter(List<TryoutModel> mLearningModelArrayList, Context mContext) {
        this.mLearningModelArrayList = mLearningModelArrayList;
        this.mContext = mContext;
        this.userid = Preferences.getKeyUser(mContext);
    }

    public void setmListener(OnClickItemContextMenuTryout mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cv_to, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                TryoutModel tryoutModel = mLearningModelArrayList.get(position);
                String listJudul = tryoutModel.getJudul();
                String listDeskripsi = tryoutModel.getDeskripsi();
                String listJenjang = tryoutModel.getNamaJenjang();
                String listDate = tryoutModel.getDatecreated();

                holder.tvJudul.setText(listJudul);
                holder.tvDeskripsi.setText(listDeskripsi);
                holder.tvJenjang.setText(listJenjang);
                holder.tvDate.setText(listDate);
                holder.btnStart.setOnClickListener(view -> mListener.onClickItem(tryoutModel.getIdTryout(), tryoutModel.getJudul(), tryoutModel.getTimer()));
    }

    @Override
    public int getItemCount() {
        if (mLearningModelArrayList.size() > 0) {
            return mLearningModelArrayList.size();
        } else {
            return 0;
        }
    }

    public void updateData(List<TryoutModel> list) {
        mLearningModelArrayList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvJudul, tvDeskripsi, tvJenjang, tvDate;
        Button btnStart;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.list_judul);
            tvDeskripsi = itemView.findViewById(R.id.list_deskripsi);
            tvJenjang = itemView.findViewById(R.id.list_jenjang);
            tvDate = itemView.findViewById(R.id.list_date);
            btnStart = itemView.findViewById(R.id.btnstart);
            cardView = itemView.findViewById(R.id.card_view);
            if(!userid.equals("3")) {
                cardView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it -> {
                Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayList.get(getAdapterPosition()).getIdTryout());
                mListener.onEditItem(mLearningModelArrayList.get(getAdapterPosition()).getIdTryout());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it -> {
                mListener.onDeleteItem(mLearningModelArrayList.get(getAdapterPosition()).getIdTryout());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 2, "Cancel").setOnMenuItemClickListener(item -> {
                Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayList.get(getAdapterPosition()).getIdTryout());
                return false;
            });
        }
    }
}
