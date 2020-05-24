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

    public static List<Module> modules = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static LearningAdapter learningAdapter;
    @SuppressLint("StaticFieldLeak")
    public static View quizBottomSheet;
    @SuppressLint("StaticFieldLeak")
    public static BottomSheetDialog quizBottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        RecyclerView learningRecyclerView = findViewById(R.id.learning_recyclerview);
        quizBottomSheet = getLayoutInflater().inflate(R.layout.test_layout, null, false);
        quizBottomSheetDialog = new BottomSheetDialog(this);
        quizBottomSheetDialog.setContentView(quizBottomSheet);


        learningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        learningAdapter = new LearningAdapter(this, modules);
        learningRecyclerView.setAdapter(learningAdapter);

//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
//                .build();
//        firestore.setFirestoreSettings(settings);

    }
}
