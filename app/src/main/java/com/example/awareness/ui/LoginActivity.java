package com.example.awareness.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.awareness.Constants;
import com.example.awareness.Constants.User;
import com.example.awareness.R;
import com.example.awareness.ui.learningactivity.LearningActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {


    EditText nameText;
    EditText phoneText;
    Button loginButton;
    TextView signupLink;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameText = findViewById(R.id.text_email);
        phoneText = findViewById(R.id.text_password);

        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.text_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login() {
        //Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        //Progress Dialogue - the symbol of loading(circle)

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);

        //TODO: USe an appropriate style
        //setindeterminate-it works(shows)figure continously till it completes  another way is loading percentage in progress dialogue
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Authenticating...");
        progressDialog.setMessage("loading");
        progressDialog.show();

        final String name = nameText.getText().toString().trim();
        final String phone = phoneText.getText().toString().trim();
        //final String emailRefined = emailText.getText().toString().replaceAll("\\W+","");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(phone).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document != null) {
                                if (document.exists()) {
                                    String nameFetched = document.getString(Constants.User.USER_NAME);
                                    if (name.equals(nameFetched)) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(User.USER_NAME, name);
                                        editor.putString(User.USER_CONTACT_NUMBER, phone);
                                        editor.apply();
                                        Constants.name_all=name;
                                        progressDialog.dismiss();
                                        onLoginSuccess();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        onLoginFailed();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "This phone number is not registered", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onLoginFailed();
                                }
                            }
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onLoginFailed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "कुछ गलत हो गया\n" +
                        "बाद में पुन: प्रयास करें", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onLoginFailed();
            }
        });


    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();

        if (name.isEmpty()) {
            nameText.setError("enter a name");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (phone.length() < 10) {
//            phoneText.setError("between 4 and 20 alphanumeric characters");
            phoneText.setError("Enter valid phone number");
            valid = false;
        } else {
            phoneText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);


        Intent i = new Intent(LoginActivity.this, LearningActivity.class);
//            i.putExtra("EXTRA", "notopenFragment");
        startActivity(i);
        finish();

    }

}

