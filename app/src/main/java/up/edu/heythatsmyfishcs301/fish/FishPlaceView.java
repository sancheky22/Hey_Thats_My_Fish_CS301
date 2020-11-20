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
    private final int PENGUIN_SIZE = 115;

    private int cWidth;
    private int cHeight;

    private final String p1Place = "Player 1 Pieces:";
    private final String p2Place = "Player 2 Pieces:";
    private final String p3Place = "Player 3 Pieces:";
    private final String p4Place = "Player 4 Pieces:";

    private FishGameState gameState;

    private Rect[][] rects;

    Bitmap redPenguin = null;
    Bitmap resizedRedPenguin = null;

    private Paint black = new Paint();

    public FishPlaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        gameState = new FishGameState();
        numPlayers = 2;
        rects = new Rect[2][4];

        int offSet = 100;

        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < 6 - numPlayers; j++){
                rects[i][j] = new Rect(10 + j * PENGUIN_SIZE, 10 + i * 200 + offSet, (j + 1) * PENGUIN_SIZE, (i + 1) * 200 + offSet);
            }
            offSet += 100;
        }


        black.setColor(Color.BLACK);
        black.setTextSize(70);

        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.redpenguin);
        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin, 115, 115, false);


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
        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();

        Rect bound = new Rect(0, 0, cWidth, cHeight);

        numPlayers = 2;

        for(int i = 0; i < rects.length; i++){
            for(int j = 0; j < rects[i].length; j++) {
                if(!(gameState.getPieceArray()[i][j].isOnBoard())){
                    canvas.drawRect(rects[i][j], black);
                }
            }
        }

        if(gamePhase == 0){
            if(numPlayers == 2){
                //init player 1's penguins
                canvas.drawText(p1Place, (float)15.0, (float)50, black);

                //draw player one's penguin
                canvas.drawBitmap(resizedRedPenguin, 30, 60, null);
                canvas.drawBitmap(resizedRedPenguin, 120, 60, null);

                //init player 2's penguins
                canvas.drawText(p2Place, (float)15.0, (float)400, black);
            }


            if(numPlayers == 3){
                canvas.drawText(p3Place, (float)15.0, (float)750, black);
            }
            else if(numPlayers == 4){
                canvas.drawText(p3Place, (float)15.0, (float)750, black);
                canvas.drawText(p4Place, (float)15.0, (float)750, black);
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
