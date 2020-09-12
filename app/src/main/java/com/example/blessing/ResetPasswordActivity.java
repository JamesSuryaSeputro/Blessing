/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

package com.example.blessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blessing.Model.LoginModel;
import com.example.blessing.Model.RegisterModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private ImageView imageView;
    private EditText edtEmail, edtNewPassword, edtConfirmNP;
    private Button btnConfirm;
    private RetrofitBuildCustom retrofitBuildCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        retrofitBuildCustom = RetrofitBuildCustom.getInstance();
        tilEmail = findViewById(R.id.til_email);
        edtEmail = findViewById(R.id.edt_email);
        edtNewPassword = findViewById(R.id.new_password);
        edtConfirmNP = findViewById(R.id.confirm_newpassword);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            checkEmail();
        });
        imageView = findViewById(R.id.rptologin);
        imageView.setOnClickListener(v -> {
            Intent moveIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(moveIntent);
        });
    }

    private void checkEmail() {
        if (edtEmail.getText().toString().equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Email masih kosong", Toast.LENGTH_SHORT).show();
        } else {
            Call<LoginModel> call = retrofitBuildCustom.getService().checkemail(edtEmail.getText().toString());
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful() && response.body().getStatus().equals("1")) {
                        edtEmail.getBackground().clearColorFilter();
                        tilEmail.setVisibility(View.GONE);
                        edtNewPassword.setVisibility(View.VISIBLE);
                        edtConfirmNP.setVisibility(View.VISIBLE);
                        tilEmail.setError(null);
                        resetPassword();
                    } else {
                        tilEmail.setErrorTextAppearance(R.style.ErrorAppearance);
                        tilEmail.setError("Email does not exist");
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }

    private void resetPassword() {
        if (!edtNewPassword.getText().toString().equals(edtConfirmNP.getText().toString())) {
            Toast.makeText(ResetPasswordActivity.this, "password tidak sama", Toast.LENGTH_SHORT).show();
        } else if (!edtNewPassword.getText().toString().equals("")) {
            Call<RegisterModel> call = retrofitBuildCustom.getService().resetpassword(
                    edtEmail.getText().toString().trim(),
                    edtNewPassword.getText().toString().trim());
            call.enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                    if (response.isSuccessful()) {
                        finish();
                        Toast.makeText(ResetPasswordActivity.this, "berhasil reset password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "gagal reset password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterModel> call, Throwable t) {
                    Log.e("RegisterActivity", "onFailure: ", t);
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}