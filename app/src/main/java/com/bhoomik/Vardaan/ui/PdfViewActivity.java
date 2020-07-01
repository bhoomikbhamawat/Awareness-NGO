package com.bhoomik.Vardaan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bhoomik.Vardaan.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        final int mode = intent.getIntExtra("mode1", 1);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("ppt" + mode + ".pdf")
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}