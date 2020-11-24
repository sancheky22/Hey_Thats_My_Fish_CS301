package up.edu.heythatsmyfishcs301.fish;

import up.edu.heythatsmyfishcs301.game.GameComputerPlayer;
import up.edu.heythatsmyfishcs301.game.infoMsg.GameInfo;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class
 **/
public abstract class FishComputerPlayer extends GameComputerPlayer {
    // boolean
    private boolean outOfGame;


    /**
     * constructor
     *
     * @param name the player's name (e.g., "Ryan")
     */
    public FishComputerPlayer(String name) {
        super(name);
        // setting out of game to false intially
        this.outOfGame = false;
    }

    // getter and setters for outOfGame boolean
    public boolean isOut(){
        return this.outOfGame;
    }

    // setter for outOfGame boolean
    public void setOutOfGame(boolean b){
        this.outOfGame = b;
    }

}
