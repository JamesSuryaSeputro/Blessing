package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.KelasModel;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.R;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class SoalAdapter extends RecyclerView.Adapter<SoalAdapter.ViewHolder> {
    private static final String TAG = "SoalAdapter";
    private List<SoalModel> mLearningModelArrayList;
    private Context mContext;
    private OnClickItemContextMenuSoal mListener;
    private String userid;

    public SoalAdapter(Context context, ArrayList<SoalModel> learningModelArrayList) {
        this.mContext = context;
        this.mLearningModelArrayList = learningModelArrayList;
        this.userid = Preferences.getKeyUser(context);
    }

    public void setmListener(OnClickItemContextMenuSoal mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public SoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cv_listsoal, parent, false);
        SoalAdapter.ViewHolder holder = new SoalAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoalAdapter.ViewHolder holder, int position) {
        SoalModel soalModel = mLearningModelArrayList.get(position);
        String soalList = soalModel.getNamaSoal();
        String kelasList = soalModel.getKelas();
        String textKelas = mContext.getResources().getString(R.string.kelas);

        holder.tvSoal.setText(soalList);
        holder.tvKelas.setText(kelasList);
        holder.mtvKelas.setText(textKelas);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onClickItem(soalModel.getIdSoal(), soalModel.getIdJenjang(), soalModel.getNamaJenjang(), soalModel.getIdMapelsoal(), soalModel.getIdKelas());
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

    public void updateData(List<SoalModel> list) {
        mLearningModelArrayList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvSoal, mtvKelas, tvKelas;
        CardView cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvSoal = view.findViewById(R.id.list_soal);
            mtvKelas = view.findViewById(R.id.tvkelas);
            tvKelas = view.findViewById(R.id.list_kelas);
            cardView = view.findViewById(R.id.card_view);
            if(!userid.equals("3")) {
                cardView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it -> {
                Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayList.get(getAdapterPosition()).getIdSoal());
                mListener.onEditItem(mLearningModelArrayList.get(getAdapterPosition()).getIdSoal(), mLearningModelArrayList.get(getAdapterPosition()).getIdMapelsoal(), mLearningModelArrayList.get(getAdapterPosition()).getNamaJenjang(), mLearningModelArrayList.get(getAdapterPosition()).getNamaSoal());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it -> {
                mListener.onDeleteItem(mLearningModelArrayList.get(getAdapterPosition()).getIdSoal());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 2, "Cancel").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayList.get(getAdapterPosition()).getNamaSoal());
                    return false;
                }
            });
        }
    }
}
