package com.example.heythatsmyfishcs301;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.graphics.Rect;

import java.text.AttributedCharacterIterator;

public class hexagonPattern extends SurfaceView {

    private Rect test = null;
    private Paint testPaint = new Paint();

    public hexagonPattern(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        testPaint.setColor(Color.CYAN);
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(5.0f);

    }

    @Override
    public void onDraw(Canvas canvas){
        HexagonDrawable hex = new HexagonDrawable(Color.BLUE);
        test = new Rect(10,10,100,100);
        hex.computeHex(test);
        hex.draw(canvas);

        canvas.drawRect(100.0f,100.0f,500.0f,500.0f,testPaint);
    }
}
