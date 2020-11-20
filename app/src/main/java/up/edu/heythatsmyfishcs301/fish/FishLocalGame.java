package up.edu.heythatsmyfishcs301.fish;

import android.graphics.Rect;
import android.util.Log;

import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.LocalGame;
import up.edu.heythatsmyfishcs301.game.actionMsg.GameAction;

/**
 *
 * The FishLocalGame receives the current GameState and sends copies to their respective players.
 * The LocalGame also checks if the game is over by displaying the name of the player who won
 * to the screen.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishLocalGame extends LocalGame {
    //This is called after each turn and it sends a copy of the game state to the next player

    // create GameState variable for this class
    private FishGameState fState;

    // create a local board variable for this class
    private FishTile[][] board;

    // Constructor for the local game that creates a new GameState
    public FishLocalGame(){
        this.fState = new FishGameState();
    }

    // takes a GamePlayer as a paramter
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        // creates a copy of the GameState using its copy constructor
        FishGameState copy = new FishGameState(fState);
        // sends copy to specified player
        p.sendInfo(copy);
    }


    // takes in currentPlayerId
    @Override
    protected boolean canMove(int playerIdx) {
        // checks if a valid move can be made
        if (playerIdx == this.fState.getPlayerTurn()){
            // if the current player and the states player turn match return true since a move can be made!
            return true;
        }
        return false;
    }


    // This method tests if either player has no moves left
    // if that's the case print to the screen who's the player and the winners score
    @Override
    protected String checkIfGameOver() {
        // get a copy of the boardState
        board = fState.getBoardState();


        if (fState.getGamePhase() == 1) {
            for (FishPenguin p : fState.getPieceArray()[fState.getPlayerTurn()]) {
                if (fState.testMove(p)) {
                    return null;
                }
            }
            return "Big oof";
        }
        return null;
    }


    //This method is called whenever a new action arrives from a player.
    //This is where we update the game state
    //This includes changing the turn, updating the Tiles on the board, scores etc.
    @Override
    protected boolean makeMove(GameAction action) {

        //This means that the action is a movement action
        if (action instanceof FishMoveAction){
            Log.d("makeMove @LocalGame", "Someone made a move");
            FishTile dest = ((FishMoveAction) action).getDestination();
            FishPenguin penguin = ((FishMoveAction) action).getPenguin();

            if(fState.movePenguin(penguin,dest.getX(),dest.getY())){
                Log.d("makeMove","Move was legal");
                this.fState.changeTurn();
                return true;
            }
            else{
                Log.d("makeMove","Move was not legal");
                return false;
            }
        }

        if(action instanceof FishPlaceAction){
            Log.d("Placing penguins", "Trying to place a penguin");
            FishTile dest = ((FishPlaceAction) action).getDestination();
            FishPenguin penguin = ((FishPlaceAction) action).getPenguin();


            if(fState.placePenguin(penguin, dest.getX(), dest.getY())){
                Log.d("Place penguin", "penguin is now on tile (" + dest.getX() + ", " + dest.getY() + ")");
                this.fState.changeTurn();

                if (fState.getGamePhase() == 0){
                    for (FishPenguin[] arr : fState.getPieceArray()){
                        for (FishPenguin p : arr){
                            if (!p.isOnBoard()){
                                //Then game phase stays the same
                                return true;
                            }
                        }
                    }
                    this.fState.setGamePhase(1);
                }
                return true;
            }
            else{
                Log.d("Place penguin", "Placing penguin failed");
                return false;
            }
        }

        //if computer makes a move, we want to set the score it obtains in the
        //actual GameState then change the player turn back to human
        else if(action instanceof FishComputerMoveAction){
            this.fState.changeTurn();
            int score = ((FishComputerMoveAction) action).getComScore();
            this.fState.changeComScore(score, ((FishComputerMoveAction) action).getPlayNum());
            Log.d("comScore changed","the computer's score is: " + score);
            return true;
        }

        return false;
    }
}
