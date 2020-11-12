package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.game.GameComputerPlayer;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

import java.util.Random;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class should be for the smart AI. Not yet implemented
 **/
public class FishComputerPlayer2 extends GameComputerPlayer {
    //constructor done!
    public FishComputerPlayer2(String name){
        super(name);
    }

    //Computer Player 1 sends a random action to the game state.
    @Override
    protected void receiveInfo(GameInfo info) {
        FishGameState copy = new FishGameState((FishGameState) info);
        Random r = new Random();
        if (copy.getPlayerTurn() != this.playerNum){
            return;
        }
        //If the game phase is mid-game
        else if (copy.getGamePhase() == 1){


        }
        //If the game phase is set up
        else{

        }
    }
}
