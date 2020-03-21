package com.example.blessing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PdfViewFixActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view_fix);
        ButterKnife.bind(this);
        webview.getSettings().setJavaScriptEnabled(true);
        String filename = getIntent().getStringExtra("link");
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+filename);
        finish();
    }
}
