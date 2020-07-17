package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.blessing.Adapter.ImagePembahasanAdapter;
import com.example.blessing.Adapter.JawabanAdapter;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembahasanActivity extends AppCompatActivity {
    private static final String TAG = PembahasanActivity.class.getSimpleName();
    private ImagePembahasanAdapter mAdapter;
    private JawabanAdapter mJawabanAdapter;
    private API service;
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    public static final String EXTRA_SOAL = "extra_soal";
    private String idsoal;
    private TextView tvJawaban, tvBelumAdaPembahasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembahasan);

        String namasoal = Preferences.getKeyNamaSoal(getBaseContext());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> " + namasoal + " </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        SnapHelper helper = new LinearSnapHelper();
        RecyclerView rvImage = findViewById(R.id.RV_imgpembahasan);
        rvImage.setHasFixedSize(true);
        helper.attachToRecyclerView(rvImage);
        rvImage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImagePembahasanAdapter(new ArrayList<>(), PembahasanActivity.this);
        rvImage.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvJawaban.setVisibility(View.GONE);
                    tvBelumAdaPembahasan.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView rvJawaban = findViewById(R.id.RV_jawaban);
        rvJawaban.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mJawabanAdapter = new JawabanAdapter(new ArrayList<>(), PembahasanActivity.this);
        rvJawaban.setAdapter(mJawabanAdapter);

        service = RetrofitBuildCustom.getInstance().getService();
        idsoal = Preferences.getKeyIdSoal(getBaseContext());
        tvJawaban = findViewById(R.id.tvjawaban);
        tvBelumAdaPembahasan = findViewById(R.id.tvbelumadapembahasan);

        getPembahasan(idsoal);
    }

    public void getPembahasan(String id) {
        service.getdetailkuisbysoal(id).enqueue(new Callback<List<KuisModel>>() {
            @Override
            public void onResponse(Call<List<KuisModel>> call, Response<List<KuisModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        mJawabanAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KuisModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(PembahasanActivity.this, SoalActivity.class);
            intent.putExtra(EXTRA_BOOLEAN, true);
            intent.putExtra(EXTRA_SOAL, idsoal);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}