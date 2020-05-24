package com.example.awareness.ui.learningactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.awareness.Constants;
import com.example.awareness.Module;
import com.example.awareness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.awareness.Constants.User.ACCESS_MODULE;
import static com.example.awareness.Constants.User.ACCESS_QUESTION;
import static com.example.awareness.Constants.User.PROGRESS_LINK;
import static com.example.awareness.Constants.User.PROGRESS_PDF;
import static com.example.awareness.Constants.User;

public class LearningActivity extends AppCompatActivity {

    List<Module> modules = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static LearningAdapter learningAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @SuppressLint("StaticFieldLeak")
    public static View quizBottomSheet;
    @SuppressLint("StaticFieldLeak")
    public static BottomSheetDialog quizBottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        RecyclerView learningRecyclerView = findViewById(R.id.learning_recyclerview);
         quizBottomSheet = getLayoutInflater().inflate(R.layout.test_layout,null,false);
        quizBottomSheetDialog = new BottomSheetDialog(this);
        quizBottomSheetDialog.setContentView(quizBottomSheet);


        learningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        learningAdapter = new LearningAdapter(this,modules);
        learningRecyclerView.setAdapter(learningAdapter);

//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
//                .build();
//        firestore.setFirestoreSettings(settings);
        // Todo: userID?
        String userId = "0123456789";
        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document!= null){
                                User.accessModule =  Objects.requireNonNull(document.getLong(ACCESS_MODULE)).intValue();
                                User.accessQuestion = Objects.requireNonNull(document.getLong(ACCESS_QUESTION)).intValue();
                                User.progressLink = document.getBoolean(PROGRESS_LINK);
                                User.progressPdf = document.getBoolean(PROGRESS_PDF);
                            }
                        }
                    }
                });

        firestore.collection("modules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        Map<String,String> attachments = new HashMap<>();
                        attachments.put("Link",document.getString("Link"));
                        attachments.put("Pdf",document.getString("Pdf"));

                        modules.add(new Module(Integer.parseInt(document.getId()) , document.getString("Topic"), attachments));
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
