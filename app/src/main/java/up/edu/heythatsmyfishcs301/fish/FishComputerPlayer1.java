package up.edu.heythatsmyfishcs301.fish;

import android.util.Log;

import up.edu.heythatsmyfishcs301.game.infoMsg.GameInfo;
import up.edu.heythatsmyfishcs301.game.infoMsg.NotYourTurnInfo;


/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 *
 * This class contains the algorithm for how the computer player moves. It implements
 * a separate method for the computer to move and sends the move action to the gameState. It picks
 * the first one-fish tile it sees and places it there
 **/
public class FishComputerPlayer1 extends FishComputerPlayer {

    private FishTile[][] boardState;
    FishGameState copy = null;


    //Constructor for Computer Player 1
    public FishComputerPlayer1(String name){
        super(name);
    }

    /**
     * Computer Player 1 sends a random action to the game state. The computer receives a copy of
     * the current state of the board and sends a move depending on that state
     *
     * @param info which holds information about the gamestate
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // if it was a "not your turn" message, just ignore it
        if (info instanceof NotYourTurnInfo){
            return;
        }

        //if the info is not of a FishGameState, ignore because it will cause problems otherwise.
        if (!(info instanceof FishGameState)) {
            return;
        }

        //Let copy be the copied state.
        copy = (FishGameState) info;

        //get a copy of the board of tiles and the array of penguins
        FishTile[][] pieceBoard = copy.getBoardState();
        FishPenguin[][] penguins = copy.getPieceArray();

        //If the game phase is zero, then the computer needs to place a penguin
        if (copy.getGamePhase() == 0){
            for (int x = 0; x < penguins[this.playerNum].length; x++){
                if (!penguins[this.playerNum][x].isOnBoard()){
                    for (FishTile[] fishTiles : pieceBoard) {
                        for (FishTile fishTile : fishTiles) {
                            if (fishTile != null && fishTile.getNumFish() == 1 && !fishTile.hasPenguin()) {
                                FishPlaceAction p = new FishPlaceAction(this, fishTile,
                                        penguins[this.playerNum][x]);
                                game.sendAction(p);
                                return;
                            }
                        }
                    }
                }
            }
        }
        else {
            // loop through the board to see there is a penguin on the tile. If there is, it checks if the
            //penguin belongs to the computer. If it does, it calls the computerMovePenguin
            for (FishTile[] fishTiles : pieceBoard) {
                for (FishTile fishTile : fishTiles) {
                    if (fishTile != null) {
                        if (fishTile.hasPenguin() && fishTile.getPenguin().getPlayer() == this.playerNum) {
                            if (!computerMovePenguin(fishTile.getPenguin())) {
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * This AI is not very smart. It starts by trying to move to the right horizontally. From there,
     * if moving to the right is not valid, it will check each direction going clockwise if moving
     * there is a valid move. At the first instance of a valid move, it will make that move.
     * It checks if a direction is a valid move by making sure that the tile isn't null, the if the
     * coordinate we're going to is within the array bounds, if the tile exists, and the tile
     * doesn't already have a penguin on it. To get access to the adjacent tiles, we just add or
     * subtract to the x and y coordinates
     *
     * @param p the penguin object to be moved
     */
    public boolean computerMovePenguin(FishPenguin p) {
        FishTile[][] pieceBoard = copy.getBoardState();
        boardState = copy.getBoardState();


        //try to move horizontally to the right
        if (p.getY() + 1 < 11 && (pieceBoard[p.getX()][p.getY() + 1] != null)) {
            if (!(pieceBoard[p.getX()][p.getY() + 1].hasPenguin()) &&
                    (pieceBoard[p.getX()][p.getY() + 1].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX()][p.getY() + 1]);
                Log.d("Move", "Computer Player Moving horizontally to the right");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }


        //try to move diagonally down to the right
        if (p.getX() + 1 < 8 && pieceBoard[p.getX() + 1][p.getY()] != null) {
            if (!(pieceBoard[p.getX() + 1][p.getY()].hasPenguin()) &&
                    (pieceBoard[p.getX() + 1][p.getY()].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX() + 1][p.getY()]);
                Log.d("Move", "Computer Player Moving diagonally down to the right");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);

                return true;
            }
        }


        //try to move diagonally down to the left
        if (p.getX() + 1 < 8 && p.getY() - 1 >= 0 && pieceBoard[p.getX() + 1][p.getY() - 1] != null) {
            if (!(pieceBoard[p.getX() + 1][p.getY() - 1].hasPenguin()) &&
                    (pieceBoard[p.getX() + 1][p.getY() - 1].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX() + 1][p.getY() - 1]);
                Log.d("Move", "Computer Player Moving diagonally down to the left");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }


        //try to move horizontally to the left
        if (p.getY() - 1 >= 0 && pieceBoard[p.getX()][p.getY() - 1] != null) {
            if (!(pieceBoard[p.getX()][p.getY() - 1].hasPenguin()) &&
                    (pieceBoard[p.getX()][p.getY() - 1].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX()][p.getY() - 1]);
                Log.d("Move", "Computer Player Moving horizontally to the left");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }


        //try to move diagonally up to the left
        if (p.getX() - 1 >= 0 && pieceBoard[p.getX() - 1][p.getY()] != null) {
            if (!(pieceBoard[p.getX() - 1][p.getY()].hasPenguin()) &&
                    (pieceBoard[p.getX() - 1][p.getY()].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX() - 1][p.getY()]);
                Log.d("Move", "Computer Player Moving diagonally up to the left");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }


        //try to move diagonally up to the right
        if (p.getX() - 1 >= 0 && p.getY() + 1 < 11 && pieceBoard[p.getX() - 1][p.getY() + 1] != null) {
            if (!(pieceBoard[p.getX() - 1][p.getY() + 1].hasPenguin()) &&
                    (pieceBoard[p.getX() - 1][p.getY() + 1].doesExist())) {

                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishMoveAction m = new FishMoveAction(this, selectedPenguin,
                        this.boardState[p.getX() - 1][p.getY() + 1]);
                Log.d("Move", "Computer Player Moving diagonally up to the right");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }

        // if the computer can't make any moves, return false
        return false;
    }
}
