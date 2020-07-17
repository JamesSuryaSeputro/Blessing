package com.example.blessing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.OnClickItemContextMenuSoal;
import com.example.blessing.Adapter.SoalAdapter;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoalActivity extends AppCompatActivity implements OnClickItemContextMenuSoal {
    private static final String TAG = SoalActivity.class.getSimpleName();
    private SoalAdapter mAdapter;
    private long mLastClickTime = 0;
    private API service;
    private FloatingActionButton fab;
    private String idjenjang, namajenjang, idmapelsoal;
    public static final String EXTRA_SOAL = "extra_soal";
    public static final String EXTRA_IDJENJANG = "extra_idjenjang";
    public static final String EXTRA_NAMAJENJANG = "extra_namajenjang";
    public static final String EXTRA_MAPELSOAL = "extra_mapelsoal";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    public static final String EXTRA_KELAS = "extra_kelas";
    public static final String EXTRA_NAMASOAL = "extra_namasoal";
    private Boolean pembahasanBankSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);

        service = RetrofitBuildCustom.getInstance().getService();

        idjenjang = getIntent().getStringExtra(EXTRA_IDJENJANG);
        namajenjang = getIntent().getStringExtra(EXTRA_NAMAJENJANG);
        idmapelsoal = getIntent().getStringExtra(EXTRA_MAPELSOAL);
        pembahasanBankSoal = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Soal </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        fab = findViewById(R.id.fab_addsoal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMoveActivity(idmapelsoal, idjenjang, namajenjang);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.RV_soal);
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
        mAdapter = new SoalAdapter(SoalActivity.this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        TextView tvJenjang = findViewById(R.id.tvjenjang);
        TextView jenjang = findViewById(R.id.jenjang);
        jenjang.setText(namajenjang);

        String idRole = Preferences.getKeyUser(getBaseContext());
        if (idRole.equals("3")) {
            fab.setVisibility(View.GONE);
        }
        if (!pembahasanBankSoal) {
            getSoalByMapel();
        } else {
            fab.setVisibility(View.GONE);
            tvJenjang.setVisibility(View.GONE);
            jenjang.setVisibility(View.GONE);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Pembahasan Bank Soal </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            getDataSoal();
        }
    }

    private void makeMoveActivity(String id, String jId, String nama) {
        preventDoubleClick();
        Intent intent = new Intent(SoalActivity.this, CreateSoalActivity.class);
        intent.putExtra(EXTRA_IDJENJANG, jId);
        intent.putExtra(EXTRA_NAMAJENJANG, nama);
        intent.putExtra(EXTRA_MAPELSOAL, id);
        this.startActivity(intent);
    }

    public void getSoalByMapel() {
        service.getsoalbymapel(idmapelsoal).enqueue(new Callback<List<SoalModel>>() {
            @Override
            public void onResponse(Call<List<SoalModel>> call, Response<List<SoalModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponseSuccessful: " + response);
                        mAdapter.updateData(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<List<SoalModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    //pembahasan bank soal
    public void getDataSoal() {
        service.getdatasoal().enqueue(new Callback<List<SoalModel>>() {
            @Override
            public void onResponse(Call<List<SoalModel>> call, Response<List<SoalModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SoalModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (!pembahasanBankSoal) {
            if (id == android.R.id.home) {
                preventDoubleClick();
                Intent moveIntent = new Intent(SoalActivity.this, MapelSoalActivity.class);
                startActivity(moveIntent);
            }
        } else {
            if (id == android.R.id.home) {
                preventDoubleClick();
                Intent moveIntent = new Intent(SoalActivity.this, MenuHasilActivity.class);
                startActivity(moveIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteItem(String id) {
        Log.d(TAG, "onDeleteItem: " + id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Hapus soal ini?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", (dialog, which) -> service.deletedatasoal(id).enqueue(new Callback<SoalModel>() {
            @Override
            public void onResponse(Call<SoalModel> call, Response<SoalModel> response) {
                getSoalByMapel();
                Toast.makeText(SoalActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SoalModel> call, Throwable t) {
                Toast.makeText(SoalActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
            }
        }));
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String mId, String id, String jId, String namajenjang, String nama) {
        Log.d(TAG, "onEditItem: " + id);
        preventDoubleClick();
        Intent intent = new Intent(SoalActivity.this, CreateSoalActivity.class);
        intent.putExtra(EXTRA_SOAL, mId);
        intent.putExtra(EXTRA_MAPELSOAL, id);
        intent.putExtra(EXTRA_IDJENJANG, jId);
        intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
        intent.putExtra(EXTRA_BOOLEAN, true);
        intent.putExtra("edittextitem", nama);
        startActivity(intent);
    }

    @Override
    public void onClickItem(String id, String jId, String namajenjang, String mId, String cId, String namasoal) {
        if (!pembahasanBankSoal) {
            preventDoubleClick();
            Intent intent = new Intent(SoalActivity.this, DetailKuisActivity.class);
            intent.putExtra(EXTRA_SOAL, id);
            intent.putExtra(EXTRA_IDJENJANG, jId);
            intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
            intent.putExtra(EXTRA_MAPELSOAL, mId);
            intent.putExtra(EXTRA_KELAS, cId);
            intent.putExtra(EXTRA_NAMASOAL, namasoal);
            startActivity(intent);
        } else {
            preventDoubleClick();
            Intent intent = new Intent(SoalActivity.this, PembahasanActivity.class);
            Preferences.setKeyIdSoal(getBaseContext(), id);
            Preferences.setKeyNamaSoal(getBaseContext(), namasoal);
            startActivity(intent);
        }
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
