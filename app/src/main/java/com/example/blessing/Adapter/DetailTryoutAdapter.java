package com.example.blessing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.R;
import com.example.blessing.Utils.Preferences;

import java.util.List;

public class DetailTryoutAdapter extends RecyclerView.Adapter<DetailTryoutAdapter.ViewHolder> {

    private static final String TAG = "DetailTryoutAdapter";
    public String userid;
    private List<DetailTryoutModel> numberList;
    private Context mContext;
    private OnClickItemContextMenuDetailTryout mListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public DetailTryoutAdapter(List<DetailTryoutModel> numberList, Context mContext) {
        this.numberList = numberList;
        this.mContext = mContext;
        this.userid = Preferences.getKeyUser(mContext);
    }

    public void setmListener(OnClickItemContextMenuDetailTryout mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public DetailTryoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.number, parent, false);
        return new DetailTryoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailTryoutAdapter.ViewHolder holder, int position) {
        DetailTryoutModel detailTryoutModel = numberList.get(position);
        holder.number.setText(String.valueOf(position + 1));
        holder.number.setSelected(selectedPos == position);
        holder.number.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + detailTryoutModel);
            mListener.onClickItem(detailTryoutModel, position);
            notifyItemChanged(selectedPos);
            selectedPos = holder.getLayoutPosition();
            notifyItemChanged(selectedPos);
        });
    }

    public void updateData(List<DetailTryoutModel> list) {

        this.numberList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (numberList.size() > 0) {
            return numberList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            if (!userid.equals("3")) {
                number.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it -> {
                Log.d(TAG, "onMenuItemClick: " + numberList.get(getAdapterPosition()).getIdTryout());
                mListener.onEditItem(numberList.get(getAdapterPosition()).getIdTryout());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it -> {
                mListener.onDeleteItem(numberList.get(getAdapterPosition()).getIdDetailtryout());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 2, "Cancel").setOnMenuItemClickListener(item -> {
                Log.d(TAG, "onMenuItemClick: " + numberList.get(getAdapterPosition()).getIdDetailtryout());
                return false;
            });
        }
    }
}
