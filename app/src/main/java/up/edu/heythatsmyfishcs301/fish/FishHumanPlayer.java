package up.edu.heythatsmyfishcs301.fish;

import android.app.Dialog;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import up.edu.heythatsmyfishcs301.R;
import up.edu.heythatsmyfishcs301.game.GameHumanPlayer;
import up.edu.heythatsmyfishcs301.game.GameMainActivity;
import up.edu.heythatsmyfishcs301.game.GamePlayer;
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
    private FishMainActivity main;

    // the surface view
    private FishView surfaceView;
    private FishScoresDrawings scores;
    private FishPlaceView fishPlace;

    //buttons
    private Button restartButton;
    private Button infoButton;
    private Button muteButton;

    // create local board variable and local penguin variable
    private FishPenguin selectedPenguin;
    private Rect selectedRect;
    FishTile dest;

    // current player turn
    int turn;

    //Variables that connect the rect array to the piece array
    int px = 0;
    int py = 0;

    boolean p0PlayOnce = true;
    boolean p1PlayOnce = true;
    boolean p2PlayOnce = true;
    boolean p3PlayOnce = true;
    int numOut = 0;

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

        List<Integer> scoreList = new ArrayList<>();

        // checks if SV is null
        if (surfaceView == null) return;

        if (fishPlace == null) return;

        // ignore the message if it's not a FishGameState message
        if (info instanceof FishGameState) {
            // update the state
            // initialize currently selectedPenguin
            selectedPenguin = null;
            // gets gameState
            gameState = (FishGameState) info;

            // sets player turn
            turn = gameState.getPlayerTurn();

            GamePlayer[] temp = gameState.getPlayers();

            // go through all the player to see if any of them are out of the game. If a player is
            // out of the game, play the death sound and set the player's corresponding boolean to
            // false so that none of the sounds will play repeatedly
            if(gameState.getPlayers() != null && numOut != allPlayerNames.length){
                    for(int i = 0; i < gameState.getPlayers().length; i++){
                        if(temp[i] instanceof FishComputerPlayer){
                            FishComputerPlayer computer = (FishComputerPlayer) temp[i];
                            if(computer.isOut()){
                                if(i == 0 && p0PlayOnce){
                                    main.playSound(0);
                                    p0PlayOnce = false;
                                    numOut++;
                                }
                                else if(i == 1 && p1PlayOnce){
                                    main.playSound(0);
                                    p1PlayOnce = false;
                                    numOut++;
                                }
                                else if(i == 2 && p2PlayOnce){
                                    main.playSound(0);
                                    p2PlayOnce = false;
                                    numOut++;
                                }
                                else if(i == 3 && p3PlayOnce){
                                    main.playSound(0);
                                    p3PlayOnce = false;
                                    numOut++;
                                }

                            }
                        }
                        else if(temp[i] instanceof FishHumanPlayer){
                            if(this.isOut()){
                                numOut++;
                            }
                        }
                    }
            }

            // if all players are out of the game, check if the human is the winner to play the
            // special sound
            if(numOut == allPlayerNames.length){
                scoreList.add(gameState.getPlayer1Score());
                scoreList.add(gameState.getPlayer2Score());
                scoreList.add(gameState.getPlayer3Score());
                scoreList.add(gameState.getPlayer4Score());

                int maxScore = Collections.max(scoreList);

                // if the human player is the winner, play the winning sound effect (sound 1).
                // In some cases, the death sound may overlap with the winning sound effect
                // so we want to stop it first before we play the sound we want
                if (maxScore == gameState.getPlayer1Score() && playerNum == 0) {
                    main.stopDeathSound();
                    main.playSound(1);
                } else if (maxScore == gameState.getPlayer2Score() && playerNum == 1) {
                    main.stopDeathSound();
                    main.playSound(1);
                } else if (maxScore == gameState.getPlayer3Score() && playerNum == 2) {
                    main.stopDeathSound();
                    main.playSound(1);
                } else if (maxScore == gameState.getPlayer4Score() && playerNum == 3) {
                    main.stopDeathSound();
                    main.playSound(1);
                }
            }


            // set current player score
            scores.setNumPlayers(gameState.getNumPlayers());
            scores.setP1Scores(gameState.getPlayer1Score());
            scores.setP2Score(gameState.getPlayer2Score());
            scores.setP3Score(gameState.getPlayer3Score());
            scores.setP4Score(gameState.getPlayer4Score());

            //send how many players there are to the placePenguin view
            fishPlace.setNumPlayers(gameState.getNumPlayers());
            fishPlace.setGamePhase(gameState.getGamePhase());
            fishPlace.setNames(allPlayerNames);
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
     * @param activity activity to be set as gui
     */
    @Override
    public void setAsGui(GameMainActivity activity) {
        main = (FishMainActivity) activity;

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

        muteButton = (Button) activity.findViewById(R.id.muteButton);
        muteButton.setOnClickListener(this);

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
        //We use two variables because we have multiple surface views
        int placeX = 0;
        int placeY = 0;

        int boardX = 0;
        int boardY = 0;

        if (view.equals(fishPlace)) {
            placeX = (int) motionEvent.getX();
            placeY = (int) motionEvent.getY();
        }

        if (view.equals(surfaceView)) {
            boardX = (int) motionEvent.getX();
            boardY = (int) motionEvent.getY();
        }


        //If the players are in the place penguin phase
        if (gameState.getGamePhase() == 0) {
            //Select a penguin to be placed.
            if (selectedRect == null) {
                for (int i = 0; i < rectArr.length; i++) {
                    for (int j = 0; j < rectArr[i].length; j++) {
                        if (rectArr[i][j] != null && rectArr[i][j].contains(placeX, placeY)) {
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
                        if (fishTile != null && fishTile.getBoundingBox().contains(boardX, boardY)) {

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
            rectArr = null;
            //Iterate through the tiles in the 2d board array until you
            //find the one that contains the place where it was touched.
            //There has to be a better way to do this :(
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] != null && b[i][j].getBoundingBox().contains(boardX, boardY)) {
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
            // also stops playing the penguin theme
            main.stopMedia();
            myActivity.recreate();
        } else if(button.equals(infoButton)){
            // pops up the help menu when the info button is pressed
            openDialog();
        }
        else if(button.equals(muteButton)){
            // When the muteButton is pressed it starts and stops the music and toggles
            // what displays on the button (Unmute and Mute)
            if(main.getIsPlaying()){
                main.stopMedia();
                muteButton.setText("Unmute");
            }
            else{
                main.startMedia();
                muteButton.setText("Mute");
            }

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