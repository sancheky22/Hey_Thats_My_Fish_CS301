package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

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

    public FishPenguin getPenguin(){
        return this.penguin;
    }

    public FishTile getDestination(){
        return this.destination;
    }

    public int getComScore(){
        return this.comScore;
    }
}
