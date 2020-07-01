package com.bhoomik.Vardaan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bhoomik.Vardaan.R;

public class CertificateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}