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
    Bitmap greenFish = null;
    Bitmap resizedOrange = null;
    Bitmap resizedRed = null;
    Bitmap resizedBlue = null;
    Bitmap resizedGreenFish = null;

    // Bitmap cursedFish = null;
    //Bitmap resizedCursedFish = null;
    private Paint black = new Paint();

    int numPlayers = 2;

    int p1Score;
    int p2Score;
    int p3Score;
    int p4Score;

    FishGameState fstate;

    public ScoresDrawings(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        black.setColor(Color.BLACK);
        black.setTextSize(50);

        fstate = new FishGameState(2);

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

        greenFish = BitmapFactory.decodeResource(getResources(), R.drawable.greenfish);
        resizedGreenFish = Bitmap.createScaledBitmap(greenFish, 300, 300, false);

        //cursedFish = BitmapFactory.decodeResource(getResources(), R.drawable.blackfish);
        //resizedCursedFish = Bitmap.createScaledBitmap(cursedFish, 300, 300, false);


    }


    @Override
    public void onDraw(Canvas canvas) {

        //drawing our resized fishes using bitmap
           int sWidth =  canvas.getWidth();
           int sHeight = canvas.getHeight();
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
        canvas.drawBitmap(this.resizedOrange, 0, 0, null);
        canvas.drawText(Integer.toString(p1Score), sWidth/2, (float)sHeight*.17f, black);

        canvas.drawBitmap(this.resizedRed, 0, sHeight/4, null);
        canvas.drawText(Integer.toString(p2Score), sWidth/2, (float)sHeight*.42f, black);

        if(numPlayers == 3){
            canvas.drawBitmap(this.resizedBlue, 0, sHeight/2, null);
            canvas.drawText(Integer.toString(p3Score), sWidth/2, (float)sHeight*.67f, black);
        }
        else if(numPlayers == 4){
            canvas.drawBitmap(this.resizedBlue, 0, sHeight/2, null);
            canvas.drawText(Integer.toString(p3Score), sWidth/2, (float)sHeight*.67f, black);

            canvas.drawBitmap(this.resizedGreenFish, 0, 3*sHeight/4, null);
            canvas.drawText(Integer.toString(p4Score), sWidth/2, (float)sHeight*.92f, black);
        }




    }

    //setter methods to be used in FishHumanPlayer
    public void setP1Scores(int i){
        System.out.println("Setting p1 score to: " + i);
        p1Score = i;
    }

    public void setP2Score(int i){
        p2Score = i;
    }

    public void setP3Score(int i){
        p3Score = i;
    }

    public void setP4Score(int i){
        p4Score = i;
    }

    public void setNumPlayers(int i){this.numPlayers = i;}

}//ScoresDrawings