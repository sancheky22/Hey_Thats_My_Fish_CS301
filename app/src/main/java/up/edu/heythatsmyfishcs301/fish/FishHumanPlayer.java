package up.edu.heythatsmyfishcs301.fish;

import android.app.Dialog;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;

import up.edu.heythatsmyfishcs301.R;
import up.edu.heythatsmyfishcs301.game.GameHumanPlayer;
import up.edu.heythatsmyfishcs301.game.GameMainActivity;
import up.edu.heythatsmyfishcs301.game.infoMsg.GameInfo;

/**
 *
 * The FishHumanPlayer handles all of the touch events and surface view interactions
 * that the human sees. Methods in this class handle touch events from the human
 * and update the Surface View accordingly.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishHumanPlayer extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener {
    //Tag for logging
    private static final String TAG = "FishHumanPlayer";

    // the most recent game state, given to us by the FishLocalGame
    private FishGameState gameState;
    private boolean outOfGame;

    // the android activity that we are running
    private GameMainActivity myActivity;

    // the surface view
    private FishView surfaceView;
    private ScoresDrawings scores;
    private FishPlaceView fishPlace;

    //buttons
    private Button restartButton;
    private Button infoButton;

    // create local board variable and local penguin variable
    private FishPenguin selectedPenguin;
    private FishPenguin[][] pieces;
    private Rect selectedRect;
    FishTile dest;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    // current player turn
    int turn;

    //Variables that connect the rect array to the piece array
    int px = 0;
    int py = 0;

    Rect[][] rectArr;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public FishHumanPlayer(String name) {
        super(name);
    }

    // Returns the top_gui of the game
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    //This method is called when the human player receives the copy of the game state.
    //This method is in charge of updating FishView I think
    @Override
    public void receiveInfo(GameInfo info) {

        // checks if SV is null
        if (surfaceView == null) return;

        if (fishPlace == null) return;


        // ignore the message if it's not a FishGameState message
        if (!(info instanceof FishGameState)) {
            return;
        }
        else {
            // update the state
            // initialize currently selectedPenguin
            selectedPenguin = null;
            // gets gameState
            gameState = (FishGameState) info;

            // initialize piece array
            pieces = gameState.getPieceArray();

            // sets player turn
            turn = gameState.getPlayerTurn();

            // set current player score
            scores.setNumPlayers(gameState.getNumPlayers());
            scores.setP1Scores(gameState.getPlayer1Score());
            scores.setP2Score(gameState.getPlayer2Score());
            scores.setP3Score(gameState.getPlayer3Score());
            scores.setP4Score(gameState.getPlayer4Score());

            //send how many players there are to the placePenguin view
            fishPlace.setNumPlayers(gameState.getNumPlayers());
            fishPlace.setGamePhase(gameState.getGamePhase());
            fishPlace.setGameState(gameState);

            fishPlace.invalidate();

            // update the surface view
            surfaceView.setGameState(new FishGameState((FishGameState)info));
            surfaceView.invalidate();
            // invalidate the scores TextView to update
            scores.invalidate();
        }
    }

    /**
     * Set the activity as the gui for the device.
     *
     * @param activity
     */
    @Override
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;
        activity.setContentView(R.layout.activity_main);
        surfaceView = activity.findViewById(R.id.fishView);
        fishPlace = activity.findViewById(R.id.fishPlaceView);

        scores = activity.findViewById(R.id.scoresDrawings);

        fishPlace.setOnTouchListener(this);
        surfaceView.setOnTouchListener(this);

        //buttons
        restartButton = (Button) activity.findViewById(R.id.restartButton);
        restartButton.setOnClickListener(this);

        infoButton = (Button) activity.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(this);

        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (gameState != null) {
            receiveInfo(gameState);
        }
    }

    //This method controls all the touch events for the screen.
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        FishTile[][] b = surfaceView.getGameState().getBoardState();
        rectArr = fishPlace.getRects();

        if(gameState.getNumPlayers() == 3){
            rectArr[playerNum][3] = null;
        }
        else if(gameState.getNumPlayers() == 4){
            rectArr[playerNum][2] = null;
            rectArr[playerNum][3] = null;
        }

        //Local variables for the location of the touch.
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        //If the players are in the place penguin phase
        if (gameState.getGamePhase() == 0) {

            //Select a penguin to be placed.
            if (selectedRect == null) {
                for (int i = 0; i < rectArr.length; i++) {
                    for (int j = 0; j < rectArr[i].length; j++) {
                        if (rectArr[i][j] != null && rectArr[i][j].contains(x, y)) {
                            if (i == playerNum) {
                                selectedRect = rectArr[i][j];
                                Log.d("Selected Rect",
                                        "Selected rect at (" + i + ", " + j + ")");
                                px = i;
                                py = j;
                            }
                        }
                    }
                }
            }

            //Place the selected penguin
            else if (selectedRect != null) {
                for (FishTile[] fishTiles : b) {
                    for (FishTile fishTile : fishTiles) {
                        if (fishTile != null && fishTile.getBoundingBox().contains(x, y)) {

                            if (!fishTile.hasPenguin() && fishTile.getNumFish() == 1) {
                                rectArr[px][py] = null;
                                fishPlace.setRects(rectArr);
                            }

                            dest = fishTile;
                            FishPlaceAction p = new FishPlaceAction(this, dest,
                                    gameState.getPieceArray()[px][py]);
                            game.sendAction(p);
                            selectedRect = null;
                        }
                    }
                }
            }
        }
        //If the game phase == 1
        else {
            //Iterate through the tiles in the 2d board array until you
            //find the one that contains the place where it was touched.
            //There has to be a better way to do this :(
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] != null && b[i][j].getBoundingBox().contains(x, y)) {
                        //the player has clicked this bounding box.

                        Log.d("From FishView", "Touched the Fish View at: " + i + ", " + j);
                        Log.d("From human player", "Current player turn is " + turn);

                        if (selectedPenguin == null) {

                            if (b[i][j].getPenguin() == null) { return false; }

                            else if (b[i][j].getPenguin().getPlayer() == this.playerNum) {
                                //The player has selected this penguin to move
                                selectedPenguin = b[i][j].getPenguin();
                                selectedPenguin.setSelected(1);

                                surfaceView.invalidate();
                                Log.d("From Human Player", "Selected a valid penguin");
                            }
                            else {
                                Log.d("From Human Player", "Player expected to touch " +
                                        "a penguin, but did not");
                            }
                        }

                        //If the move is successful, then deselect the penguin.
                        else {
                            FishMoveAction m = new FishMoveAction(this,
                                    selectedPenguin, b[i][j]);

                            game.sendAction(m);
                            Log.d("From Human Player", "Sent action to Local Game");
                            selectedPenguin.setSelected(0);
                            selectedPenguin = null;
                            surfaceView.invalidate();
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This onClick method controls the two buttons on the gui
     * @param button - button passed in from the listener
     */
    @Override
    public void onClick(View button) {
        if(button.equals(restartButton)){
            // restarts game and goes back to main menu
            myActivity.recreate();
        }else if(button.equals(infoButton)){
            openDialog();
        }else{
            return; // do nothing
        }
    }

    /**
     *  External Citation
     *  Date: 20 December 2020
     *  Problem: Creating a dialog popup (for the help menu).
     *  Resource:
     *  https://developer.android.com/guide/topics/ui/dialogs
     *  Solution: I looked at the documentation to help figure
     *  out how to add a dialog popup for the info button.
     */
    public void openDialog() {
        final Dialog dialog = new Dialog(myActivity); // Context, this, etc.
        dialog.setContentView(R.layout.activity_display_help);
        dialog.show();
    }

    public void setRectArr(Rect[][] arr){
        this.rectArr = arr;
    }

    public Rect[][] getRectArr(){
        return this.rectArr;
    }

    public boolean isOut(){
        return this.outOfGame;
    }

    public void setOutOfGame(boolean b){
        this.outOfGame = b;
    }
}