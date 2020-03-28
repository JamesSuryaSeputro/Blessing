package com.example.blessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.example.blessing.Adapter.CustomRecyclerViewListener;
import com.example.blessing.Adapter.MapelAdapter;
import com.example.blessing.Adapter.OnItemClickListener;
import com.example.blessing.Model.LearningModel;

import java.util.ArrayList;

public class MateriActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ArrayList<LearningModel> learningModelArrayList;
    private MapelAdapter adapter;
    private CustomRecyclerViewListener customRecyclerViewListener;
    private long mLastClickTime = 0;

    private String[] TextList = new String[]{"Kombinatorik dan Peluang 1", "Luas Daerah Integral", "Transformasi Geometri",
            "Peluang 1", "Deret Hitung dan Ukur", "Deret Geometri Tak Hingga"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Materi </font>"));

        recyclerView = (RecyclerView) findViewById(R.id.text_learning);

        Intent intent = getIntent();
//        String MapelDetail = intent.getStringExtra(EXTRA_MAPEL);

//        adapter = new LearningAdapter(textLearning(), this, new CustomRecyclerViewListener() {
//            @Override
//            public void onItemClick(String id) {
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();
//                switch (id) {
//                    case "Kombinatorik dan Peluang 1": {
//                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Soal%20Kombinatorik%20dan%20Binomial%201.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                    case "Luas Daerah Integral": {
//                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Luas%20Daerah%20Integral.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                    case "Transformasi Geometri": {
//                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/TRANSFORMASI%20GEOMETRI.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                    case "Peluang 1": {
//                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Soal%20Peluang%201.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                    case "Deret Hitung dan Ukur": {
//                        Intent intent = new Intent (TitleActivity.this, PdfViewFixActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Luas%20Daerah%20Integral.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                    case "Deret Geometri Tak Hingga": {
//                        finish();
//                        Intent intent = new Intent (getApplicationContext(), PdfViewFixActivity.class);
//                        intent.putExtra("link","https://blessingbimbel.000webhostapp.com/Assets/Deret%20hitung%20dan%20ukur.pdf");
//                        startActivity(intent);
//                        break;
//                    }
//                }
//            }
//        });
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
