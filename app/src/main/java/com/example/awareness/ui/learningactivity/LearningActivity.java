package com.example.awareness.ui.learningactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.awareness.Constants;
import com.example.awareness.Module;
import com.example.awareness.R;
import com.example.awareness.ui.Dashboard;
import com.example.awareness.ui.Form;
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

    public static List<Module> modules = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static LearningAdapter learningAdapter;
    @SuppressLint("StaticFieldLeak")
    public static View quizBottomSheet;
    @SuppressLint("StaticFieldLeak")
    public static BottomSheetDialog quizBottomSheetDialog;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public void certificate(MenuItem item) {
    }

    public void FormStudents(MenuItem item) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("mode", Constants.Forms.FORM_STUDENTS);
        startActivity(intent);
    }

    public void FormKit(MenuItem item) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("mode", Constants.Forms.FORM_KIT);
        startActivity(intent);
    }

    public void about(MenuItem item) {
    }

    public void logout(MenuItem item) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        progressBar = findViewById(R.id.progress_circle);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView learningRecyclerView = findViewById(R.id.learning_recyclerview);
        quizBottomSheet = getLayoutInflater().inflate(R.layout.test_layout, null, false);
        quizBottomSheetDialog = new BottomSheetDialog(this);
        quizBottomSheetDialog.setContentView(quizBottomSheet);


        learningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        learningAdapter = new LearningAdapter(this, modules);
        learningRecyclerView.setAdapter(learningAdapter);
        if(modules.size() > 0){
            progressBar.setVisibility(View.GONE);
        }

//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
//                .build();
//        firestore.setFirestoreSettings(settings);

        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);

        String userId = preferences.getString(User.USER_CONTACT_NUMBER,null);
        if(userId != null) {
            firestore.collection("users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    User.accessModule = Objects.requireNonNull(document.getLong(User.ACCESS_MODULE)).intValue();
                                    User.accessQuestion = Objects.requireNonNull(document.getLong(User.ACCESS_QUESTION)).intValue();
                                    User.progressLink = document.getBoolean(User.PROGRESS_LINK);
                                    User.progressPdf = document.getBoolean(User.PROGRESS_PDF);
                                }
                            }
                        }
                    });

            firestore.collection("modules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        modules.clear();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, String> attachments = new HashMap<>();
                            attachments.put("Link", document.getString("Link"));
                            attachments.put("Pdf", document.getString("Pdf"));

                            modules.add(new Module(Integer.parseInt(document.getId()), document.getString("Topic"), attachments));
                        }
                        Collections.sort(modules, new LearningActivity.SortbyModuleNumber());
                        try {
                            learningAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Log.e("TAGG", e.toString());
                        }

                    } else {
                        Log.e("TAGG", "Error getting modules", task.getException());
                    }
                }
            });
        }

    }

    static class SortbyModuleNumber implements Comparator<Module> {

        @Override
        public int compare(Module a, Module b) {
            return a.getModuleNumber() - b.getModuleNumber();
        }
    }


}



}
