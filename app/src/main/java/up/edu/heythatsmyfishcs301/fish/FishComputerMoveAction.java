package up.edu.heythatsmyfishcs301.fish;

import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.actionMsg.GameAction;

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
    private int playNum;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FishComputerMoveAction(GamePlayer player, FishPenguin penguin, FishTile tile, int score, int playerNum) {
        super(player);
        this.penguin = penguin;
        this.destination = tile;
        this.comScore = score;
        this.playNum = playerNum;
    }

    //Needed in order to send the computer's score to the gamestate
    public int getComScore(){
        return this.comScore;
    }

    //needed in order to update the correct computer's score in the localGame
    public int getPlayNum() {
        return this.playNum;
    }
}
