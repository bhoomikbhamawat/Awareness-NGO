package com.bhoomik.Vardaan.ui.aboutactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bhoomik.Vardaan.R;
import com.bhoomik.Vardaan.model.AboutIndividual;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class CreatorUs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_us);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        AboutAdapter aboutAdapter = new AboutAdapter(new ArrayList<AboutCategory>() {{
            add(new AboutCategory("Core Developers", new ArrayList<AboutIndividual>() {{
                add(new AboutIndividual("bhoomikbhamawat.eee18@iitbhu.ac.in", "Bhoomik Bhamawat", R.drawable.bhoomik, "8003044991"));
                add(new AboutIndividual("kmonu9581@gmail.com", "Monu Kumar", R.drawable.mk, "8505089989"));
                add(new AboutIndividual("divyabhamawat16@gmail.com", "Divya Jain", R.drawable.divya, "9001847205"));
                add(new AboutIndividual("aditijain8901@gmail.com", "Aditi Jain", R.drawable.aditi, "7340666132"));
            }}, CreatorUs.this));


        }});

        RecyclerView aboutRecyclerView = findViewById(R.id.about_recycler_view);
        aboutRecyclerView.setAdapter(aboutAdapter);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(CreatorUs.this, FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        aboutRecyclerView.setLayoutManager(manager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}