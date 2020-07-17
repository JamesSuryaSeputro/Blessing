package com.example.blessing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
import android.os.SystemClock;
=======
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Html;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD

import com.bumptech.glide.Glide;
import com.example.blessing.Model.LoginModel;
=======
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.blessing.Model.LoginModel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Model.RegisterModel;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private long mLastClickTime = 0;
    private API service;
    private EditText newName;
    private TextView nama;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nama = findViewById(R.id.nama);
        TextView email = findViewById(R.id.email);
        TextView rolename = findViewById(R.id.role);
        CircleImageView circleImageView = findViewById(R.id.img_role);
        newName = findViewById(R.id.newname);
        newName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (newName.getRight() - newName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        newName.setFocusableInTouchMode(true);
                        return true;
                    }
                }
                return false;
            }
        });
        newName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        Button btnSimpan = findViewById(R.id.btnnewname);
        btnSimpan.setOnClickListener(this);
        service = RetrofitBuildCustom.getInstance().getService();
        ImageView imageView = findViewById(R.id.backtomain);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent moveIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(moveIntent);
            }
        });

        Log.d("TEST1", "onResponse: " + Preferences.getKeyNama(getBaseContext()));
        nama.setText(Preferences.getKeyNama(getBaseContext()));
        Log.d("AK1", "onCreate: Profile " + Preferences.getKeyNama(getBaseContext()));
        email.setText(Preferences.getKeyEmail(getBaseContext()));
        rolename.setText(Preferences.getKeyRolename(getBaseContext()));
        id = Preferences.getKeyId(getBaseContext());
        String idRole = Preferences.getKeyUser(getBaseContext());

        if (idRole.equals("1")) {
            Glide.with(this)
                    .load(getImage("admin"))
                    .into(circleImageView);
        } else if (idRole.equals("2")) {
            Glide.with(this)
                    .load(getImage("teacher"))
                    .into(circleImageView);
        } else {
            Glide.with(this)
                    .load(getImage("student"))
                    .into(circleImageView);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void updateDataUser(String id, LoginModel loginModel) {
        if (newName.getText().toString().equals("")) {
            Toast.makeText(ProfileActivity.this, "nama belum diisi", Toast.LENGTH_SHORT).show();
        } else {
            Call<LoginModel> call = service.updatedatauser(id, loginModel);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            clearAll();
                            Preferences.setKeyNama(getBaseContext(),response.body().getNama());
                            Log.d("TEST2", "onResponse: " + Preferences.getKeyNama(getBaseContext()));
                            nama.setText(Preferences.getKeyNama(getBaseContext()));
                            Toast.makeText(ProfileActivity.this, "nama berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "nama gagal diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Log.e("CreateMapelActivity", "onFailure: ", t);
                }
            });
        }
    }

    private void clearAll() {
        newName.getText().clear();
    }

    @Override
    public void onClick(View v) {
        LoginModel loginModel = new LoginModel();
        loginModel.setNama(newName.getText().toString());
        updateDataUser(id, loginModel);
    }
}
