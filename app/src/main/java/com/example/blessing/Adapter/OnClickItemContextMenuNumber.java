package com.example.blessing.Adapter;

import com.example.blessing.Model.KuisModel;

public interface OnClickItemContextMenuNumber {
    void onDeleteItem(String id);
    void onEditItem(String id, String sId, String dkId);
    void onClickItem(KuisModel kuisModel, int posisi);
}
