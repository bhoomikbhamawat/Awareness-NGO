package com.bhoomik.Vardaan.ui.aboutactivity;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bhoomik.Vardaan.R;

public class DeveloperInfo extends AppCompatActivity {
    private Animator mCurrentAnimator;

    private boolean zoomMode;
    private ImageView expandedImageView;
    private Rect startBounds;
    private float startScaleFinal;
    private boolean showingMin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

//       Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Profile");
//        setSupportActionBar(toolbar);
//       getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name1 = findViewById(R.id.user_name_profile);
        TextView email1 = findViewById(R.id.user_email_profile);
        TextView branch1 = findViewById(R.id.user_rollno_profile);
        int image = getIntent().getIntExtra("Image", 0);
        String name = getIntent().getStringExtra("Name");
        String email = getIntent().getStringExtra("Email");
        String branch = getIntent().getStringExtra("Branch");

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        name1.setText(name);
        branch1.setText(branch);
        email1.setText(email);
        ImageView userProfilePictureImageView = findViewById(R.id.user_profile_picture_profile);
//        expandedImageView = findViewById(R.id.expanded_image_profile);
        userProfilePictureImageView.setImageResource(image);


        // The system "short" animation time duration, in milliseconds. This
        // duration is ideal for subtle animations or animations that occur
        // very frequently.
        int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}