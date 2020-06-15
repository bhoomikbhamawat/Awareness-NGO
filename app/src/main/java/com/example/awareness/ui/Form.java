package com.example.awareness.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.awareness.Constants.Forms;

public class Form extends AppCompatActivity {


    String url;
    private LinearLayout uploadedImageContainer, uploadedImageContainerStudentsList;
    AutoCompleteTextView categorySpinner;
    TextInputLayout categotySpinnerLayout;
    private EditText Name_kit, Panchayat_kit, Village_kit, Place_kit, Age_kit, Falla_kit, Rajasava_kit;
    private TextView Name_kp;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST_STUDENTS_LIST = 10;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private static final int CAPTURE_IMAGE_REQUEST_STUDENTS_LIST = 20;
    private static final int CAMERA_PERMISSION_REQUEST = 3;
    int mYear, mMonth, mDay, mHour, mMinute;
    ScrollView layout_formkit;
    String mDateTime = "";
    CardView name_card;
    TextView dateTime;
    private TextView removeImage, removeImageStudentsList;
    private ArrayList<String> UserImage, imageStudentsList;
    View uploadImageLayout;
    BottomSheetDialog uploadImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final int mode = intent.getIntExtra("mode", -1);
        Age_kit = findViewById(R.id.age_kit);
        name_card = findViewById(R.id.card_name);
        Name_kp = findViewById(R.id.name_kp);
        categorySpinner = findViewById(R.id.category_spinner);
        categotySpinnerLayout = findViewById(R.id.category_spinner_layout);
        Name_kit = findViewById(R.id.name_kit);
        if (mode == Forms.FORM_STUDENTS) {
            findViewById(R.id.form_students_extra).setVisibility(View.VISIBLE);
            findViewById(R.id.students_list_card).setVisibility(View.VISIBLE);
            Name_kit.setText(Constants.name_all);
            Age_kit.setHint("प्रशिक्षणार्थियो की संख्या");

            TextView studentsPhotograph = findViewById(R.id.students_photograph);
            studentsPhotograph.setVisibility(View.VISIBLE);

            url = Forms.STUDENTS_FORM_URL;
        } else {
            name_card.setVisibility(View.VISIBLE);
            Name_kp.setText(Constants.name_all);
            url = Forms.ADD_USER_URL;
            Age_kit.setHint("उम्र");
            categotySpinnerLayout.setVisibility(View.VISIBLE);
        }


        layout_formkit = findViewById(R.id.layout_formkit);
        Village_kit = findViewById(R.id.village_kit);
        Panchayat_kit = findViewById(R.id.panchayat_kit);
        Age_kit = findViewById(R.id.age_kit);
        Falla_kit = findViewById(R.id.Falla_kit);
        Rajasava_kit = findViewById(R.id.RajasavGaav_kit);

        Place_kit = findViewById(R.id.place_kit);
        uploadedImageContainer = findViewById(R.id.uploaded_image_container);
        removeImage = findViewById(R.id.remove_image);
        dateTime = findViewById(R.id.event_date_time);
        ImageButton addImageStudentsList = findViewById(R.id.add_image_students_list);
        uploadedImageContainerStudentsList = findViewById(R.id.uploaded_image_container_students_list);
        removeImageStudentsList = findViewById(R.id.remove_image_students_list);

        final AutoCompleteTextView SamitiSpinner = findViewById(R.id.Samiti_spinner);


        uploadImageLayout = getLayoutInflater().inflate(R.layout.uploadimage_dialog_layout, null, false);
        uploadImageDialog = new BottomSheetDialog(this);
        uploadImageDialog.setContentView(uploadImageLayout);
        ImageButton addImage = findViewById(R.id.add_image);
        Button sendButton = findViewById(R.id.send_button);


        UserImage = new ArrayList<>();
        imageStudentsList = new ArrayList<>();


        //Creating send button & addImage Button
        int endMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true);
        Toolbar.LayoutParams LayoutParam = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParam.gravity = Gravity.END;
        LayoutParam.setMarginEnd(endMargin);

        /*sendButton = new ImageButton(getApplicationContext());
        sendButton.setLayoutParams(LayoutParam);
        sendButton.setImageDrawable(getApplication().getResources().getDrawable(R.drawable.ic_send_black_24dp));
        sendButton.setBackgroundResource(typedValue.resourceId);*/

       /* addImage = new ImageButton(getApplicationContext());
        addImage.setLayoutParams(LayoutParam);
        addImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_attachment_black_24dp));
        addImage.setBackgroundResource(typedValue.resourceId);*/

        List<String> panchayatiSamiti = new ArrayList<>();
        panchayatiSamiti.add("फलासिया");
        panchayatiSamiti.add("कोटड़ा");
        panchayatiSamiti.add("सायरा");

        List<String> caste = new ArrayList<>();
        caste.add("SC");
        caste.add("ST");
        caste.add("Minority");
        caste.add("OBC");
        caste.add("General");
        ArrayAdapter<String> casteAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, caste);
        categorySpinner.setAdapter(casteAdapter);
        ArrayAdapter<String> panchayatiSamitiAdapter = new ArrayAdapter<String>(Form.this, android.R.layout.simple_spinner_dropdown_item, panchayatiSamiti);
        SamitiSpinner.setAdapter(panchayatiSamitiAdapter);

        getCurrentTime();
        final Calendar newCalendar = Calendar.getInstance();

        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;

                        mDateTime = year + "-" + month + "-" + dayOfMonth;
                        Date date = null;
                        String date6 = "";
                        try {
                            Locale hindi = new Locale("hi", "IN");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", hindi);

                            date = new SimpleDateFormat("yyyy-MM-dd").parse(mDateTime);
                            date6 = sdf.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dateTime.setText(date6);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadedImageContainer.getChildCount() < 5) {

                    uploadImageDialog.show();
                    uploadImageLayout.findViewById(R.id.attachimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadImageDialog.dismiss();
                            attachImage(PICK_IMAGE_REQUEST);
                        }
                    });
                    uploadImageLayout.findViewById(R.id.captureimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadImageDialog.dismiss();
                            captureImage(CAPTURE_IMAGE_REQUEST);
                        }
                    });

                } else {
                    try {
                        Snackbar.make(layout_formkit, "आप अपनी अधिकतम अपलोड सीमा 4 पर पहुंच गए हैं", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        Log.e("ComplainFragment", "Snackbar: You have reached your maximum upload limit of 4", e);
                        Toast.makeText(Form.this, "आप अपनी अधिकतम अपलोड सीमा 4 पर पहुंच गए हैं", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        addImageStudentsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadedImageContainerStudentsList.getChildCount() < 5) {

                    uploadImageDialog.show();
                    uploadImageLayout.findViewById(R.id.attachimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadImageDialog.dismiss();
                            attachImage(PICK_IMAGE_REQUEST_STUDENTS_LIST);
                        }
                    });
                    uploadImageLayout.findViewById(R.id.captureimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadImageDialog.dismiss();
                            captureImage(CAPTURE_IMAGE_REQUEST_STUDENTS_LIST);
                        }
                    });

                } else {
                    try {
                        Snackbar.make(layout_formkit, "आप अपनी अधिकतम अपलोड सीमा 4 पर पहुंच गए हैं", Snackbar.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        Log.e("ComplainFragment", "Snackbar: You have reached your maximum upload limit of 4", e);
                        Toast.makeText(Form.this, "आप अपनी अधिकतम अपलोड सीमा 4 पर पहुंच गए हैं", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < uploadedImageContainer.getChildCount(); ) {
                    uploadedImageContainer.removeViewAt(i);
                }
                UserImage.clear();
                removeImage.setVisibility(View.GONE);
            }
        });

        removeImageStudentsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < uploadedImageContainerStudentsList.getChildCount(); ) {
                    uploadedImageContainerStudentsList.removeViewAt(i);
                }
                imageStudentsList.clear();
                removeImageStudentsList.setVisibility(View.GONE);
            }
        });
        // Send Button
        sendButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Form.this, "भेजें", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Name_kit.getText())) {
                    Snackbar.make(layout_formkit, "कृपया नाम भरें", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(Age_kit.getText())) {
                    Snackbar.make(layout_formkit, "कृपया आयु भरें", Snackbar.LENGTH_SHORT).show();

                } else if (mode != Forms.FORM_STUDENTS && TextUtils.isEmpty(categorySpinner.getText())) {
                    Snackbar.make(layout_formkit, "कृपया जाति भरें", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(SamitiSpinner.getText())) {
                    Snackbar.make(layout_formkit, "कृपया पंचायत समिति भरे", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(Panchayat_kit.getText())) {
                    Snackbar.make(layout_formkit, "कृपया ग्राम पंचायत भरें", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(Falla_kit.getText())) {
                    Snackbar.make(layout_formkit, "कृपया फला भरें", Snackbar.LENGTH_SHORT).show();

                } else  if (TextUtils.isEmpty(Village_kit.getText())) {
                    Snackbar.make(layout_formkit, "कृपया गाँव भरें", Snackbar.LENGTH_SHORT).show();

                }else if (mDateTime.isEmpty()) {
                    Snackbar.make(layout_formkit, "कृपया तारीख भरें", Snackbar.LENGTH_SHORT).show();

                }  else  {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Form.this);
                    a_builder.setMessage("मेरे द्वारा दी गई उपरोक्त सभी जानकारीया सही है |");
                    a_builder.setCancelable(false);
                    a_builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uname_kp = "";

                            final String uName = Name_kit.getText().toString();
                            if (mode != Forms.FORM_STUDENTS) {
                                uname_kp = Name_kp.getText().toString();
                            }
                            final String uAge = Age_kit.getText().toString();
                            final String uVillage = Village_kit.getText().toString();
                            final String uPanchayat = Panchayat_kit.getText().toString();
                            String uPlace = "";
                            if (!Place_kit.getText().toString().isEmpty()) {
                                uPlace = Place_kit.getText().toString();
                            }
                            final String uFalla = Falla_kit.getText().toString();
                            String uRajasava = "";
                            if (!Rajasava_kit.getText().toString().isEmpty()) {
                                uRajasava = Rajasava_kit.getText().toString();
                            }


                            final ProgressDialog pdialog = new ProgressDialog(Form.this);
                            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pdialog.setMessage("आपका फॉर्म रजिस्टर हो रहा है....");
                            pdialog.show();
                            String Category1 = "";
                            if (mode != Forms.FORM_STUDENTS) {
                                Category1 = categorySpinner.getText().toString().trim();
                            }
                            final String Samiti1 = SamitiSpinner.getText().toString().trim();

                            final String finalUPlace = uPlace;
                            final String finalURajasava = uRajasava;
                            final String finalCategory = Category1;
                            final String finalUName = uName;
                            final String finalUname_kp = uname_kp;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pdialog.dismiss();
                                            if ((response).equals("Success")) {
                                                Snackbar.make(layout_formkit, "सफलतापूर्वक किया गया", Snackbar.LENGTH_LONG).show();
                                            } else if ((response).equals("Block")) {
                                                Snackbar.make(layout_formkit, "Complain Registered but You are blocked for misuse of app Contact concerned authority", Snackbar.LENGTH_LONG).show();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pdialog.dismiss();
                                            try {
                                                Snackbar.make(layout_formkit, "कुछ गलत हो गया\n" +
                                                        "बाद में पुन: प्रयास करें", Snackbar.LENGTH_LONG).show();
                                            } catch (NullPointerException e) {
                                                Log.e("ComplainFragment", "Snackbar: Something went Wrong\nTry again Later", e);
                                                Toast.makeText(Form.this, "कुछ गलत हो गया\n" + "बाद में पुन: प्रयास करें", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(Forms.KEY_ACTION, "insert");
                                    params.put(Forms.KEY_Name_kp, finalUname_kp);

                                    // params.put(KEY_Complaint_Emailid, ComplaineeEmailaddress);
                                    params.put(Forms.KEY_uName, finalUName);
                                    params.put(Forms.KEY_Age, uAge);
                                    params.put(Forms.KEY_Date, mDateTime);
                                    params.put(Forms.KEY_Category, finalCategory);
                                    params.put(Forms.KEY_Place, finalUPlace);
                                    params.put(Forms.KEY_Fala, uFalla);
                                    params.put(Forms.KEY_Village, uVillage);
                                    params.put(Forms.KEY_Rajasav, finalURajasava);
                                    params.put(Forms.KEY_GramPanchayat, uPanchayat);
                                    params.put(Forms.KEY_Samiti, Samiti1);
                                    // Log.d("12301001", finalUName + uAge + finalCategory + finalUPlace + uFalla + uVillage + finalURajasava + uPanchayat + Samiti1);
                                    //Log.d("imageuser", UserImage.toString());
                                    if (UserImage != null) {
                                        for (int i = 0; i < UserImage.size(); i++) {
                                            params.put(Forms.KEY_LOST_IMAGE + i, UserImage.get(i));
                                        }
                                    }

                                    if (imageStudentsList != null) {
                                        for (int i = 0; i < imageStudentsList.size(); i++) {
                                            params.put(Forms.KEY_LIST_IMAGE + i, imageStudentsList.get(i));
                                        }
                                    }
                                    return params;
                                }

                            };

                            int socketTimeout = 30000; // 30 seconds. You can change it
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            stringRequest.setRetryPolicy(policy);


                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                            requestQueue.add(stringRequest);

                        }
                    });

                    a_builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                        }
                    });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Alert!");
                    alert.show();

                }
            }


        });

    }

    private void attachImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private void captureImage(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Form.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);

            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, requestCode);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());


        Bitmap rbitmap;
        if (((requestCode == PICK_IMAGE_REQUEST) || (requestCode == PICK_IMAGE_REQUEST_STUDENTS_LIST)) && resultCode == RESULT_OK) {
            assert data != null;
            ClipData mClipData = data.getClipData();

            int currentImageCount;
            if (requestCode == PICK_IMAGE_REQUEST) {
                currentImageCount = uploadedImageContainer.getChildCount();
            } else {
                currentImageCount = uploadedImageContainerStudentsList.getChildCount();
            }

            if (mClipData != null && mClipData.getItemCount() > 1 && mClipData.getItemCount() < 6 - currentImageCount) {

                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ImageView image = new ImageView(Form.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.setMargins(margin, margin, margin, margin);
                    image.setLayoutParams(params);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Uri imageUri = mClipData.getItemAt(i).getUri();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Form.this.getContentResolver(), imageUri);
                        rbitmap = getResizedBitmap(bitmap, 500);//Setting the Bitmap to ImageView
                        String userImage = getStringImage(rbitmap);
                        if (requestCode == PICK_IMAGE_REQUEST) {
                            UserImage.add(userImage);
                        } else {
                            imageStudentsList.add(userImage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    image.setImageURI(imageUri);
                    if (requestCode == PICK_IMAGE_REQUEST) {
                        uploadedImageContainer.addView(image);
                        uploadedImageContainer.setVisibility(View.VISIBLE);
                        removeImage.setVisibility(View.VISIBLE);
                    } else {
                        uploadedImageContainerStudentsList.addView(image);
                        uploadedImageContainerStudentsList.setVisibility(View.VISIBLE);
                        removeImageStudentsList.setVisibility(View.VISIBLE);
                    }

                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Form.this.getContentResolver(), imageUri);
                    rbitmap = getResizedBitmap(bitmap, 500);//Setting the Bitmap to ImageView
                    String userImage = getStringImage(rbitmap);
                    if (requestCode == PICK_IMAGE_REQUEST) {
                        UserImage.add(userImage);
                    } else {
                        imageStudentsList.add(userImage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView image = new ImageView(Form.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                params.setMargins(margin, margin, margin, margin);
                image.setLayoutParams(params);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                image.setImageURI(imageUri);
                if (requestCode == PICK_IMAGE_REQUEST) {
                    uploadedImageContainer.addView(image);
                    uploadedImageContainer.setVisibility(View.VISIBLE);
                    removeImage.setVisibility(View.VISIBLE);
                } else {
                    uploadedImageContainerStudentsList.addView(image);
                    uploadedImageContainerStudentsList.setVisibility(View.VISIBLE);
                    removeImageStudentsList.setVisibility(View.VISIBLE);
                }
            } else {
                try {
                    Snackbar.make(layout_formkit, "4 से अधिक चित्र अपलोड नहीं किए जा सकते", Snackbar.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Log.e("ComplainFragment", "Can't upload more than 4 images", e);
                    Toast.makeText(Form.this, "4 से अधिक चित्र अपलोड नहीं किए जा सकते", Toast.LENGTH_SHORT).show();

                }
            }

        } else if (((requestCode == CAPTURE_IMAGE_REQUEST) || (requestCode == CAPTURE_IMAGE_REQUEST_STUDENTS_LIST)) && resultCode == RESULT_OK) {
            assert data != null;

            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert bitmap != null;
            rbitmap = getResizedBitmap(bitmap, 500);//Setting the Bitmap to ImageView
            String userImage = getStringImage(rbitmap);
            if (requestCode == CAPTURE_IMAGE_REQUEST) {
                UserImage.add(userImage);
            } else {
                imageStudentsList.add(userImage);
            }

            ImageView image = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.setMargins(margin, margin, margin, margin);
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            image.setImageBitmap(bitmap);

            if (requestCode == CAPTURE_IMAGE_REQUEST) {
                uploadedImageContainer.addView(image);
                uploadedImageContainer.setVisibility(View.VISIBLE);
                removeImage.setVisibility(View.VISIBLE);
            } else {
                uploadedImageContainerStudentsList.addView(image);
                uploadedImageContainerStudentsList.setVisibility(View.VISIBLE);
                removeImageStudentsList.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(Form.this, "You haven't picked image", Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    public void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = 18;
        mMinute = 0;
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
