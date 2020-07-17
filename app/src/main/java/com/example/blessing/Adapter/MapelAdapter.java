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

import com.example.blessing.Model.MapelModel;
import com.example.blessing.R;

import java.util.ArrayList;
import java.util.List;

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.ViewHolder>{

    private static final String TAG = "MapelAdapter";
    private List<MapelModel> mLearningModelArrayList;
    private Context mContext;
    private OnClickItemContextMenuMapel mListener;

    public MapelAdapter(Context context, ArrayList<MapelModel> learningModelArrayList) {
        this.mContext = context;
        this.mLearningModelArrayList = learningModelArrayList;
    }

    public void setmListener(OnClickItemContextMenuMapel mListener) {
        this.mListener = mListener;
    }
    
    @NonNull
    @Override
    public MapelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cv_list, parent, false);
        MapelAdapter.ViewHolder holder = new MapelAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MapelAdapter.ViewHolder holder, final int position) {
        MapelModel mapelModel = mLearningModelArrayList.get(position);
        String mapelList = mapelModel.getNamaMapel();
        holder.textView.setText(mapelList);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mListener.onClickItem(mapelModel.getIdMapel(), mapelModel.getNamaMapel());
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.d("MapelAdapter", "long press");
//                mListener.onItemClickLongListener(Integer.parseInt(mapelModel.getIdMapel()));
//                return true;
//            }
//        });

    }

    public void updateData(List<MapelModel> list) {
      // mLearningModelArrayList.addAll(score);
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
        TextView textView;
        CardView cardView;

        //ingat ini di kasih sebagai deklarasi dari inherintancenya dan pendeklarasian tentang alamat dari element
        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.list_item);
            cardView = view.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit").setOnMenuItemClickListener(it ->{
                Log.d(TAG, "onMenuItemClick: "+mLearningModelArrayList.get(getAdapterPosition()).getIdMapel());
                mListener.onEditItem(mLearningModelArrayList.get(getAdapterPosition()).getIdMapel(),mLearningModelArrayList.get(getAdapterPosition()).getNamaMapel());
                return false;
            });
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Delete").setOnMenuItemClickListener(it ->{
                mListener.onDeleteItem(mLearningModelArrayList.get(getAdapterPosition()).getIdMapel());
                return false;
            });
            menu.add(this.getAdapterPosition(),v.getId(), 2, "Cancel").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(TAG, "onMenuItemClick: "+mLearningModelArrayList.get(getAdapterPosition()).getIdMapel());
                    return false;
                }
            });
        }
    }
}

