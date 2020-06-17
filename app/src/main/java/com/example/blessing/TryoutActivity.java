package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.OnClickItemContextMenuTryout;
import com.example.blessing.Adapter.SoalAdapter;
import com.example.blessing.Adapter.TryoutAdapter;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Model.TryoutModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TryoutActivity extends AppCompatActivity implements OnClickItemContextMenuTryout {
    private static final String TAG = TryoutActivity.class.getSimpleName();
    private TryoutAdapter mAdapter;
    private FloatingActionButton fab;
    private long mLastClickTime = 0;
    private API service;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryout);

        fab = findViewById(R.id.fab_addto);
        fab.setOnClickListener(view -> {
            preventDoubleClick();
            Intent intent = new Intent(TryoutActivity.this, CreateTryoutActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.RV_tryout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TryoutAdapter(new ArrayList<>(), TryoutActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        service = RetrofitBuildCustom.getInstance().getService();

        getDataTryout();
    }

    public void getDataTryout() {
        service.getdatatryout().enqueue(new Callback<List<TryoutModel>>() {
            @Override
            public void onResponse(Call<List<TryoutModel>> call, Response<List<TryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onDeleteItem(String id) {

    }

    @Override
    public void onEditItem(String id) {

    }

    @Override
    public void onClickItem(String id) {

    }
}