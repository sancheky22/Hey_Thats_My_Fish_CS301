package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.LocalGame;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

public class FishLocalGame extends LocalGame {
    //This is called after each turn and it sends a copy of the game state to the next player
    private FishGameState fState = new FishGameState();

    public FishLocalGame(){
        super();
        this.fState = new FishGameState();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        FishGameState copy = new FishGameState();
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
        return null;
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
                return true;
            }
            else{
                Log.d("makeMove","Move was not legal");
                return false;
            }
        }

        return false;
    }
}
