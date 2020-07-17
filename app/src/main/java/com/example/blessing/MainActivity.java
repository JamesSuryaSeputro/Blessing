package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.blessing.Utils.Preferences;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
<<<<<<< HEAD
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private TextView tvNamaUser;
    private long mLastClickTime = 0;
    private String idRole;
=======
    private TextView tvNamaUser;
    private long mLastClickTime = 0;
    private String idRole;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_BOOLEAN = "extra_boolean";
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.blessing_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        idRole = Preferences.getKeyUser(getBaseContext());
        tvNamaUser = findViewById(R.id.tvnamauser);
        TextView tvMateri = findViewById(R.id.tvmateri);
        CardView cvMateri = findViewById(R.id.materi);
        CardView cvBankSoal = findViewById(R.id.banksoal);
        CardView cvTryout = findViewById(R.id.tryout);
        CardView cvPembahasan = findViewById(R.id.pembahasan);
        displaySharedPreferences();

        if (!idRole.isEmpty()) {
            Log.d(TAG, "idrole: " + idRole);
        }

        if (idRole.equals("3")) {
            tvMateri.setVisibility(View.GONE);
            cvMateri.setVisibility(View.GONE);
        }

        cvMateri.setOnClickListener(v -> {
            preventDoubleClick();
            makeMoveActivity(MapelActivity.class);
        });


        cvBankSoal.setOnClickListener(v -> {
            preventDoubleClick();
            makeMoveActivity(MapelSoalActivity.class);
        });

        cvTryout.setOnClickListener(v -> {
            preventDoubleClick();
            makeMoveActivity(TryoutActivity.class);
        });

        cvPembahasan.setOnClickListener(v -> {
            preventDoubleClick();
            makeMoveActivity(MenuHasilActivity.class);
        });
    }

    private void makeMoveActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    private void displaySharedPreferences() {
        tvNamaUser.setText(Preferences.getKeyNama(getBaseContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.registerguru);
        if(idRole.equals("3")){
            item.setVisible(false);
        } else if (idRole .equals("2")){
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.myProfile:
                makeMoveActivity(ProfileActivity.class);
                break;
            case R.id.registerguru:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra(EXTRA_BOOLEAN, true);
                startActivity(intent);
                break;
            case R.id.Logout:
                Preferences.clearLoggedinUser(getBaseContext());
                makeMoveActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        displaySharedPreferences();
        super.onResume();
    }
}