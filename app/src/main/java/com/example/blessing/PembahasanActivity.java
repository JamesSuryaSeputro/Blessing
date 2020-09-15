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

import com.example.blessing.Adapter.ImagePembahasanAdapter;
import com.example.blessing.Adapter.JawabanAdapter;
import com.example.blessing.Adapter.OnClickImageChooser;
import com.example.blessing.Model.ImageModel;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.FilePath;
import com.example.blessing.Utils.PermissionUtils;
import com.example.blessing.Utils.Preferences;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.blessing.Adapter.ImagePembahasanAdapter;
import com.example.blessing.Adapter.JawabanAdapter;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
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

public class PembahasanActivity extends AppCompatActivity implements OnClickImageChooser {
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    public static final String EXTRA_SOAL = "extra_soal";
    private static final String TAG = PembahasanActivity.class.getSimpleName();
    private ImagePembahasanAdapter mAdapter;
    private JawabanAdapter mJawabanAdapter;
    private API service;
    private String idsoal;
    private TextView tvJawaban, tvBelumAdaPembahasan;
    private static final int READ_FILE_REQ = 42;
    private String imgPath = "";
    private String idDetailTryout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembahasan);

        String namasoal = Preferences.getKeyNamaSoal(getBaseContext());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> " + namasoal + " </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        SnapHelper helper = new LinearSnapHelper();
        RecyclerView rvImage = findViewById(R.id.RV_imgpembahasan);
        rvImage.setHasFixedSize(true);
        helper.attachToRecyclerView(rvImage);
        rvImage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImagePembahasanAdapter(new ArrayList<>(), PembahasanActivity.this);
        rvImage.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvJawaban.setVisibility(View.GONE);
                    tvBelumAdaPembahasan.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView rvJawaban = findViewById(R.id.RV_jawaban);
        rvJawaban.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mJawabanAdapter = new JawabanAdapter(new ArrayList<>(), PembahasanActivity.this);
        rvJawaban.setAdapter(mJawabanAdapter);
        mJawabanAdapter.setmListener(this);

        service = RetrofitBuildCustom.getInstance().getService();
        idsoal = Preferences.getKeyIdSoal(getBaseContext());
        tvJawaban = findViewById(R.id.tvjawaban);
        tvBelumAdaPembahasan = findViewById(R.id.tvbelumadapembahasan);

        getPembahasan(idsoal);
    }

    public void getPembahasan(String id) {
        service.getdetailkuisbysoal(id).enqueue(new Callback<List<KuisModel>>() {
            @Override
            public void onResponse(Call<List<KuisModel>> call, Response<List<KuisModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        mJawabanAdapter.updateData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KuisModel>> call, Throwable t) {
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
            RequestBody idDetailKuis = RequestBody.create(iddetailtryout, MediaType.parse("text/plain"));
            MultipartBody.Part imgJawaban = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.updateimgjawabandetailkuis(imgJawaban, idDetailKuis);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(PembahasanActivity.this, "Berhasil update gambar jawaban", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PembahasanActivity.this, "Gagal update gambar jawaban", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(PembahasanActivity.this, SoalActivity.class);
            intent.putExtra(EXTRA_BOOLEAN, true);
            intent.putExtra(EXTRA_SOAL, idsoal);
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
                    imgPath = FilePath.getPath(PembahasanActivity.this, filePath);
                    new PembahasanActivity.UploadJawaban().execute();
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
                    Toast.makeText(PembahasanActivity.this, "Please Wait . . .", Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(PembahasanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ChooseFile();
        } else if (PermissionUtils.neverAskAgainSelected(PembahasanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            displayNeverAskAgainDialog();
        } else {
            ActivityCompat.requestPermissions(PembahasanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_FILE_REQ);
        }
    }
}