package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing.Model.LearningModel;
import com.example.blessing.Utils.Preferences;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<LearningModel> learningModelArrayList;
    private MainAdapter adapter;
    private TextView textView;
    private long mLastClickTime = 0;

    private int[] ImageList = new int[]{R.drawable.study, R.drawable.exam, R.drawable.elearning, R.drawable.onlinelearning};
    private String[] TextList = new String[]{"PELAJARAN", "UJIAN", "UTBK", "RUMUS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.blessing_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.item_learning);
        textView = (TextView) findViewById(R.id.riwayat);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMoveActivity(MateriActivity.class);
            }
        });

        adapter = new MainAdapter(itemLearning(), this, new CustomRecyclerViewListener() {
            @Override
            public void onClickCustomItem(String id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (id) {
                    case "PELAJARAN": {
                        Log.d("MainActivity", "BTN CLICKED");
                        makeMoveActivity(MapelActivity.class);
                        break;
                    }
                    case "UJIAN": {
                        break;
                    }
                    case "UTBK": {
                        break;
                    }
                    case "RUMUS": {
                        break;
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void makeMoveActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
    }

    private ArrayList<LearningModel> itemLearning() {

        ArrayList<LearningModel> list = new ArrayList<>();
        for (int i = 0; i < ImageList.length; i++) {
            LearningModel learningmodel = new LearningModel();
            learningmodel.setLearnimage(ImageList[i]);
            learningmodel.setLearntext(TextList[i]);
            list.add(learningmodel);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.myProfile:
                makeMoveActivity(ProfileActivity.class);
                break;
            case R.id.Logout:
                Preferences.clearLoggedinUser(getBaseContext());
                makeMoveActivity(LoginActivity.class);
                finish();
                break;
        }
    }
}