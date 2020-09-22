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
import android.graphics.Rect;

import java.text.AttributedCharacterIterator;
import java.util.Random;

public class hexagonPattern extends SurfaceView {

    //instance variables necessary to draw the initial board state
    private int cWidth;
    private int cHeight;
    private int hexWidth;
    private int hexHeight;
    private int hexMargin;
    private HexagonDrawable hex = new HexagonDrawable(Color.CYAN);
    private Rect tile;
    Bitmap redPenguin = null;
    Bitmap resizedRedPenguin = null;

    Bitmap oneFish = null;
    Bitmap twoFish = null;
    Bitmap threeFish = null;

    Bitmap rOneFish = null;
    Bitmap rTwoFish = null;
    Bitmap rThreeFish = null;

    private int randTile;

    Bitmap orangePenguin = null;
    Bitmap resizedOrangePenguin = null;


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

        testPaint.setColor(Color.WHITE);
        oneFish = BitmapFactory.decodeResource(getResources(), R.drawable.onefishtile);
        twoFish = BitmapFactory.decodeResource(getResources(), R.drawable.twofishtile);
        threeFish = BitmapFactory.decodeResource(getResources(), R.drawable.threefishtile);
        rOneFish = Bitmap.createScaledBitmap(oneFish, 50, 50, false);
        rTwoFish = Bitmap.createScaledBitmap(twoFish, 50, 50, false);
        rThreeFish = Bitmap.createScaledBitmap(threeFish, 50, 50, false);

        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.redpenguin);
        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin, 115, 115, false);
        orangePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.orangepenguin);
        resizedOrangePenguin = Bitmap.createScaledBitmap(orangePenguin, 115, 115, false);

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
        for(int i=0;i<=8;i++) {

            Random rand = new Random();
            randTile = rand.nextInt(3);



            //Draws even rows
            if (i%2==0) {
                numRows = 8;
            }
            //Draws Odd rows
            else{
                numRows = 7;
                tile.left += hexWidth/2;
                tile.right += hexWidth/2;
            }

            //Loop through each column to draw a hexagon at that point.
            //This is where each hexagon is drawn. Each hexagon in this loop is drawn at the location of 'tile'.
            //Tile is a Rect object, so it has a left, top, right, and bottom and you can use those coordinates to draw whatever you want on a hexagon
            for (int j = 0; j < numRows; j++) {
                hex.computeHex(tile);
                hex.draw(c);

                if(randTile == 1){
                    c.drawBitmap(this.rOneFish, (float)tile.left + tile.right / 2, (float)(tile.top + tile.bottom) / 2, null);
                }
                else if(randTile == 2){
                    c.drawBitmap(this.rTwoFish, (float)tile.left + tile.right / 2, (float)(tile.top + tile.bottom) / 2, null);
                }
                else if(randTile == 3){
                    c.drawBitmap(this.rThreeFish, (float)tile.left + tile.right / 2, (float)(tile.top + tile.bottom) / 2, null);
                }


                //TEMPORARY CODE FOR HW 1
                //This is an example of drawing a small white circle on a specific hexagon:
               // if (i == 1 && j == 0) {
                   // testPaint.setColor(Color.WHITE);
                    //you can access the location of the hexagons with tile.left, tile.right, etc.
                    //c.drawCircle((tile.left + tile.right) / 2, (tile.top + tile.bottom) / 2, hexWidth / 4, testPaint);
               // }

                //If you wanted to access the top left corner of the hexagon, you would use: (tile.left, tile.top)


                //If you want to draw it to multiple hexagons you could do something like this:
                if ( i == 3 && j == 0)
                {
                    c.drawBitmap(this.resizedRedPenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 2 && j == 0)
                {
                    c.drawBitmap(this.resizedOrangePenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 6 && j == 4)
                {
                    c.drawBitmap(this.resizedRedPenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 3 && j == 4)
                {
                    c.drawBitmap(this.resizedOrangePenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 5 && j == 1)
                {
                    c.drawBitmap(this.resizedRedPenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 7 && j == 2)
                {
                    c.drawBitmap(this.resizedOrangePenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 4 && j == 3)
                {
                    c.drawBitmap(this.resizedRedPenguin, 0.1f*hexWidth+tile.left, tile.top, null);
                }
                else if(i == 1 && j == 6)
                {

                    testPaint.setColor(Color.GREEN);
                    c.drawBitmap(this.resizedRedPenguin, 0.0f, 300.0f, null);
                    //c.drawCircle((tile.left + tile.right) / 2, (tile.top + tile.bottom) / 2, hexWidth / 4, testPaint);

                    c.drawBitmap(this.resizedOrangePenguin, 0.1f*hexWidth+tile.left, tile.top, null);

                }

                //We will absolutely not do it this way, but for now we can index hexagons like this.
                //I think in the future we will create a real Tile class which will contain instance variables like: numFish, isGone, and have methods like getNeighbors().
                //I think this is sufficient for now, and just call me if there's anything you need, Ryan




                tile.left += hexWidth;
                tile.right += hexWidth;
                //fdgg
            }

            //Reset hexagon values after row is drawn.
            tile.top += hexHeight;
            tile.bottom += hexHeight;
            tile.left = 0;
            tile.right = hexWidth;

        }
    }
}
