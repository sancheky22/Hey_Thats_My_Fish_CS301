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
    // Variable for current phase of the game
    private int gamePhase;

    // Labels that display on right side of GUI denoting the pieces each player has
    private final String p1Place = "Player 1 Pieces:";
    private final String p2Place = "Player 2 Pieces:";
    private final String p3Place = "Player 3 Pieces:";
    private final String p4Place = "Player 4 Pieces:";

    // instance of the game state
    private FishGameState gameState;
    // max number of players the game can have
    private int numPlayers = 4;
    // temp int
    int temp;

    // array of rectangles that are used for the hitbox of the penguins
    private Rect[][] rects = new Rect[4][4];
    // array to store all the bitmaps corresponding to each hitbox
    private Bitmap[][] bitArr = new Bitmap[4][4];

    // Bitmap variables
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

    // FishPlaceView width,height
    int fpWidth;
    int fpHeight;
    // resize variable for penguins in the
    int resize;
    // boolean to only draw hitboxes once
    boolean drawOnce = true;

    //  paint for hitbox
    private Paint black = new Paint();

    /**
     *
     * @param context
     * @param attrs
     *
     * Constructor for the Surface View that is used to handle PlaceAction events
     *
     */
    public FishPlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        // depending on the number of players, creates a new instance of the gameState
        gameState = new FishGameState(numPlayers);

        // setting text color and size
        black.setColor(Color.BLACK);
        black.setTextSize(50);

        // bitmaps for our penguin pieces
        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.redpenguin);
        orangePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.orangepenguin);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        greenPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.greenpenguin);
        cursedPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.cursed);


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

    /**
     *
     * @param canvas
     *
     * onDraw method that takes in a canvas and is in charge of drawing bitmaps to surface view
     *
     *
     */
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
                    rects[i][j] = new Rect(10 + j * resize, 10 + i * 150 + offSet,
                            (j + 1) * resize, (i + 1) * 150 + offSet);
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
                    bitArr[i][j] = resizedGreenPenguin;
                }
            }
        }

        int numPlayers = gameState.getNumPlayers();

        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < 6-numPlayers; j++) {
                if (rects[i][j] != null) {
                    if (!(gameState.getPieceArray()[i][j].isOnBoard())) {
                        canvas.drawBitmap(bitArr[i][j], rects[i][j].left, rects[i][j].top, null);
                    }
                    temp = rects[i][j].top;
                }
            }
            if (gamePhase == 0) {
                canvas.drawText("Player " + (i + 1) + " Pieces", 15, temp - 15, black);
            }
        }
    }


    // getter for hitbox array
    public Rect[][] getRects(){
        return this.rects;
    }

    // setter for rectangle hitboxes
    public void setRects(Rect[][] arr) {
        for (int i = 0; i<arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                this.rects[i][j] = arr[i][j];
            }
        }
    }

    // setters for current number of players
    public void setNumPlayers(int num){
        this.numPlayers = num;
    }

    // setter for which phase of the game we're in
    public void setGamePhase(int phase){
        this.gamePhase = phase;
    }

    // gets the current gameState
    public FishGameState getGameState(){
        return this.gameState;
    }

    // sets the current gameState
    public void setGameState(FishGameState fgs){
        this.gameState = new FishGameState(fgs);
    }
}
