package up.edu.heythatsmyfishcs301.fish;

import android.util.Log;

import up.edu.heythatsmyfishcs301.game.GameComputerPlayer;
import up.edu.heythatsmyfishcs301.game.infoMsg.GameInfo;
import up.edu.heythatsmyfishcs301.game.infoMsg.NotYourTurnInfo;

import java.util.ArrayList;
import java.util.Random;


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
                for(int i =0; i < pieceBoard.length; i++){
                    for(int j=0; j< pieceBoard[i].length;j++){
                        if(pieceBoard[i][j] != null){
                            if(pieceBoard[i][j].hasPenguin() && pieceBoard[i][j].getPenguin().getPlayer() == this.playerNum){
                                if(!computerMovePenguin(pieceBoard[i][j].getPenguin())){
                                    //Penguin is removed from board.
                                    continue;
                                }
                                else {
                                    return;
                                }

                            }
                        }
                    }

                }
            Log.d("Move","Computer Player 2 Moving");
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
    public boolean computerMovePenguin(FishPenguin p) {
        FishTile[][] pieceBoard = copy.getBoardState();
        boardState = copy.getBoardState();

        //If the move is legal, then add to the player's score the fish on the tile and remove the
        // tile from the game. Then pass the turn.
        // algorithm finds optimal move and sends it to the local game
            for (int i = 3; i > 0;i--) {

                // checks if it can move to a tile with 3 fish then 2 fish and then 1 fish
                //  try to move horizontally to the right
                if (p.getY() + 1 < 11 && (pieceBoard[p.getX()][p.getY() + 1] != null)) {
                    if (!(pieceBoard[p.getX()][p.getY() + 1].hasPenguin()) &&
                            (pieceBoard[p.getX()][p.getY() + 1].getNumFish() == i) &&
                            (pieceBoard[p.getX()][p.getY() + 1].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()][p.getY()+1]);
                        Log.d("Move", "Computer Player Moving horizontally to the right");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);
                        return true;
                    }
                }

                // checks if the upper left tile to see if the computer can move there
                if (p.getX() + 1 < 8 && pieceBoard[p.getX() + 1][p.getY()] != null) {
                    if (!(pieceBoard[p.getX() + 1][p.getY()].hasPenguin()) &&
                            (pieceBoard[p.getX()+1][p.getY()].getNumFish()==i) &&
                            (pieceBoard[p.getX() + 1][p.getY()].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()+1][p.getY()]);
                        Log.d("Move","Computer Player Moving diagonally down to the right");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);


                        return true;
                    }
                }

                //try to move diagonally down to the left
                if (p.getX() + 1 < 8 && p.getY() - 1 >= 0 && pieceBoard[p.getX() + 1][p.getY() - 1] != null) {
                    // tries to move to tiles with 3 fish first
                    if (!(pieceBoard[p.getX() + 1][p.getY() - 1].hasPenguin()) &&
                            (pieceBoard[p.getX()+1][p.getY()-1].getNumFish()==i) &&
                            (pieceBoard[p.getX() + 1][p.getY() - 1].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()+1][p.getY()-1]);
                        Log.d("Move","Computer Player Moving diagonally down to the left");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);
                        return true;

                    }
                }

                //try to move horizontally to the left
                if (p.getY() - 1 >= 0 && pieceBoard[p.getX()][p.getY() - 1] != null) {
                    // moves to tiles with 3 fish first
                    if (!(pieceBoard[p.getX()][p.getY() - 1].hasPenguin()) &&
                            (pieceBoard[p.getX()][p.getY()-1].getNumFish()==i) &&
                            (pieceBoard[p.getX()][p.getY() - 1].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()][p.getY()-1]);
                        Log.d("Move","Computer Player Moving horizontally to the left");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);
                        return true;

                    }
                }

                //try to move diagonally up to the left
                if (p.getX() - 1 >= 0 && pieceBoard[p.getX() - 1][p.getY()] != null) {
                    // moves to tiles with 3 fish first
                    if (!(pieceBoard[p.getX() - 1][p.getY()].hasPenguin()) &&
                            (pieceBoard[p.getX()-1][p.getY()].getNumFish()==i) &&
                            (pieceBoard[p.getX() - 1][p.getY()].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()-1][p.getY()]);
                        Log.d("Move","Computer Player Moving diagonally up to the left");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);
                        return true;

                    }
                }

                //try to move diagonally up to the right
                if (p.getX() - 1 >= 0 && p.getY() + 1 < 11 && pieceBoard[p.getX() - 1][p.getY() + 1] != null) {
                    // moves to tiles with 3 fish first
                    if (!(pieceBoard[p.getX() - 1][p.getY() + 1].hasPenguin()) &&
                            (pieceBoard[p.getX()-1][p.getY()+1].getNumFish()==i) &&
                            (pieceBoard[p.getX() - 1][p.getY() + 1].doesExist())) {

                        FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                        FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                                this.boardState[p.getX()-1][p.getY()+1]);
                        Log.d("Move","Computer Player Moving diagonally up to the right");
                        Log.d("Computer Moved", "Computer moved to (" + p.getX() +
                                "," + p.getY() + ")");
                        game.sendAction(m);
                        return true;

                    }
                }
            }
        return false;
    }
}