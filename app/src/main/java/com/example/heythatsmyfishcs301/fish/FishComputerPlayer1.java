package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GameComputerPlayer;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

import java.util.Random;

public class FishComputerPlayer1 extends GameComputerPlayer {

    //constructor done!
    public FishComputerPlayer1(String name){
        super(name);
    }

    //Computer Player 1 sends a random action to the game state.
    @Override
    protected void receiveInfo(GameInfo info) {
        FishGameState copy = new FishGameState((FishGameState) info);
        //Random r = new Random();
        if (copy.getPlayerTurn() != this.playerNum)
            return;

        //If the game phase is mid-game (Moving penguins)
        else if (copy.getGamePhase() == 1){
            //total penguins
            //0  to length of array-1 = row
            //0 to height of array-1 = column

            // FishPenguin random locations/
            /*
            int penguinRandRow = (int) (copy.getPieceArray().length-1 * Math.random());
            int penguinRandCol = (int) (copy.getPieceArray().length-1 * Math.random());

            // FishTile random locations
            int tileRandRow = (int) (copy.getBoardState().length-1 * Math.random());
            int tileRandCol = (int) (copy.getBoardState().length-1 * Math.random());
            */

            // using our copy of gamestate
            FishTile[][] pieceBoard = copy.getBoardState();
            // loop through
            for(int i =0; i < pieceBoard.length; i++){
                for(int j=0; j< pieceBoard[i].length;j++){
                    if(pieceBoard[i][j]==null){

                    }
                }

            }

            Log.d("Move","Computer Player Moving");
            //FishMoveAction moveAction = new FishMoveAction(this,copy.getPieceArray()[1][0], );
            //game.sendAction(moveAction);
        }
        //If the game phase is set up (Placing Penguins)
        else{
            FishPlaceAction placeAction = new FishPlaceAction(this);
            this.game.sendAction(placeAction);
        }
    }
}
