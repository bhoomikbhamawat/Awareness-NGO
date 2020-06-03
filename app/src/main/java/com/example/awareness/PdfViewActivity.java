package com.example.awareness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        Intent intent = getIntent();
        final int mode = intent.getIntExtra("mode1", 1);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("ppt"+ mode + ".pdf")
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


}