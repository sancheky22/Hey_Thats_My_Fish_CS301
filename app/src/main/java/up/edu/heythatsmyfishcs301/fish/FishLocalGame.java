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

        //test if human player has any valid moves left. Does so by going through the whole board and checking
        //if it has a penguin on it then checks if the penguin belongs to the human player. Then it calls the
        //testMove function and if there's no legal moves left, it returns who won. If there is a legal move left
        //it checks if the computer has any valid moves left
        for(int i =0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if(board[i][j] != null && board[i][j].hasPenguin() && board[i][j].getPenguin().getPlayer() == 0) {
                    FishPenguin curPenguin = board[i][j].getPenguin();

                    if((fState.getPlayer1Score() - fState.getPlayer2Score()) > 0){

                    }

                    if(!fState.testMove(curPenguin) && (fState.getPlayer1Score() - fState.getPlayer2Score()) > 0) {
                        Log.d("Position", "Penguin position is " + curPenguin.getX() + "," + curPenguin.getY());
                        return playerNames[0] + " has no moves left and " + playerNames[0] + " won with a score of "
                                + fState.getPlayer1Score();
                    }
                    else if(!fState.testMove(curPenguin) && (fState.getPlayer1Score() - fState.getPlayer2Score()) < 0){
                        return playerNames[0] + " has no moves left and " + playerNames[1] + " won with a score of "
                                + fState.getPlayer2Score();
                    }

                }
            }
        }

        //test if computer player has any moves left. Method to check is the same as above, checking if the human has any valid moves left
        //if the computer player also has valid moves left, then checkGameOver will return null
        for(int i =0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if(board[i][j] != null && board[i][j].hasPenguin() && board[i][j].getPenguin().getPlayer() == 1) {
                    FishPenguin curPenguin = board[i][j].getPenguin();

                    if(!fState.testMove(curPenguin) && (fState.getPlayer1Score() - fState.getPlayer2Score()) > 0) {
                        Log.d("Position", "Penguin position is " + curPenguin.getX() + "," + curPenguin.getY());
                        return playerNames[1] + " has no moves left and " + playerNames[0] + " won with a score of "
                                + fState.getPlayer1Score();
                    }
                    else if(!fState.testMove(curPenguin) && (fState.getPlayer1Score() - fState.getPlayer2Score()) < 0){
                        return playerNames[1] + " has no moves left and " + playerNames[1] + " won with a score of "
                                + fState.getPlayer2Score();
                    }

                }
            }
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
                //this.fState.changeTurn();
                return true;
            }
            else{
                Log.d("makeMove","Move was not legal");
                return false;
            }
        }

        if(action instanceof  FishPlaceAction){
            Log.d("Placing penguins", "Trying to place a penguin");
            FishTile dest = ((FishPlaceAction) action).getDestination();
            Rect rect = ((FishPlaceAction) action).getRect();
            FishPenguin penguin = ((FishPlaceAction) action).getPenguin();

            if(fState.placePenguin(penguin, dest.getX(), dest.getY())){
                Log.d("Place penguin", "penguin is now on tile (" + dest.getX() + ", " + dest.getY() + ")");
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
