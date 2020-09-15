/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

package com.example.blessing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.blessing.Adapter.ImagePembahasanAdapter;
import com.example.blessing.Adapter.ImagePembahasanTryoutAdapter;
import com.example.blessing.Adapter.JawabanAdapter;
import com.example.blessing.Adapter.JawabanTryoutAdapter;
import com.example.blessing.Adapter.MateriAdapter;
import com.example.blessing.Adapter.OnClickImageChooser;
import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.Model.ImageModel;
import com.example.blessing.Model.RegisterModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.FilePath;
import com.example.blessing.Utils.PermissionUtils;
import com.example.blessing.Utils.Preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembahasanTryoutActivity extends AppCompatActivity implements OnClickImageChooser {
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private static final String TAG = PembahasanTryoutActivity.class.getSimpleName();
    private ImagePembahasanTryoutAdapter mAdapter;
    private JawabanTryoutAdapter mJawabanAdapter;
    private API service;
    private TextView tvJawabanTo, tvBelumAdaPembahasanTo;
    private static final int READ_FILE_REQ = 42;
    private String imgPath = "";
    private String idDetailTryout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembahasan_tryout);

        String judultryout = Preferences.getKeyJudulTryout(getBaseContext());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> " + judultryout + " </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        SnapHelper helper = new LinearSnapHelper();
        RecyclerView rvImageTo = findViewById(R.id.RV_imgpembahasanto);
        rvImageTo.setHasFixedSize(true);
        helper.attachToRecyclerView(rvImageTo);
        rvImageTo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImagePembahasanTryoutAdapter(new ArrayList<>(), PembahasanTryoutActivity.this);
        rvImageTo.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvJawabanTo.setVisibility(View.GONE);
                    tvBelumAdaPembahasanTo.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView rvJawabanTo = findViewById(R.id.RV_jawabanto);
        rvJawabanTo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mJawabanAdapter = new JawabanTryoutAdapter(new ArrayList<>(), PembahasanTryoutActivity.this);
        rvJawabanTo.setAdapter(mJawabanAdapter);
        mJawabanAdapter.setmListener(this);

        service = RetrofitBuildCustom.getInstance().getService();

        String idtryout = Preferences.getKeyIdTryout(getBaseContext());

        tvJawabanTo = findViewById(R.id.tvjawabanto);
        tvBelumAdaPembahasanTo = findViewById(R.id.tvbelumadapembahasanto);

        getPembahasanTryout(idtryout);
    }

    public void getPembahasanTryout(String id) {
        service.getdetailtryout(id).enqueue(new Callback<List<DetailTryoutModel>>() {
            @Override
            public void onResponse(Call<List<DetailTryoutModel>> call, Response<List<DetailTryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        mJawabanAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DetailTryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void updateImgJawaban(String iddetailtryout) {
        if (imgPath.equals("") || imgPath.isEmpty()) {

        } else if (!imgPath.isEmpty()) {
            Log.d(TAG, "uploadFile: " + imgPath);
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody idDetailTryout = RequestBody.create(iddetailtryout, MediaType.parse("text/plain"));
            MultipartBody.Part imgJawaban = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.updateimgjawabandetailtryout(imgJawaban, idDetailTryout);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(PembahasanTryoutActivity.this, "Berhasil update gambar jawaban", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PembahasanTryoutActivity.this, "Gagal update gambar jawaban", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    Log.e("onFailed", "onFailure: ", t);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(PembahasanTryoutActivity.this, TryoutActivity.class);
            intent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void ChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, READ_FILE_REQ);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[]
                                                   grantResults) {
        if (READ_FILE_REQ == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case READ_FILE_REQ:
                if (resultCode == RESULT_OK && data != null) {
                    Uri filePath = data.getData();
                    imgPath = FilePath.getPath(PembahasanTryoutActivity.this, filePath);
                    new PembahasanTryoutActivity.UploadJawaban().execute();
                }
                break;
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("We need to choose image, please permit the permission through settings screen."
                + "\n\nApp Permissions → Enable Storage");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private class UploadJawaban extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "idDetailTryout: " + idDetailTryout);
                updateImgJawaban(idDetailTryout);
                runOnUiThread(() -> {
                    Log.d(TAG, "doInBackground: Upload Progress");
                    Toast.makeText(PembahasanTryoutActivity.this, "Please Wait . . .", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: Mulai Upload");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPreExecute: Selesai Upload");
        }
    }

    @Override
    public void onClickItem(String id) {
        idDetailTryout = id;
        if (ContextCompat.checkSelfPermission(PembahasanTryoutActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ChooseFile();
        } else if (PermissionUtils.neverAskAgainSelected(PembahasanTryoutActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            displayNeverAskAgainDialog();
        } else {
            ActivityCompat.requestPermissions(PembahasanTryoutActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_FILE_REQ);
        }
    }
}