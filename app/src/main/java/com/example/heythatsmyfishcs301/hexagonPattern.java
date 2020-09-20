package com.example.heythatsmyfishcs301;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.graphics.Rect;

import java.text.AttributedCharacterIterator;

public class hexagonPattern extends SurfaceView {

    private Rect test = null;
    private HexagonDrawable hex;

    public hexagonPattern(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

    }

    @Override
    public void onDraw(Canvas canvas){
        /*
        hex = new HexagonDrawable(Color.BLUE);
        test = new Rect(10,10,100,100);
        hex.computeHex(test);
        hex.draw(canvas);
         */
    }
}
