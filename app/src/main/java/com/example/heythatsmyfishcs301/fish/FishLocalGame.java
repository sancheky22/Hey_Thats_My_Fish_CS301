package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.LocalGame;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

public class FishLocalGame extends LocalGame {
    //This is called after each turn and it sends a copy of the game state to the next player
    private FishGameState fState;

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        FishGameState copy = new FishGameState(this.fState);
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


    //This method is called whenever a new action arrives from a player.
    @Override
    protected boolean makeMove(GameAction action) {

        return false;
    }
}
