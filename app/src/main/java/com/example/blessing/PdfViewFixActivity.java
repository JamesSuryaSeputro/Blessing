/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

package com.example.blessing;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.webkit.WebView;

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
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
        finish();
    }
}
