package com.example.blessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.blessing.Model.LearningModel;

import java.util.ArrayList;

public class TitleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<LearningModel> learningModelArrayList;
    private LearningAdapter adapter;
    private CustomRecyclerViewListener customRecyclerViewListener;
    private long mLastClickTime = 0;

    private String[] TextList = new String[]{"Kombinatorik dan Peluang 1", "Luas Daerah Integral", "Transformasi Geometri",
            "Peluang 1", "Deret Hitung dan Ukur", "Deret Geometri Tak Hingga"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.blessing_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.text_learning);

        adapter = new LearningAdapter(textLearning(), this, new CustomRecyclerViewListener() {
            @Override
            public void onClickCustomItem(String id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (id) {
                    case "Kombinatorik dan Peluang 1": {
                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Soal%20Kombinatorik%20dan%20Binomial%201.pdf");
                        startActivity(intent);
                        break;
                    }
                    case "Luas Daerah Integral": {
                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Luas%20Daerah%20Integral.pdf");
                        startActivity(intent);
                        break;
                    }
                    case "Transformasi Geometri": {
                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/TRANSFORMASI%20GEOMETRI.pdf");
                        startActivity(intent);
                        break;
                    }
                    case "Peluang 1": {
                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Soal%20Peluang%201.pdf");
                        startActivity(intent);
                        break;
                    }
                    case "Deret Hitung dan Ukur": {
                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Luas%20Daerah%20Integral.pdf");
                        startActivity(intent);
                        break;
                    }
                    case "Deret Geometri Tak Hingga": {
                        finish();
                        Intent intent = new Intent (getApplicationContext(), PdfViewFixActivity.class);
                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Deret%20hitung%20dan%20ukur.pdf");
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

//    private void makemoveactivity(Class activity){
//        Intent intent = new Intent (this, activity);
//        this.startActivity(intent);
//    }

    private ArrayList<LearningModel> textLearning() {

        ArrayList<LearningModel> list = new ArrayList<>();
        for (int i = 0; i < TextList.length; i++) {
            LearningModel learningmodel = new LearningModel();
            learningmodel.setLearntext(TextList[i]);
            list.add(learningmodel);
        }
        return list;
    }
}
