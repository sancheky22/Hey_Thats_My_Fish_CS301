package up.edu.heythatsmyfishcs301.fish;


import android.util.Log;
import up.edu.heythatsmyfishcs301.game.infoMsg.GameState;
import java.lang.Integer;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This is the GameState. This object holds all of the info of the game such as current positions, points, turns, etc.
 * We pass this object to the localGame which copies it and sends images to the players so that they can make decisions.
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
    public FishGameState(int n){
        this.playerTurn = 0;
        //numPlayers is set to 2 for the alpha
        this.numPlayers = n;
        this.player1Score = 0;
        this.player2Score = 0;
        this.player3Score = 0;
        this.player4Score = 0;
        this.gamePhase = 0;
        this.validMoves = true;
        //this.pieceArray = alphaInitializePieces();

        this.boardState = initializeBoard();
        pieceArray = new FishPenguin[2][4];
        pieceArray = initializePieces(2);
//        for (int i = 0; i<pieceArray.length;i++){
//            for (int j = 0; j<pieceArray[i].length;j++){
//                boardState[pieceArray[i][j].getX()][pieceArray[i][j].getY()].setHasPenguin(true);
//                boardState[pieceArray[i][j].getX()][pieceArray[i][j].getY()].setPenguin(pieceArray[i][j]);
//            }
//        }
        //this.pieceArray = initializePieces(this.numPlayers);
    }

    /**
    Copy constructor. Creates a deep copy of the game state
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
        for (int i=0;i<this.boardState.length;i++){
            for(int j=0;j<this.boardState[0].length;j++){
                this.boardState[i][j] = o.getBoardState()[i][j];
            }
        }
        //this.pieceArray = new FishPenguin[o.numPlayers][6-o.numPlayers];

        //For the alpha, we know that the size of the array will be [2][1]
        this.pieceArray = new FishPenguin[2][4];
        for (int i=0;i<this.pieceArray.length;i++){
            for(int j=0;j<this.pieceArray[0].length;j++){
                this.pieceArray[i][j] = o.getPieceArray()[i][j];
            }
        }
    }

    /**
     * TestMove will see if a penguin p has valid moves.
     */
    public boolean testMove(FishPenguin p){
        //tests tile to the right horizontally
        if(p.getY() + 1 <= 8 && this.boardState[p.getX()][p.getY() + 1] != null && this.boardState[p.getX()][p.getY() + 1].doesExist() && !this.boardState[p.getX()][p.getY() + 1].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move right horizontally");
            }
            else{
                Log.d("Possible", "computer can make move right horizontally");
            }
            return true;
        }

        //tests tile down to the right
        else if(p.getX() + 1 < 8 && this.boardState[p.getX() + 1][p.getY()] != null && this.boardState[p.getX() + 1][p.getY()].doesExist() && !this.boardState[p.getX() + 1][p.getY()].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move down to the right");
            }
            else{
                Log.d("Possible", "computer can make move down to the right");
            }
            return true;
        }

        //test tile down to the left
        else  if(p.getX() + 1 < 8 && p.getY() - 1 >= 0 && this.boardState[p.getX() + 1][p.getY() - 1] != null && this.boardState[p.getX() + 1][p.getY() - 1].doesExist() && !this.boardState[p.getX() + 1][p.getY() - 1].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move down to the left");
            }
            else{
                Log.d("Possible", "computer can make move down to the left");
            }
            return true;
        }

        //test tile to the left horizontally
        else  if(p.getY() - 1 >= 0 && this.boardState[p.getX()][p.getY() - 1] != null && this.boardState[p.getX()][p.getY() - 1].doesExist() && !this.boardState[p.getX()][p.getY() - 1].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move left horizontally");
            }
            else{
                Log.d("Possible", "computer can make move left horizontally");
            }
            return true;
        }

        //test tile up to the left
        else  if(p.getX() - 1 >= 0 && this.boardState[p.getX() - 1][p.getY()] != null && this.boardState[p.getX() - 1][p.getY()].doesExist() && !this.boardState[p.getX() - 1][p.getY()].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move to the left");
            }
           else{
                Log.d("Possible", "computer can make move to the left");
            }
            return true;
        }

        //test tile up to the right
        else if(p.getX() - 1 >= 0 && p.getY() + 1 <= 8 && boardState[p.getX() - 1][p.getY() + 1] != null && boardState[p.getX() - 1][p.getY() + 1].doesExist() && boardState[p.getX() - 1][p.getY() + 1].hasPenguin()){
            if(p.getPlayer() == 0){
                Log.d("Possible", "human can make move to the left");
            }
            else{
                Log.d("Possible", "computer can make move up to the right");
            }

            return true;
        }

        Log.d("Move not possible", "can not make move");
        return false;
    }

    /**
     * Place Penguin is called at the start of the game, when players choose where their penguins start
     */
    //Action: When the player moves a penguin onto the board at the beginning of the game.
    public boolean placePenguin(FishPenguin p, int x, int y) {
        if(boardState[x][y].hasPenguin()){
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
    Action: When the player moves a penguin p to the coordinate (x,y) in the hex board.
    After this action is taken, the original tile needs to be removed, the player's score needs to be updated, and the turn needs to be incremented.
    TODO: Replace the logic in this method so that it takes a Tile object rather than two indices for the board state.
    */
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
            direction = 1;
        }
        else if (p.getX() == x){
            direction = 0;
        }
        else if (p.getY()+p.getX() == x+y){
            direction = 2;
        }
        else{
            return false;
        }
        //If the new move is up left diag or down right diag
        if (direction == 1){
            //s is the sign of (new coordinate - old coordinate)
            //if s is positive, then you are moving to the right
            int s = Integer.signum(x-p.getX());
            for (int i = p.getX()+s; i != x; i+=s){
                if (this.boardState[i][p.getY()].hasPenguin() || !this.boardState[i][p.getY()].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is horizontal (left or right)
        else if (direction == 0){
            int s = Integer.signum(y-p.getY());
            for (int i = p.getY()+s; i != y; i+=s){
                if (this.boardState[p.getX()][i].hasPenguin() || !this.boardState[p.getX()][i].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is up right diag or down left diag
        else {
            //If s is positive, you are moving upper right diag
        int s = Integer.signum(y-p.getY());
        for (int i = s; abs(i) != abs(y-p.getY()); i+=s){
            if (this.boardState[p.getX()-i][p.getY()+i].hasPenguin() || !this.boardState[p.getX()-i][p.getY()+i].doesExist()){
                return false;
            }
        }
    }

    //If the move is legal, then add to the player's score the fish on the tile and remove the tile from the game. Then pass the turn.
    addScore(playerTurn,this.boardState[p.getX()][p.getY()].getNumFish());
    this.boardState[p.getX()][p.getY()].setExists(false);
    this.boardState[p.getX()][p.getY()].setHasPenguin(false);
    p.setXPos(x);
    p.setYPos(y);
    Log.d("Human Moved", "human moved to (" + p.getX() + "," + p.getY() + ")");
    this.boardState[x][y].setPenguin(p);
    this.boardState[x][y].setHasPenguin(true);
    //this.playerTurn = (this.playerTurn+1)%this.numPlayers;
    //this.playerTurn = (this.playerTurn+1)%2;
    return true;
    }

    /**
    * change turn method that changes current turn of the game
    */
    public void changeTurn() {
        this.playerTurn = (this.playerTurn+1)%this.numPlayers;
    }

    /**
    method useed in FishLocalGame and is needed so that when the AI makes a move,
    it can be sent to the actual gamestate
    This is in the alpha, but will be generalized later
     */
    public void changeComScore(int i){
        this.player2Score = i;
    }

    /**
    Helper method that is called whenever a player's score needs to be incremented
    p = player's turn, s = score to be added
    */
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

     /**
      Helper method to initialize the board at the beginning of the game.
      This method constructs the board based on the 2d array above.
      This also means that it has to initialize the number of fish on each of the tiles based on a specific distribution (30 ones, 20 twos, 10 threes).
     */
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
        //hardcoded value for alpha release
        //f[6][6].setHasPenguin(true);
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
                tempguin.setPlayer(i);
                tempguin.setOnBoard(false);
                p[i][j] = tempguin;
            }
        }
        return p;
    }

    /**
     This method will initialize the penguin array for the alpha version of the game.
     This method will be deleted later on when we implement that starting phase of the game.
     */
    private FishPenguin[][] alphaInitializePieces(){
        FishPenguin[][] p = new FishPenguin[2][4];

        // player 1's penguins
        p[0][0] = new FishPenguin(0);
        p[0][1] = new FishPenguin(0);
        p[0][2] = new FishPenguin(0);
        p[0][3] = new FishPenguin(0);



        // player 2's penguins
        p[1][0] = new FishPenguin(1);
        p[1][1] = new FishPenguin(1);
        p[1][2] = new FishPenguin(1);
        p[1][3] = new FishPenguin(1);

        // Player 1 penguin 1
        p[0][0].setXPos(5);
        p[0][0].setYPos(5);

        // Player 1 penguin 2
        p[0][1].setXPos(0);
        p[0][1].setYPos(5);

        // Player 1 penguin 3
        p[0][2].setXPos(0);
        p[0][2].setYPos(6);

        // Player 1 penguin 4
        p[0][3].setXPos(0);
        p[0][3].setYPos(7);

        // player 2 penguin 2
        p[1][0].setXPos(6);
        p[1][0].setYPos(6);

        // Player 2 penguin 2
        p[1][1].setXPos(1);
        p[1][1].setYPos(5);

        // Player 2 penguin 3
        p[1][2].setXPos(1);
        p[1][2].setYPos(6);

        // Player 2 penguin 4
        p[1][3].setXPos(1);
        p[1][3].setYPos(7);




        p[0][0].setOnBoard(true);
        p[1][0].setOnBoard(true);

        p[0][1].setOnBoard(true);
        p[0][2].setOnBoard(true);
        p[0][3].setOnBoard(true);


        p[1][1].setOnBoard(true);
        p[1][2].setOnBoard(true);
        p[1][3].setOnBoard(true);

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