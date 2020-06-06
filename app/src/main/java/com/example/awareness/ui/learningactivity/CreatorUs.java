package com.example.awareness.ui.learningactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awareness.AppTeamAdapter;
import com.example.awareness.R;
import com.example.awareness.ui.AboutIndividual;

import java.util.ArrayList;
import java.util.List;

public class CreatorUs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_us);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView developerRecyclerView = findViewById(R.id.developer_recyclerview);
        RecyclerView otherMembersRecyclerView = findViewById(R.id.other_members_recyclerview);

//        AboutAdapter aboutAdapter = new AboutAdapter(new ArrayList<AboutCategory>() {{
//            add(new AboutCategory("Core Developers", new ArrayList<AboutIndividual>() {{
//                add(new AboutIndividual("bhoomikbhamawat.eee18@iitbhu.ac.in", "Bhoomik Bhamawat", R.drawable.userdrawable, "8003044991"));
//                add(new AboutIndividual("kmonu9581@gmail.com", "Monu Kumar", R.drawable.userdrawable, "8505089989"));
//                add(new AboutIndividual("djsweet94@gmail.com", "Divya Jain", R.drawable.userdrawable, "9001847205"));
//                add(new AboutIndividual("aditijain8901@gmail.com", "Aditi Jain", R.drawable.userdrawable, "7340666132"));
//            }}, CreatorUs.this));
//
//           /* add(new AboutCategory("Design", new ArrayList<AboutIndividual>() {{
//                add(new AboutIndividual("150040007", "Soham Khadtare", "soham.jpg"));
//            }}, context));*/
//
//
//
//            /*add(new AboutCategory("Alumni", new ArrayList<AboutIndividual>() {{
//                add(new AboutIndividual("abhijit.tomar", "Anant Gowadiya", "tomar.jpg"));
//                add(new AboutIndividual(null, "Abhinav Singh", "bijoy.jpg"));
//                 }}, context));*/
//
//        }});

        List<AboutIndividual> developers = new ArrayList<AboutIndividual>() {
            {
                add(new AboutIndividual("bhoomikbhamawat.eee18@iitbhu.ac.in", "Bhoomik Bhamawat", R.drawable.userdrawable, "8003044991"));
                add(new AboutIndividual("kmonu9581@gmail.com", "Monu Kumar", R.drawable.userdrawable, "8505089989"));
            }
        };
        AppTeamAdapter developerAdapter = new AppTeamAdapter(this, developers);
        developerRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        developerRecyclerView.setAdapter(developerAdapter);


        List<AboutIndividual> otherMembers = new ArrayList<AboutIndividual>() {
            {
                add(new AboutIndividual("djsweet94@gmail.com", "Divya Jain", R.drawable.userdrawable, "9001847205"));
                add(new AboutIndividual("aditijain8901@gmail.com", "Aditi Jain", R.drawable.userdrawable, "7340666132"));
            }
        };
        AppTeamAdapter otherMembersAdapter = new AppTeamAdapter(this, otherMembers);
        otherMembersRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        otherMembersRecyclerView.setAdapter(otherMembersAdapter);
//        FlexboxLayoutManager manager = new FlexboxLayoutManager(CreatorUs.this, FlexDirection.ROW);
//        manager.setJustifyContent(JustifyContent.SPACE_EVENLY);
//        aboutRecyclerView.setLayoutManager(manager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}