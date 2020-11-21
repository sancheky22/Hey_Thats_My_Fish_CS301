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
    private int numPlayers;
    private final int PENGUIN_SIZE = 150;

    private int cWidth;
    private int cHeight;

    private final String p1Place = "Player 1 Pieces:";
    private final String p2Place = "Player 2 Pieces:";
    private final String p3Place = "Player 3 Pieces:";
    private final String p4Place = "Player 4 Pieces:";

    private FishGameState gameState;
    int temp;

    private Rect[][] rects;
    private Bitmap[][] bitArr;

    Bitmap redPenguin = null;
    Bitmap resizedRedPenguin = null;
    Bitmap orangePenguin = null;
    Bitmap resizedOrangePenguin = null;
    Bitmap bluePenguin = null;
    Bitmap resizedBluePenguin = null;


    private Paint black = new Paint();

    public FishPlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        gameState = new FishGameState(3);
        numPlayers = gameState.getNumPlayers();
        rects = new Rect[numPlayers][6 - numPlayers];
        bitArr = new Bitmap [numPlayers][6 - numPlayers];

        black.setColor(Color.BLACK);
        black.setTextSize(70);

        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.actuppenguin);
        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin, PENGUIN_SIZE, PENGUIN_SIZE, false);
        orangePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.orangepenguin);
        resizedOrangePenguin = Bitmap.createScaledBitmap(orangePenguin, PENGUIN_SIZE, PENGUIN_SIZE, false);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        resizedBluePenguin = Bitmap.createScaledBitmap(bluePenguin, PENGUIN_SIZE, PENGUIN_SIZE, false);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        resizedBluePenguin = Bitmap.createScaledBitmap(bluePenguin, PENGUIN_SIZE, PENGUIN_SIZE, false);

        int offSet = 100;

        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < 6 - numPlayers; j++){
                rects[i][j] = new Rect(10 + j * PENGUIN_SIZE, 10 + i * 150 + offSet, (j + 1) * PENGUIN_SIZE, (i + 1) * 150 + offSet);
            }
            offSet += 150;
        }

        //fill the bitmap array with penguin bitmaps
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
            }
        }





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


        for(int i = 0; i < rects.length; i++){
            for(int j = 0; j < rects[i].length; j++) {
                if(!(gameState.getPieceArray()[i][j].isOnBoard())){
                    //canvas.drawRect(rects[i][j], black);
                    canvas.drawBitmap(bitArr[i][j], rects[i][j].left, rects[i][j].top, null);
                    temp = rects[i][j].top;
                }
            }
            if(gamePhase == 0){

               canvas.drawText("Player " + i + " Pieces", 15, temp - 15, black);
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
