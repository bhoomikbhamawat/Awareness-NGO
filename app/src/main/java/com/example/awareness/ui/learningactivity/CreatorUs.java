package com.example.awareness.ui.learningactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.awareness.R;
import com.example.awareness.ui.AboutAdapter;
import com.example.awareness.ui.AboutCategory;
import com.example.awareness.ui.AboutIndividual;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class CreatorUs extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_us);



        AboutAdapter aboutAdapter = new AboutAdapter(new ArrayList<AboutCategory>() {{
            add(new AboutCategory("Core Developers", new ArrayList<AboutIndividual>() {{
                add(new AboutIndividual("bhoomikbhamawat.eee18@iitbhu.ac.in", "Bhoomik Bhamawat", R.drawable.userdrawable,"8003044991"));
                add(new AboutIndividual("monukumar.min18@iitbhu.ac.in", "Monu Kumar",  R.drawable.userdrawable,"8505089989"));
                add(new AboutIndividual("djsweet94@gmail.com", "Divya Jain",  R.drawable.userdrawable,"9001847205"));
                add(new AboutIndividual("aditijain8901@gmail.com", "Aditi Jain",  R.drawable.userdrawable,"7340666132"));
            }}, CreatorUs.this));

           /* add(new AboutCategory("Design", new ArrayList<AboutIndividual>() {{
                add(new AboutIndividual("150040007", "Soham Khadtare", "soham.jpg"));
            }}, context));*/



            /*add(new AboutCategory("Alumni", new ArrayList<AboutIndividual>() {{
                add(new AboutIndividual("abhijit.tomar", "Anant Gowadiya", "tomar.jpg"));
                add(new AboutIndividual(null, "Abhinav Singh", "bijoy.jpg"));
                 }}, context));*/

        }});

        RecyclerView aboutRecyclerView = findViewById(R.id.about_recycler_view);
        aboutRecyclerView.setAdapter(aboutAdapter);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(CreatorUs.this, FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        aboutRecyclerView.setLayoutManager(manager);
    }
}