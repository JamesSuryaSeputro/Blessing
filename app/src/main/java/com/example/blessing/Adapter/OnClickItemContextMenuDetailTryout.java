package com.example.blessing.Adapter;

import com.example.blessing.Model.DetailTryoutModel;

public interface OnClickItemContextMenuDetailTryout {
    void onDeleteItem(String id);
    void onEditItem(String id);
    void onClickItem(DetailTryoutModel detailTryoutModel, int posisi);
}
