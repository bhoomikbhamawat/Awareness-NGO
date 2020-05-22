package com.example.awareness.ui.learningactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.awareness.Module;
import com.example.awareness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LearningActivity extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<Module> modules = new ArrayList<>();
    LearningAdapter learningAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        RecyclerView learningRecyclerView = findViewById(R.id.learning_recyclerview);
        learningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        learningAdapter = new LearningAdapter(this,modules);
        learningRecyclerView.setAdapter(learningAdapter);

        firestore.collection("modules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        modules.add(new Module(Integer.parseInt(document.getId()) , document.getString("Topic")));
                    }
                    Collections.sort(modules,new SortbyModuleNumber());
                    learningAdapter.notifyDataSetChanged();

                }else{
                    Log.e("TAGG", "Error getting modules",task.getException());
                }
            }
        });
    }
    class SortbyModuleNumber implements Comparator<Module>{

        @Override
        public int compare(Module a, Module b) {
            return a.getModuleNumber() - b.getModuleNumber();
        }
    }
}
