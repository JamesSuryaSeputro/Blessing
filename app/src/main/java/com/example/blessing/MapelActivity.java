package com.example.blessing;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.blessing.Model.LearningModel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapelActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<LearningModel> learningModelArrayList;
    private MapelAdapter adapter;
    private CustomRecyclerViewListener customRecyclerViewListener;
    private long mLastClickTime = 0;
    private API service;

    private String[] TextList = new String[]{"Matematika Dasar", "Bahasa Inggris", "Fisika", "Biologi", "Kimia"};
    private MenuItem btnAdd, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        service = RetrofitBuildCustom.getInstance().getService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Mata Pelajaran </font>"));

        recyclerView = (RecyclerView) findViewById(R.id.text_learning);

        adapter = new MapelAdapter(new ArrayList<MapelModel>(), this, new CustomRecyclerViewListener() {
            @Override
            public void onClickCustomItem(String id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (id) {
                    case "Matematika Dasar": {
                        makemoveactivity(TitleActivity.class);
                        break;
                    }
                    case "Bahasa Inggris": {
                        break;
                    }
                    case "Fisika": {
                        break;
                    }
                    case "Biologi": {
                        makemoveactivity(CreateMapelActivity.class);
                        break;
                    }
                    case "Kimia": {
                        break;
                    }
                    default: {
                        Log.d("JAMES", "onClickCustomItem: clicked " + id);
                        break;
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getdatamapel();
    }

    public void getdatamapel() {
        service.getdatamapel().enqueue(new Callback<List<MapelModel>>() {
            @Override
            public void onResponse(Call<List<MapelModel>> call, Response<List<MapelModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        adapter.updatedata(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MapelModel>> call, Throwable t) {

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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


