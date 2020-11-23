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
    private FishTile[][] board;


    // Constructor for the local game that creates a new GameState
    public FishLocalGame(int numPlayers){
        super();
        this.fState = new FishGameState(numPlayers);
    }

    // takes a GamePlayer as a parameter
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        // creates a copy of the GameState using its copy constructor
        fState.setNumPlayers(players.length);
        FishGameState copy = new FishGameState(fState);
        // sends copy to specified player
        p.sendInfo(copy);
    }


    // takes in currentPlayerId
    @Override
    protected boolean canMove(int playerIdx) {
        // checks if a valid move can be made
        return (playerIdx == this.fState.getPlayerTurn());
    }


    // This method tests if either player has no moves left
    // if that's the case print to the screen who's the player and the winners score
    @Override
    protected String checkIfGameOver() {
        String end = "Game Over";

        // get a copy of the boardState
        board = fState.getBoardState();
        int playerTurn = fState.getPlayerTurn();

        //The game can not end in the place penguin phase
        if (fState.getGamePhase() == 0){
            return null;
        }

        //Checks if the next player has any legal moves.
        //If they do, then don't end the game.
        for (FishPenguin p : fState.getPieceArray()[(playerTurn+1)%fState.getNumPlayers()]){
            if (fState.testMove(p)) {
                return null;
            }
        }

        //If they don't then you can remove their penguins from the board and perform
        //all of the necessary actions (removing tiles, checking scores, etc.)
        for (FishPenguin p : fState.getPieceArray()[(playerTurn+1)%fState.getNumPlayers()]){
            p.setOnBoard(false);
            int px = p.getX();
            int py = p.getY();
            board[px][py].setHasPenguin(false);
            board[px][py].setPenguin(null);
            board[px][py].setExists(false);
            fState.addScore(playerTurn,board[px][py].getNumFish());
            fState.changeTurn();
        }

        for (FishPenguin[] arr : fState.getPieceArray()){
            for (FishPenguin p : arr){
                if (!p.isOnBoard()){
                    return null;
                }
            }
        }

        return end;
        //get a copy of the piece array
        //pieces = fState.getPieceArray();

        //test if human player has any valid moves left. Does so by going through the whole board and checking
        //if it has a penguin on it then checks if the penguin belongs to the human player. Then it calls the
        //testMove function and if there's no legal moves left, it returns who won. If there is a legal move left
        //it checks if the computer has any valid moves left
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
            this.fState.changeComScore(score);
            Log.d("comScore changed","the computer's score is: " + score);
            return true;
        }

        return false;
    }
}
