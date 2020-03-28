package com.example.blessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blessing.Model.MapelModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMapelActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMapel;
    private API service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mapel);
        edtMapel = findViewById(R.id.mapel);
        service = RetrofitBuildCustom.getInstance().getService();
        Button btnCreateMapel = findViewById(R.id.btninputmapel);
        btnCreateMapel.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void postDataMapel() {
        if (edtMapel.getText().toString().equals("")) {
            Toast.makeText(CreateMapelActivity.this, "isi mata pelajaran dulu", Toast.LENGTH_SHORT).show();
        } else {
            Call<MapelModel> call = service.postdatamapel(edtMapel.getText().toString());
            call.enqueue(new Callback<MapelModel>() {
                @Override
                public void onResponse(Call<MapelModel> call, Response<MapelModel> response) {
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateMapelActivity.this, "berhasil tambah mata pelajaran", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateMapelActivity.this, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                if (edtMapel != null) {
                    postDataMapel();
                }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


