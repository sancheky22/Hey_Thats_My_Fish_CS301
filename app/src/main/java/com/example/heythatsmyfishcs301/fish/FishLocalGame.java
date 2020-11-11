package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.LocalGame;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

public class FishLocalGame extends LocalGame {
    //This is called after each turn and it sends a copy of the game state to the next player
    private FishGameState fState;
    private FishTile[][] board;

    public FishLocalGame(){
        this.fState = new FishGameState();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        FishGameState copy = new FishGameState(fState);
        p.sendInfo(copy);
    }

    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx == this.fState.getPlayerTurn()){
            return true;
        }
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        String win = null;
        board = fState.getBoardState();

        for(int i =0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    if (board[i][j].hasPenguin() && board[i][j].getPenguin().getPlayer() == 1) {

                    }
                }
            }
        }

        return win;
    }


    //This method is called whenever a new action arrives from a player.\
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

        //if computer makes a move, we want to set the score it obtains in the
        //actual gamestate then change the player turn back to human
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
