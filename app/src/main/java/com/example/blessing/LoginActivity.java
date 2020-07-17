package com.example.blessing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blessing.Model.LoginModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RetrofitBuildCustom retrofitBuildCustom;
    private TextInputEditText EdtEmail, EdtPassword;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView btnTextView = findViewById(R.id.toregister);
        btnTextView.setOnClickListener(this);

        EdtEmail = findViewById(R.id.txtemail);
        EdtPassword = findViewById(R.id.txtpassword);

        retrofitBuildCustom = RetrofitBuildCustom.getInstance();
        Button btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toregister:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(moveIntent);
                break;
            case R.id.btnlogin:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (!EdtEmail.getText().equals("") && !EdtPassword.getText().equals("")) {
                    Log.d("LoginActivity", "BTNLOGIN CLICKED");
                    checkLogin();
                }
                break;
        }
    }

    private void checkLogin() {
        API api = retrofitBuildCustom.getService();
        Call<LoginModel> call = api.checklogin(EdtEmail.getText().toString(), EdtPassword.getText().toString());
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("1")) {
                    Preferences.setStatusLogin(getBaseContext(), true);
                    Log.d("AK1", "onResponse: " + response.body().toString());
                    Log.d("AK1", "onResponse: " + response.body().getNama() + " " + response.body().getEmail());
                    Preferences.setKeyId(getBaseContext(), response.body().getId());
                    Preferences.setKeyNama(getBaseContext(), response.body().getNama());
                    Preferences.setKeyEmail(getBaseContext(), response.body().getEmail());
                    Preferences.setKeyRole(getBaseContext(), response.body().getRoleid());
                    Preferences.setKeyRolename(getBaseContext(), response.body().getNamaRole());
                    Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(moveIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "login gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("LoginActivity", "onFailure: ", t);
            }
        });
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
