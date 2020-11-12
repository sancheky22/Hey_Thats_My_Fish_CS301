package com.example.heythatsmyfishcs301.fish;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
//import android.util.FloatMath;
import java.lang.Math;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
/**
 * External Citation
 * Date:    9/20/20
 * Problem: Need to draw regular hexagons to the screen using canvas
 * Resource: https://gist.github.com/AnderWeb/face86a92e28fe32983a#file-hexagondrawable-java
 * Solution: We used this class to draw to our canvas.
 * Created by AnderWeb (Gustavo Claramunt) on 7/10/14.
 *
 * This class was created in 2014 when the FloatMath library was faster than the Math library
 * Since then, the Math library was improved and FloatMath was left deprecated in API level 22
 * So we have slightly modified this class to make it compatible with our project.
 */




public class HexagonDrawable extends Drawable {

    public static final int SIDES = 6;
    private Path hexagon = new Path();
    private Path temporal = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public HexagonDrawable(int color) {
        paint.setColor(color);
        //hexagon.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(hexagon, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        computeHex(bounds);
        invalidateSelf();
    }

    public void computeHex(Rect bounds) {

        final int width = bounds.width();
        final int height = bounds.height();
        final int size = Math.min(width, height);
        final int centerX = bounds.left + (width / 2);
        final int centerY = bounds.top + (height / 2);

        hexagon.reset();
        hexagon.addPath(createHexagon(size, centerX, centerY));
        hexagon.addPath(createHexagon((int) (size * .8f), centerX, centerY));
    }

    private Path createHexagon(int size, int centerX, int centerY) {
        //Define an offset for the angle that the path is calculated on.
        float offset = (float) Math.PI/2.0f;
        final float section = (float) ((2.0 * Math.PI)/ SIDES);
        int radius = size / 2;
        Path hex = temporal;
        hex.reset();
        //Initial path point
        hex.moveTo(
                (centerX + radius * (float) Math.cos(offset)),
                (centerY + radius * (float) Math.sin(offset)));

        for (int i = 1; i < SIDES; i++) {
            hex.lineTo(
                    //Add the offset angle at the end to find the next point
                    (centerX + radius * (float) Math.cos((section * i)+offset)),
                    (centerY + radius * (float) Math.sin((section * i)+offset)));
        }

        hex.close();
        return hex;
    }
}
