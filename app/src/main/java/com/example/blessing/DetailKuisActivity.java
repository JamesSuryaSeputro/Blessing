package com.example.blessing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.blessing.Adapter.NumberAdapter;
import com.example.blessing.Adapter.OnClickItemContextMenuNumber;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.Model.NilaiSoalModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.Preferences;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKuisActivity extends AppCompatActivity implements OnClickItemContextMenuNumber, View.OnClickListener {
    public static final String TAG = DetailKuisActivity.class.getSimpleName();
    private NumberAdapter mAdapter;
    private List<KuisModel> kuisModel = new ArrayList<>();
    private long mLastClickTime = 0;
    private String idsoal;
    private String idjenjang;
    private String namajenjang;
    private String idmapelsoal;
    private String namasoal;
    private String idnilaisoal;
    public static final String EXTRA_SOAL = "extra_soal";
    public static final String EXTRA_IDKUIS = "extra_idkuis";
    public static final String EXTRA_IDJENJANG = "extra_idjenjang";
    public static final String EXTRA_NAMAJENJANG = "extra_namajenjang";
    public static final String EXTRA_MAPELSOAL = "extra_mapelsoal";
    private static final String EXTRA_IDDETAILKUIS = "extra_detailkuis";
    public static final String EXTRA_BOOLEAN = "extra_boolean";
    public static final String EXTRA_NAMASOAL = "extra_namasoal";
    private static final String EXTRA_IDNILAISOAL = "extra_idnilaisoal";
    private FloatingActionButton fabExpand, fabNumber, fabAddKuis, fabRefresh;
    private API service;
    private TextView A, B, C, D, E, tvMulai;
    private PhotoView imgKuis;
    private LinearLayout optionLayout;
    private ProgressBar progressBar;
    boolean isOpen = false;
    private int noSoal = 0;
    private View previousView;
    private Menu menuItem;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kuis);

        idsoal = getIntent().getStringExtra(EXTRA_SOAL);
        idjenjang = getIntent().getStringExtra(EXTRA_IDJENJANG);
        namajenjang = getIntent().getStringExtra(EXTRA_NAMAJENJANG);
        idmapelsoal = getIntent().getStringExtra(EXTRA_MAPELSOAL);
        String idkuis = getIntent().getStringExtra(EXTRA_IDKUIS);
        namasoal = getIntent().getStringExtra(EXTRA_NAMASOAL);
        idnilaisoal = getIntent().getStringExtra(EXTRA_IDNILAISOAL);
        id = Preferences.getKeyId(getBaseContext());

        imgKuis = findViewById(R.id.img_kuis);
        progressBar = findViewById(R.id.progressBar);
        A = findViewById(R.id.opt_A);
        B = findViewById(R.id.opt_B);
        C = findViewById(R.id.opt_C);
        D = findViewById(R.id.opt_D);
        E = findViewById(R.id.opt_E);
        tvMulai = findViewById(R.id.tvmulai);
        optionLayout = findViewById(R.id.optionlayout);

        A.setOnClickListener(this);
        B.setOnClickListener(this);
        C.setOnClickListener(this);
        D.setOnClickListener(this);
        E.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Kuis </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        fabExpand = findViewById(R.id.fab_expand);
        fabNumber = findViewById(R.id.fab_addnumber);
        fabAddKuis = findViewById(R.id.fab_addkuis);
        fabRefresh = findViewById(R.id.fab_refresh);

        fabExpand.setOnClickListener(v -> {
            if (isOpen) {
                isOpen = false;
                fabNumber.hide();
                fabRefresh.hide();
            } else {
                isOpen = true;
                fabNumber.show();
                fabRefresh.show();
            }
        });

        fabNumber.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + idsoal);
            saveNumber(idsoal);
            getDetailKuisBySoal();
        });

        fabRefresh.setOnClickListener(v -> {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.RV_number);
        mAdapter = new NumberAdapter(DetailKuisActivity.this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvMulai.setVisibility(View.GONE);
                    Toast.makeText(DetailKuisActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        service = RetrofitBuildCustom.getInstance().getService();
        String idRole = Preferences.getKeyUser(getBaseContext());
        if (idRole.equals("3")) {
            fabExpand.setVisibility(View.GONE);
        }

        //nomor kuis
        getDetailKuisBySoal();
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    private void makeMoveActivity(String id, String sId, String mId, String jId, String namajenjang) {
        Intent intent = new Intent(DetailKuisActivity.this, CreateKuisActivity.class);
        Log.d(TAG, "makeMoveActivity: idkuis: " + id + " idsoal: " + sId + " mapelsoal: " + mId + " idjenjang: " + jId + " namajenjang: " + namajenjang);
        intent.putExtra(EXTRA_IDKUIS, id);
        intent.putExtra(EXTRA_SOAL, sId);
        intent.putExtra(EXTRA_MAPELSOAL, mId);
        intent.putExtra(EXTRA_IDJENJANG, jId);
        intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
        this.startActivity(intent);
    }

    private void makeMoveBackActivity(String mId, String jId, String namajenjang) {
        Intent intent = new Intent(DetailKuisActivity.this, SoalActivity.class);
        intent.putExtra(EXTRA_MAPELSOAL, mId);
        intent.putExtra(EXTRA_IDJENJANG, jId);
        intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
        this.startActivity(intent);
    }

    public void keluarKuis() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Yakin ingin keluar dari kuis?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeMoveBackActivity(idmapelsoal, idjenjang, namajenjang);
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                preventDoubleClick();
                keluarKuis();
                return (true);
            case R.id.submit:
                try {
                    submitKuis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuItem = menu;
        getMenuInflater().inflate(R.menu.toolbar, menu);
        hideSubmit();
        return true;
    }

    private void hideSubmit() {
        MenuItem item = menuItem.findItem(R.id.submit);
        item.setVisible(false);
    }

    private void showSubmit() {
        MenuItem item = menuItem.findItem(R.id.submit);
        item.setVisible(true);
    }

    //get nomor kuis
    public void getDetailKuisBySoal() {
        service.getdetailkuisbysoal(idsoal).enqueue(new Callback<List<KuisModel>>() {
            @Override
            public void onResponse(Call<List<KuisModel>> call, Response<List<KuisModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        kuisModel = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KuisModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void getDetailKuisByIdKuis(String id) {
        service.getdetailkuisbyidkuis(id).enqueue(new Callback<List<KuisModel>>() {
            @Override
            public void onResponse(Call<List<KuisModel>> call, Response<List<KuisModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "response: " + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KuisModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void saveNumber(String number) {
        Call<KuisModel> call = service.postdatakuis(number);
        call.enqueue(new Callback<KuisModel>() {
            @Override
            public void onResponse(Call<KuisModel> call, Response<KuisModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailKuisActivity.this, "berhasil menambahkan nomor", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailKuisActivity.this, "gagal menambahkan nomor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KuisModel> call, Throwable t) {
                Log.e("DetailKuisActivity", "onFailure: ", t);
            }
        });
    }

    public void saveNilaiSoal() {
        Call<NilaiSoalModel> call = service.postdatanilaisoal(idsoal, id, String.valueOf(countingScore(kuisModel)), String.valueOf(kuisModel.size()));
        call.enqueue(new Callback<NilaiSoalModel>() {
            @Override
            public void onResponse(Call<NilaiSoalModel> call, Response<NilaiSoalModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        idnilaisoal = response.body().getIdNilaisoal();
                    }
                    Toast.makeText(DetailKuisActivity.this, "Berhasil submit nilai", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailKuisActivity.this, "Gagal submit nilai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NilaiSoalModel> call, Throwable t) {
                Log.e("CreateMapelActivity", "onFailure: ", t);
            }
        });
    }

    public void updateNilaiSoal(String id, String nilaisoal, String jumlahsoal) {
        Call<NilaiSoalModel> call = service.updatedatanilaisoal(id, nilaisoal, jumlahsoal);
        call.enqueue(new Callback<NilaiSoalModel>() {
            @Override
            public void onResponse(Call<NilaiSoalModel> call, Response<NilaiSoalModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailKuisActivity.this, "Berhasil update nilai", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailKuisActivity.this, "Gagal update upnilai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NilaiSoalModel> call, Throwable t) {
                Log.e("CreateMapelActivity", "onFailure: ", t);
            }
        });
    }

    @Override
    public void onDeleteItem(String id) {
        Log.d(TAG, "onDeleteItem: " + id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Apakah kamu yakin?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                service.deletedatakuis(id).enqueue(new Callback<KuisModel>() {
                    @Override
                    public void onResponse(Call<KuisModel> call, Response<KuisModel> response) {
                        getDetailKuisBySoal();
                        Toast.makeText(DetailKuisActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<KuisModel> call, Throwable t) {
                        Toast.makeText(DetailKuisActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String id, String sId, String dkId) {
        Log.d(TAG, "onEditItem: " + dkId);
        if (dkId == null) {
            Toast.makeText(this, "Buat kuis dulu!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DetailKuisActivity.this, CreateKuisActivity.class);
            intent.putExtra(EXTRA_IDKUIS, id);
            intent.putExtra(EXTRA_SOAL, sId);
            intent.putExtra(EXTRA_IDDETAILKUIS, dkId);
            intent.putExtra(EXTRA_MAPELSOAL, idmapelsoal);
            intent.putExtra(EXTRA_IDJENJANG, idjenjang);
            intent.putExtra(EXTRA_NAMAJENJANG, namajenjang);
            intent.putExtra(EXTRA_BOOLEAN, true);
            startActivity(intent);
        }
    }

    @Override
    public void onClickItem(KuisModel kuisModel, int posisi) {
        Log.d(TAG, "onClickItem: " + kuisModel.getIdKuis());
        tvMulai.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        optionLayout.setVisibility(View.VISIBLE);
        fabNumber.hide();
        fabAddKuis.hide();
        fabRefresh.hide();
        LinearLayout linearLayout = findViewById(R.id.optionlayout);
        getDetailKuisByIdKuis(kuisModel.getIdKuis());
        noSoal = posisi;
        showSubmit();

        if (kuisModel.getIdDetailkuis() == null) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View view = linearLayout.getChildAt(i);
                view.setEnabled(false);
            }
        } else {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View view = linearLayout.getChildAt(i);
                view.setEnabled(true);
            }
        }

        if (kuisModel.getJawabanUser() == null) {
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (kuisModel.getJawabanUser().equals("A")) {
            A.setTextColor(Color.BLUE);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (kuisModel.getJawabanUser().equals("B")) {
            B.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (kuisModel.getJawabanUser().equals("C")) {
            C.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (kuisModel.getJawabanUser().equals("D")) {
            D.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (kuisModel.getJawabanUser().equals("E")) {
            E.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
        } else {
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        }

        fabExpand.setOnClickListener(v -> {
            if (isOpen) {
                isOpen = false;
                fabNumber.hide();
                fabAddKuis.hide();
                fabRefresh.hide();
            } else {
                isOpen = true;
                fabNumber.show();
                fabAddKuis.show();
                fabRefresh.show();
            }
        });

        fabAddKuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kuisModel.getIdDetailkuis() == null) {
                    DetailKuisActivity.this.makeMoveActivity(kuisModel.getIdKuis(), idsoal, idmapelsoal, idjenjang, namajenjang);
                } else {
                    Toast.makeText(DetailKuisActivity.this, "Kuis sudah dibuat", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate();

        String imgPath = RetrofitBuildCustom.BASE_URL;
        Glide.with(this)
                .load(imgPath + "/uploads/" + kuisModel.getImgPertanyaan())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imgKuis.setScaleType(ImageView.ScaleType.CENTER);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imgKuis.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .apply(options)
                .into(imgKuis);
    }

    @Override
    public void onClick(View v) {
        TextView previousText = (TextView) previousView;
        TextView curText = (TextView) v;
        // If this isn't selected, deselect the previous one (if any)
        if (previousText != null && previousText.isSelected()) {
            previousText.setSelected(false);
            previousText.setTextColor(Color.BLACK);
        }
        curText.setSelected(true);
        curText.setTextColor(Color.BLUE);
        previousView = v;
        switch (v.getId()) {
            case R.id.opt_A:
                kuisModel.get(noSoal).setJawabanUser("A");
                break;
            case R.id.opt_B:
                kuisModel.get(noSoal).setJawabanUser("B");
                break;
            case R.id.opt_C:
                kuisModel.get(noSoal).setJawabanUser("C");
                break;
            case R.id.opt_D:
                kuisModel.get(noSoal).setJawabanUser("D");
                break;
            case R.id.opt_E:
                kuisModel.get(noSoal).setJawabanUser("E");
                break;
        }
        Log.d(TAG, "Score: " + countingScore(kuisModel));
    }

    public void submitKuis() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Yakin sudah mau submit kuis?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scoreDialog();
                hideSubmit();
                if (idnilaisoal == null) {
                    saveNilaiSoal();
                } else {
                    Log.d(TAG, "idnilaisoal: " + idnilaisoal);
                    updateNilaiSoal(idnilaisoal, String.valueOf(countingScore(kuisModel)), String.valueOf(kuisModel.size()));
                }
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    public void scoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("RESULT");
        builder.setMessage("Skor kamu adalah " + countingScore(kuisModel) + " /" + kuisModel.size() + " pertanyaan");
        builder.setCancelable(false);
        builder.setPositiveButton("lihat pembahasan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DetailKuisActivity.this, PembahasanActivity.class);
                //intent.putExtra(EXTRA_BOOLEAN, true);
                Preferences.setKeyIdSoal(getBaseContext(), idsoal);
                Preferences.setKeyNamaSoal(getBaseContext(), namasoal);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    private int countingScore(List<KuisModel> kuisModelList) {
        int score = 0;
        //for ini yang bikin GC jadiin RxJava di IOThread||ComputingThread
        for (int i = 0; i < kuisModelList.size(); i++) {
            KuisModel kuisModel = kuisModelList.get(i);
            if (kuisModel.getJawaban() != null && kuisModel.getJawaban().equals(kuisModel.getJawabanUser())) {
                score += 1;
            }
        }
        return score;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
