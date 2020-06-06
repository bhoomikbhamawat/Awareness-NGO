package com.example.awareness.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awareness.Constants.Organisation;
import com.example.awareness.R;

public class AboutNgo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ngo);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.name);
        TextView description = findViewById(R.id.description);

        Intent intent = getIntent();
        int organisation = intent.getIntExtra(Organisation.ORGANISATION_NAME, -1);

        if (organisation == Organisation.SHRUSHTI) {
            image.setImageResource(R.drawable.shrushti);
            name.setText(R.string.shrushti);
            description.setText(R.string.description_shrushti);

        } else if (organisation == Organisation.JUMIO) {
            image.setImageResource(R.drawable.jumio_logo);
            name.setText(R.string.jumio);
            description.setText(R.string.description_jumio);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}