/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blessing.Model.KelasModel;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import com.example.blessing.Model.JenjangModel;
import com.example.blessing.Model.KelasModel;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;
import static com.example.blessing.MapelActivity.EXTRA_MAPEL;
import static com.example.blessing.MapelSoalActivity.EXTRA_IDJENJANG;
import static com.example.blessing.MapelSoalActivity.EXTRA_MAPELSOAL;
import static com.example.blessing.SoalActivity.EXTRA_NAMAJENJANG;
import static com.example.blessing.SoalActivity.EXTRA_SOAL;

public class CreateSoalActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = SoalActivity.class.getSimpleName();
    private EditText editText;
    private Spinner spinner;
    private String idsoal, idjenjang, namajenjang, idmapelsoal;
    private Boolean updatesoal;
    private API service;
    private long mLastClickTime = 0;
    private List<String> listSpinner = new ArrayList<>();
    private String selectedId;
    private String idRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_soal);
        editText = findViewById(R.id.soal);
        editText.setHint(getIntent().getStringExtra("edittextitem") == null ? "Input nama soal" : getIntent().getStringExtra("edittextitem"));

        idsoal = getIntent().getStringExtra(EXTRA_SOAL);
        idjenjang = getIntent().getStringExtra(EXTRA_IDJENJANG);
        namajenjang = getIntent().getStringExtra(EXTRA_NAMAJENJANG);
        idmapelsoal = getIntent().getStringExtra(EXTRA_MAPELSOAL);
        updatesoal = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);

        spinner = findViewById(R.id.spinnersoal);
        //spinner.setPrompt("Pilih kelas");
        Button button1 = findViewById(R.id.btnsoal);
        button1.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Input Soal </font>", Html.FROM_HTML_MODE_LEGACY));

        TextView tvKelas = findViewById(R.id.tvpilihkelas);
        TextView judul = findViewById(R.id.judulsoal);

        service = RetrofitBuildCustom.getInstance().getService();
        Button button = findViewById(R.id.btnsoal);
        button.setOnClickListener(this);

        getDataKelas(idjenjang);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsoal:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (editText != null && !updatesoal) {
                    saveDataSoal(idjenjang, idmapelsoal);
                } else {
                    updateDataSoal(idsoal, idmapelsoal);
                }
                break;
        }
    }

    private void makeMoveActivity(String id, String nama, String idjenjang) {
        Intent intent = new Intent(CreateSoalActivity.this, SoalActivity.class);
        intent.putExtra(EXTRA_MAPELSOAL, id);
        intent.putExtra(EXTRA_NAMAJENJANG, nama);
        intent.putExtra(EXTRA_IDJENJANG, idjenjang);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            makeMoveActivity(idmapelsoal, namajenjang, idjenjang);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDataSoal(String idjenjang, String idmapelsoal) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(CreateSoalActivity.this, "isi judul soal", Toast.LENGTH_SHORT).show();
        } else if (selectedId == null) {
            Toast.makeText(CreateSoalActivity.this, "pilih kelas", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "saveDataSoal: " + selectedId);
            Call<SoalModel> call = service.postdatasoal(idjenjang, idmapelsoal, selectedId, editText.getText().toString());
            call.enqueue(new Callback<SoalModel>() {
                @Override
                public void onResponse(Call<SoalModel> call, Response<SoalModel> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response);
                        clearAll();
                        Toast.makeText(CreateSoalActivity.this, "berhasil menyimpan soal", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateSoalActivity.this, "gagal menyimpan soal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SoalModel> call, Throwable t) {
                    Log.e("CreateSoalActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void getDataKelas(String id) {
        service.getdatakelasbyidjenjang(id).enqueue(new Callback<List<KelasModel>>() {
            @Override
            public void onResponse(Call<List<KelasModel>> call, Response<List<KelasModel>> response) {
                List<KelasModel> kelasModelList = response.body();
                for (int i = 0; i < kelasModelList.size(); i++) {
                    listSpinner.add(kelasModelList.get(i).getKelas());
                }
                listSpinner.add(0, "- Pilih Kelas -");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateSoalActivity.this, android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemSelected: " + position);
                        if (position != 0) {
                            selectedId = String.valueOf(kelasModelList.get(position - 1).getIdKelas());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<KelasModel>> call, Throwable t) {

            }
        });
    }

    public void updateDataSoal(String id, String idkelas) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(CreateSoalActivity.this, "isi judul soal", Toast.LENGTH_SHORT).show();
        } else {
            Call<SoalModel> call = service.updatedatasoal(id, idkelas, editText.getText().toString());
            call.enqueue(new Callback<SoalModel>() {
                @Override
                public void onResponse(Call<SoalModel> call, Response<SoalModel> response) {
                    Log.d(TAG, "Update: " + id);
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateSoalActivity.this, "update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateSoalActivity.this, "update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SoalModel> call, Throwable t) {
                    Log.e("CreateSoalActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearAll() {
        editText.getText().clear();
    }

}
