package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.infoMsg.GameState;

import java.lang.Integer;
import static java.lang.Math.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/


public class FishGameState extends GameState {


    final int BOARD_HEIGHT = 8;
    final int BOARD_LENGTH = 11;

    //store the current players turn (0,1,2,3)
    private int playerTurn;

    // number of players playing the game
    private int numPlayers;

    //store scores for all players
    //Note: if the game has fewer than 4 players, then the non-existent player scores will stay at 0
    private int player1Score;
    private int player2Score;
    private int player3Score;
    private int player4Score;

    //Stores the phase of the game (0: placement phase, 1: main game, 2: End game screen)
    private int gamePhase;

    //Check if there is still at least one valid move on the board.
    private boolean validMoves;

    //FishTile[][] is a 2d array that stores the hexagons
    private FishTile[][] boardState;
    //FishPenguin[][] is a 2d array to store all the penguins.
    private FishPenguin[][] pieceArray;

    //Arraylist of integers that will hold 1s, 2s, or 3s. Will be used to randomly assign each tile a number of fish
    ArrayList<Integer> fishArray = new ArrayList<>(60);


    // Default constructor
    public FishGameState(){
        this.playerTurn = 0;
        //numPlayers is set to 2 for the alpha
        this.numPlayers = 2;
        this.player1Score = 5;
        this.player2Score = 0;
        this.player3Score = 0;
        this.player4Score = 0;
        this.gamePhase = 0;
        this.validMoves = true;
        this.pieceArray = alphaInitializePieces();
        this.boardState = initializeBoard();
        for (int i = 0; i<pieceArray.length;i++){
            for (int j = 0; j<pieceArray[i].length;j++){
                boardState[pieceArray[i][j].getX()][pieceArray[i][j].getY()].setPenguin(pieceArray[i][j]);
            }
        }
        //this.pieceArray = initializePieces(this.numPlayers);

    }

    // copy constructor. Copies values from o to a new instance of the game state
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
        for (int i=0;i<this.boardState.length;i++){
            for(int j=0;j<this.boardState[0].length;j++){
                this.boardState[i][j] = o.getBoardState()[i][j];
            }
        }
        //this.pieceArray = new FishPenguin[o.numPlayers][6-o.numPlayers];
        this.pieceArray = new FishPenguin[2][1];
        for (int i=0;i<this.pieceArray.length;i++){
            for(int j=0;j<this.pieceArray[0].length;j++){
                this.pieceArray[i][j] = o.getPieceArray()[i][j];
            }
        }
    }

    /**
     * probably can get rid of
    @Override
    public String toString(){
        Log.d("toString","\n");
        StringBuilder h = boardStateString();
        return "Player Turn: " + this.playerTurn + "\n" +
                "Number of Players: " + this.numPlayers + "\n" +
                "Player 1 Score: " + this.player1Score + "\n" +
                "Player 2 Score: " + this.player2Score + "\n" +
                "Player 3 Score: " + this.player3Score + "\n" +
                "Player 4 Score: " + this.player4Score + "\n" +
                "Current Phase of the Game: " + this.gamePhase + "\n" +
                "Are there valid moves left:" + this.validMoves + "\n" +
                "This is the hexagon array visualized (T's are tiles and N's are null): " + "\n" + h.toString()+"\n";
    }

    public StringBuilder boardStateString(){
    StringBuilder s = new StringBuilder();

        for(int i=0; i<boardState.length;i++) {
            for (int j = 0; j < boardState[0].length; j++) {
                if (boardState[i][j] == null) {
                    s.append("N"); //null tile is on the board
                } else {
                    s.append("T"); //tile is present on the board
                }
            }
            s.append("\n");
        }
        s.append("\n This is the piece array visualized (Amount of penguins per player and N's are null): \n");
        for(int k=0; k<pieceArray.length; k++){
            for(int l=0; l<pieceArray[0].length;l++){
                if(pieceArray[k][l] == null){
                    s.append("N"); //null tile is on the board
                }
                else{
                    s.append(this.pieceArray[k][l].getPlayer()); //penguins present
                    }
                }
            s.append("\n");
        }

        return s;

    }
    */

    /**
     Action methods will go underneath this comment.
     */


    //TODO: Possibly move the logic for the actions into their own classes.
    //Action: When the player moves a penguin onto the board at the beginning of the game.
    public boolean placePenguin(FishPenguin p, int x, int y) {
        if (p.isOnBoard()){
            return false;
        }
        else {
            p.setXPos(x);
            p.setYPos(y);
            return true;
        }
    }

    //Action: When the player moves a penguin p to the coordinate (x,y) in the hex board.
    //After this action is taken, the original tile needs to be removed, the player's score needs to be updated, and the turn needs to be incremented.
    public boolean movePenguin(FishPenguin p, int x, int y){
        //Make sure the penguin is not moving to the same tile
        if(p.getX() == x && p.getY() == y){
            return false;
        }
        //Make sure that the space you are moving to exists (might be redundant later im not sure)
        if (!this.boardState[x][y].doesExist()){
            return false;
        }
        //0 means horizontal, 1 means down right diag, 2 means up right diag
        int direction;
        if (p.getY() == y){
            direction = 0;
        }
        else if (p.getX() == x){
            direction = 1;
        }
        else if (p.getY()+p.getX() == x+y){
            direction = 2;
        }
        else{
            return false;
        }
        //If the new move is horizontal
        if (direction == 0){
            //s is the sign of (new coordinate - old coordinate)
            //if s is positive, then you are moving to the right
            int s = Integer.signum(x-p.getX());
            for (int i = p.getX()+s; i == x; i+=s){
                if (this.boardState[i][p.getY()].hasPenguin() || !this.boardState[i][p.getY()].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is vertical (down right diag)
        else if (direction == 1){
            int s = Integer.signum(y-p.getY());
            for (int i = p.getY()+s; i == y; i+=s){
                if (this.boardState[p.getX()][i].hasPenguin() || !this.boardState[p.getX()][i].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is up right diag
        else {
            int s = Integer.signum((y-x) - (p.getY()-p.getX()));
            for (int i = 0; i == abs(x-p.getX()); i++){
                if (this.boardState[p.getX()+i][p.getY()+i].hasPenguin() || !this.boardState[p.getX()+i][p.getY()+i].doesExist()){
                    return false;
                }
            }
        }

        //If the move is legal, then add to the player's score the fish on the tile and remove the tile from the game. Then pass the turn.


        addScore(playerTurn,this.boardState[p.getX()][p.getY()].getNumFish());
        this.boardState[p.getX()][p.getY()].setExists(false);
        p.setXPos(x);
        p.setYPos(y);
        this.boardState[x][y].setPenguin(p);
        //this.playerTurn = (this.playerTurn+1)%this.numPlayers;
        this.playerTurn = 0;
        return true;
    }


    //Helper method that is called whenever a player's score needs to be incremented
    //p = player's turn, s = score to be added
    private void addScore(int pT, int s){
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



    /**
     * External Citation
     * Date: October 12, 2020
     * Problem: wanted to index a hexagonal tiling in a clever way.
     * Resource: https://www.redblobgames.com/grids/hexagons/ ("Map storage in axial coordinates")
     *
     *
     *
     *
     * Because there are three different independent directions player can move, we needed a third way to check if they
     * were moving in a straight line. So the solution to this is to just model it cleverly as a 2d array by adding some offsets.
     * The above resource explains it well, but basically it makes it very easy to check if you are moving in a striaght line.
     *
     * This is what a 6x4 hexagon array looks like in our 2d array. 0's represent hexes and x's represent null spaces.
     *      x x 0 0 0 0
     *      x x 0 0 0 0
     *      x 0 0 0 0 x
     *      x 0 0 0 0 x
     *      0 0 0 0 x x
     *      0 0 0 0 x x
     *
     * The reason we choose to index them in this way is because we can easily check if a hexagon is adjacent to another hexagon.
     * Each hex has 6 neighbors and they lok like this:
     *
     *  x 0 0
     *  0 h 0
     *  0 0 x
     *
     *  where h's neighbors are the 0's.
     *  a nice consequence of this arrangement is that we can check if two hexagons lie on the same line very easily.
     *  There are three axes that they could lie on: horizontal, upper right, and lower right
     *  These correspond in the 2d array as: same x value, same y value, and same x+y value, respectively.
     *  For example, if we have two hexagons at p1 = (2,4) and p2 = (5,4), then we know those two hexagons are on the same upper right line because y1=y2
     */

    //Helper method to initialize the board at the beginning of the game.
    private FishTile[][] initializeBoard(){
        //n is the number of null cells you need at the beginning of the array
        int n;
        //c is the number of real tiles you have in a specific row. If the row is even, c = 8, if row is odd c = 7
        int c;
        FishTile t;
        FishTile[][] f = new FishTile[BOARD_HEIGHT][BOARD_LENGTH];

        int counter = 0;

        initFish();
        //Loop through a 2d array and initialize each hexagon
        for (int i = 0; i < BOARD_HEIGHT;i++)
        {
            //Basically this value of n is based on i and it tells you how many null cells you need at the start of the row.
            //(i,n): (1,3), (2,3), (3,2), (4,2), (5,1)....
            n = 4-((i+1)+(i+1)%2)/2;
            c = 0;
            for (int j = 0; j < BOARD_LENGTH;j++)
            {
                //if n is not 0, then there are still null cells that must be placed into the array.
                //if i is even then c will hit 8 and then will start inputting null cells to finalize the array.
                //This all results in an array where the null cells are where they need to be, and an extra null cell for odd rows.
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
     * This method starts by adding thirty 1's into the fishArray arraylist, then it adds twenty 2's, and finally ten 3's
     * It then shuffles the arraylist so that when we go through the 2d array of fishTiles to assign each tile a numFish,
     * it will be random
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
     Helper method to initialize the array of penguin pieces that belong to each player. The first coordinate represents
     the player number and the second coordinate represents the index of the penguin.
     The parameter n in this context is the number of players (2-4)
     The rules of the game state that for 2 player games, each player has 4 penguins. 3 players have 3 penguins each, and 4 players have 2 each.
     These numbers are pretty arbitrary but it works out nicely because the follow a simple pattern: PenguinsPerPlayer = 6-numPlayers
     */
    private FishPenguin[][] initializePieces(int n){
        int m = 6-n;
        FishPenguin tempguin;
        FishPenguin[][] p = new FishPenguin[n][m];
        for (int i=0;i<p.length;i++){
            for (int j=0;j<p[0].length;j++){
                tempguin = new FishPenguin(i);
                p[i][j] = tempguin;
            }
        }
        return p;
    }


    //This method will initialize the penguin array for the alpha version of the game.
    //This method will be deleted later on when we implement that starting phase of the game.
    private FishPenguin[][] alphaInitializePieces(){
        FishPenguin[][] p = new FishPenguin[2][1];

        p[0][0] = new FishPenguin(0);
        p[1][0] = new FishPenguin(1);

        p[0][0].setXPos(5);
        p[0][0].setYPos(5);

        p[1][0].setXPos(6);
        p[1][0].setYPos(6);

        p[0][0].setOnBoard(true);
        p[1][0].setOnBoard(true);

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

//        FishTile[][] tiles = null;
//        tiles = new FishTile[BOARD_HEIGHT][BOARD_LENGTH];
//        for (int i=0;i<tiles.length;i++){
//            for(int j=0;j<tiles[0].length;j++){
//                tiles[i][j] = this.getBoardState()[i][j];
//            }
//        }
//        return tiles;
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