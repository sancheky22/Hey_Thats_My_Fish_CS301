package up.edu.heythatsmyfishcs301.fish;

/**
 * This object contains all the variables that a piece would have such as: player, position, and whether or not it is on the board.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/

public class FishPenguin {

    /**
     * keep track of position x,y,z
     * if they can make a valid move (boolean) loop through array to check if each piece has valid move if not
     * return false and EndGame
     */
    private int player;
    private int xPos;
    private int yPos;
    private boolean onBoard;
    private boolean legalMoves;

    //Constructor for a penguin piece. Needs a player who placed it and the location it was placed in.
    public FishPenguin(int playerNumber){
        this.player = playerNumber;
        this.onBoard = false;
        this.legalMoves = true;
    }

    /**
     * Copy constructor: creates a deep copy
     * @param p: Object to be copied
     */
    public FishPenguin(FishPenguin p){
        this.player = p.getPlayer();
        this.onBoard = p.isOnBoard();
        this.legalMoves = p.hasLegalMoves();
        this.xPos = p.getX();
        this.yPos = p.getY();
    }

    //Getter methods
    public int getX(){
        return this.xPos;
    }

    public int getY(){
        return this.yPos;
    }

    public int getPlayer(){
        return this.player;
    }

    public boolean hasLegalMoves(){
        return this.legalMoves;
    }

    public boolean isOnBoard(){
        return this.onBoard;
    }

    //Setter methods
    public void setXPos(int x){
        this.xPos = x;
    }

    public void setYPos(int y){
        this.yPos = y;
    }

    //This method should probably never ever ever be called but its here anyways just in case i guess
    public void setPlayer(int p){
        this.player = p;
    }

    public void setOnBoard(boolean b){
        this.onBoard = b;
    }

    public void setLegalMoves(boolean b){
        this.legalMoves = b;
    }
}
