package com.example.blessing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import com.example.blessing.Model.KelasModel;
import com.example.blessing.Model.UploadModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.FilePath;
import com.example.blessing.Utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.blessing.MapelActivity.EXTRA_BOOLEAN;
import static com.example.blessing.MapelActivity.EXTRA_MAPEL;
import static com.example.blessing.MateriActivity.EXTRA_MATERI;
import static com.example.blessing.MateriActivity.EXTRA_NAMAMAPEL;

public class CreateMateriActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private TextView textView;
    private static final int READ_FILE_REQ = 42;
    private static final String TAG = CreateMateriActivity.class.getSimpleName();
    private String pdfPath = "";
    private Uri filePath;
    private String mapelid, materiid, namamapel;
    private Boolean updatemateri;
    private API service;
    private long mLastClickTime = 0;
    private String selectedId;
    private List<String> listSpinner = new ArrayList<>();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Simpan Materi</font>", Html.FROM_HTML_MODE_LEGACY));

        setContentView(R.layout.activity_create_materi);

        service = RetrofitBuildCustom.getInstance().getService();
        mapelid = getIntent().getStringExtra(EXTRA_MAPEL);
        materiid = getIntent().getStringExtra(EXTRA_MATERI);
        updatemateri = getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);
        namamapel = getIntent().getStringExtra(EXTRA_NAMAMAPEL);

        Button btnChoosePdf = findViewById(R.id.btnchoosepdf);
        Button btnUploadMateri = findViewById(R.id.btnuploadmateri);
        spinner = findViewById(R.id.spinnerkelasmateri);

        editText = findViewById(R.id.edtmateri);
        editText.setHint(getIntent().getStringExtra("edittextitem") == null ? "Input nama materi":getIntent().getStringExtra("edittextitem"));
        textView = findViewById(R.id.textviewmateri);

        btnChoosePdf.setOnClickListener(this);
        btnUploadMateri.setOnClickListener(this);

        getDataKelas();

        if (updatemateri){
            //true
            textView.setText(R.string.edit_materi);
            btnUploadMateri.setText(R.string.simpan);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnchoosepdf:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (ContextCompat.checkSelfPermission(CreateMateriActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ChooseFile();
                } else if (PermissionUtils.neverAskAgainSelected(CreateMateriActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(CreateMateriActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_FILE_REQ);
                }
                break;
            case R.id.btnuploadmateri:
                checkFileisHere();
                new UploadPdf().execute();
                break;
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("We need to upload PDF, please permit the permission through settings screen."
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //  this.finish();
            makeMoveActivity(mapelid);
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeMoveActivity(String id) {
        Intent intent = new Intent(CreateMateriActivity.this, MateriActivity.class);
        intent.putExtra(EXTRA_MAPEL,id);
        intent.putExtra(EXTRA_NAMAMAPEL, namamapel);
        this.startActivity(intent);
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

    public void ChooseFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, READ_FILE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case READ_FILE_REQ:
                if (resultCode == RESULT_OK && data != null) {
                    filePath = data.getData();
                    pdfPath = FilePath.getPath(CreateMateriActivity.this, filePath);
                    Toast.makeText(CreateMateriActivity.this, Environment.getExternalStorageDirectory() + "/" + pdfPath, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onActivityResult: " + pdfPath);
                }
                break;
        }
    }

    private void getDataKelas() {
        service.getdatakelas().enqueue(new Callback<List<KelasModel>>() {
            @Override
            public void onResponse(Call<List<KelasModel>> call, Response<List<KelasModel>> response) {
                List<KelasModel> kelasModelList = response.body();
                for (int i = 0; i < kelasModelList.size(); i++) {
                    listSpinner.add(kelasModelList.get(i).getKelas());
                }
                listSpinner.add(0, "- Pilih Kelas -");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateMateriActivity.this, android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemSelected: " + position);
                        if (position != 0) {
                            selectedId = String.valueOf(kelasModelList.get(position - 1).getIdKelas());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<KelasModel>> call, Throwable t) {

            }
        });
    }

    private class UploadPdf extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!updatemateri) {
                    uploadFile();
                } else {
                    updateDataMateri(materiid);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "doInBackground: Upload Progress");
                        Toast.makeText(CreateMateriActivity.this, "Uploading . . .", Toast.LENGTH_SHORT).show();
                    }
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
            pdfPath = "";
            Log.d(TAG, "onPreExecute: Selesai Upload");
        }
    }

    private boolean checkFileisHere(){
        if (pdfPath.equals("")){
            Toast.makeText(CreateMateriActivity.this, "File belum dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }else if (editText.getText().toString().equals("")){
            Toast.makeText(CreateMateriActivity.this, "nama materi masih kosong", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public void uploadFile() {
        Log.d("CreateMateriActivity", "button upload");
        if (editText.getText().toString().equals("")) {

        } else if (pdfPath.equals("") || pdfPath.isEmpty()){

        } else if (!pdfPath.isEmpty()) {
            Log.d(TAG, "uploadFile: "+ pdfPath);
            File file = new File(pdfPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody JudulMateri = RequestBody.create(editText.getText().toString(), MediaType.parse("text/plain"));
            RequestBody MapelId = RequestBody.create(mapelid, MediaType.parse("text/plain"));
            RequestBody SelectedId = RequestBody.create(selectedId, MediaType.parse("text/plain"));
            MultipartBody.Part NamaMateri = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<UploadModel> call = getResponse.uploadmateri(NamaMateri, JudulMateri, MapelId, SelectedId);
            call.enqueue(new Callback<UploadModel>() {
                @Override
                public void onResponse(Call<UploadModel> call, Response<UploadModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: "+response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            clearAll();
                            Toast.makeText(CreateMateriActivity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateMateriActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadModel> call, Throwable t) {
                    Log.e("onFailed", "onFailure: ", t);
                }
            });
        }
    }

    public void updateDataMateri(String id) {
        if (editText.getText().toString().equals("")) {

        } else if (pdfPath.equals("") || pdfPath.isEmpty()){

        } else if (!pdfPath.isEmpty()) {
            Log.d(TAG, "uploadFile: "+ pdfPath);
            File file = new File(pdfPath);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("*/*"));
            RequestBody JudulMateri = RequestBody.create(editText.getText().toString(), MediaType.parse("text/plain"));
            RequestBody SelectedId = RequestBody.create(selectedId, MediaType.parse("text/plain"));
            MultipartBody.Part NamaMateri = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            API getResponse = RetrofitBuildCustom.getRetrofit().create(API.class);
            Call<UploadModel> call = getResponse.updatedatamateri(id, NamaMateri, JudulMateri, SelectedId);
            call.enqueue(new Callback<UploadModel>() {
                @Override
                public void onResponse(Call<UploadModel> call, Response<UploadModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: "+response.body().getStatus());
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(CreateMateriActivity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateMateriActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadModel> call, Throwable t) {
                    Log.e("onFailed", "onFailure: ", t);
                }
            });
        }
    }

    private void clearAll() {
        editText.getText().clear();
    }
}
