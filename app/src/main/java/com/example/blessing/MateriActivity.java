package com.example.blessing;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Adapter.MateriAdapter;
import com.example.blessing.Adapter.OnClickItemContextMenuMateri;
import com.example.blessing.Model.MateriModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.PermissionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MateriActivity extends AppCompatActivity implements OnClickItemContextMenuMateri {

    private ArrayList<MateriModel> mLearningModelArrayList = new ArrayList<>();
    private MateriAdapter mAdapter;
    private long mLastClickTime = 0;
    private API service;
    private FloatingActionButton fab;
    private static final int READ_FILE_REQ = 42;
    public static final String TAG = MateriActivity.class.getSimpleName();
    public static final String EXTRA_MATERI = "extra_materi";
    public static final String EXTRA_MAPEL = "extra_mapel";
    public static final String EXTRA_NAMAMAPEL = "extra_namamapel";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    private String mapelid, namamapel;
    private long downloadID;
    private DownloadManager manager;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Mengambil id download yang diterima dengan broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Cek apakah broadcast yang diterima adalah file yang bergantung pada id download
            String status = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(status)) {
                if (downloadID == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    Cursor c = manager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Log.d(TAG, "onReceive: " + uriString);
//
                            FileProvider.getUriForFile(MateriActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(uriString));
                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                            pdfIntent.setDataAndType(Uri.parse(uriString), "application/pdf");
                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            try {
                                startActivity(pdfIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MateriActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Toast.makeText(MateriActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        service = RetrofitBuildCustom.getInstance().getService();

        namamapel = getIntent().getStringExtra(EXTRA_NAMAMAPEL);
        mapelid = getIntent().getStringExtra(EXTRA_MAPEL);

        TextView tvNamaMapel = findViewById(R.id.tvnamamapel);
        tvNamaMapel.setText(namamapel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Materi </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        fab = findViewById(R.id.fab_addmateri);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMoveActivity(mapelid);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.RV_materi);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //recyclerView.setAdapter(mAdapter);

        mAdapter = new MateriAdapter(MateriActivity.this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getMateriByMapel();
    }


    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("We need to download PDF, please permit the permission through settings screen."
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (READ_FILE_REQ == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    public void getMateriByMapel() {
        service.getmateribymapel(mapelid).enqueue(new Callback<List<MateriModel>>() {
            @Override
            public void onResponse(Call<List<MateriModel>> call, Response<List<MateriModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //disini data baru di masukin
                        mAdapter.updateData(response.body());
                        //tambah data disini ke array pakai for kalau mau langsung jadiin response.body jadi collectionlist
                        mLearningModelArrayList.addAll(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<List<MateriModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void download(String fileName) {
        String path = RetrofitBuildCustom.BASE_URL + "uploads/" + fileName;
        Log.d(TAG, "onReceive: " + path);
        manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        try {
            downloadID = manager.enqueue(request);
            checkDownloadStatus(MateriActivity.this, DownloadManager.STATUS_RUNNING);
        } catch (Exception e) {
            Log.d("DOWNLOADED_INFO", "startDownload =" + e.getMessage());
            e.printStackTrace();
            manager.remove(downloadID);
        }
    }

    public static boolean checkDownloadStatus(Context context, int status) {
        DownloadManager downloadManager = (DownloadManager)
                context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();

        query.setFilterByStatus(status);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            c.close();
            Log.i("DOWNLOAD_STATUS", String.valueOf(status));
            return true;
        }
        Log.i("AUTOMATION_DOWNLOAD", "DEFAULT");
        return false;
    }

    private void makeMoveActivity(String id) {
        Intent intent = new Intent(this, CreateMateriActivity.class);
        intent.putExtra(EXTRA_MAPEL, id);
        intent.putExtra(EXTRA_NAMAMAPEL, namamapel);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent moveIntent = new Intent(MateriActivity.this, MapelActivity.class);
            startActivity(moveIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteItem(String id, String judul) {
        Log.d(TAG, "onDeleteItem: " + id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Hapus materi " + judul + "?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Call Retrofit Delete
                service.deletedatamateri(id).enqueue(new Callback<MateriModel>() {
                    @Override
                    public void onResponse(Call<MateriModel> call, Response<MateriModel> response) {
                        getMateriByMapel();
                        Toast.makeText(MateriActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MateriModel> call, Throwable t) {
                        Toast.makeText(MateriActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String mId, String id, String judul) {
        Log.d(TAG, "onEditItem: " + id);
        Intent intent = new Intent(MateriActivity.this, CreateMateriActivity.class);
        intent.putExtra(EXTRA_MATERI, mId);
        intent.putExtra(EXTRA_MAPEL, id);
        intent.putExtra(EXTRA_NAMAMAPEL, namamapel);
        intent.putExtra("edittextitem", judul);
        intent.putExtra(EXTRA_BOOLEAN, true);
        startActivity(intent);
    }

    @Override
    public void onClickItem(String id, String fileName, String judul) {
        Log.d(TAG, "onClickItem: " + id);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (ContextCompat.checkSelfPermission(MateriActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            builder.setMessage("Download materi " + judul + "?");
            builder.setCancelable(false);
            builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onDownload: " + fileName);
                    download(fileName);
                }
            });
            builder.setNegativeButton("tidak", null);
            builder.show();
        } else if (PermissionUtils.neverAskAgainSelected(MateriActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            displayNeverAskAgainDialog();
        } else {
            ActivityCompat.requestPermissions(MateriActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_FILE_REQ);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onDownloadComplete);
    }
}

