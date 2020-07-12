package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.NilaiTryoutAdapter;
import com.example.blessing.Model.NilaiTryoutModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiTryoutActivity extends AppCompatActivity {
    private static final String TAG = NilaiTryoutAdapter.class.getSimpleName();
    private NilaiTryoutAdapter mAdapter;
    private API service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_tryout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Nilai Tryout </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        service = RetrofitBuildCustom.getInstance().getService();

        RecyclerView recyclerView = findViewById(R.id.RV_nilaitryout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new NilaiTryoutAdapter(new ArrayList<>(), NilaiTryoutActivity.this);
        recyclerView.setAdapter(mAdapter);

        String id = Preferences.getKeyId(getBaseContext());

        Log.d(TAG, "id: " + id);
        getDataNilaiTryout(id);
    }

    public void getDataNilaiTryout(String id) {
        service.getnilaitryout(id).enqueue(new Callback<List<NilaiTryoutModel>>() {
            @Override
            public void onResponse(Call<List<NilaiTryoutModel>> call, Response<List<NilaiTryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NilaiTryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(NilaiTryoutActivity.this, MenuHasilActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}