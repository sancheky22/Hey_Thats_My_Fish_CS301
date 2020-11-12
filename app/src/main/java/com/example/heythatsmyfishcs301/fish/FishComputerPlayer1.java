package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GameComputerPlayer;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;
import com.example.heythatsmyfishcs301.game.infoMsg.NotYourTurnInfo;

import java.util.Random;

import static java.lang.Math.abs;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishComputerPlayer1 extends GameComputerPlayer {

    private FishTile[][] boardState;
    FishGameState copy = null;

    //Constructor for The Computer Player
    public FishComputerPlayer1(String name){
        super(name);
    }

    //Computer Player 1 sends a random action to the game state.
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

        if (copy.getPlayerTurn() != this.playerNum)
            return;

        //If the game phase is mid-game (Moving penguins)
        //for alpha release set to 0, change to 1 later
        if (copy.getGamePhase() == 0){
            boardState = copy.getBoardState();

            // using our copy of gamestate
            FishTile[][] pieceBoard = copy.getBoardState();
            // loop through
            if(copy.getPlayerTurn() == 1){
                for(int i =0; i < pieceBoard.length; i++){
                    for(int j=0; j< pieceBoard[i].length;j++){
                        //computer player hard coded to play2 for alpha release
                        if(pieceBoard[i][j] != null){
                            if(pieceBoard[i][j].hasPenguin() && pieceBoard[i][j].getPenguin().getPlayer() == 1){
                                computerMovePenguin(pieceBoard[i][j].getPenguin());
                                break;
                            }
                        }

                    }

                }
            }
            Log.d("Move","Computer Player Moving");
        }
        //If the game phase is set up (Placing Penguins)
        else{
            FishPlaceAction placeAction = new FishPlaceAction(this);
            this.game.sendAction(placeAction);
        }
    }


    public boolean computerMovePenguin(FishPenguin p) {

        int direction = 0;
        FishTile[][] pieceBoard = copy.getBoardState();


        //If the move is legal, then add to the player's score the fish on the tile and remove the tile from the game. Then pass the turn.
        if (copy.getPlayerTurn() == 1) {

        //try to move horizontally to the right
        if (p.getY() + 1 <= 8 && (pieceBoard[p.getX()][p.getY() + 1] != null) ) {
            if(!(pieceBoard[p.getX()][p.getY() + 1].hasPenguin()) && (pieceBoard[p.getX()][p.getY() + 1].doesExist())) {
                addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                this.boardState[p.getX()][p.getY()].setExists(false);
                this.boardState[p.getX()][p.getY() + 1].setPenguin(p);
                p.setXPos(p.getX());
                p.setYPos(p.getY() + 1);


                FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin, this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                Log.d("Move", "Computer Player Moving horizontally to the right");
                Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                game.sendAction(m);
                return true;
            }
        }

        //try to move diagonally down to the right
            if(p.getX() + 1 < 8 && pieceBoard[p.getX() + 1][p.getY()] != null){
                if (!(pieceBoard[p.getX() + 1][p.getY()].hasPenguin()) && (pieceBoard[p.getX() + 1][p.getY()].doesExist())) {
                    addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                    this.boardState[p.getX()][p.getY()].setExists(false);
                    this.boardState[p.getX() + 1][p.getY()].setPenguin(p);
                    p.setXPos(p.getX() + 1);
                    p.setYPos(p.getY());

                    FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                    FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin,this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                    Log.d("Move","Computer Player Moving diagonally down to the right");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                    game.sendAction(m);

                    return true;
                }
            }


            //try to move diagonally down to the left
            if(p.getX() + 1 < 8 && p.getY() - 1 >= 0 && pieceBoard[p.getX() + 1][p.getY() - 1] != null){
                if (!(pieceBoard[p.getX() + 1][p.getY() - 1].hasPenguin()) && (pieceBoard[p.getX() + 1][p.getY() - 1].doesExist())) {
                    addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                    this.boardState[p.getX()][p.getY()].setExists(false);
                    this.boardState[p.getX() + 1][p.getY() - 1].setPenguin(p);
                    p.setXPos(p.getX() + 1);
                    p.setYPos(p.getY() - 1);

                    FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                    FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin,this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                    Log.d("Move","Computer Player Moving diagonally down to the left");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                    game.sendAction(m);
                    return true;
                }
            }



        //try to move horizontally to the left
            if(p.getY() - 1 >= 0 && pieceBoard[p.getX()][p.getY() - 1] != null){
                if (!(pieceBoard[p.getX()][p.getY() - 1].hasPenguin()) && (pieceBoard[p.getX()][p.getY() - 1].doesExist())) {
                    addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                    this.boardState[p.getX()][p.getY()].setExists(false);
                    this.boardState[p.getX()][p.getY() - 1].setPenguin(p);
                    p.setXPos(p.getX());
                    p.setYPos(p.getY() - 1);

                    FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                    FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin,this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                    Log.d("Move","Computer Player Moving horizontally to the left");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                    game.sendAction(m);
                    return true;
                }
            }


        //try to move diagonally up to the left
            if(p.getX() - 1 >= 0 && pieceBoard[p.getX() - 1][p.getY()] != null){
                if (!(pieceBoard[p.getX() - 1][p.getY()].hasPenguin()) && (pieceBoard[p.getX() - 1][p.getY()].doesExist())) {
                    addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                    this.boardState[p.getX()][p.getY()].setExists(false);
                    this.boardState[p.getX() - 1][p.getY()].setPenguin(p);
                    p.setXPos(p.getX() - 1);
                    p.setYPos(p.getY());

                    FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                    FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin,this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                    Log.d("Move","Computer Player Moving diagonally up to the left");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                    game.sendAction(m);
                    return true;
                }
            }


        //try to move diagonally up to the right
            if(p.getX() - 1 >= 0 && p.getY() + 1 <= 8 && pieceBoard[p.getX() - 1][p.getY() + 1] != null){
                if (!(pieceBoard[p.getX() - 1][p.getY() + 1].hasPenguin()) && (pieceBoard[p.getX() - 1][p.getY() + 1].doesExist())) {
                    addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
                    this.boardState[p.getX()][p.getY()].setExists(false);
                    this.boardState[p.getX() - 1][p.getY() + 1].setPenguin(p);
                    p.setXPos(p.getX() - 1);
                    p.setYPos(p.getY() + 1);

                    FishPenguin selectedPenguin = this.boardState[p.getX()][p.getY()].getPenguin();
                    FishComputerMoveAction m = new FishComputerMoveAction(this, selectedPenguin,this.boardState[p.getX()][p.getY()], copy.getPlayer2Score());
                    Log.d("Move","Computer Player Moving diagonally up to the right");
                    Log.d("Computer Moved", "Computer moved to (" + p.getX() + "," + p.getY() + ")");
                    game.sendAction(m);
                    return true;
                }
            }
    }
        return false;
    }

    private void addScore(int pT, int s) {
        switch (pT) {
            case 0:
                copy.setPlayer1Score(copy.getPlayer1Score() + s);
                break;
            case 1:
                copy.setPlayer2Score(copy.getPlayer2Score() + s);
                break;
            case 2:
                copy.setPlayer3Score(copy.getPlayer3Score() + s);
                break;
            case 3:
                copy.setPlayer4Score(copy.getPlayer4Score() + s);
                break;
        }
    }

}
