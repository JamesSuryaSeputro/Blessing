/*
 * Copyright (c) 2020 James Surya Seputro.
 * All rights reserved.
 */

package com.example.blessing;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;
import java.util.Objects;

//loading PDF file from local storage
public class PdfViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {
    private static final int READ_FILE_REQ = 42;
    public ProgressDialog pDialog;
    private int pageNumber = 0;
    private String pdfFilename;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdfView);
        initDialog();
    }

    private void displayFromFile(File file) {

        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        pdfFilename = getFilename(uri);

        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    private String getFilename(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tree, String s) {
        for (PdfDocument.Bookmark b : tree) {

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), s + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFilename, page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(PdfViewActivity.this, "Unable to Load File", Toast.LENGTH_SHORT).show();
    }

    protected void initDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }
}
