package com.example.blessing;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView = findViewById(R.id.profile_photo);
        Glide.with(this)
                .load("https://www.dicoding.com/images/small/avatar/2019031623320214325b5c427348f613ea68af1a8c8b8b.jpg")
                .into(circleImageView);
    }
}
