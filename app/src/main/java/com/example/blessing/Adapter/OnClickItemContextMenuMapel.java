package com.example.blessing.Adapter;

public interface OnClickItemContextMenuMapel {
    void onDeleteItem(String id);
    void onEditItem(String id, String nama);
    void onClickItem(String id);
}// mapel
// 2 interface ini kan untuk handle context yang ada itu cuman delete sama edit click itu kan view.onclicklistener harusnya lu bedahin buat kenapa buat SOLID nggak jadi kaku. beratti lu harus membuat component baru yang sama fungsinya