package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.blessing.Model.JenjangModel;
import com.example.blessing.Model.MapelSoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;
import static com.example.blessing.MapelSoalActivity.EXTRA_MAPELSOAL;

public class CreateMapelSoalActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private API service;
    private long mLastClickTime = 0;
    private Spinner spinner;
    private String idmapelsoal;
    private Boolean updatemapelsoal;
    private List<String> listSpinner = new ArrayList<>();
    public static final String TAG = CreateMapelSoalActivity.class.getSimpleName();
    private String selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mapel_soal);
        editText = findViewById(R.id.mapelsoal);
        editText.setHint(getIntent().getStringExtra("edittextitem") == null ? "Input nama mapel" : getIntent().getStringExtra("edittextitem"));

        service = RetrofitBuildCustom.getInstance().getService();
        Button btnMapelSoal = findViewById(R.id.btnmapelsoal);
        btnMapelSoal.setOnClickListener(this);

        idmapelsoal = getIntent().getStringExtra(EXTRA_MAPELSOAL);
        updatemapelsoal = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);


        TextView judul = findViewById(R.id.judulmapelsoal);
        TextView tvJenjang = findViewById(R.id.tvpilihjenjang);
        spinner = findViewById(R.id.spinnermapelsoal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Input Mapel Soal </font>", Html.FROM_HTML_MODE_LEGACY));

        if (!updatemapelsoal) {
            //true
            Log.d(TAG, "OnCreate: MapelSoal");
        } else {
            tvJenjang.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            judul.setText("Edit Mapel Soal");
        }
        getDataJenjang();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnmapelsoal:
                if (editText != null && !updatemapelsoal) {
                    saveDataMapelSoal();
                } else {
                    MapelSoalModel mapelSoalModel = new MapelSoalModel();
                    mapelSoalModel.setNamaMapelsoal(editText.getText().toString());
                    updateDataMapel(idmapelsoal, mapelSoalModel);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(CreateMapelSoalActivity.this, MapelSoalActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataJenjang() {
        service.getdatajenjang().enqueue(new Callback<List<JenjangModel>>() {
            @Override
            public void onResponse(Call<List<JenjangModel>> call, Response<List<JenjangModel>> response) {
                List<JenjangModel> jenjangModelList = response.body();
                for (int i = 0; i < jenjangModelList.size(); i++) {
                    listSpinner.add(jenjangModelList.get(i).getNamaJenjang());
                }
                listSpinner.add(0, "- Pilih Jenjang -"); // no db 0-> SMA size 3 -> size 4 != database
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateMapelSoalActivity.this, android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemSelected: " + position);
                        if (position != 0) {
                            selectedId = String.valueOf(jenjangModelList.get(position - 1).getIdJenjang());
                        }
                        // API  SMP[0] SMA[1] WWW[2] -> Dari response Retrofit soalnya lu taroh adapternya di dalam call
                        //LIST Local -> Pilih[0] SMP[1-1] SMA[2-1] www{3-1] ->Spinner
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<JenjangModel>> call, Throwable t) {

            }
        });
    }

    public void saveDataMapelSoal() {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(CreateMapelSoalActivity.this, "isi mata pelajaran", Toast.LENGTH_SHORT).show();
        } else if (selectedId == null) {
            Toast.makeText(CreateMapelSoalActivity.this, "pilih jenjang", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "saveDataMapelSoal: " + spinner.getSelectedItemId());
            Call<MapelSoalModel> call = service.postdatamapelsoal(selectedId, editText.getText().toString());
            call.enqueue(new Callback<MapelSoalModel>() {
                @Override
                public void onResponse(Call<MapelSoalModel> call, Response<MapelSoalModel> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response);
                        clearAll();
                        Toast.makeText(CreateMapelSoalActivity.this, "berhasil menambah mapel", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMapelSoalActivity.this, "gagal menambah mapel", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MapelSoalModel> call, Throwable t) {
                    Log.e("CreateMapelSoalActivity", "onFailure: ", t);
                }
            });
        }
    }

    public void updateDataMapel(String id, MapelSoalModel mapelSoalModel) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(CreateMapelSoalActivity.this, "isi mata pelajaran", Toast.LENGTH_SHORT).show();
        } else {
            Call<MapelSoalModel> call = service.updatedatamapelsoal(id, mapelSoalModel);
            call.enqueue(new Callback<MapelSoalModel>() {
                @Override
                public void onResponse(Call<MapelSoalModel> call, Response<MapelSoalModel> response) {
                    Log.d(TAG, "Update: " + id + mapelSoalModel);
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateMapelSoalActivity.this, "update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMapelSoalActivity.this, "update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MapelSoalModel> call, Throwable t) {
                    Log.e("CreateMapelSoalActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearAll() {
        editText.getText().clear();
    }
}
