package com.example.blessing.Adapter;

import android.content.Context;
<<<<<<< HEAD
=======
import android.database.Cursor;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
<<<<<<< HEAD

=======
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.MateriModel;
<<<<<<< HEAD
import com.example.blessing.R;

=======
import com.example.blessing.Model.SoalModel;
import com.example.blessing.R;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import java.util.ArrayList;
import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.ViewHolder> {

    private static final String TAG = "MateriAdapter";
    private List<MateriModel> mLearningModelArrayList;
    private Context mContext;
    private OnClickItemContextMenuMateri mListener;

    public MateriAdapter(Context context, ArrayList<MateriModel> learningModelArrayList) {
        this.mContext = context;
        this.mLearningModelArrayList = learningModelArrayList;
    }

    public void setmListener(OnClickItemContextMenuMateri mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MateriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        MateriAdapter.ViewHolder holder = new MateriAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MateriAdapter.ViewHolder holder, int position) {
        MateriModel materiModel = mLearningModelArrayList.get(position);
        String materiList = materiModel.getJudulMateri();
        String kelasList = materiModel.getKelas();

        holder.tvMateri.setText(materiList);
        holder.tvListKelas.setText(kelasList);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onClickItem(materiModel.getIdMateri(), materiModel.getNamaMateri(), materiModel.getJudulMateri());
            }
        });
    }

    public void updateData(List<MateriModel> list) {

        mLearningModelArrayList = list;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvMateri, tvListKelas;
        CardView cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvMateri = view.findViewById(R.id.list_judul);
            tvListKelas = view.findViewById(R.id.list_kelas);
            cardView = view.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it ->{
                Log.d(TAG, "onMenuItemClick: "+mLearningModelArrayList.get(getAdapterPosition()).getIdMateri());
                mListener.onEditItem(mLearningModelArrayList.get(getAdapterPosition()).getIdMateri(), mLearningModelArrayList.get(getAdapterPosition()).getIdMapel(), mLearningModelArrayList.get(getAdapterPosition()).getJudulMateri());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it ->{
                mListener.onDeleteItem(mLearningModelArrayList.get(getAdapterPosition()).getIdMateri(), mLearningModelArrayList.get(getAdapterPosition()).getJudulMateri());
                return false;
            });
            menu.add(this.getAdapterPosition(),v.getId(), 2, "Cancel").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(TAG, "onMenuItemClick: "+mLearningModelArrayList.get(getAdapterPosition()).getIdMateri());
                    return false;
                }
            });
        }
    }
}
