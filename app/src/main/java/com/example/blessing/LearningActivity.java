package com.example.blessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.blessing.Model.LearningModel;

import java.util.ArrayList;

public class LearningActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private ArrayList<LearningModel> learningModelArrayList;
    private LearningAdapter adapter;
    private CustomRecyclerViewListener customRecyclerViewListener;
    private long mLastClickTime = 0;

    private String[] TextList = new String[]{"Matematika Dasar", "Bahasa Inggris", "Fisika", "Biologi", "Kimia"};
    private MenuItem btnAdd, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Mata Pelajaran");

        recyclerView = (RecyclerView) findViewById(R.id.text_learning);

        adapter = new LearningAdapter(textLearning(), this, new CustomRecyclerViewListener() {
            @Override
            public void onClickCustomItem(String id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (id) {
                    case "Matematika Dasar": {
                        makemoveactivity(TitleActivity.class);
                        break;
                    }
                    case "Bahasa Inggris": {
                        break;
                    }
                    case "Fisika": {
                        break;
                    }
                    case "Biologi": {
                        makemoveactivity(CreateMapelActivity.class);
                        break;
                    }
                    case "Kimia": {
                        break;
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        new Handler().post(new Runnable() {
                               @Override
                               public void run() {
                                   final View view = findViewById(R.id.btnadd);
                                   if (view != null) {
                                       view.setOnLongClickListener(new View.OnLongClickListener() {
                                           @Override
                                           public boolean onLongClick(View v) {

                                               // Do something...

                                               Toast.makeText(getApplicationContext(), "Long pressed", Toast.LENGTH_SHORT).show();
                                               return true;
                                           }
                                       });
                                   }
                               }
                           }
        );
        return super.onCreateOptionsMenu(menu);
    }
    private void makemoveactivity(Class activity){
        Intent intent = new Intent (this, activity);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btnadd:
//                Toast.makeText(this, "selected", Toast.LENGTH_SHORT);
                makemoveactivity(CreateMapelActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

