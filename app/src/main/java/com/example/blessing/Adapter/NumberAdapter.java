package com.example.blessing.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.KuisModel;
import com.example.blessing.Model.MateriModel;
import com.example.blessing.Model.NumberModel;
import com.example.blessing.R;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    private static final String TAG = "NumberAdapter";
    private List<KuisModel> numberList;
    private Context mContext;
    private OnClickItemContextMenuNumber mListener;
    private int selectedPos = RecyclerView.NO_POSITION;
    public String userid;

    public NumberAdapter(Context context, ArrayList<KuisModel> numberList) {
        this.mContext = context;
        this.numberList = numberList;
        this.userid = Preferences.getKeyUser(context);
    }

    public void setmListener(OnClickItemContextMenuNumber mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KuisModel kuisModel = numberList.get(position);
        holder.number.setText(String.valueOf(position + 1));
        holder.number.setSelected(selectedPos == position);
        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + kuisModel.getIdKuis());
                mListener.onClickItem(kuisModel, position);
                notifyItemChanged(selectedPos);
                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);
            }
        });
    }

    public void updateData(List<KuisModel> list) {

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
            Log.d(TAG, "ViewHolderUserId: " + userid);
            if(!userid.equals("3")) {
                number.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it -> {
                Log.d(TAG, "onMenuItemClick: " + numberList.get(getAdapterPosition()).getIdKuis());
                mListener.onEditItem(numberList.get(getAdapterPosition()).getIdKuis(), numberList.get(getAdapterPosition()).getIdSoal(), numberList.get(getAdapterPosition()).getIdDetailkuis());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it -> {
                mListener.onDeleteItem(numberList.get(getAdapterPosition()).getIdKuis());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 2, "Cancel").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(TAG, "onMenuItemClick: " + numberList.get(getAdapterPosition()).getIdKuis());
                    return false;
                }
            });
        }
    }
}
