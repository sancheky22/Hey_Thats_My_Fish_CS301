package com.example.heythatsmyfishcs301;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.boardView);
        //sfvTrack.setZOrderOnTop(true);    // necessary
        //SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        //sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
    }


}
