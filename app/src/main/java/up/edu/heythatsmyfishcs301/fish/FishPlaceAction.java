package up.edu.heythatsmyfishcs301.fish;

import android.graphics.Rect;

import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.actionMsg.GameAction;

/**
 *
 * CURRENTLY UNIMPLEMENTED
 * The FishPlaceAction will allow the GamePlayer's to place their FishPenguin pieces across the board
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishPlaceAction extends GameAction {
    // Instance variables for PlaceAction
    FishTile destination;
    private FishPenguin penguin;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FishPlaceAction(GamePlayer player, FishTile dest, FishPenguin p) {
        super(player);
        this.destination = dest;
        this.penguin = p;
    }


    // getters for instance variables
    public FishTile getDestination(){
        return this.destination;
    }

    public FishPenguin getPenguin(){
        return this.penguin;
    }

}
