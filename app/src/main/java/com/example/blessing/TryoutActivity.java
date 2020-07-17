package com.example.blessing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.example.blessing.Adapter.OnClickItemContextMenuTryout;
import com.example.blessing.Adapter.TryoutAdapter;
import com.example.blessing.Model.TryoutModel;
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

public class TryoutActivity extends AppCompatActivity implements OnClickItemContextMenuTryout {
    public static final String EXTRA_IDTO = "extra_idto";
    public static final String EXTRA_JUDUL = "extra_judul";
    public static final String EXTRA_TIMER = "extra_timer";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private static final String TAG = TryoutActivity.class.getSimpleName();
    private TryoutAdapter mAdapter;
    private long mLastClickTime = 0;
    private API service;
    private Boolean pembahasanTryout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Tryout </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        FloatingActionButton fab = findViewById(R.id.fab_addto);
        fab.setOnClickListener(view -> {
            preventDoubleClick();
            Intent intent = new Intent(TryoutActivity.this, CreateTryoutActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.RV_tryout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TryoutAdapter(new ArrayList<>(), TryoutActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        service = RetrofitBuildCustom.getInstance().getService();
        pembahasanTryout = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);

        String idRole = Preferences.getKeyUser(getBaseContext());

        if(idRole.equals("3")){
            fab.setVisibility(View.GONE);
        }

        if (pembahasanTryout) {
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Pembahasan Tryout </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        getDataTryout();
    }

    public void getDataTryout() {
        service.getdatatryout().enqueue(new Callback<List<TryoutModel>>() {
            @Override
            public void onResponse(Call<List<TryoutModel>> call, Response<List<TryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            preventDoubleClick();
            if(!pembahasanTryout) {
                Intent moveIntent = new Intent(TryoutActivity.this, MainActivity.class);
                startActivity(moveIntent);
            }
            else{
                Intent moveIntent = new Intent(TryoutActivity.this, MenuHasilActivity.class);
                startActivity(moveIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onDeleteItem(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TryoutActivity.this, R.style.AlertDialogCustom);
        builder.setMessage("Hapus tryout ini?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", (dialog, which) -> service.deletedatatryout(id).enqueue(new Callback<TryoutModel>() {
            @Override
            public void onResponse(Call<TryoutModel> call, Response<TryoutModel> response) {
                Log.d(TAG, "idto: " + id);
                getDataTryout();
                Toast.makeText(TryoutActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TryoutModel> call, Throwable t) {
                Toast.makeText(TryoutActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
            }
        }));
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String id) {
        preventDoubleClick();
        Intent intent = new Intent(TryoutActivity.this, CreateTryoutActivity.class);
        intent.putExtra(EXTRA_IDTO, id);
        intent.putExtra(EXTRA_BOOLEAN, true);
        startActivity(intent);
    }

    @Override
    public void onClickItem(String id, String judul, String timer) {
        preventDoubleClick();
        if (!pembahasanTryout) {
            Intent intent = new Intent(TryoutActivity.this, DetailTryoutActivity.class);
            intent.putExtra(EXTRA_IDTO, id);
            intent.putExtra(EXTRA_JUDUL, judul);
            intent.putExtra(EXTRA_TIMER, timer);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TryoutActivity.this, PembahasanTryoutActivity.class);
            Preferences.setKeyIdTryout(getBaseContext(), id);
            Preferences.setKeyJudulTryout(getBaseContext(), judul);
            startActivity(intent);
        }
    }
}