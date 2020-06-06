package com.example.awareness.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.awareness.Constants.Organisation;
import com.example.awareness.R;
import com.example.awareness.ui.learningactivity.CreatorUs;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void contactUs(View view) {
    }

    public void shrushti(View view) {
        Intent intent = new Intent(this, AboutNgo.class);
        intent.putExtra(Organisation.ORGANISATION_NAME, Organisation.SHRUSHTI);
        startActivity(intent);
    }

    public void jumio(View view) {
        Intent intent = new Intent(this, AboutNgo.class);
        intent.putExtra(Organisation.ORGANISATION_NAME, Organisation.JUMIO);
        startActivity(intent);
    }

    public void developers(View view) {
        startActivity(new Intent(this, CreatorUs.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}