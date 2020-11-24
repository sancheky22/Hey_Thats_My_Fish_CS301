package up.edu.heythatsmyfishcs301.fish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import up.edu.heythatsmyfishcs301.R;

/**
 * Description: The scoresDrawings class draws the fishes that displays the scores. The scores
 * are displayed and updated to the current player1 and player2 scores.

 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/

public class FishPlaceView extends SurfaceView {
    private int gamePhase;

    private FishGameState gameState;
    private int numPlayers = 4;
    int temp;

    private Rect[][] rects = new Rect[4][4];
    private Bitmap[][] bitArr = new Bitmap[4][4];

    Bitmap redPenguin = null;
    Bitmap resizedRedPenguin = null;
    Bitmap orangePenguin = null;
    Bitmap resizedOrangePenguin = null;
    Bitmap bluePenguin = null;
    Bitmap resizedBluePenguin = null;
    Bitmap greenPenguin = null;
    Bitmap resizedGreenPenguin = null;
    Bitmap cursedPenguin = null;
    Bitmap resizedCursedPenguin = null;

    int fpWidth;
    int fpHeight;
    int resize;
    boolean drawOnce = true;

    private Paint black = new Paint();

    public FishPlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        gameState = new FishGameState(numPlayers);

        black.setColor(Color.BLACK);
        black.setTextSize(50);

        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.redpenguin);
        orangePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.orangepenguin);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        greenPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.greenpenguin);
        cursedPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.cursed);

        //int offSet = 100;

        /**
        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < 6 - numPlayers; j++){
                rects[i][j] = new Rect(10 + j * PENGUIN_SIZE, 10 + i * 150 + offSet, (j + 1) * PENGUIN_SIZE, (i + 1) * 150 + offSet);

            }
            offSet += 150;
        }
         */

        //Will create a 4x4 array that contains all of the drawings and hitboxes.
        //Will draw a portion of them in onDraw
//        for (int i = 0; i < 4; i++){
//            for (int j = 0; j < 4; j++){
//                rects[i][j] = new Rect(10 + j * oogabooga, 10 + i * 150 + offSet, (j + 1) * oogabooga, (i + 1) * 150 + offSet);
//            }
//            offSet += 150;
//        }

        //fill the bitmap array with penguin bitmaps
//        for(int i = 0; i < bitArr.length; i++){
//            for(int j = 0; j < bitArr[i].length; j++){
//                if(i == 0){
//                    bitArr[i][j] = resizedOrangePenguin;
//                }
//                else if(i == 1){
//                    bitArr[i][j] = resizedRedPenguin;
//                }
//                else if(i == 2){
//                    bitArr[i][j] = resizedBluePenguin;
//                }
//                else{
//                    bitArr[i][j] = resizedCursedPenguin;
//                }
//            }
//        }



        /**
         *External Citation
         * Date: 9/18/20
         * Problem: Wanted the background of the surfaceview to be transparent instead of black
         * Resource: https://stackoverflow.com/questions/5391089/how-to-make-surfaceview-transparent
         *
         * Created by: Jens Jensen (4/14/2014)
         *
         * Solution: We copied these lines of code into our constructor to get rid of the black
         * background
         */
        //Surface View of the Scores that the fish and text view are on
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.fishPlaceView);
        sfvTrack.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void onDraw(Canvas canvas){
        fpWidth =  canvas.getWidth();
        fpHeight = canvas.getHeight();

        int offSet = 100;

        resize = fpWidth / 4;

        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin, resize, resize, false);
        resizedOrangePenguin = Bitmap.createScaledBitmap(orangePenguin, resize, resize, false);
        resizedBluePenguin = Bitmap.createScaledBitmap(bluePenguin, resize, resize, false);
        resizedGreenPenguin = Bitmap.createScaledBitmap(greenPenguin, resize, resize, false);
        resizedCursedPenguin = Bitmap.createScaledBitmap(cursedPenguin, resize, resize, false);

        if(drawOnce){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++){
                    rects[i][j] = new Rect(10 + j * resize, 10 + i * 150 + offSet, (j + 1) * resize, (i + 1) * 150 + offSet);
                }
                offSet += 80;
            }
        }
        drawOnce = false;


        for(int i = 0; i < bitArr.length; i++){
            for(int j = 0; j < bitArr[i].length; j++){
                if(i == 0){
                    bitArr[i][j] = resizedOrangePenguin;
                }
                else if(i == 1){
                    bitArr[i][j] = resizedRedPenguin;
                }
                else if(i == 2){
                    bitArr[i][j] = resizedBluePenguin;
                }
                else{
                    bitArr[i][j] = resizedCursedPenguin;
                }
            }
        }

        int numPlayers = gameState.getNumPlayers();

        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < 6-numPlayers; j++) {
                if(!(gameState.getPieceArray()[i][j].isOnBoard()) && rects[i][j] != null){
                    canvas.drawBitmap(bitArr[i][j], rects[i][j].left, rects[i][j].top, null);
                    temp = rects[i][j].top;
                }
            }
            if(gamePhase == 0){

                canvas.drawText("Player " + (i+1) + " Pieces", 15, temp - 15, black);
            }
        }
    }

    public Rect[][] getRects(){
        return this.rects;
    }

    public void setRects(Rect[][] arr) {
        for (int i = 0; i<arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                this.rects[i][j] = arr[i][j];
            }
        }
    }

    public void setNumPlayers(int num){
        this.numPlayers = num;
    }

    public void setGamePhase(int phase){
        this.gamePhase = phase;
    }

    public FishGameState getGameState(){
        return this.gameState;
    }

    public void setGameState(FishGameState fgs){
        this.gameState = new FishGameState(fgs);
    }
}
