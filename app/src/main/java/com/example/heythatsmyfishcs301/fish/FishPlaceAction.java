package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

public class FishPlaceAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FishPlaceAction(GamePlayer player) {
        super(player);
    }
}
