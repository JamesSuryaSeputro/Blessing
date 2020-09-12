package com.example.blessing;

/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.blessing.Model.ImageModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.FilePath;
import com.example.blessing.Utils.PermissionUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDetailTryoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int READ_FILE_REQ = 42;
    private static final String TAG = CreateDetailTryoutActivity.class.getSimpleName();
    private static final String EXTRA_IDTO = "extra_idto";
    private static final String EXTRA_POSISI = "extra_posisi";
    private static final String EXTRA_BOOLEAN = "extra_boolean";
    private String imgPath = "";
    private EditText edtToFilename;
    private TextView tvPreviewTo;
    private Spinner spnrTo;
    private PhotoView toImgPreview;
    private long mLastClickTime = 0;
    private String[] arraySpinner = new String[]{"A", "B", "C", "D", "E"};
    private String idtryout;
    private Boolean updateDetailTryout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_detail_tryout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Simpan Detail Tryout </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        edtToFilename = findViewById(R.id.to_filename);
        Button btnChooseImgTo = findViewById(R.id.btnchooseimgto);
        btnChooseImgTo.setOnClickListener(this);
        Button btnSaveTo = findViewById(R.id.btnsaveto);
        btnSaveTo.setOnClickListener(this);
        toImgPreview = findViewById(R.id.to_img_preview);
        tvPreviewTo = findViewById(R.id.to_tvpreview);
        spnrTo = findViewById(R.id.to_spinner);

        idtryout = getIntent().getStringExtra(EXTRA_IDTO);
        int posisi = getIntent().getIntExtra(EXTRA_POSISI, 0);
        updateDetailTryout = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrTo.setAdapter(adapter);
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnchooseimgto:
                preventDoubleClick();
                if (ContextCompat.checkSelfPermission(CreateDetailTryoutActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ChooseFile();
                } else if (PermissionUtils.neverAskAgainSelected(CreateDetailTryoutActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(CreateDetailTryoutActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_FILE_REQ);
                }
                break;
            case R.id.btnsaveto:
                new CreateDetailTryoutActivity.UploadImg().execute();
                break;
        }
    }

    public void ChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, READ_FILE_REQ);
    }

    private void checkFileisHere() {
        if (imgPath.equals("")) {
            Toast.makeText(CreateDetailTryoutActivity.this, "Belum memilih gambar", Toast.LENGTH_SHORT).show();
        }
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
                    tvPreviewTo.setVisibility(View.VISIBLE);
                    toImgPreview.setVisibility(View.VISIBLE);

                    Uri filePath = data.getData();
                    imgPath = FilePath.getPath(CreateDetailTryoutActivity.this, filePath);
                    if (imgPath != null && !imgPath.equals("")) {
                        Glide.with(this)
                                .load(imgPath)
                                .into(toImgPreview);
                    }
                    edtToFilename.setText(imgPath);
                    Log.d(TAG, "onActivityResult: " + imgPath);
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

    public void saveDetailTryout() {
        if (imgPath.equals("") || imgPath.isEmpty()) {

        } else if (!imgPath.isEmpty()) {
            Log.d(TAG, "uploadFile: " + imgPath);
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody jawabanTo = RequestBody.create(spnrTo.getSelectedItem().toString(), MediaType.parse("text/plain"));
            RequestBody idTo = RequestBody.create(idtryout, MediaType.parse("text/plain"));
            MultipartBody.Part imgTo = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.postdatadetailtryout(imgTo, jawabanTo, idTo);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(CreateDetailTryoutActivity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateDetailTryoutActivity.this, "Failed", Toast.LENGTH_LONG).show();
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

    public void updateDetailTryout(String idDetailTryout) {
        if (imgPath.equals("") || imgPath.isEmpty()) {

        } else if (!imgPath.isEmpty()) {
            Log.d(TAG, "uploadFile: " + imgPath);
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody jawabanTo = RequestBody.create(spnrTo.getSelectedItem().toString(), MediaType.parse("text/plain"));
            MultipartBody.Part imgTo = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.updatedatadetailtryout(idDetailTryout, imgTo, jawabanTo);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(CreateDetailTryoutActivity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateDetailTryoutActivity.this, "Failed", Toast.LENGTH_LONG).show();
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
            Intent moveIntent = new Intent(CreateDetailTryoutActivity.this, DetailTryoutActivity.class);
            moveIntent.putExtra(EXTRA_IDTO, idtryout);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class UploadImg extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                checkFileisHere();
                if (!updateDetailTryout) {
                    saveDetailTryout();
                } else {
                    Log.d(TAG, "updatetryout: " + idtryout);
                    updateDetailTryout(idtryout);
                }
                runOnUiThread(() -> {
                    Log.d(TAG, "doInBackground: Upload Progress");
                    Toast.makeText(CreateDetailTryoutActivity.this, "Please Wait . . .", Toast.LENGTH_SHORT).show();
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
            imgPath = "";
            edtToFilename.getText().clear();
            tvPreviewTo.setVisibility(View.GONE);
            toImgPreview.setVisibility(View.GONE);

            Log.d(TAG, "onPreExecute: Selesai Upload");
        }
    }
}