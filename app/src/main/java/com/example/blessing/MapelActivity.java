package com.example.blessing;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.blessing.Adapter.CustomRecyclerViewListener;
import com.example.blessing.Adapter.MapelAdapter;
import com.example.blessing.Adapter.OnItemClickListener;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapelActivity extends AppCompatActivity {
    //BEFORE : ArrayList<MapelModel> mLearningModelArrayList
    //INGAT JIKA INGIN BUAT MASUKIN DATA KE ARRAYLIST HARUS BUAT NEW ARRAYLIST
    private ArrayList<MapelModel> mLearningModelArrayList = new ArrayList<>();
    private MapelAdapter mAdapter;
    private OnItemClickListener onItemClickListener;
    private long mLastClickTime = 0;
    private API service;
    public static final String EXTRA_MAPEL = "MapelDetail";
    public static final String TAG = MapelActivity.class.getSimpleName();

    private String[] TextList = new String[]{"Matematika Dasar", "Bahasa Inggris", "Fisika", "Biologi", "Kimia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        service = RetrofitBuildCustom.getInstance().getService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Mata Pelajaran </font>"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.text_learning);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        //  ini salah karena lu declare bahwa mLearningModelArrayList Sedangkan itu masih NULL
        //  BEFORE: mAdapter = new MapelAdapter(MapelActivity.this, mLearningModelArrayList);
        mAdapter = new MapelAdapter(MapelActivity.this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        //BEFORE: mAdapter.setOnItemClickListener(MapelActivity.this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //item yang di click
                Log.d(TAG, "onItemClickListener: " + position);
                //Jangan pakai
                // BEFORE: Intent intent = new Intent(this, MateriActivity.class); <-- this tidak kurang menejelaskan bisa aja this itu yang lain
                Intent intent = new Intent(MapelActivity.this, MateriActivity.class);
                Log.d("", "onItemClickListener: " + position);
                //Ini Objectnya Kosong mLearningModelArrayList nggak pernah di isi data jadinya kosong diisi pass saat ngambil data dari server
                MapelModel clickMapel = mLearningModelArrayList.get(position);

                intent.putExtra(EXTRA_MAPEL, clickMapel.getNamaMapel());
                startActivity(intent);
            }
        });

        getDataMapel();
    }

    public void getDataMapel() {
        service.getdatamapel().enqueue(new Callback<List<MapelModel>>() {
            @Override
            public void onResponse(Call<List<MapelModel>> call, Response<List<MapelModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //disini data baru di masukin
                        mAdapter.updatedata(response.body());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        new Handler().post(new Runnable() {
                               @Override
                               public void run() {
                                   final View view = findViewById(R.id.btnadd);
                                   if (view != null) {
                                       view.setOnLongClickListener(new View.OnLongClickListener() {
                                           @Override
                                           public boolean onLongClick(View v) {

                                               // Do something...

                                               Toast.makeText(getApplicationContext(), "Long pressed", Toast.LENGTH_SHORT).show();
                                               return true;
                                           }
                                       });
                                   }
                               }
                           }
        );
        return super.onCreateOptionsMenu(menu);
    }

    private void makemoveactivity(Class activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnadd:
//                Toast.makeText(this, "selected", Toast.LENGTH_SHORT);
                makemoveactivity(CreateMapelActivity.class);
                return true;
        }
        int id = item.getItemId();

        if (id == android.R.id.home) {
//            this.finish();
            Intent moveIntent = new Intent(MapelActivity.this, MainActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}


