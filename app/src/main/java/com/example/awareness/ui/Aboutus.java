package com.example.awareness.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "bhoomikbhamawat.eee18@iitbhu.ac.in"));
        startActivity(Intent.createChooser(emailIntent, "email"));

    } public void rateus(View view) {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
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