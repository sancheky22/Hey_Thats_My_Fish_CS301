package up.edu.heythatsmyfishcs301.fish;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.infoMsg.GameState;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This is the GameState. This object holds all of the info of the game such as current positions,
 * points, turns, etc. We pass this object to the localGame which copies it and sends images to the
 * players so that they can make decisions.
 **/

public class FishGameState extends GameState {

    // Variables for our games board
    final int BOARD_HEIGHT = 8;
    final int BOARD_LENGTH = 11;
    private int numPlayers;

    // array of our games players
    private GamePlayer[] players;

    //store the current players turn (0,1,2,3)
    private int playerTurn;

    //store scores for all players
    //Note: if the game has fewer than 4 players, then the extra player scores will stay at 0
    private int player1Score, player2Score, player3Score, player4Score;

    //Stores the phase of the game (0: placement phase, 1: main game, 2: End game screen)
    private int gamePhase;

    //Check if there is still at least one valid move on the board.
    private boolean validMoves;

    //FishTile[][] is a 2d array that stores the hexagons
    private FishTile[][] boardState;
    //FishPenguin[][] is a 2d array to store all the penguins.
    private FishPenguin[][] pieceArray;

    // ArrayList of integers that will hold 1s, 2s, or 3s.
    // Will be used to randomly assign each tile a number of fish
    ArrayList<Integer> fishArray = new ArrayList<>(60);


    /**
     * Default constructor
     * Creates an instance of the game state class
     *
     * @param n - number of players in the game (Initialized from the Main Activity)
     */
    public FishGameState(int n){
        this.playerTurn = 0;
        this.numPlayers = n;
        this.player1Score = 0;
        this.player2Score = 0;
        this.player3Score = 0;
        this.player4Score = 0;
        this.gamePhase = 0;
        this.validMoves = true;


        this.boardState = initializeBoard();
        pieceArray = new FishPenguin[numPlayers][6 - numPlayers];
        pieceArray = initializePieces(numPlayers);
    }


    /**
     * Copy constructor to make deep copies.
     *
     * @param o - object to be copied
     */
     public FishGameState(FishGameState o){
        this.playerTurn = o.playerTurn;
        this.numPlayers = o.numPlayers;
        this.player1Score = o.player1Score;
        this.player2Score = o.player2Score;
        this.player3Score = o.player3Score;
        this.player4Score = o.player4Score;
        this.gamePhase = o.gamePhase;
        this.validMoves = o.validMoves;

        //Deep copies for array mean you have to loop through it and individually copy each element
        this.boardState = new FishTile[BOARD_HEIGHT][BOARD_LENGTH];
        for (int i = 0 ; i < this.boardState.length ; i++) {
            for (int j = 0; j < this.boardState[0].length; j++) {
                this.boardState[i][j] = o.getBoardState()[i][j];
            }
        }

        this.pieceArray = new FishPenguin[numPlayers][6 - numPlayers];
        for (int i = 0 ; i < this.pieceArray.length; i++){
            for(int j = 0 ; j < this.pieceArray[0].length; j++){
                this.pieceArray[i][j] = o.getPieceArray()[i][j];
            }
        }
    }

    /**
     * set players so that the gameState has access to the array of players in the game
     * used to play certain sound effects in the FishHumanPlayer class
     *
     * @param list
     */
    public void setPlayers(GamePlayer[] list){
         this.players = list;
    }

    /**
     * returns the array of players in the game. Used in FishHumanPlayer class to see which type
     * of player is out of the game so that a sound effect can be played
     *
     * @return GamePlayer[]
     */
    public GamePlayer[] getPlayers(){
         return this.players;
    }

    /**
     * testMove takes a penguin object and sees if that object has any valid moves.
     *
     * @param p - penguin to be tested
     * @return boolean, returns true if the penguin has a valid move it can take.
     */
    public boolean testMove(FishPenguin p){
        //Create local variables for these values so the method is easier to read.
        FishTile[][] b = boardState;
        int x = p.getX();
        int y = p.getY();

        Log.d("From TestMove", "Attempting to see " +
                "if a penguin from player " + p.getPlayer() + " has a legal move...");

        //need to test 6 things:
        //+x, -x, +y, -y, +x & -y, -x & +y
        for (int i = -1; i <= 1; i += 2) {
            try {
                //If an adjacent tile has a penguin or does not exist, then it can not be moved to.
                if (b[x + i][y] != null && !b[x + i][y].hasPenguin()
                        && b[x + i][y].doesExist()) {
                    Log.d("From TestMove", "Found a legal move 1");
                    return true;
                }
            }
            catch(Exception ignore){}

            try {
                if (b[x + i][y - i] != null && !b[x + i][y - i].hasPenguin()
                        && b[x + i][y - i].doesExist()) {
                    Log.d("From TestMove", "Found a legal move 3");
                    return true;
                }
            }
            catch(Exception ignore){}

            try {
                if (b[x][y + i] != null && !b[x][y + i].hasPenguin()
                        && b[x][y + i].doesExist()) {
                    Log.d("From TestMove", "Found a legal move 2");
                    return true;
                }
            }
            catch(Exception ignore){}
        }
        Log.d("From TestMove", "Did not find a legal move.");
        return false;
    }


    //Action: When the player moves a penguin onto the board at the beginning of the game.

    /**
     * Place Penguin action is called when a player places their penguin
     * @param p - penguin to be placed onto board.
     * @param x - x coordinate for destination
     * @param y - y coordinate for destination
     * @return boolean, returns true if move is legal
     */
    public boolean placePenguin(FishPenguin p, int x, int y) {
        if(boardState[x][y].hasPenguin() || boardState[x][y].getNumFish() != 1){
            return false;
        }

        boardState[x][y].setPenguin(p);
        boardState[x][y].setHasPenguin(true);
        p.setOnBoard(true);
        p.setXPos(x);
        p.setYPos(y);
        return true;
    }


    /**
     * Called when a player needs to move their penguin
     *
     * @param p - penguin to be moved
     * @param x - x coordinate for destination
     * @param y - y coordinate for destination
     * @return boolean True if the action is legal
     */
    public boolean movePenguin(FishPenguin p, int x, int y){
        if (reachable(p,boardState[x][y])){
            int px = p.getX();
            int py = p.getY();
            //If the move is legal, then add to the player's score the fish on the tile and remove the
            //tile from the game. Then pass the turn and return true.
            addScore(playerTurn, boardState[px][py].getNumFish());
            boardState[px][py].setExists(false);
            boardState[px][py].setHasPenguin(false);
            p.setXPos(x);
            p.setYPos(y);
            boardState[x][y].setPenguin(p);
            boardState[x][y].setHasPenguin(true);
            return true;
        }
        return false;
    }

    /**
     * Returns true if a penguin can move to a tile
     * @param p penguin
     * @param t tile
     * @return true if p can move to t
     */
    public boolean reachable(FishPenguin p, FishTile t){
        if (this.getGamePhase() == 0) return false;
        // if the penguin is null return
        if (p == null) return false;

        // get x and y coordinates of penguin
        int px = p.getX();
        int py = p.getY();

        // get x and y coordinates of FishTile
        int x = t.getX();
        int y = t.getY();

        //Make sure the penguin is not moving to the same tile
        if(px == x && py == y){
            return false;
        }

        //Make sure that the space you are moving to exists (might be redundant later im not sure)
        if (!boardState[x][y].doesExist()){
            return false;
        }

        //0 means horizontal, 1 means down right diag, 2 means up right diag
        int direction;
        if (py == y){
            direction = 1;
        }
        else if (px == x){
            direction = 0;
        }
        else if (py+px == x+y){
            direction = 2;
        }
        else{
            return false;
        }

        //If the new move is up left diag or down right diag
        if (direction == 1){
            //s is the sign of (new coordinate - old coordinate)
            //if s is positive, then you are moving to the right
            int s = Integer.signum(x-px);
            for (int i = px+s; i != x + s; i+=s){
                if (boardState[i][py].hasPenguin() || !boardState[i][py].doesExist()){
                    return false;
                }
            }
        }

        //If the new move is horizontal (left or right)
        else if (direction == 0){
            int s = Integer.signum(y-py);
            for (int i = py+s; i != y + s; i+=s){
                if (boardState[px][i].hasPenguin() || !boardState[px][i].doesExist()){
                    return false;
                }
            }
        }

        //If the new move is up right diagonal or down left diagonal
        else {
            //If s is positive, you are moving upper right diagonal
            int s = Integer.signum(y-py);
            for (int i = s; i != y-py + s; i+=s){
                if (boardState[px-i][py+i].hasPenguin() || !boardState[px-i][py+i].doesExist()){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Helper method to switch the turn of the game state
     */
    public void changeTurn() {
        this.playerTurn = (this.playerTurn+1)%this.numPlayers;
    }


    /**
     * Helper method that is called whenever a player's score needs to be incremented
     * @param pT Player's score to be edited
     * @param s the score to be added
     */
     public void addScore(int pT, int s){
        switch(pT){
            case 0:
                setPlayer1Score(getPlayer1Score()+s);
                break;
            case 1:
                setPlayer2Score(getPlayer2Score()+s);
                break;
            case 2:
                setPlayer3Score(getPlayer3Score()+s);
                break;
            case 3:
                setPlayer4Score(getPlayer4Score()+s);
                break;
        }
    }


    /*
     * External Citation
     * Date: October 12, 2020
     * Problem: wanted to index a hexagonal tiling in a clever way.
     * Resource: https://www.redblobgames.com/grids/hexagons/ ("Map storage in axial coordinates")
     *
     * Because there are three different independent directions player can move, we needed a third
     * way to check if they were moving in a straight line. So the solution to this is to just model
     * it cleverly as a 2d array by adding some offsets. The above resource explains it well, but
     * basically it makes it very easy to check if you are moving in a straight line.
     *
     * This is what a 6x4 hexagon array looks like in our 2d array. 0's represent hexes
     * and x's represent null spaces.
     *      x x 0 0 0 0
     *      x x 0 0 0 0
     *      x 0 0 0 0 x
     *      x 0 0 0 0 x
     *      0 0 0 0 x x
     *      0 0 0 0 x x
     *
     * The reason we choose to index them in this way is because we can easily
     * check if a hexagon is adjacent to another hexagon.
     * Each hex has 6 neighbors and they lok like this:
     *
     *  x 0 0
     *  0 h 0
     *  0 0 x
     *
     *  where h's neighbors are the 0's.
     *  a nice consequence of this arrangement is that we can check if two hexagons
     *  lie on the same line very easily. There are three axes that they could lie on: horizontal,
     *  upper right, and lower right. These correspond in the 2d array as: same x value,
     *  same y value, and same x+y value, respectively. For example, if we have two hexagons at
     *  p1 = (2,4) and p2 = (5,4), then we know those two hexagons are on the same upper right line
     *  because y1=y2
     */

    /**
     *
     * Helper method to initialize the board at the beginning of the game.
     * This method constructs the board based on the 2d array above.
     * This also means that it has to initialize the number of fish on each of the tiles
     * based on a specific distribution (30 ones, 20 twos, 10 threes).
     * @return FishTile[][], This is the board of the game.
     */
     private FishTile[][] initializeBoard(){
        //n is the number of null cells you need at the beginning of the array
        int n;
        //c is the number of real tiles you have in a specific row.
         // If the row is even, c = 8, if row is odd c = 7
        int c;
        FishTile t;
        FishTile[][] f = new FishTile[BOARD_HEIGHT][BOARD_LENGTH];
        int counter = 0;
        initFish();
        //Loop through a 2d array and initialize each hexagon
        for (int i = 0; i < BOARD_HEIGHT;i++)
        {
            //Basically this value of n is based on i and it tells you how many null cells
            // you need at the start of the row.
            //(i,n): (1,3), (2,3), (3,2), (4,2), (5,1)....
            n = 4-((i+1)+(i+1)%2)/2;
            c = 0;
            for (int j = 0; j < BOARD_LENGTH;j++)
            {
                //if n is not 0, then there are still null cells that must be placed into the array.
                //if i is even then c will hit 8 and then will start storing null cells in the array.
                //This all results in an array where the null cells are where they need to be,
                //and an extra null cell for odd rows.
                if (n!=0 || c == (8 - i%2)) {
                    t = null;
                    n--;
                }
                else {
                    t = new FishTile(i, j);
                    t.setNumFish(fishArray.get(counter));
                    counter++;
                    c++;
                }
                f[i][j] = t;
            }
        }
        return f;
    }

    /**
     *  External Citation
     * Date: November 9, 2020
     * Problem: need to shuffle the array so that it's random and not just groups of 1s, 2s and 3s
     * Resource: https://www.geeksforgeeks.org/shuffle-or-randomize-a-list-in-java/
     *
     * Solution: Used the Collections.shuffle() to randomize the array
     *
     * This method starts by adding thirty 1's into the fishArray arraylist, then it adds twenty
     * 2's, and finally ten 3's. It then shuffles the ArrayList so that when we go through the 2d
     * array of fishTiles to assign each tile a numFish, it will be random
     */
    public void initFish() {
        for (int i = 0; i < 30; i++) {
            fishArray.add(1);
        }

        for (int i = 0; i < 20; i++) {
            fishArray.add(2);
        }

        for (int i = 0; i < 10; i++) {
            fishArray.add(3);
        }

        Collections.shuffle(fishArray);
    }

    /**
     *
     * Helper method to initialize the array of penguin pieces that belong to each player.
     * The first coordinate represents the player number and the second coordinate represents the
     * index of the penguin. The parameter n in this context is the number of players (2-4)
     * The rules of the game state that for 2 player games, each player has 4 penguins. 3 players
     * have 3 penguins each, and 4 players have 2 each. These numbers are pretty arbitrary but it
     * works out nicely because the follow a simple pattern: PenguinsPerPlayer = 6-numPlayers
     *
     * @param n number of players
     * @return FishPenguin[][], the array that is initialilzed.
     */
    private FishPenguin[][] initializePieces(int n){
        int m = 6-n;
        FishPenguin tempguin;
        FishPenguin[][] p = new FishPenguin[n][m];
        for (int i=0;i<p.length;i++){
            for (int j=0;j<p[0].length;j++){
                tempguin = new FishPenguin(i);
                tempguin.setPlayer(i);
                tempguin.setOnBoard(false);
                p[i][j] = tempguin;
            }
        }
        return p;
    }


    /**
     * Getter methods for instance variables
     */

    public int getPlayerTurn() {
        return this.playerTurn;
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public int getPlayer1Score(){
        return this.player1Score;
    }

    public int getPlayer2Score() {
        return this.player2Score;
    }

    public int getPlayer3Score(){
        return this.player3Score;
    }

    public int getPlayer4Score(){
        return this.player4Score;
    }

    public int getGamePhase(){
        return this.gamePhase;
    }

    public boolean getValidMoves(){
        return this.validMoves;
    }

    public FishTile[][] getBoardState(){
        return this.boardState;
    }

    public FishPenguin[][] getPieceArray(){
        return this.pieceArray;
    }

    /**
     * Setter methods for instance variables
     */

    public void setPlayerTurn(int x) {
        this.playerTurn = x;
    }

    public void setNumPlayers(int x) {
       this.numPlayers = x;
    }

    public void setPlayer1Score(int x){
        this.player1Score = x;
    }

    public void setPlayer2Score(int x) {
        this.player2Score = x;
    }

    public void setPlayer3Score(int x){
        this.player3Score = x;
    }

    public void setPlayer4Score(int x){
        this.player4Score = x;
    }

    public void setGamePhase(int x){
        this.gamePhase = x;
    }

    public void setValidMoves(boolean x){
        this.validMoves = x;
    }
}