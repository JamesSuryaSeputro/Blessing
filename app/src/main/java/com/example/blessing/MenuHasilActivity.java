package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Objects;

public class MenuHasilActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    public static final String EXTRA_BOOLEAN = "extra_boolean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pembahasan);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CardView cvPembahasanBankSoal = findViewById(R.id.cv_pembahasanbanksoal);
        CardView cvPembahasanTryout = findViewById(R.id.cv_pembahasantryout);
        CardView cvNilaiBankSoal = findViewById(R.id.cv_nilaibanksoal);
        CardView cvNilaiTryout = findViewById(R.id.cv_nilaitryout);

        cvPembahasanBankSoal.setOnClickListener(v -> {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuHasilActivity.this, SoalActivity.class);
            moveIntent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(moveIntent);
        });

        cvPembahasanTryout.setOnClickListener(v -> {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuHasilActivity.this, TryoutActivity.class);
            moveIntent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(moveIntent);
        });

        cvNilaiBankSoal.setOnClickListener(v -> {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuHasilActivity.this, NilaiSoalActivity.class);
            startActivity(moveIntent);
        });

        cvNilaiTryout.setOnClickListener(v -> {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuHasilActivity.this, NilaiTryoutActivity.class);
            startActivity(moveIntent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuHasilActivity.this, MainActivity.class);
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
}