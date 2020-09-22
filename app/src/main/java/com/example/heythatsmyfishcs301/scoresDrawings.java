package com.example.heythatsmyfishcs301;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class scoresDrawings extends SurfaceView {

    Bitmap orangeFish = null;

    public scoresDrawings(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);


        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.scoresDrawings);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        orangeFish = BitmapFactory.decodeResource(getResources(), R.drawable.orangefish);

    }
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.orangeFish, 1.0f, 1.0f, null);
    }
}