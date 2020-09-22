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

    //instance variables necessary to draw the initial board state
    private int cWidth;
    private int cHeight;
    private int hexWidth;
    private int hexHeight;
    private int hexMargin;
    private HexagonDrawable hex = new HexagonDrawable(Color.CYAN);
    private Rect tile;

    private Paint testPaint = new Paint();


    //Constructor. Initializes important stuff
    public hexagonPattern(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        //add external citation here
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.hexagonPattern);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        testPaint.setColor(Color.CYAN);
    }


    //This method is called by something idk what yet but it draws stuff to the screen
    @Override
    public void onDraw(Canvas canvas){
        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();

        //setBackgroundColor(Color.WHITE);
        drawHex(canvas);

    }


    //This method uses the dimensions of the Canvas to draw a board of an appropriate size
    public void drawHex(Canvas c){
        int numRows = 8;
        hexHeight = cHeight/8;
        hexWidth = cWidth/8;

        //Size of each individual tile
        tile = new Rect(0,0,hexWidth,hexHeight);

        //This loop will draw the hexagonal array.
        for(int j=0;j<=8;j++) {

            //Draws even rows
            if (j%2==0) {
                numRows = 8;
            }
            //Draws Odd rows
            else{
                numRows = 7;
                tile.left += hexWidth/2;
                tile.right += hexWidth/2;
            }

            //Loop through each column to draw a hexagon at that point.
            for (int i = 0; i < numRows; i++) {
                hex.computeHex(tile);
                hex.draw(c);
                tile.left += hexWidth;
                tile.right += hexWidth;
            }

            //Reset hexagon values after row is drawn.
            tile.top += hexHeight;
            tile.bottom += hexHeight;
            tile.left = 0;
            tile.right = hexWidth;

        }
    }
}
