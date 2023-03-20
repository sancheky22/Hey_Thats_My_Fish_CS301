package up.edu.heythatsmyfishcs301.fish;

import up.edu.heythatsmyfishcs301.game.GameComputerPlayer;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class inherits from GameComputerPlayer and was made so that each of our games
 * computer players (Dumb and Smart) have a outOfGame boolean. This boolean allows us to easily
 * remove players from the game when they have no valid moves left.
 *
 **/
public abstract class FishComputerPlayer extends GameComputerPlayer {
    // Boolean to represent if a player is no longer in the game
    private boolean outOfGame;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "Ryan")
     */
    public FishComputerPlayer(String name) {
        super(name);
        // Setting out of game to false initially
        this.outOfGame = false;
    }

    // Getter and setters for outOfGame boolean
    public boolean isOut(){
        return this.outOfGame;
    }

    // Setter for outOfGame boolean
    public void setOutOfGame(boolean b){
        this.outOfGame = b;
    }

}
