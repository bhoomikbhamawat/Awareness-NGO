package com.bhoomik.Vardaan.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bhoomik.Vardaan.R;
import com.bhoomik.Vardaan.utils.Constants;
import com.bhoomik.Vardaan.utils.Constants.User;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    SharedPreferences sharedPreferences;
    Animation fromright,fromleft,fromdown;
    ImageView jumio,shrushti,girl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        jumio = findViewById(R.id.jumio_logo);
        shrushti = findViewById(R.id.imageView2);
        girl = findViewById(R.id.imageView);
        fromdown = AnimationUtils.loadAnimation(this,R.anim.fromdown);
        fromleft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
        fromright = AnimationUtils.loadAnimation(this,R.anim.fromright);
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString(User.USER_NAME,null);






        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(name == null) {

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    Intent i = new Intent(MainActivity.this, TestActivity.class);

                    startActivity(i);
                    finish();
                }
                else {
                    Constants.name_all=name;
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        fromdown.setAnimationListener(listener);
        jumio.setAnimation(fromleft);
        shrushti.setAnimation(fromright);
        girl.startAnimation(fromdown);



//        videoView = (VideoView) findViewById(R.id.videoView);
//
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animated_logo);
//        videoView.setVideoURI(video);
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
////                startActivity(new Intent(MainActivity.this,SignInActivity.class));
//                if(name.equals("")) {
//
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
////                    Intent i = new Intent(MainActivity.this, TestActivity.class);
//
//                    startActivity(i);
//                    finish();
//                }
//                else {
//                    Constants.name_all=name;
//                    startActivity(new Intent(MainActivity.this, Dashboard.class));
//                    finish();
//                }
//            }
//        });
//
//        videoView.start();
    }
}
