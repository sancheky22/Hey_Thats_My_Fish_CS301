package com.example.heythatsmyfishcs301.fish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.heythatsmyfishcs301.R;
import com.example.heythatsmyfishcs301.game.LocalGame;

import java.util.ArrayList;
import java.util.Random;

public class FishView extends SurfaceView {

    //instance variables necessary to draw the initial board state

    private FishGameState gameState = new FishGameState();
    private int cWidth;
    private int cHeight;
    private HexagonDrawable hex = new HexagonDrawable(0xFFC3F9FF);
    private HexagonDrawable bigHex = new HexagonDrawable(0xFF5685C5);
    private Rect tile;
    private Rect bigTile;
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




    public FishView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);

        /**
         *External Citation
         * Date: 9/18/20
         * Problem: Wanted the background of the surfaceview to be transparent instead of black
         * Resource: https://stackoverflow.com/questions/5391089/how-to-make-surfaceview-transparent
         *
         * Created by: Jens Jensen (4/14/2014)
         *
         * Solution: We copied these lines of code into our constructor to get rid of the black background
         */
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.fishView);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        testPaint.setColor(Color.WHITE);
        oneFish = BitmapFactory.decodeResource(getResources(), R.drawable.onefishtile);
        twoFish = BitmapFactory.decodeResource(getResources(), R.drawable.twofishtile);
        threeFish = BitmapFactory.decodeResource(getResources(), R.drawable.threefishtile);
        rOneFish = Bitmap.createScaledBitmap(oneFish, 90, 90, false);
        rTwoFish = Bitmap.createScaledBitmap(twoFish, 90, 90, false);
        rThreeFish = Bitmap.createScaledBitmap(threeFish, 90, 90, false);

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
        //drawHex(canvas);
        drawBoard(canvas,gameState);
    }


    //This method uses the dimensions of the Canvas to draw a board of an appropriate size
    //Each hexagon can fit into a Rect object as its bounds so we take a rect and draw a hexagon inside of it
    //we also draw any other properties of the tile that need to be drawn (Fish and penguins).
    //We repeat this process for all 8 rows and the end result is a complete board.
    public void drawBoard(Canvas c, FishGameState g){
        FishTile[][] board = g.getBoardState();
        FishPenguin[][] penguins = g.getPieceArray();

        int hexHeight = cHeight/8;
        int hexWidth = cWidth/8;
        int margin = 15;

        //This Rect object is where we draw the hexagon. We will move it kind of like a stencil and then draw the hexagon inside it
        Rect bound = new Rect(0,0, hexWidth, hexHeight);


        //Here we go through the array and if the tile is null, then it is a placeholder and we skip it.
        //If it is a tile that exists, we draw it.
        //Increment the Rect bound object
        //At the end of the row, reset bound to the start.
        for (int i=0; i < board.length; i++){
            for (int j=0; j < board[i].length; j++){
                //If it is a placeholder cell, then do not move the bounds.
                if (board[i][j] == null){
                    continue;
                }
                if (board[i][j].doesExist()){
                    //Draw order: hexagon, fish, penguin
                    //Draws the hexagon
                    bigHex.computeHex(bound);
                    bigHex.draw(c);
                    Rect s = new Rect(bound);
                    Paint p = new Paint();


                    s.top += margin;
                    s.bottom -= margin;
                    s.right -= margin;
                    s.left += margin;

                    /**
                     * draw bounding box for hexagons.
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(10.0f);
                    p.setColor(0xFF000000);
                    c.drawRect(s,p);
                    */
                    hex.computeHex(s);
                    hex.draw(c);

                    this.initFish(g);

                    //draw the fish on the hexagon
                    //TODO: Draw the fish images on the tiles.
//                    switch(board[i][j].getNumFish()){
//                        case 1:
//                            //Draw 1 fish
//                            c.drawBitmap(rOneFish, hexWidth, hexHeight, null);
//                            break;
//                        case 2:
//                            //Draw 2 fish
//                            c.drawBitmap(rTwoFish, hexWidth, hexHeight, null);
//                            break;
//                        case 3:
//                            //Draw 3 fish
//                            c.drawBitmap(rThreeFish, hexWidth, hexHeight, null);
//                            break;
//                    }
                }

                //increment the bounds
                bound.right += hexWidth;
                bound.left += hexWidth;

            }

            //Need to set the bound back to the start and up one more line.
            //If the next line is even, set the left and right bounds to 0
            //If it's odd, offset it by a little bit.
            float spacing = 0.75f;

            bound.left = ((i+1)%2)*hexWidth/2;
            bound.right = ((i+1)%2)*hexWidth/2 + hexWidth;
            bound.bottom += hexHeight*spacing;
            bound.top += hexHeight*spacing;
        }

        //After we have drawn all of the tiles, we draw the penguins on the board in this loop
        for (int i = 0; i < penguins.length; i++){
            for (int j = 0; j< penguins[i].length;j++){
                //TODO: Draw the penguin at its location.
                FishPenguin p = penguins[i][j];
            }
        }
        this.initFish(g);
        for (int i=0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch(board[i][j].getNumFish()){
                    case 1:
                        //Draw 1 fish
                        c.drawBitmap(rOneFish, hexWidth, hexHeight, null);
                        break;
                    case 2:
                        //Draw 2 fish
                        c.drawBitmap(rTwoFish, hexWidth, hexHeight, null);
                        break;
                    case 3:
                        //Draw 3 fish
                        c.drawBitmap(rThreeFish, hexWidth, hexHeight, null);
                        break;
                }
            }
        }
    }

    public void initFish(FishGameState g){
        FishTile[][] fishBoard = g.getBoardState();
        ArrayList<Integer> fishArray = new ArrayList<>(60);
        int x;
        int y;

        for(int u = 1; u < 60; u++){
            fishArray.add(u);
        }

        for(int u = 0; u < 30; u++){
            Random rand = new Random();
            int oneFish = (rand.nextInt(60)+1);
            x = this.getXIndex(oneFish, g);
            y = this.getYIndex(oneFish, g) ;
            fishBoard[x][y].setNumFish(1);
            fishArray.remove(oneFish);
        }

        for(int u = 0; u < 20; u++){
            Random rand = new Random();
            int twoFish = (rand.nextInt(60)+1);
            x = this.getXIndex(twoFish, g);
            y = this.getYIndex(twoFish, g) ;
            fishBoard[x][y].setNumFish(2);
            fishArray.remove(twoFish);
        }

        for(int u = 0; u < 10; u++){
            Random rand = new Random();
            int threeFish = (rand.nextInt(60)+1);
            x = this.getXIndex(threeFish, g);
            y = this.getYIndex(threeFish, g) ;
            fishBoard[x][y].setNumFish(3);
            fishArray.remove(threeFish);
        }

    }

    public int getXIndex(int whichTile, FishGameState g){
        FishTile[][] board = g.getBoardState();
        int x = 0;
        int limit = 1;

        for (int i=0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(limit != whichTile) {
                    x = i;
                }
                limit++;
            }
        }

        return x;
    }

    public int getYIndex(int whichTile, FishGameState g){
        FishTile[][] board = g.getBoardState();
        int y = 0;
        int limit = 1;

        for (int i=0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(limit != whichTile) {
                    y = i;
                }
                limit++;
            }
        }

        return y;
    }

}
