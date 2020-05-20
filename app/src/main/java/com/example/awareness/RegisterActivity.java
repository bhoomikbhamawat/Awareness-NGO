package com.example.awareness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.common.api.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextMobile,
            editTextyear, editTextdu,editTextPass,editTextRoom;
    private AutoCompleteTextView editTextHostel,editTextBranch;
    private Spinner editTextMess;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Register");
    private Button buttonSubmit;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;


    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linearLayout = findViewById(R.id.linear_l);
        editTextName =  findViewById(R.id.input_name);
        editTextEmail =  findViewById(R.id.input_email);
        editTextMobile =  findViewById(R.id.input_phone);
        editTextRoom = findViewById(R.id.input_room);
        editTextMess=findViewById(R.id.input_mess);
        editTextdu =  findViewById(R.id.input_du_no);
        editTextyear =  findViewById(R.id.input_year);
        editTextPass = findViewById(R.id.input_password);

        buttonSubmit = findViewById(R.id.button);

        String[] clg_branches = getResources().getStringArray(R.array.branches);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, clg_branches);
        editTextBranch = findViewById(R.id.input_department);
        editTextBranch.setThreshold(1);
        editTextBranch.setAdapter(adapter);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Mess");


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Log.d("whynot","dfs");

                     //If no error continue else prompt user to start internet

                       //  final Calendar calendar = Calendar.getInstance();
                     final AlertDialog.Builder warning = new AlertDialog.Builder(RegisterActivity.this);
                warning.setTitle("Notification");
                     //AlertDialog.Builder a_builder = new AlertDialog.Builder(RegisterActivity.this);
                         warning.setMessage("I am aware that if I will misuse this facility by any way I would be deregistered from this app");
                        // warning.setCancelable(true);
                        warning.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 FirebaseDatabase PostReference = FirebaseDatabase.getInstance();
                                 DatabaseReference mPostReference = PostReference.getReference("Register");

                                 final ProgressDialog pdialog = new ProgressDialog(RegisterActivity.this);
                                 pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                 pdialog.setMessage("Registering your complain....");
                                 pdialog.show();
                                 String name = editTextName.getText().toString();
                                 String mobile = editTextMobile.getText().toString();
                                 SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d h:mm:ss a");
                                 PersonDetails personDetails = new PersonDetails(
                                         name,
                                         mobile,
                                         1 + "");
                                 myRef.child(name).setValue(personDetails);
                                 SharedPreferences sharedPref =getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                                 SharedPreferences.Editor editor = sharedPref.edit();
                                 editor.putString(Constants.name, name);
                                 editor.putString(Constants.password, mobile);
                                 editor.commit();
                                 pdialog.dismiss();
                                 Intent i = new Intent(RegisterActivity.this, Dashboard.class);
                                 startActivity(i);
                                 finish();
                             }
                         });


                         warning.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.cancel();
                             }
                         });
                AlertDialog alert = warning.create();
                alert.show();
                     }





        });



    }
}
