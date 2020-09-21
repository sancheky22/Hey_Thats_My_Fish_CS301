package com.example.heythatsmyfishcs301;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class scoresDrawings extends SurfaceView {

    Bitmap orangeFish = null;
    Bitmap penguin = null;

    public scoresDrawings(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        setBackgroundColor(Color.TRANSPARENT);

        orangeFish = BitmapFactory.decodeResource(getResources(), R.drawable.orangefish);
        penguin = BitmapFactory.decodeResource(getResources(), R.drawable.penguin1);

    }
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.orangeFish, 1.0f, 1.0f, null);
        canvas.drawBitmap(this.penguin, 1.0f, 2.0f, null);
    }
}

