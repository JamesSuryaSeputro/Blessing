package com.example.blessing;

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

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;

public class CreateKuisActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button btnChooseImg, btnSimpanKuis;
    private Spinner spinner;
    private TextView textView;
    private PhotoView imgPreview;
    private String imgPath = "";
    private Uri filePath;
    private long mLastClickTime = 0;
    private static final int READ_FILE_REQ = 42;
    private static final String TAG = CreateKuisActivity.class.getSimpleName();
    private String idsoal, namajenjang, idmapelsoal, idkuis, iddetailkuis;
    public static final String EXTRA_SOAL = "extra_soal";
    public static final String EXTRA_NAMAJENJANG = "extra_namajenjang";
    public static final String EXTRA_MAPELSOAL = "extra_mapelsoal";
    public static final String EXTRA_IDKUIS = "extra_idkuis";
    public static final String EXTRA_IDDETAILKUIS = "extra_detailkuis";
    private API service;
    private Boolean updatekuis;
    private String[] arraySpinner = new String[]{"A", "B", "C", "D", "E"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kuis);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Simpan Kuis </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        editText = findViewById(R.id.img_filename);
        btnChooseImg = findViewById(R.id.btnchooseimg);
        spinner = findViewById(R.id.spinnerjawaban);
        btnSimpanKuis = findViewById(R.id.btnsimpankuis);
        textView = findViewById(R.id.tvpreview);
        imgPreview = findViewById(R.id.img_preview);

        btnChooseImg.setOnClickListener(this);
        btnSimpanKuis.setOnClickListener(this);

        idsoal = getIntent().getStringExtra(EXTRA_SOAL);
        namajenjang = getIntent().getStringExtra(EXTRA_NAMAJENJANG);
        idmapelsoal = getIntent().getStringExtra(EXTRA_MAPELSOAL);
        updatekuis = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);
        idkuis = getIntent().getStringExtra(EXTRA_IDKUIS);
        iddetailkuis = getIntent().getStringExtra(EXTRA_IDDETAILKUIS);

        service = RetrofitBuildCustom.getInstance().getService();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
            case R.id.btnchooseimg:
                preventDoubleClick();
                if (ContextCompat.checkSelfPermission(CreateKuisActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ChooseFile();
                } else if (PermissionUtils.neverAskAgainSelected(CreateKuisActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(CreateKuisActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_FILE_REQ);
                }
                break;
            case R.id.btnsimpankuis:
                new UploadImg().execute();
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
            Toast.makeText(CreateKuisActivity.this, "Belum memilih gambar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
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
                    textView.setVisibility(View.VISIBLE);
                    imgPreview.setVisibility(View.VISIBLE);

                    filePath = data.getData();
                    imgPath = FilePath.getPath(CreateKuisActivity.this, filePath);
                    if (imgPath != null && !imgPath.equals("")) {
                        Glide.with(this)
                                .load(imgPath)
                                .into(imgPreview);
                    }
                    editText.setText(imgPath);
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

    private class UploadImg extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                checkFileisHere();
                if (!updatekuis) {
                    saveKuis();
                } else {
                    Log.d(TAG, "updatekuis: " + iddetailkuis);
                    updateKuis(iddetailkuis);
                }
                runOnUiThread(() -> {
                    Log.d(TAG, "doInBackground: Upload Progress");
                    Toast.makeText(CreateKuisActivity.this, "Please Wait . . .", Toast.LENGTH_SHORT).show();
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
            editText.getText().clear();
            textView.setVisibility(View.GONE);
            imgPreview.setVisibility(View.GONE);

            Log.d(TAG, "onPreExecute: Selesai Upload");
        }
    }

    public void saveKuis() {
        if (imgPath.equals("") || imgPath.isEmpty()) {

        } else if (!imgPath.isEmpty()) {
            Log.d(TAG, "uploadFile: " + imgPath);
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            Log.d(TAG, "Spinner kuis: " + spinner.getSelectedItem().toString());
            RequestBody jawaban = RequestBody.create(spinner.getSelectedItem().toString(), MediaType.parse("text/plain"));
            Log.d(TAG, "id kuis: " + idkuis);
            RequestBody idKuis = RequestBody.create(idkuis, MediaType.parse("text/plain"));
            Log.d(TAG, "pertanyaan: " + file.getName());
            MultipartBody.Part imgPertanyaan = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.postdatadetailkuis(imgPertanyaan, jawaban, idKuis);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(CreateKuisActivity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateKuisActivity.this, "Failed", Toast.LENGTH_LONG).show();
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

    public void updateKuis(String iddetailkuis) {
        if (imgPath.equals("") || imgPath.isEmpty()) {

        } else if (!imgPath.isEmpty()) {
            Log.d(TAG, "uploadFile: " + imgPath);
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody jawaban = RequestBody.create(spinner.getSelectedItem().toString(), MediaType.parse("text/plain"));
            MultipartBody.Part imgPertanyaan = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<ImageModel> call = getResponse.updatedatadetailkuis(iddetailkuis, imgPertanyaan, jawaban);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(CreateKuisActivity.this, "Update berhasil", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateKuisActivity.this, "Update gagal", Toast.LENGTH_LONG).show();
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

    private void makeMoveActivity(String id, String sId, String mId, String namajenjang) {
        Intent intent = new Intent(CreateKuisActivity.this, DetailKuisActivity.class);
        intent.putExtra(EXTRA_IDKUIS, id);
        intent.putExtra(EXTRA_SOAL, sId);
        intent.putExtra(EXTRA_MAPELSOAL, mId);
        intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            makeMoveActivity(idkuis, idsoal, idmapelsoal, namajenjang);
        }
        return super.onOptionsItemSelected(item);
    }
}