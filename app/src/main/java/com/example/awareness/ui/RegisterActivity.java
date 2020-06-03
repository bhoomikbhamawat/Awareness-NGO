package com.example.awareness.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.awareness.Constants;
import com.example.awareness.R;
import com.example.awareness.ui.learningactivity.LearningActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.awareness.Constants.Register.Register_Url;
import static com.example.awareness.Constants.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText RegName, RegGaav, RegMobile, RegRajasav, RegAge, RegFaala, RegGramPanch, RegCity, RegSamiti;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Button buttonSubmit;
    LinearLayout linearLayout;
    private String Category;
    SharedPreferences sharedPreferences;


    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linearLayout = findViewById(R.id.linear_l);
        RegName = findViewById(R.id.input_name);
        RegGaav = findViewById(R.id.input_Gaav);
        RegRajasav = findViewById(R.id.input_rajasav);
        RegMobile = findViewById(R.id.input_phone_reg);
        RegAge = findViewById(R.id.input_age_reg);
        RegFaala = findViewById(R.id.input_faala);
        RegGramPanch = findViewById(R.id.reg_grampanc);
        RegCity = findViewById(R.id.input_city);
        RegSamiti = findViewById(R.id.reg_samiti);
        final Spinner categorySpinner = findViewById(R.id.category_spinner_register);
        buttonSubmit = findViewById(R.id.button);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Mess");

        List<String> complaints = new ArrayList<>();
        complaints.add(0, "जाति");
        complaints.add("SC");
        complaints.add("ST");
        complaints.add("Minority");
        complaints.add("OBC");
        complaints.add("General");
        ArrayAdapter<String> complaintsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, complaints) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setPadding(0, view.getPaddingTop(), 0, view.getPaddingBottom());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                switch (position) {
                    case 0:

                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 52);
                        break;
                    default:
                        tv.setTypeface(null, Typeface.NORMAL);
                        tv.setTextColor(Color.BLACK);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
                        break;
                }
                return view;
            }
        };
        complaintsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(complaintsAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Category = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Category.equals("जाति")) {
                    try {
                        Snackbar.make(linearLayout, "कृपया जाति भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        //Log.e("ComplainFragment", "Snackbar: Please specify complain type", e);
                        Toast.makeText(RegisterActivity.this, "कृपया जाति भरें", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegName.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया नाम भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        //Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया नाम भरे", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegAge.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया उम्र भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया उम्र भरे", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegGaav.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया गाँव भरे", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया गाँव भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया गाँव भरे", Toast.LENGTH_SHORT).show();
                    }
                } else if (RegMobile.length() < 10) {
                    try {
                        Snackbar.make(linearLayout, "कृपया मोबाइल न. भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया मोबाइल न. भरे", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegFaala.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया फला भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया फला भरें", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegGramPanch.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया ग्राम पंचायत भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया ग्राम पंचायत भरें", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegCity.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया शहर भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        // Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया शहर भरें", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegRajasav.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया  राजस्व गाँव भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        //Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया  राजस्व गाँव भरे", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(RegSamiti.getText())) {
                    try {
                        Snackbar.make(linearLayout, "कृपया पंचायत समिति भरें", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                        //Log.e("ComplainFragment", "Snackbar: कृपया समय भरे", e);
                        Toast.makeText(RegisterActivity.this, "कृपया पंचायत समिति भरे", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    final AlertDialog.Builder warning = new AlertDialog.Builder(RegisterActivity.this);
                    warning.setTitle("Notification");
                    //AlertDialog.Builder a_builder = new AlertDialog.Builder(RegisterActivity.this);
                    warning.setMessage("मेरे द्वारा दी गई उपरोक्त सभी जानकारीया सही है |");
                    // warning.setCancelable(true);
                    warning.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase PostReference = FirebaseDatabase.getInstance();
                            DatabaseReference mPostReference = PostReference.getReference("Register");

                            final ProgressDialog pdialog = new ProgressDialog(RegisterActivity.this);
                            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pdialog.setMessage("आपका रजिस्टर प्रक्रिया में है....");
                            pdialog.show();
                            final String Category1 = Category.trim();
                            final String rname = RegName.getText().toString();
                            final String rmobile = RegMobile.getText().toString();
                            final String rVillage = RegGaav.getText().toString();
                            final String rPanchayat = RegGramPanch.getText().toString();
                            final String rsamiti = RegSamiti.getText().toString();
                            final String rFaala = RegFaala.getText().toString();
                            final String rRajasav = RegRajasav.getText().toString();
                            final String rCity = RegCity.getText().toString();
                            final String rAge = RegAge.getText().toString();

                            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Register_Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pdialog.dismiss();
                                            if ((response.toString()).equals("Success")) {
                                                ////here cloud firestore registration is reqiured
                                                Snackbar.make(linearLayout, "सफलतापूर्वक किया गया", Snackbar.LENGTH_LONG).show();
//                                                PersonDetails personDetails = new PersonDetails(
//                                                        rname,
//                                                        rmobile,
//                                                        1 + "");
                                                Map<String, Object> user = new HashMap<String, Object>() {
                                                    {
                                                        put(User.USER_NAME,rname);
                                                        put(User.ACCESS_MODULE, 1);
                                                        put(User.ACCESS_QUESTION, 1);
                                                        put(User.PROGRESS_LECTURE,false);
                                                        put(User.PROGRESS_LINK, true);
                                                        put(User.PROGRESS_PDF, false);

                                                    }

                                                };
                                                firestore.collection("users").document(rmobile).set(user);
                                                SharedPreferences sharedPref = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                editor.putString(User.USER_CONTACT_NUMBER, rmobile);
                                                editor.putString(User.USER_NAME, rname);
                                                editor.apply();
                                                Constants.name_all = rname;
                                                Intent i = new Intent(RegisterActivity.this, LearningActivity.class);
                                                startActivity(i);
                                                finish();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pdialog.dismiss();
                                            try {
                                                Snackbar.make(linearLayout, "कुछ गलत हो गया\n" +
                                                        "बाद में पुन: प्रयास करें", Snackbar.LENGTH_LONG).show();
                                            } catch (NullPointerException e) {
                                                Log.e("ComplainFragment", "Snackbar: Something went Wrong\nTry again Later", e);
                                                Toast.makeText(RegisterActivity.this, "कुछ गलत हो गया\n" + "बाद में पुन: प्रयास करें", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(Constants.Register.KEY_ACTION, "insert");
                                    //params.put(Constants.Register.KEY_Name_kp, "Bhoomik");

                                    // params.put(KEY_Complaint_Emailid, ComplaineeEmailaddress);
                                    params.put(Constants.Register.KEY_uName, rname);
                                    params.put(Constants.Register.KEY_Age, rAge);
                                    // params.put(Constants.Register.KEY_Date, r);
                                    params.put(Constants.Register.KEY_Category, Category1);
                                    //params.put(Constants.Register.KEY_Place, r);
                                    params.put(Constants.Register.KEY_Fala, rFaala);
                                    params.put(Constants.Register.KEY_Village, rVillage);
                                    params.put(Constants.Register.KEY_Rajasav, rRajasav);
                                    params.put(Constants.Register.KEY_GramPanchayat, rPanchayat);
                                    params.put(Constants.Register.KEY_Samiti, rsamiti);
                                    params.put(Constants.Register.KEY_Mobile, rmobile);
                                    params.put(Constants.Register.KEY_City, rCity);




                                    return params;
                                }

                            };

                            int socketTimeout = 30000; // 30 seconds. You can change it
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            stringRequest.setRetryPolicy(policy);

                            firestore.collection("users").document(rmobile).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null) {
                                            if (document.exists()) {
                                                pdialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "You are already registered. Please login", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                            } else{
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    }
                                }
                            });


                        }
                    });


                    warning.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = warning.create();
                    alert.show();
                }
            }


        });


    }
}
