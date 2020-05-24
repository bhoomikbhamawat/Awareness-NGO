package com.example.awareness.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.awareness.Constants;
import com.example.awareness.R;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString(Constants.name,"");

        if(name.equals("")) {

            Intent i = new Intent(MainActivity.this, Dashboard.class);
//                    Intent i = new Intent(MainActivity.this, TestActivity.class);

            startActivity(i);
            finish();
        }
        else {
            Constants.name_all=name;
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }

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
