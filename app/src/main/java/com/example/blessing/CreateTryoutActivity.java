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

import com.example.blessing.Model.JenjangModel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Model.TryoutModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;
import static com.example.blessing.SoalActivity.EXTRA_SOAL;

public class CreateTryoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EXTRA_IDTO = "extra_idto";
    private EditText edtJudulTo, edtDeskripsi;
    private API service;
    private String idtryout;
    private Boolean updatetryout;
    private List<String> listSpinner = new ArrayList<>();
    private String selectedId;
    private long mLastClickTime = 0;
    private EditText edtTimer;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tryout);
        service = RetrofitBuildCustom.getInstance().getService();

        edtJudulTo = findViewById(R.id.edtjudulto);
        edtDeskripsi = findViewById(R.id.edtdeskripsi);
        TextView tvJudulTryout = findViewById(R.id.judultryout);
        Button button = findViewById(R.id.btnsimpanto);
        edtTimer = findViewById(R.id.timer);
        spinner = findViewById(R.id.spinnerjenjangto);
        button.setOnClickListener(this);

        updatetryout = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);
        idtryout = getIntent().getStringExtra(EXTRA_IDTO);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Simpan Tryout </font>", Html.FROM_HTML_MODE_LEGACY));

        getDataJenjang();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnsimpanto) {
            preventDoubleClick();
            if (edtJudulTo != null && !updatetryout) {
                saveDataTryout();
            } else {
                updateDataTryout(idtryout);
            }
        }
    }

    private void getDataJenjang() {
        service.getdatajenjang().enqueue(new Callback<List<JenjangModel>>() {
            @Override
            public void onResponse(Call<List<JenjangModel>> call, Response<List<JenjangModel>> response) {
                List<JenjangModel> jenjangModelList = response.body();
                for (int i = 0; i < jenjangModelList.size(); i++) {
                    listSpinner.add(jenjangModelList.get(i).getNamaJenjang());
                }
                listSpinner.add(0, "- Pilih Jenjang -");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateTryoutActivity.this, android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            selectedId = String.valueOf(jenjangModelList.get(position - 1).getIdJenjang());
                        }
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

    public void saveDataTryout() {
        if (edtJudulTo.getText().toString().equals("")) {
            Toast.makeText(CreateTryoutActivity.this, "isi judul TO", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Timer", "EDTTIMER: " + edtTimer.getText().toString());
            Call<TryoutModel> call = service.postdatatryout(edtJudulTo.getText().toString(), edtDeskripsi.getText().toString(), edtTimer.getText().toString(), selectedId);
            call.enqueue(new Callback<TryoutModel>() {
                @Override
                public void onResponse(Call<TryoutModel> call, Response<TryoutModel> response) {
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateTryoutActivity.this, "berhasil menambah TO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateTryoutActivity.this, "gagal menambah TO", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TryoutModel> call, Throwable t) {
                    Log.e("CreateMapelActivity", "onFailure: ", t);
                }
            });
        }
    }

    public void updateDataTryout(String id) {
        if (edtJudulTo.getText().toString().equals("")) {
            Toast.makeText(CreateTryoutActivity.this, "isi judul TO", Toast.LENGTH_SHORT).show();
        } else {
            Call<TryoutModel> call = service.updatedatatryout(id, edtJudulTo.getText().toString(), edtDeskripsi.getText().toString(), edtTimer.getText().toString(), selectedId);
            call.enqueue(new Callback<TryoutModel>() {
                @Override
                public void onResponse(Call<TryoutModel> call, Response<TryoutModel> response) {
                    if (response.isSuccessful()) {
                        clearAll();
                        Toast.makeText(CreateTryoutActivity.this, "update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateTryoutActivity.this, "update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TryoutModel> call, Throwable t) {
                    Log.e("CreateTryoutActivity", "onFailure: ", t);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            preventDoubleClick();
            Intent moveIntent = new Intent(CreateTryoutActivity.this, TryoutActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    private void clearAll() {
        edtJudulTo.getText().clear();
        edtDeskripsi.getText().clear();
        edtTimer.getText().clear();
    }
}