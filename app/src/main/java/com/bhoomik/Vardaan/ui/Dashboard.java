package com.bhoomik.Vardaan.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bhoomik.Vardaan.R;
import com.bhoomik.Vardaan.model.Module;
import com.bhoomik.Vardaan.ui.aboutactivity.AboutNgo;
import com.bhoomik.Vardaan.ui.aboutactivity.Aboutus;
import com.bhoomik.Vardaan.ui.aboutactivity.CreatorUs;
import com.bhoomik.Vardaan.ui.language.QuizLanguage;
import com.bhoomik.Vardaan.ui.learningactivity.LearningActivity;
import com.bhoomik.Vardaan.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.bhoomik.Vardaan.ui.learningactivity.LearningActivity.modules;
import static com.bhoomik.Vardaan.utils.Constants.User;

public class Dashboard extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
        Intent intent = new Intent(this, Aboutus.class);
        startActivity(intent);
    }

    public void aboutngo(MenuItem item) {
        Intent intent = new Intent(this, AboutNgo.class);
        intent.putExtra(Constants.Organisation.ORGANISATION_NAME, Constants.Organisation.JUMIO);
        startActivity(intent);
    }

    public void creator(MenuItem item) {
        Intent intent = new Intent(this, CreatorUs.class);
        startActivity(intent);
    }

    public void logout(MenuItem item) {
        final AlertDialog.Builder logout = new AlertDialog.Builder(this);

        View layout = getLayoutInflater().inflate(R.layout.logout_layout, null, false);
        final EditText entryName = layout.findViewById(R.id.entry_name);
        logout.setView(layout);
        logout.setCancelable(true);

        final AlertDialog logoutDialog = logout.create();


        layout.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entryName.getText().toString().equals(Constants.name_all)) {
//                    preferences.getAll().clear();
                    SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(Dashboard.this, LoginActivity.class));
                    finish();
                    startActivity(new Intent(Dashboard.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(Dashboard.this, "गलत नाम डाला जा रहा है", Toast.LENGTH_SHORT).show();
                    entryName.getText().clear();
                }
            }
        });

        layout.findViewById(R.id.btn_logout_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.cancel();

            }
        });
        logoutDialog.show();
    }

    public void translate(MenuItem item) {
        startActivity(new Intent(this, QuizLanguage.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView welcomeText = findViewById(R.id.welcome);


        findViewById(R.id.shrushti_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AboutNgo.class);
                intent.putExtra(Constants.Organisation.ORGANISATION_NAME, Constants.Organisation.SHRUSHTI);
                startActivity(intent);
            }
        });

        findViewById(R.id.jumio_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AboutNgo.class);
                intent.putExtra(Constants.Organisation.ORGANISATION_NAME, Constants.Organisation.JUMIO);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String name = preferences.getString(User.USER_NAME, null);

        if (name != null) {
            welcomeText.setText("स्वागत है " + name);

            if (name.equals(Constants.GUEST_USER_NAME)) {
                SharedPreferences guestPreferences = getSharedPreferences(Constants.GUEST_USER_NAME, MODE_PRIVATE);

                User.accessModule = guestPreferences.getInt(User.ACCESS_MODULE, 1);
                User.accessQuestion = guestPreferences.getInt(User.ACCESS_QUESTION, 1);
                User.progressLecture = guestPreferences.getBoolean(User.PROGRESS_LECTURE, false);
                User.progressLink = guestPreferences.getBoolean(User.PROGRESS_LINK, false);
                User.progressPdf = guestPreferences.getBoolean(User.PROGRESS_PDF, false);

            }
        }


        String userId = preferences.getString(User.USER_CONTACT_NUMBER, null);
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    User.accessModule = Objects.requireNonNull(document.getLong(User.ACCESS_MODULE)).intValue();
                                    User.accessQuestion = Objects.requireNonNull(document.getLong(User.ACCESS_QUESTION)).intValue();
                                    if (document.contains(User.PROGRESS_LECTURE)) {
                                        User.progressLecture = document.getBoolean(User.PROGRESS_LECTURE);
                                    }
                                    if (document.contains(User.PROGRESS_LINK)) {
                                        User.progressLink = document.getBoolean(User.PROGRESS_LINK);
                                    }
                                    if (document.contains(User.PROGRESS_PDF)) {
                                        User.progressPdf = document.getBoolean(User.PROGRESS_PDF);
                                    }

                                }
                            }
                        }
                    });


        }

        firestore.collection("modules").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    modules.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Map<String, Object> attachments = document.getData();

                        modules.add(new Module(Integer.parseInt(document.getId()), document.getString("Topic"), attachments));
                    }
                    Collections.sort(modules, new SortbyModuleNumber());

                } else {
                    Log.e("TAGG", "Error getting modules", task.getException());
                }
            }
        });

    }


    static class SortbyModuleNumber implements Comparator<Module> {

        @Override
        public int compare(Module a, Module b) {
            return a.getModuleNumber() - b.getModuleNumber();
        }
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void learningSection(View view) {
        startActivity(new Intent(this, LearningActivity.class));
    }

    public void Formstudents(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("mode", Constants.Forms.FORM_STUDENTS);
        startActivity(intent);
    }

    public void FormKit(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("mode", Constants.Forms.FORM_KIT);
        startActivity(intent);
    }
}
