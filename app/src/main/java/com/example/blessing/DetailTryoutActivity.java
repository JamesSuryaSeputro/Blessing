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
import androidx.lifecycle.Observer;
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
import com.example.blessing.Adapter.DetailTryoutAdapter;
import com.example.blessing.Adapter.OnClickItemContextMenuDetailTryout;
import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.Model.NilaiTryoutModel;
import com.example.blessing.Service.API;
import com.example.blessing.Service.RetrofitBuildCustom;
import com.example.blessing.Utils.CustomCountDownTimer;
import com.example.blessing.Utils.Preferences;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTryoutActivity extends AppCompatActivity implements OnClickItemContextMenuDetailTryout, View.OnClickListener {
    public static final String TAG = DetailTryoutActivity.class.getSimpleName();
    private TextView tvMulaiTo, A, B, C, D, E, tvTimer;
    private PhotoView imgTo;
    private LinearLayout optionLayoutTo;
    private ProgressBar progressBarTo;
    private API service;
    private DetailTryoutAdapter mAdapter;
    private View previousView;
    private Menu menuItem;
    private String id;
    private List<DetailTryoutModel> mDetailTryoutModel = new ArrayList<>();
    private long mLastClickTime = 0;
    private String idtryout, judul, idnilaitryout;
    private static final String EXTRA_IDTO = "extra_idto";
    private static final String EXTRA_JUDUL = "extra_judul";
    private static final String EXTRA_BOOLEAN = "extra_boolean";
    private static final String EXTRA_TIMER = "extra_timer";
    private static final String EXTRA_IDNILAITRYOUT = "extra_idnilaitryout";
    private int noSoal = 0;
    private boolean isQuizRunning = false;
    private CustomCountDownTimer customCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tryout);

        idtryout = getIntent().getStringExtra(EXTRA_IDTO);
        judul = getIntent().getStringExtra(EXTRA_JUDUL);
        String timer = getIntent().getStringExtra(EXTRA_TIMER);
        idnilaitryout = getIntent().getStringExtra(EXTRA_IDNILAITRYOUT);
        id = Preferences.getKeyId(getBaseContext());

        tvMulaiTo = findViewById(R.id.tvmulaito);
        A = findViewById(R.id.ans_A);
        B = findViewById(R.id.ans_B);
        C = findViewById(R.id.ans_C);
        D = findViewById(R.id.ans_D);
        E = findViewById(R.id.ans_E);
        imgTo = findViewById(R.id.img_to);
        optionLayoutTo = findViewById(R.id.optionlayoutto);
        progressBarTo = findViewById(R.id.progressbarto);
        FloatingActionButton fabAddTo = findViewById(R.id.fab_addto);
        String idRole = Preferences.getKeyUser(getBaseContext());
        tvTimer = findViewById(R.id.tvtimer);
        String waktu = getText(R.string.waktu) + timer + getText(R.string.menit);
        String defWaktu = getText(R.string.waktu) + "10" + getText(R.string.menit);

        if (timer != null) {
            tvTimer.setText(waktu);
            customCountDownTimer = new CustomCountDownTimer(TimeUnit.MINUTES.toMillis(Long.parseLong(timer)));
        } else {
            tvTimer.setText(defWaktu);
            customCountDownTimer = new CustomCountDownTimer(TimeUnit.MINUTES.toMillis(10));
        }

        A.setOnClickListener(this);
        B.setOnClickListener(this);
        C.setOnClickListener(this);
        D.setOnClickListener(this);
        E.setOnClickListener(this);

        fabAddTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMoveActivity(idtryout);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Tryout </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        customCountDownTimer.getCountDownTimer().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvTimer.setText(s);
            }
        });

        customCountDownTimer.getIsStillRunning().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (!aBoolean) {
                    isQuizRunning = aBoolean;
                    //ini artinya waktu udah abis
                    hideSubmit();

                    if (idnilaitryout == null) {
                        saveNilaiTryout();
                    } else {
                        updateNilaiTryout(idnilaitryout, String.valueOf(countingScore(mDetailTryoutModel)), String.valueOf(mDetailTryoutModel.size()));
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailTryoutActivity.this, R.style.AlertDialogCustom);
                    builder.setTitle("WAKTU HABIS!");
                    builder.setMessage("Skor kamu adalah " + countingScore(mDetailTryoutModel) + " /" + mDetailTryoutModel.size() + " pertanyaan");
                    builder.setCancelable(false);
                    builder.setPositiveButton("lihat pembahasan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DetailTryoutActivity.this, PembahasanTryoutActivity.class);
                            intent.putExtra(EXTRA_BOOLEAN, true);
                            Preferences.setKeyIdTryout(getBaseContext(), idtryout);
                            Preferences.setKeyJudulTryout(getBaseContext(), judul);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DetailTryoutActivity.this, TryoutActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });

        service = RetrofitBuildCustom.getInstance().getService();

        RecyclerView recyclerView = findViewById(R.id.RV_tonumber);
        mAdapter = new DetailTryoutAdapter(new ArrayList<>(), DetailTryoutActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmListener(this);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    tvMulaiTo.setVisibility(View.GONE);
                    Toast.makeText(DetailTryoutActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (idRole.equals("3")) {
            fabAddTo.setVisibility(View.GONE);
        }

        getDetailTryout(idtryout);

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

    public void getDetailTryout(String id) {
        service.getdetailtryout(id).enqueue(new Callback<List<DetailTryoutModel>>() {
            @Override
            public void onResponse(Call<List<DetailTryoutModel>> call, Response<List<DetailTryoutModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.updateData(response.body());
                        mDetailTryoutModel = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DetailTryoutModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void saveNilaiTryout() {
        Call<NilaiTryoutModel> call = service.postdatanilaitryout(idtryout, id, String.valueOf(countingScore(mDetailTryoutModel)), String.valueOf(mDetailTryoutModel.size()));
        call.enqueue(new Callback<NilaiTryoutModel>() {
            @Override
            public void onResponse(Call<NilaiTryoutModel> call, Response<NilaiTryoutModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("AK1", "onResponse: " + response.body().getIdNilaitryout());
                        idnilaitryout = response.body().getIdNilaitryout();
                    }
                    Toast.makeText(DetailTryoutActivity.this, "Berhasil submit nilai", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailTryoutActivity.this, "Gagal submit nilai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NilaiTryoutModel> call, Throwable t) {
                Log.e("CreateMapelActivity", "onFailure: ", t);
            }
        });
    }

    public void updateNilaiTryout(String id, String nilaitryout, String jumlahtryout) {
        Call<NilaiTryoutModel> call = service.updatedatanilaitryout(id, nilaitryout, jumlahtryout);
        call.enqueue(new Callback<NilaiTryoutModel>() {
            @Override
            public void onResponse(Call<NilaiTryoutModel> call, Response<NilaiTryoutModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailTryoutActivity.this, "Berhasil update nilai", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailTryoutActivity.this, "Gagal update upnilai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NilaiTryoutModel> call, Throwable t) {
                Log.e("CreateMapelActivity", "onFailure: ", t);
            }
        });
    }

    private void preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    private void makeMoveActivity(String id) {
        Intent intent = new Intent(DetailTryoutActivity.this, CreateDetailTryoutActivity.class);
        intent.putExtra(EXTRA_IDTO, id);
        this.startActivity(intent);
    }

    public void keluarTryout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Yakin ingin keluar?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DetailTryoutActivity.this, TryoutActivity.class);
                intent.putExtra(EXTRA_IDTO, idtryout);
                startActivity(intent);
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
                keluarTryout();
                return (true);
            case R.id.submit:
                try {
                    submitTryout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteItem(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Apakah kamu yakin?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                service.deletedatadetailtryout(id).enqueue(new Callback<DetailTryoutModel>() {
                    @Override
                    public void onResponse(Call<DetailTryoutModel> call, Response<DetailTryoutModel> response) {
                        getDetailTryout(idtryout);
                        Toast.makeText(DetailTryoutActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DetailTryoutModel> call, Throwable t) {
                        Toast.makeText(DetailTryoutActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    @Override
    public void onEditItem(String id) {
        Intent intent = new Intent(DetailTryoutActivity.this, CreateDetailTryoutActivity.class);
        intent.putExtra(EXTRA_IDTO, id);
        intent.putExtra(EXTRA_BOOLEAN, true);
        startActivity(intent);
    }

    @Override
    public void onClickItem(DetailTryoutModel detailTryoutModel, int posisi) {

        if (!isQuizRunning) {
            customCountDownTimer.start();
            isQuizRunning = true;
        }

        showSubmit();
        tvMulaiTo.setVisibility(View.GONE);
        progressBarTo.setVisibility(View.VISIBLE);
        optionLayoutTo.setVisibility(View.VISIBLE);
        noSoal = posisi;

        if (detailTryoutModel.getUserAnswer() == null) {
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (detailTryoutModel.getUserAnswer().equals("A")) {
            A.setTextColor(Color.BLUE);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (detailTryoutModel.getUserAnswer().equals("B")) {
            B.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (detailTryoutModel.getUserAnswer().equals("C")) {
            C.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            D.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (detailTryoutModel.getUserAnswer().equals("D")) {
            D.setTextColor(Color.BLUE);
            A.setTextColor(Color.BLACK);
            B.setTextColor(Color.BLACK);
            C.setTextColor(Color.BLACK);
            E.setTextColor(Color.BLACK);
        } else if (detailTryoutModel.getUserAnswer().equals("E")) {
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

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate();

        String imgPath = RetrofitBuildCustom.BASE_URL;
        Glide.with(this)
                .load(imgPath + "/uploads/" + detailTryoutModel.getImgTo())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBarTo.setVisibility(View.GONE);
                        imgTo.setScaleType(ImageView.ScaleType.CENTER);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarTo.setVisibility(View.GONE);
                        imgTo.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .apply(options)
                .into(imgTo);
    }

    @Override
    public void onClick(View v) {
        TextView previousText = (TextView) previousView;
        TextView curText = (TextView) v;
        if (previousText != null && previousText.isSelected()) {
            previousText.setSelected(false);
            previousText.setTextColor(Color.BLACK);
        }
        curText.setSelected(true);
        curText.setTextColor(Color.BLUE);
        previousView = v;
        switch (v.getId()) {
            case R.id.ans_A:
                mDetailTryoutModel.get(noSoal).setUserAnswer("A");
                break;
            case R.id.ans_B:
                mDetailTryoutModel.get(noSoal).setUserAnswer("B");
                break;
            case R.id.ans_C:
                mDetailTryoutModel.get(noSoal).setUserAnswer("C");
                break;
            case R.id.ans_D:
                mDetailTryoutModel.get(noSoal).setUserAnswer("D");
                break;
            case R.id.ans_E:
                mDetailTryoutModel.get(noSoal).setUserAnswer("E");
                break;
        }
        Log.d(TAG, "userScore : " + countingScore(mDetailTryoutModel));
    }

    public void submitTryout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Yakin sudah mau submit?");
        builder.setCancelable(false);
        builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scoreDialog();
                hideSubmit();
                customCountDownTimer.cancel();
                isQuizRunning = false;
                Log.d(TAG, "idnilaitryout: " + idnilaitryout);
                if (idnilaitryout == null) {
                    saveNilaiTryout();
                } else {
                    updateNilaiTryout(idnilaitryout, String.valueOf(countingScore(mDetailTryoutModel)), String.valueOf(mDetailTryoutModel.size()));
                }
            }
        });
        builder.setNegativeButton("tidak", null);
        builder.show();
    }

    public void scoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("RESULT");
        builder.setMessage("Skor kamu adalah " + countingScore(mDetailTryoutModel) + " /" + mDetailTryoutModel.size() + " pertanyaan");
        builder.setCancelable(false);
        builder.setPositiveButton("lihat pembahasan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideSubmit();
                Intent intent = new Intent(DetailTryoutActivity.this, PembahasanTryoutActivity.class);
                // intent.putExtra(EXTRA_BOOLEAN, true);
                Preferences.setKeyIdTryout(getBaseContext(), idtryout);
                Preferences.setKeyJudulTryout(getBaseContext(), judul);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    private int countingScore(List<DetailTryoutModel> detailTryoutModelList) {
        int score = 0;
        for (int i = 0; i < detailTryoutModelList.size(); i++) {
            DetailTryoutModel detailTryoutModel = detailTryoutModelList.get(i);
            if (detailTryoutModel.getJawabanTo() != null && detailTryoutModel.getJawabanTo().equals(detailTryoutModel.getUserAnswer())) {
                score += 1;
            }
        }
        return score;
    }
}