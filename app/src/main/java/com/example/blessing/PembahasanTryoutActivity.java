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

<<<<<<< HEAD
import com.example.blessing.Adapter.ImagePembahasanTryoutAdapter;
=======
import com.example.blessing.Adapter.ImagePembahasanAdapter;
import com.example.blessing.Adapter.ImagePembahasanTryoutAdapter;
import com.example.blessing.Adapter.JawabanAdapter;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import com.example.blessing.Adapter.JawabanTryoutAdapter;
import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembahasanTryoutActivity extends AppCompatActivity {
<<<<<<< HEAD
    public static final String EXTRA_BOOLEAN = "extra_boolean";
=======
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
    private static final String TAG = PembahasanTryoutActivity.class.getSimpleName();
    private ImagePembahasanTryoutAdapter mAdapter;
    private JawabanTryoutAdapter mJawabanAdapter;
    private API service;
<<<<<<< HEAD
=======
    public static final String EXTRA_BOOLEAN = "extra_boolean";
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
    private TextView tvJawabanTo, tvBelumAdaPembahasanTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembahasan_tryout);

        String judultryout = Preferences.getKeyJudulTryout(getBaseContext());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> " + judultryout + " </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        SnapHelper helper = new LinearSnapHelper();
        RecyclerView rvImageTo = findViewById(R.id.RV_imgpembahasanto);
        rvImageTo.setHasFixedSize(true);
        helper.attachToRecyclerView(rvImageTo);
        rvImageTo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImagePembahasanTryoutAdapter(new ArrayList<>(), PembahasanTryoutActivity.this);
        rvImageTo.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvJawabanTo.setVisibility(View.GONE);
                    tvBelumAdaPembahasanTo.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView rvJawabanTo = findViewById(R.id.RV_jawabanto);
        rvJawabanTo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mJawabanAdapter = new JawabanTryoutAdapter(new ArrayList<>(), PembahasanTryoutActivity.this);
        rvJawabanTo.setAdapter(mJawabanAdapter);

        service = RetrofitBuildCustom.getInstance().getService();

        String idtryout = Preferences.getKeyIdTryout(getBaseContext());

        tvJawabanTo = findViewById(R.id.tvjawabanto);
        tvBelumAdaPembahasanTo = findViewById(R.id.tvbelumadapembahasanto);

        getPembahasanTryout(idtryout);
    }


    public void getPembahasanTryout(String id) {
        service.getdetailtryout(id).enqueue(new Callback<List<DetailTryoutModel>>() {
            @Override
            public void onResponse(Call<List<DetailTryoutModel>> call, Response<List<DetailTryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        mJawabanAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DetailTryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(PembahasanTryoutActivity.this, TryoutActivity.class);
            intent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}