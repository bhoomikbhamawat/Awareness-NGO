package com.example.awareness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.text_email);
        passwordText = findViewById(R.id.text_password);

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

        final String name = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        //final String emailRefined = emailText.getText().toString().replaceAll("\\W+","");

        FirebaseDatabase LoginReference = FirebaseDatabase.getInstance();
        DatabaseReference mLoginReference = LoginReference.getReference("Register");
        mLoginReference.child(name).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.child("mobile").getValue(String.class);
                        try {
                            //checking if already registered or not
                            if(password.equals(value) )
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.name, name);
                                //editor.putString(Constants.email, email);
                                editor.putString(Constants.password,password);
                                //editor.putString(Constants.isactive,isactive);
                                editor.apply();
                                editor.commit();
                                progressDialog.dismiss();
                                onLoginSuccess();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                onLoginFailed();
                            }
                        }
                        //Exception e - if on firebase it is unable to check as that it is not their
                        catch (Exception e){
                            Toast.makeText(LoginActivity.this, "You are not registered with this email id", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onLoginFailed();
                        Log.w("registered or not", "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = emailText.getText().toString();
        String phone = passwordText.getText().toString();

        if (name.isEmpty()) {
            emailText.setError("enter a name");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 4 || phone.length() > 20) {
            passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
       // final String email = sharedPreferences.getString(Constants.email,"");
       // final String email_refined = email.replaceAll("\\W+", "");

        FirebaseDatabase PostReference = FirebaseDatabase.getInstance();
        DatabaseReference cPostReference = PostReference.getReference("Data");
        cPostReference.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Saving to internal storage

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("activated or not", "loadPost:onCancelled", databaseError.toException());
                    }
                });
        loginButton.setEnabled(true);

        //this is to understand and write why this has done

            Intent i = new Intent(LoginActivity.this, Dashboard.class);
            i.putExtra("EXTRA", "notopenFragment");
            startActivity(i);
            finish();

    }

}

