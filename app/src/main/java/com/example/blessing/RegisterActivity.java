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

import com.example.blessing.Model.RegisterModel;
import com.example.blessing.Service.RetrofitBuildCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Toast> toasts;
    private RetrofitBuildCustom retrofitBuildCustom;
    private EditText edtNama, edtEmail, edtPassword, edtConfirmpass;
    private ImageView imageView;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNama = findViewById(R.id.txtusername);
        edtEmail = findViewById(R.id.txtemail);
        edtPassword = findViewById(R.id.txtpassword);
        edtConfirmpass = findViewById(R.id.txtconfirmpass);
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
        retrofitBuildCustom = RetrofitBuildCustom.getInstance();
        Button btnRegister = findViewById(R.id.btnregister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnregister:
                if (edtNama != null && edtEmail != null && edtPassword != null && edtConfirmpass != null) {
                    CheckRegister();
                }
                break;
        }
    }


    private void CheckRegister() {
        //check edt tidak kosong
        if (edtNama.getText().toString().equals("") || edtEmail.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "data belum terisi", Toast.LENGTH_SHORT).show();
        } else if (!edtPassword.getText().toString().equals(edtConfirmpass.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "password tidak sama", Toast.LENGTH_SHORT).show();
        } else {
            Call<RegisterModel> call = retrofitBuildCustom.getService().checkregister(edtNama.getText().toString(),
                    edtEmail.getText().toString(),
                    edtPassword.getText().toString().trim(),
                    edtConfirmpass.getText().toString().trim());
            call.enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                        if (response.isSuccessful()) {
                            clearall();
                            Toast.makeText(RegisterActivity.this, "registrasi berhasil", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "registrasi gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                @Override
                public void onFailure(Call<RegisterModel> call, Throwable t) {
                    Log.e("RegisterActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearall() {
        edtNama.getText().clear();
        edtEmail.getText().clear();
        edtConfirmpass.getText().clear();
        edtPassword.getText().clear();
    }

}
