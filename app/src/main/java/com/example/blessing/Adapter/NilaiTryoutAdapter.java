package com.example.blessing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.NilaiTryoutModel;
import com.example.blessing.R;

import java.util.List;

public class NilaiTryoutAdapter extends RecyclerView.Adapter<NilaiTryoutAdapter.ViewHolder> {
    private static final String TAG = "NilaiTryoutAdapter";
    private List<NilaiTryoutModel> listNilai;
    private Context mContext;

    public NilaiTryoutAdapter(List<NilaiTryoutModel> listNilai, Context mContext) {
        this.listNilai = listNilai;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NilaiTryoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_nilaitryout, parent, false);
        return new NilaiTryoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NilaiTryoutAdapter.ViewHolder holder, int position) {
        NilaiTryoutModel nilaiTryoutModel = listNilai.get(position);
        String listJudul = nilaiTryoutModel.getJudul();
        String listNilai = nilaiTryoutModel.getNilaiTryout();
        String listJumlah = nilaiTryoutModel.getJumlahTryout();
        String listDate = nilaiTryoutModel.getDatecreated();

        holder.tvListJudul.setText(listJudul);
        holder.tvListNilaiTryout.setText(listNilai);
        holder.tvListJumlahTryout.setText(listJumlah);
        holder.tvListDate.setText(listDate);
    }

    @Override
    public int getItemCount() {
        if (listNilai.size() > 0) {
            return listNilai.size();
        } else {
            return 0;
        }
    }

    public void updateData(List<NilaiTryoutModel> list) {
        listNilai = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListJudul, tvListNilaiTryout, tvListDate, tvListJumlahTryout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvListJudul = itemView.findViewById(R.id.list_judul);
            tvListNilaiTryout = itemView.findViewById(R.id.list_nilaitryout);
            tvListJumlahTryout = itemView.findViewById(R.id.list_jumlahtryout);
            tvListDate = itemView.findViewById(R.id.list_date);
        }
    }
}
