package com.example.blessing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.MapelAdapter;
import com.example.blessing.Adapter.OnClickItemContextMenuMapel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapelActivity extends AppCompatActivity implements OnClickItemContextMenuMapel {
    public static final String TAG = MapelActivity.class.getSimpleName();
    public static final String EXTRA_MAPEL = "extra_mapel";
    public static final String EXTRA_NAMAMAPEL = "extra_namamapel";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private ArrayList<MapelModel> mLearningModelArrayList = new ArrayList<>();
    private MapelAdapter mAdapter;
    private API service;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        service = RetrofitBuildCustom.getInstance().getService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Mata Pelajaran </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        fab = (FloatingActionButton) findViewById(R.id.fab_addmapel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapelActivity.this, CreateMapelActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RV_mapel);
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
        mAdapter = new MapelAdapter(MapelActivity.this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        String idRole = Preferences.getKeyUser(getBaseContext());

        getDataMapel();

    }

    public void getDataMapel() {
        service.getdatamapel().enqueue(new Callback<List<MapelModel>>() {
            @Override
            public void onResponse(Call<List<MapelModel>> call, Response<List<MapelModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //disini data baru di masukin
                        mAdapter.updateData(response.body());
                        //tambah data disini ke array pakai for kalau mau langsung jadiin response.body jadi collectionlist
                        mLearningModelArrayList.addAll(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<List<MapelModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(MapelActivity.this, MainActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteItem(String id) {
        Log.d(TAG, "onDeleteItem: " + id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Hapus mata pelajaran?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Call Retrofit Delete
                service.deletedatamapel(id).enqueue(new Callback<MapelModel>() {
                    @Override
                    public void onResponse(Call<MapelModel> call, Response<MapelModel> response) {
                        Log.d(TAG, "onResponse: " + id);
                        getDataMapel();
                        Toast.makeText(MapelActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MapelModel> call, Throwable t) {
                        Toast.makeText(MapelActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String id, String nama) {
        Log.d(TAG, "onEditItem: "+id);
        Intent intent = new Intent(MapelActivity.this, CreateMapelActivity.class);
        intent.putExtra(EXTRA_MAPEL, id);
        intent.putExtra(EXTRA_BOOLEAN, true);
        //put extra itu kayak MAP
        //Key, value
        intent.putExtra("edittextitem", nama);
        startActivity(intent);
    }

    @Override
    public void onClickItem(String id, String namamapel) {
        Log.d(TAG, "onClickItem: "+id);
        Intent intent = new Intent(MapelActivity.this, MateriActivity.class);
        intent.putExtra(EXTRA_MAPEL, id);
        intent.putExtra(EXTRA_NAMAMAPEL, namamapel);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}


