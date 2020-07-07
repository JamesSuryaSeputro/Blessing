package com.example.blessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import com.example.blessing.Adapter.NilaiSoalAdapter;
import com.example.blessing.Model.NilaiSoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiSoalActivity extends AppCompatActivity {
    private static final String TAG = NilaiSoalActivity.class.getSimpleName();
    private NilaiSoalAdapter mAdapter;
    private API service;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_soal);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Nilai Bank Soal </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        service = RetrofitBuildCustom.getInstance().getService();

        RecyclerView recyclerView = findViewById(R.id.RV_nilaisoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new NilaiSoalAdapter(new ArrayList<>(), NilaiSoalActivity.this);
        recyclerView.setAdapter(mAdapter);

        id = Preferences.getKeyId(getBaseContext());

        Log.d(TAG, "id: " + id);
        getDataNilaiSoal(id);
    }

    public void getDataNilaiSoal(String id) {
        service.getnilaisoal(id).enqueue(new Callback<List<NilaiSoalModel>>() {
            @Override
            public void onResponse(Call<List<NilaiSoalModel>> call, Response<List<NilaiSoalModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NilaiSoalModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(NilaiSoalActivity.this, MenuHasilActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}