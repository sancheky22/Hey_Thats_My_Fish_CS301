package com.example.heythatsmyfishcs301;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;

import java.text.AttributedCharacterIterator;

public class hexagonPattern extends SurfaceView {

    private int hexWidth = 200;
    private int hexHeight = 200;
    private int hexMargin = 10;
    private Paint testPaint = new Paint();
    private HexagonDrawable hex = new HexagonDrawable(Color.CYAN);
    private Rect test = new Rect(10,10,100,100);

    public hexagonPattern(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        //add external citation here
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.hexagonPattern);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        testPaint.setColor(Color.CYAN);
        testPaint.setStrokeWidth(5.0f);
    }

    @Override
    public void onDraw(Canvas canvas){

        //setBackgroundColor(Color.WHITE);
        drawHex(canvas);

    }

    public void drawHex(Canvas c){
        for (int i=0;i<=8;i++)
        {
            test.left += hexMargin+hexWidth;
            test.right += hexMargin+hexWidth;
            hex.computeHex(test);
            hex.draw(c);
        }


    }
}
