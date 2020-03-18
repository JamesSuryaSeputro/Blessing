package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blessing.Model.LoginModel;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Toast> toasts;
    private RetrofitBuildCustom retrofitBuildCustom;
    private EditText EdtNama, EdtEmail, EdtPassword, EdtConfirmpass;
    private ImageView imageView;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EdtNama = findViewById(R.id.txtusername);
        EdtEmail = findViewById(R.id.txtemail);
        EdtPassword = findViewById(R.id.txtpassword);
        EdtConfirmpass = findViewById(R.id.txtconfirmpass);
        imageView = findViewById(R.id.tologin);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent moveIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(moveIntent);
            }
        });

        retrofitBuildCustom = new RetrofitBuildCustom();
        Button btnRegister = findViewById(R.id.btnregister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnregister:
                if (EdtNama != null && EdtEmail != null && EdtPassword != null && EdtConfirmpass != null) {
                    CheckRegister();
                }
                break;
        }
    }


    private void CheckRegister() {
        //check edt tidak kosong
        if (EdtNama.getText().toString().equals("") || EdtEmail.getText().toString().equals("") || EdtPassword.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "data belum terisi", Toast.LENGTH_SHORT).show();
        } else if (!EdtPassword.getText().toString().equals(EdtConfirmpass.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "password tidak sama", Toast.LENGTH_SHORT).show();
        } else {
            Call<LoginModel> call = retrofitBuildCustom.generateAPI().checkregister(EdtNama.getText().toString(),
                    EdtEmail.getText().toString(),
                    EdtPassword.getText().toString().trim(),
                    EdtConfirmpass.getText().toString().trim());
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            clearall();
                            Toast.makeText(RegisterActivity.this, "registrasi berhasil", Toast.LENGTH_SHORT).show();
                        } else if (response.body().getStatus() == 0)
                            Toast.makeText(RegisterActivity.this, "registrasi gagal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Log.e("RegisterActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearall() {
        EdtNama.getText().clear();
        EdtEmail.getText().clear();
        EdtConfirmpass.getText().clear();
        EdtPassword.getText().clear();
    }

}
