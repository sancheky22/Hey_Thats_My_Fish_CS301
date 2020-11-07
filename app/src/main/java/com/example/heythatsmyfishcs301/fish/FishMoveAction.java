package com.example.heythatsmyfishcs301.fish;

import android.util.Log;

import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.actionMsg.GameAction;

import static java.lang.Math.abs;

public class FishMoveAction extends GameAction {

    //Instance Variables for a move action:
    FishPenguin penguin;
    FishTile destination;
    FishGameState g;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FishMoveAction(GamePlayer player, FishGameState gameState, FishPenguin penguin, FishTile tile) {
        super(player);
        this.penguin = new FishPenguin(penguin);
        this.destination = new FishTile(tile);
        this.g = new FishGameState(gameState);
        if (movePenguin(penguin,destination.getX(),destination.getY())){
            Log.d("From Fish Move Action", "Legal move");
        }
        else{
            Log.d("From Fish Move Action", "Illegal move");
        }
    }

    public boolean movePenguin(FishPenguin p, int x, int y){
        FishTile[][] boardState = g.getBoardState();

        //Make sure the penguin is not moving to the same tile
        if(p.getX() == x && p.getY() == y){
            return false;
        }
        //Make sure that the space you are moving to exists (might be redundant later im not sure)
        if (!boardState[x][y].doesExist()){
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
                if (boardState[i][p.getY()].hasPenguin() || !boardState[i][p.getY()].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is vertical (down right diag)
        else if (direction == 1){
            int s = Integer.signum(y-p.getY());
            for (int i = p.getY()+s; i == y; i+=s){
                if (boardState[p.getX()][i].hasPenguin() || !boardState[p.getX()][i].doesExist()){
                    return false;
                }
            }
        }
        //If the new move is up right diag
        else {
            int s = Integer.signum((y-x) - (p.getY()-p.getX()));
            for (int i = 0; i == abs(x-p.getX()); i++){
                if (boardState[p.getX()+i][p.getY()+i].hasPenguin() || !boardState[p.getX()+i][p.getY()+i].doesExist()){
                    return false;
                }
            }
        }

        //If the move is legal, then add to the player's score the fish on the tile and remove the tile from the game. Then pass the turn.
        addScore(g.getPlayerTurn(),boardState[p.getX()][p.getY()].getValue());
        boardState[p.getX()][p.getY()].setExists(false);
        p.setXPos(x);
        p.setYPos(y);
        boardState[x][y].setHasPenguin(true);
        boardState[x][y].setPenguin(new FishPenguin(p));
        g.setPlayerTurn((g.getPlayerTurn()+1)%g.getNumPlayers());
        return true;
    }

    //Helper method that is called whenever a player's score needs to be incremented
    //p = player's turn, s = score to be added
    public void addScore(int pT, int s){
        switch(pT){
            case 0:
                g.setPlayer1Score(g.getPlayer1Score()+s);
                break;
            case 1:
                g.setPlayer2Score(g.getPlayer2Score()+s);
                break;
            case 2:
                g.setPlayer3Score(g.getPlayer3Score()+s);
                break;
            case 3:
                g.setPlayer4Score(g.getPlayer4Score()+s);
                break;
        }
    }
}
