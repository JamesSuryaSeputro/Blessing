package com.example.blessing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.NilaiSoalModel;
import com.example.blessing.R;

import java.util.List;

public class NilaiSoalAdapter extends RecyclerView.Adapter<NilaiSoalAdapter.ViewHolder> {
    private static final String TAG = "NilaiSoalAdapter";
    private List<NilaiSoalModel> listNilai;
    private Context mContext;

    public NilaiSoalAdapter(List<NilaiSoalModel> listNilai, Context mContext) {
        this.listNilai = listNilai;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_nilaisoal, parent, false);
        return new NilaiSoalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NilaiSoalModel nilaiSoalModel = listNilai.get(position);
        String listNamaSoal = nilaiSoalModel.getNamaSoal();
        String listNilai = nilaiSoalModel.getNilaiSoal();
        String listJumlah = nilaiSoalModel.getJumlahSoal();
        String listDate = nilaiSoalModel.getDatecreated();

        holder.tvListNamaSoal.setText(listNamaSoal);
        holder.tvListNilaiSoal.setText(listNilai);
        holder.tvListJumlahSoal.setText(listJumlah);
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

    public void updateData(List<NilaiSoalModel> list) {
        listNilai = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListNamaSoal, tvListNilaiSoal, tvListDate, tvListJumlahSoal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvListNamaSoal = itemView.findViewById(R.id.list_namasoal);
            tvListNilaiSoal = itemView.findViewById(R.id.list_nilaisoal);
            tvListJumlahSoal = itemView.findViewById(R.id.list_jumlahsoal);
            tvListDate = itemView.findViewById(R.id.list_date);
        }
    }
}
