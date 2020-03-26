package com.example.blessing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.blessing.Utils.Preferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView nama = findViewById(R.id.nama);
        TextView email = findViewById(R.id.email);
//        if(Preferences.getKeyLoggedinUser(getBaseContext()) != null){
//            nama.setText(Preferences.getKeyNama(getBaseContext()));
//        }
//        else
//        {
//            // ini guna untuk set data ke preference kalau mau abis login berhasil baru set preferencenya berarti harus return 4 value status, id, email dan nama
//            Preferences.setKeyNama(getBaseContext(), "nama");
//            Preferences.setKeyEmail(getBaseContext(), "email");
//            finish();
//        }
//        tvEmail = findViewById(R.id.email);
//        tvEmail.setText(Preferences.getKeyEmail(getBaseContext()));

        nama.setText(Preferences.getKeyNama(getBaseContext()));
        Log.d("AK1", "onCreate: Profile "+ Preferences.getKeyNama(getBaseContext()));
        email.setText(Preferences.getKeyEmail(getBaseContext()));

        circleImageView = findViewById(R.id.profile_photo);
        Glide.with(this)
                .load("https://www.dicoding.com/images/small/avatar/2019031623320214325b5c427348f613ea68af1a8c8b8b.jpg")
                .into(circleImageView);
    }
}
