package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class is needed in order for the computer player to make a move
 * An instance of this class is created whenever the computer makes a
 * move and the action is sent to the gameState (that is done in the
 * FishLocalGame class)
 **/
public class FishComputerMoveAction extends GameAction {

    private FishPenguin penguin;
    private FishTile destination;
    private int comScore;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FishComputerMoveAction(GamePlayer player, FishPenguin penguin, FishTile tile, int score) {
        super(player);
        this.penguin = penguin;
        this.destination = tile;
        this.comScore = score;
    }

    //Needed in order to send the computer's score to the gamestate
    public int getComScore(){
        return this.comScore;
    }
}
