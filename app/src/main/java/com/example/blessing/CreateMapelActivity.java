package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blessing.Model.MapelModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;
import static com.example.blessing.MapelActivity.EXTRA_MAPEL;

public class CreateMapelActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMapel;
    private API service;
    private String mapelid;
    private Boolean updatemapel;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mapel);
        edtMapel = findViewById(R.id.mapel);
        TextView tvMapel = findViewById(R.id.tvmapel);

        edtMapel.setHint(getIntent().getStringExtra("edittextitem") == null ? "Input nama mapel" : getIntent().getStringExtra("edittextitem"));

        service = RetrofitBuildCustom.getInstance().getService();
        Button btnCreateMapel = findViewById(R.id.btninputmapel);
        btnCreateMapel.setOnClickListener(this);

        mapelid = getIntent().getStringExtra(EXTRA_MAPEL);
        updatemapel = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Simpan Mata Pelajaran </font>", Html.FROM_HTML_MODE_LEGACY));

        if (updatemapel) {
            //true
            tvMapel.setText(R.string.edit_mapel);
        }
    }

    public void saveDataMapel() {
        if (edtMapel.getText().toString().equals("")) {
            Toast.makeText(CreateMapelActivity.this, "isi mata pelajaran", Toast.LENGTH_SHORT).show();
        } else {
            Call<MapelModel> call = service.postdatamapel(edtMapel.getText().toString());
            call.enqueue(new Callback<MapelModel>() {
                @Override
                public void onResponse(Call<MapelModel> call, Response<MapelModel> response) {
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateMapelActivity.this, "berhasil menambah mapel", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMapelActivity.this, "gagal menambah mapel", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MapelModel> call, Throwable t) {
                    Log.e("CreateMapelActivity", "onFailure: ", t);
                }
            });
        }
    }

    public void updateDataMapel(String id, MapelModel mapel) {
        if (edtMapel.getText().toString().equals("")) {
            Toast.makeText(CreateMapelActivity.this, "isi mata pelajaran", Toast.LENGTH_SHORT).show();
        } else {
            Call<MapelModel> call = service.updatedatamapel(id, mapel);
            call.enqueue(new Callback<MapelModel>() {
                @Override
                public void onResponse(Call<MapelModel> call, Response<MapelModel> response) {
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateMapelActivity.this, "update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMapelActivity.this, "update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MapelModel> call, Throwable t) {
                    Log.e("CreateMapelActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearAll() {
        edtMapel.getText().clear();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //  this.finish();
            Intent moveIntent = new Intent(CreateMapelActivity.this, MapelActivity.class);
            startActivity(moveIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btninputmapel:
                if (edtMapel != null && !updatemapel) {
                    saveDataMapel();
                } else {
                    MapelModel mapel = new MapelModel();
                    mapel.setNamaMapel(edtMapel.getText().toString());
                    //mapel.getNamaMapel();
                    updateDataMapel(mapelid, mapel);
                }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


