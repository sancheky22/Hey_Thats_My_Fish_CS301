package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GameComputerPlayer;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

import java.util.Random;

import static java.lang.Math.abs;

public class FishComputerPlayer1 extends GameComputerPlayer {

    private FishTile[][] boardState;
    FishGameState copy = null;

    //constructor done!
    public FishComputerPlayer1(String name){
        super(name);
    }

    //Computer Player 1 sends a random action to the game state.
    @Override
    protected void receiveInfo(GameInfo info) {
        if (!(info instanceof FishGameState)) {
            return;
        }

        copy = (FishGameState) info;
        //Random r = new Random();
        if (copy.getPlayerTurn() != this.playerNum)
            return;

        //If the game phase is mid-game (Moving penguins)
        if (copy.getGamePhase() == 1){
            boardState = copy.getBoardState();
            //total penguins
            //0  to length of array-1 = row
            //0 to height of array-1 = column

            // FishPenguin random locations/
            /*
            int penguinRandRow = (int) (copy.getPieceArray().length-1 * Math.random());
            int penguinRandCol = (int) (copy.getPieceArray().length-1 * Math.random());

            // FishTile random locations
            int tileRandRow = (int) (copy.getBoardState().length-1 * Math.random());
            int tileRandCol = (int) (copy.getBoardState().length-1 * Math.random());
            */

            // using our copy of gamestate
            FishTile[][] pieceBoard = copy.getBoardState();
            // loop through
            for(int i =0; i < pieceBoard.length; i++){
                for(int j=0; j< pieceBoard[i].length;j++){
                    //computer player hard coded to play2 for alpha release
                    if(pieceBoard[i][j].hasPenguin() == true && pieceBoard[i][j].getPenguin().getPlayer() == 2){
                        computerMovePenguin(pieceBoard[i][j].getPenguin());

                    }
                }

            }

            Log.d("Move","Computer Player Moving");
            //FishMoveAction moveAction = new FishMoveAction(this,copy.getPieceArray()[1][0], );
            //game.sendAction(moveAction);
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
        if (!(pieceBoard[p.getX()][p.getY() + 1].hasPenguin()) && (pieceBoard[p.getX()][p.getY() + 1].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX());
            p.setYPos(p.getY() + 1);
            this.boardState[p.getX()][p.getY() + 1].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }

        //try to move diagonally down to the right
        if (!(pieceBoard[p.getX() + 1][p.getY()].hasPenguin()) && (pieceBoard[p.getX() + 1][p.getY()].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX() + 1);
            p.setYPos(p.getY());
            this.boardState[p.getX() + 1][p.getY()].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }

        //try to move diagonally down to the left
        if (!(pieceBoard[p.getX() + 1][p.getY() - 1].hasPenguin()) && (pieceBoard[p.getX() + 1][p.getY() - 1].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX() + 1);
            p.setYPos(p.getY() - 1);
            this.boardState[p.getX() + 1][p.getY() - 1].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }

        //try to move horizontally to the left
        if (!(pieceBoard[p.getX()][p.getY() - 1].hasPenguin()) && (pieceBoard[p.getX()][p.getY() - 1].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX());
            p.setYPos(p.getY() - 1);
            this.boardState[p.getX()][p.getY() - 1].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }

        //try to move diagonally up to the left
        if (!(pieceBoard[p.getX() - 1][p.getY()].hasPenguin()) && (pieceBoard[p.getX() - 1][p.getY()].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX()][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX() - 1);
            p.setYPos(p.getY());
            this.boardState[p.getX() - 1][p.getY()].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }

        //try to move diagonally up to the right
        if (!(pieceBoard[p.getX() - 1][p.getY() + 1].hasPenguin()) && (pieceBoard[p.getX() - 1][p.getY() + 1].doesExist())) {
            addScore(copy.getPlayerTurn(), this.boardState[p.getX() + 1][p.getY()].getNumFish());
            this.boardState[p.getX()][p.getY()].setExists(false);
            p.setXPos(p.getX() - 1);
            p.setYPos(p.getY() + 1);
            this.boardState[p.getX() - 1][p.getY() + 1].setPenguin(p);
            //copy.setPlayerTurn((copy.getPlayerTurn()+1)%copy.getNumPlayers());
            copy.setPlayerTurn(0);
            return true;
        }
    }
        copy.setPlayerTurn(0);
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
