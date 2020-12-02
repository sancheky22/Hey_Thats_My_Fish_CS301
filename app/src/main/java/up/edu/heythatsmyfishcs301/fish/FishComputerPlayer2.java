package up.edu.heythatsmyfishcs301.fish;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import up.edu.heythatsmyfishcs301.game.infoMsg.GameInfo;
import up.edu.heythatsmyfishcs301.game.infoMsg.NotYourTurnInfo;


/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class should be for the smart AI. Compared to the dumb AI, it picks a random tile from the
 * board that has one fish on it and places its penguin there. This way, its penguins are more
 * spread out and are able to reach more areas of the board
 **/
public class FishComputerPlayer2 extends FishComputerPlayer {

    private FishTile[][] boardState;
    FishGameState copy = null;

    //Constructor for Computer Player 2
    public FishComputerPlayer2(String name){
        super(name);
    }

    // Array list for all tiles on board with one fish
    ArrayList<FishTile> oneTiles = new ArrayList<>(30);


    /**
     * Checks what phase of the game it is. This method receives the current
     * state of the board and sends actions to the game accordingly. If it's phase 0, the computer
     * will place its penguins. If it's phase 2, the computer will move it's penguins.
     *
     * @param info contains information about the gamestate
     */
    //Computer Player 2 sends a random action to the game state.
    @Override
    protected void receiveInfo(GameInfo info) {
        //this is correct one
        // if it was a "not your turn" message, just ignore it
        if (info instanceof NotYourTurnInfo){
            return;
        }

        //if the info is not of a FishGameState, ignore because it will cause problems otherwise.
        if (!(info instanceof FishGameState)) {
            return;
        }

        //get a copy of the gamestate and if it's not the player's turn, return
        copy = (FishGameState) info;
        if (copy.getPlayerTurn() != this.playerNum) return;

        // get a copy of the tiles on the board and the penguin object array
        FishTile[][] pieceBoard = copy.getBoardState();
        FishPenguin[][] penguins = copy.getPieceArray();


        /**
         *External Citation
         * Date: 11/21/20
         * Problem: Wanted to get random item out of arraylist
         * Resource: https://stackoverflow.com/questions/5034370/retrieving-a-random-item-from-arraylist
         *
         * Solution: Used example code from this stackoverflow post
         */
        //If the game phase is zero, then the computer needs to place a penguin
        //If the game phase is set up (Placing Penguins)
        // IDEA: When Comp player places penguins it puts it on tiles surrounded by the most fish
        if (copy.getGamePhase() == 0){
            // Random variable that will be used to select FishTiles out of of oneTiles array
            Random rand = new Random();

            // goes through the array of penguins it owns and if any of them are not on the board
            // it will attempt to place them on the board
            for (int x = 0; x<penguins[this.playerNum].length;x++){
                if (!penguins[this.playerNum][x].isOnBoard()){
                    for (int i = 0; i< pieceBoard.length; i++){
                        for (int j = 0; j<pieceBoard[i].length; j++){
                            if (pieceBoard[i][j] != null && pieceBoard[i][j].getNumFish() == 1 &&
                                    !pieceBoard[i][j].hasPenguin()){

                                // add tiles with one fish to array list
                                oneTiles.add(pieceBoard[i][j]);

                                // random int that goes through arraylist of FishTiles
                                //Bound set to size of array list
                                int index = rand.nextInt(oneTiles.size());

                                // create a new FishTile object that is a random FishTile from array
                                // list of tiles with one fish
                                FishTile validTiles = oneTiles.get(index);

                                // send action to the game with tile that has one fish
                                FishPlaceAction p = new FishPlaceAction(this,validTiles,
                                        penguins[this.playerNum][x]);
                                game.sendAction(p);
                            }
                        }
                    }
                }
            }


        }else {
            // loop through the board to see there is a penguin on the tile. If there is, it checks if the
            //penguin belongs to the computer. If it does, it calls the computerMovePenguin
//                for(int i =0; i < pieceBoard.length; i++){
//                    for(int j=0; j< pieceBoard[i].length;j++){
//                        if(pieceBoard[i][j] != null){
//                            if(pieceBoard[i][j].hasPenguin() && pieceBoard[i][j].getPenguin().getPlayer() == this.playerNum){
//                                if(!computerMovePenguin(pieceBoard[i][j].getPenguin())){
//                                    //Penguin is removed from board.
//                                    continue;
//                                }
//                                else {
//                                    return;
//                                }
//
//                            }
//                        }
//                    }
//
//                }
            for (int i = 3; i > 0; i--) {
                for (FishPenguin p : copy.getPieceArray()[this.playerNum]) {
                    if(computerMovePenguin(p,i)){
                        return;
                    }
                }
                Log.d("Move", "Computer Player 2 Moving");
            }
        }
    }


    /**
     * Improved AI has a few improvements to the original moving algorithm:
     * It checks every single direction for the tile that has the most fish and moves there first
     * (If it can move to a tile with 3 fish and a tile with 2 fish it will choose the tile with 3
     * fish first)
     *
     * DESCRIPTION of moving algorithm:
     * It starts by trying to move to the right horizontally. From there, if moving to the right is
     * not valid, it will check each direction going clockwise if moving there is a valid move. At
     * the first instance of a valid move, it will make that move. It checks if a direction is a
     * valid move by making sure that the tile isn't null, the if the coordinate we're going to is
     * within the array bounds, if the tile exists, and the tile doesn't already have a penguin on
     * it. To get access to the adjacent tiles, we just add or subtract to the x and y coordinates
     *
     * @param p the penguin that needs to be moved
     */
    public boolean computerMovePenguin(FishPenguin p, int target) {
        FishTile[][] pieceBoard = copy.getBoardState();
        boardState = copy.getBoardState();

        //If the move is legal, then add to the player's score the fish on the tile and remove the
        // tile from the game. Then pass the turn.
        // algorithm finds optimal move and sends it to the local game

        for (int j = -11; j < 11; j++) {
            // checks if it can move to a tile with 3 fish then 2 fish and then 1 fish
            //  try to move horizontally to the right
            try {
                if (!(pieceBoard[p.getX()][p.getY() + j].hasPenguin()) &&
                        (pieceBoard[p.getX()][p.getY() + j].getNumFish() == target) &&
                        (pieceBoard[p.getX()][p.getY() + j].doesExist())) {
                    FishMoveAction m = new FishMoveAction(this, p,
                            this.boardState[p.getX()][p.getY() + j]);
                    Log.d("Move", "Computer Player Moving horizontally to the right");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                            "," + p.getY() + ")");
                    if (isLegal(m)) {
                        game.sendAction(m);
                        return true;
                    }
                }
            }
            catch(Exception ignored) {}
            try {
                // checks if the upper left tile to see if the computer can move there
                if (!(pieceBoard[p.getX() + j][p.getY()].hasPenguin()) &&
                        (pieceBoard[p.getX() + j][p.getY()].getNumFish() == target) &&
                        (pieceBoard[p.getX() + j][p.getY()].doesExist())) {

                    FishMoveAction m = new FishMoveAction(this, p,
                            this.boardState[p.getX() + j][p.getY()]);
                    Log.d("Move", "Computer Player Moving diagonally down to the right");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                            "," + p.getY() + ")");
                    if (isLegal(m)) {
                        game.sendAction(m);
                        return true;
                    }
                }
            }
            catch(Exception ignored){}

            try{
                //try to move diagonally down to the left
                // tries to move to tiles with 3 fish first
                if (!(pieceBoard[p.getX() + j][p.getY() - j].hasPenguin()) &&
                        (pieceBoard[p.getX() + j][p.getY() - j].getNumFish() == target) &&
                        (pieceBoard[p.getX() + j][p.getY() - j].doesExist())) {
                    FishMoveAction m = new FishMoveAction(this, p,
                            this.boardState[p.getX() + j][p.getY() - j]);
                    Log.d("Move", "Computer Player Moving diagonally down to the left");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                            "," + p.getY() + ")");
                    if (isLegal(m)) {
                        game.sendAction(m);
                        return true;
                    }
                }
            }
            catch(Exception ignored){}
        }
        return false;
    }

    /**
     * helper method to see if a move is legal before it is sent to the game.
     * @param m move to be checked
     * @return
     */
    private boolean isLegal(FishMoveAction m){
        FishPenguin p = m.getPenguin();
        FishTile t = m.getDestination();
        int px = p.getX();
        int py = p.getY();

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
                if (boardState[i][py].hasPenguin() || !this.boardState[i][py].doesExist()){
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

        //If the new move is up right diag or down left diag
        else {
            //If s is positive, you are moving upper right diag
            int s = Integer.signum(y-py);
            for (int i = s; i != y-py + s; i+=s){
                if (boardState[px-i][py+i].hasPenguin() || !boardState[px-i][py+i].doesExist()){
                    return false;
                }
            }
        }
        return true;
    }
}