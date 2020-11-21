package up.edu.heythatsmyfishcs301.fish;

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
import up.edu.heythatsmyfishcs301.R;

/**
 * Description: The scoresDrawings class draws the fishes that displays the scores. The scores
 * are displayed and updated to the current player1 and player2 scores.

 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/

public class ScoresDrawings extends SurfaceView {

    Bitmap orangeFish = null;
    Bitmap redFish = null;
    Bitmap blueFish = null;
    Bitmap resizedOrange = null;
    Bitmap resizedRed = null;
    Bitmap resizedBlue = null;
    private Paint black = new Paint();

    int p1Score;
    int p2Score;

    FishGameState fstate;

    public ScoresDrawings(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        black.setColor(Color.BLACK);
        black.setTextSize(50);

        fstate = new FishGameState(3);

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
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.scoresDrawings);
        sfvTrack.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        /**
         *External Citation
         * Date: 9/18/20
         * Problem: Needed to draw the fish png files to the surfaceview
         * Resource: Class lecture for creating the bitmap, and also https://stackoverflow.com/quest
         * ions/17839388/creating-a-scaled-bitmap-with-createscaledbitmap-in-android
         * to scale the images
         * Created by: Dr. Andrew Nuxoll (9/17/2020) and Geobits (7/24/2013)
         *
         * Solution: We referenced the examples from lecture and the stackoverflow tutorial to crea
         * te a bitmap of our image and then resize it to fit the surfaceview
         */

        //Getting our fishes and resizing them for the scoresDrawing surface View
        orangeFish = BitmapFactory.decodeResource(getResources(), R.drawable.orangefish);
        resizedOrange = Bitmap.createScaledBitmap(orangeFish, 300, 300, false);

        redFish = BitmapFactory.decodeResource(getResources(), R.drawable.redfish);
        resizedRed = Bitmap.createScaledBitmap(redFish, 300, 300, false);

        blueFish = BitmapFactory.decodeResource(getResources(), R.drawable.bluefish);
        resizedBlue = Bitmap.createScaledBitmap(blueFish, 300, 300, false);
    }


    @Override
    public void onDraw(Canvas canvas) {

        //drawing our resized fishes using bitmap
        canvas.drawBitmap(this.resizedOrange, 1.0f, 1.0f, null);
        canvas.drawBitmap(this.resizedRed, 0.0f, 300.0f, null);

        /**
         *External Citation
         * Date: 9/18/20
         * Problem: Forgot how to turn an int into a string
         * Resource: https://www.geeksforgeeks.org/different-ways-for-integer-to-string-conversion
         * s-in-java/
         * Created by: Geeksforgeeks (12/08/2020)
         *
         * Solution: Used the Integer.toString method shown on the website
         */
        //draws our scores for player1 and player 2 onto our fishes
        canvas.drawText(Integer.toString(p1Score), (float)140.0, (float)160.0, black);
        canvas.drawText(Integer.toString(p2Score), (float)140.0, (float)465.0, black);
    }

    //setter methods to be used in FishHumanPlayer
    public void setP1Scores(int i){
        System.out.println("Setting p1 score to: " + i);
        p1Score = i;
    }

    public void setP2Score(int i){
        p2Score = i;
    }

}//ScoresDrawings