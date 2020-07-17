package com.example.blessing;

import android.annotation.SuppressLint;
import android.os.Bundle;
<<<<<<< HEAD
import android.webkit.WebView;
=======
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

//loading PDF file from remote url or online document
public class PdfViewFixActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view_fix);
        ButterKnife.bind(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        String filename = getIntent().getStringExtra("link");
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+filename);
        finish();
    }
}
