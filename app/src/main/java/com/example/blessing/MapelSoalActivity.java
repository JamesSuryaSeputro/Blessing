package com.example.blessing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.MapelSoalAdapter;
import com.example.blessing.Adapter.OnClickItemContextMenuMapelSoal;
import com.example.blessing.Model.JenjangModel;
import com.example.blessing.Model.MapelSoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapelSoalActivity extends AppCompatActivity implements OnClickItemContextMenuMapelSoal {
    private Spinner spinner;
    private ArrayList<MapelSoalModel> mLearningModelArrayList = new ArrayList<>();
    private List<String> listSpinner = new ArrayList<>();
    private API service;
    public static final String TAG = MapelSoalActivity.class.getSimpleName();
    public static final String EXTRA_IDJENJANG = "extra_idjenjang";
    public static final String EXTRA_NAMAJENJANG = "extra_namajenjang";
    public static final String EXTRA_MAPELSOAL = "extra_mapelsoal";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private MapelSoalAdapter mAdapter;
    private long mLastClickTime = 0;
    private FloatingActionButton fab;
    private String idRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel_soal);
        service = RetrofitBuildCustom.getInstance().getService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Mata Pelajaran Soal </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        fab = (FloatingActionButton) findViewById(R.id.fab_addsoal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapelSoalActivity.this, CreateMapelSoalActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RV_mapelsoal);
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
        //recyclerView.setAdapter(mAdapter);
        mAdapter = new MapelSoalAdapter(MapelSoalActivity.this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        spinner = findViewById(R.id.spinner);

        idRole = Preferences.getKeyUser(getBaseContext());
        if(idRole.equals("3")){
            fab.setVisibility(View.GONE);
        }

        //getData Recyclerview
        getDataMapelSoal();
        //getdata spinner
        getDataJenjang();
    }

    private void getDataJenjang() {
        service.getdatajenjang().enqueue(new Callback<List<JenjangModel>>() {
            @Override
            public void onResponse(Call<List<JenjangModel>> call, Response<List<JenjangModel>> response) {
                List<JenjangModel> jenjangModelList = response.body();
                //ini jalan di mainthread Ini GC calon calon
                for (int i = 0; i < jenjangModelList.size(); i++) {
                    listSpinner.add(jenjangModelList.get(i).getNamaJenjang());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MapelSoalActivity.this, android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //String mJenjang = parent.getItemAtPosition(position).toString();
                        String mJenjang = spinner.getSelectedItem().toString();
                        mAdapter.updateDataChangeFilterSchool(Integer.parseInt(jenjangModelList.get(position).getIdJenjang()));
                        //Toast.makeText(MapelSoalActivity.this, "Memilih jenjang " + mJenjang, Toast.LENGTH_SHORT).show();
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

    public void getDataMapelSoal() {
        service.getdatamapelsoal().enqueue(new Callback<List<MapelSoalModel>>() {
            @Override
            public void onResponse(Call<List<MapelSoalModel>> call, Response<List<MapelSoalModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "idmapelsoal " + mAdapter);
                        mAdapter.updateData(response.body());
                        mLearningModelArrayList.addAll(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MapelSoalModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(MapelSoalActivity.this, MainActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteItem(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Apakah kamu yakin?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                service.deletedatamapelsoal(id).enqueue(new Callback<MapelSoalModel>() {
                    @Override
                    public void onResponse(Call<MapelSoalModel> call, Response<MapelSoalModel> response) {
                        Log.d(TAG, "onResponse: " + id);
                        Toast.makeText(MapelSoalActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MapelSoalModel> call, Throwable t) {
                        Toast.makeText(MapelSoalActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String id, String nama) {
        Log.d(TAG, "onEditItem: " + id);
        Intent intent = new Intent(MapelSoalActivity.this, CreateMapelSoalActivity.class);
        intent.putExtra(EXTRA_MAPELSOAL, id);
        intent.putExtra(EXTRA_BOOLEAN, true);
        intent.putExtra("edittextitem", nama);
        startActivity(intent);
    }

    @Override
    public void onClickItem(String id, String jId, String namajenjang) {
        Log.d(TAG, "onClickItem: " + id);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent intent = new Intent(MapelSoalActivity.this, SoalActivity.class);
        intent.putExtra(EXTRA_MAPELSOAL, id);
        intent.putExtra(EXTRA_IDJENJANG, jId);
        intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}