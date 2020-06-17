package com.example.blessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;

import com.example.blessing.Utils.Preferences;

import java.util.Objects;

public class MenuPembahasanActivity extends AppCompatActivity {
    private CardView cvMenuBankSoal, cvMenuTryout;
    private long mLastClickTime = 0;
    public static final String EXTRA_BOOLEAN = "extra_boolean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pembahasan);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cvMenuBankSoal = findViewById(R.id.cv_menubanksoal);
        cvMenuTryout = findViewById(R.id.cv_menutryout);

        cvMenuBankSoal.setOnClickListener(v -> {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuPembahasanActivity.this, SoalActivity.class);
            moveIntent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(moveIntent);
        });

        cvMenuTryout.setOnClickListener(v -> {
            preventDoubleClick();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            preventDoubleClick();
            Intent moveIntent = new Intent(MenuPembahasanActivity.this, MainActivity.class);
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