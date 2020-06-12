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

import com.example.blessing.Model.MapelSoalModel;
import com.example.blessing.R;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MapelSoalAdapter extends RecyclerView.Adapter<MapelSoalAdapter.ViewHolder> {
    private static final String TAG = "MapelSoalAdapter";
    private List<MapelSoalModel> mLearningModelArrayList;
    private List<MapelSoalModel> mLearningModelArrayListNew = new ArrayList<>();
    private Context mContext;
    private OnClickItemContextMenuMapelSoal mListener;
    private String userid;

    public MapelSoalAdapter(Context context, ArrayList<MapelSoalModel> learningModelArrayList) {
        this.mContext = context;
        this.mLearningModelArrayList = learningModelArrayList;
        this.userid = Preferences.getKeyUser(context);
    }

    public void setmListener(OnClickItemContextMenuMapelSoal mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cv_list, parent, false);
        MapelSoalAdapter.ViewHolder holder = new MapelSoalAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MapelSoalAdapter.ViewHolder holder, int position) {
        if (mLearningModelArrayListNew!= null){
            if (!mLearningModelArrayListNew.isEmpty()){
                MapelSoalModel mapelSoalModel = mLearningModelArrayListNew.get(position);
                String mapelList = mapelSoalModel.getNamaMapelsoal();
                holder.textView.setText(mapelList);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClickItem(mapelSoalModel.getIdMapelsoal(), mapelSoalModel.getIdJenjang(), mapelSoalModel.getNamaJenjang());
                    }
                });
            }
        }
    }

    public void updateData(List<MapelSoalModel> list) {
        mLearningModelArrayList = list;
        notifyDataSetChanged();
    }

    public void removeData(List<MapelSoalModel> list) {
        mLearningModelArrayListNew = list;
        notifyDataSetChanged();
    }

    public void updateDataChangeFilterSchool(int id){
        Log.d(TAG, "updateDataChangeFilterSchool: "+id);
        //mLearningModelArrayList.removeIf(it -> it.getIdMapelsoal().equals(String.valueOf(id)));
        mLearningModelArrayListNew = mLearningModelArrayList.stream().filter(it -> it.getIdJenjang().equals(String.valueOf(id))).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mLearningModelArrayListNew.size() > 0) {
            return mLearningModelArrayListNew.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.list_item);
            cardView = view.findViewById(R.id.card_view);
            if(!userid.equals("3")) {
                cardView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it -> {
                Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayListNew.get(getAdapterPosition()).getNamaMapelsoal());
                mListener.onEditItem(mLearningModelArrayListNew.get(getAdapterPosition()).getIdMapelsoal(), mLearningModelArrayListNew.get(getAdapterPosition()).getNamaMapelsoal());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it -> {
                mListener.onDeleteItem(mLearningModelArrayListNew.get(getAdapterPosition()).getIdMapelsoal());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 2, "Cancel").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(TAG, "onMenuItemClick: " + mLearningModelArrayListNew.get(getAdapterPosition()).getNamaMapelsoal());
                    return false;
                }
            });
        }
    }
}

