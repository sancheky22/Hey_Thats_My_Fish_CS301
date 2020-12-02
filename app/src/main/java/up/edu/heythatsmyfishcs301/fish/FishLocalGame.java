package up.edu.heythatsmyfishcs301.fish;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.LocalGame;
import up.edu.heythatsmyfishcs301.game.actionMsg.GameAction;

/**
 *
 * The FishLocalGame receives the current GameState and sends copies to their respective players.
 * The LocalGame also checks if the game is over by displaying the name of the player who won
 * to the screen.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishLocalGame extends LocalGame {
    //This is called after each turn and it sends a copy of the game state to the next player

    // create GameState variable for this class
    private FishGameState fState;
    private FishTile[][] board;

    /**
     * Constructor for the local game.
     * @param numPlayers passed in from the MainActivity.
     */
    public FishLocalGame(int numPlayers){
        super();
        fState = new FishGameState(numPlayers);
    }


    /**
     * This method is called whenever an action is received.
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return boolean If false, sends a NotYourTurnInfo to the player in the parameter
     */
    @Override
    protected boolean canMove(int playerIdx) {
        //In order to know if the player can move, we need to know if that player is on the board.
        //If they are not, we increment the turn.
        //Because this method is passed to every single player in the game, we need to keep the
        //player turn the same. This double for loop accomplishes this.
        for (int i = 0; i <= 4; i++) {
            for (GamePlayer player : players) {
                boolean myTurn = (fState.getPlayerTurn() == getPlayerIdx(player));
                if (!myTurn){
                    continue;
                }

                /*
                 * We have these try catch statements all over the Local Game.
                 * The purpose of these is to access the out variable that all FishGamePlayers have.
                 * It attempts to cast the GamePlayer object from the player array to a human
                 * or computer. We use a try catch because it allows us to handle ClassCastException
                 * errors.
                 */
                try {
                    FishComputerPlayer temp = (FishComputerPlayer) player;
                    if (temp.isOut() && myTurn) {
                        fState.changeTurn();
                    }
                } catch (ClassCastException e) {
                    Log.d("From canMove", "Player cast exception");
                    FishHumanPlayer temp = (FishHumanPlayer) player;
                    if (temp.isOut() && myTurn) {
                        fState.changeTurn();
                    }
                }
            }
        }
        //If it is not the player's turn, return false so they do not move.
        return playerIdx == fState.getPlayerTurn();
    }

    /**
     * This method is called whenever a new action arrives from a player.
     * This is where we update the game state
     * This includes changing the turn, updating the Tiles on the board, scores etc.
     * If it is an illegal action, it returns false and the game framework sends the player an
     * instance of the IllegalMoveInfo.
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return boolean True if action is legal.
     */
    @Override
    protected boolean makeMove(GameAction action) {

        //This means that the action is a movement action
        if (action instanceof FishMoveAction){
            Log.d("makeMove @LocalGame", "Someone made a move");
            FishTile dest = ((FishMoveAction) action).getDestination();
            FishPenguin penguin = ((FishMoveAction) action).getPenguin();

            //Update the game state properly
            //If it is an illegal move, then this met
            if(fState.movePenguin(penguin,dest.getX(),dest.getY())){
                Log.d("makeMove","Move was legal");
                fState.changeTurn();
                return true;
            }
            else{
                Log.d("makeMove","Move was not legal");
                return false;
            }
        }

        //This means that the action was a place action.
        if(action instanceof FishPlaceAction){
            Log.d("Placing penguins", "Trying to place a penguin");
            FishTile dest = ((FishPlaceAction) action).getDestination();
            FishPenguin penguin = ((FishPlaceAction) action).getPenguin();

            if(fState.placePenguin(penguin, dest.getX(), dest.getY())){
                Log.d("Place penguin", "penguin is now on tile (" + dest.getX() + ", "
                        + dest.getY() + ")");

                fState.changeTurn();
                if (fState.getGamePhase() == 0){
                    for (FishPenguin[] arr : fState.getPieceArray()){
                        for (FishPenguin p : arr){
                            if (!p.isOnBoard()){
                                //Then game phase stays the same
                                return true;
                            }
                        }
                    }
                    //We set the game phase to 1 once all of the penguins are on the board.
                    this.fState.setGamePhase(1);
                }
                return true;
            }
            else{
                Log.d("Place penguin", "Placing penguin failed");
                return false;
            }
        }
        return false;
    }


    /**
     * This method is called when the game sends the game state to each player.
     * In this method, we make sure one more time that if there are any players with no moves, we
     * remove them from the board. We have to do it again here because the state was just updated.
     * @param p player to send info to
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

        //Local variables that will be useful later.
        boolean myTurn;
        int nextTurn = fState.getPlayerTurn();

        //If it is not the place phase
        if (fState.getGamePhase() == 1) {

            /*
             * Checks if the next player has any legal moves.
             * If they do, then give everyone the info.
             */
            for (FishPenguin penguin : fState.getPieceArray()[nextTurn]) {
                if (fState.testMove(penguin)) {
                    FishGameState copy = new FishGameState(this.fState);
                    copy.setPlayers(players);
                    p.sendInfo(copy);
                    return;
                }
            }

            /*
             * if they don't then you can remove them from the board.
             * perform all of the necessary actions (removing tiles, checking scores, etc.)
             * Then we can send everyone the info.
             */
            for (FishPenguin penguin : fState.getPieceArray()[nextTurn]) {
                penguin.setOnBoard(false);
                int px = penguin.getX();
                int py = penguin.getY();
                board[px][py].setHasPenguin(false);
                board[px][py].setPenguin(null);
                board[px][py].setExists(false);
            }


            //We can put that player out of the game at this point because they have no moves.
            try {
                FishComputerPlayer player = (FishComputerPlayer) players[nextTurn];
                player.setOutOfGame(true);
            }
            catch(ClassCastException e){
                FishHumanPlayer player = (FishHumanPlayer) players[nextTurn];
                player.setOutOfGame(true);
            }

            /*
             * We do an additional check to increment the player's turn in the game state one more
             * time because we just altered it.
             */
            for (int i = 0; i <= 4; i++) {
                for (GamePlayer player : players) {
                    myTurn = (fState.getPlayerTurn() == getPlayerIdx(player));
                    if (!myTurn){
                        continue;
                    }
                    try {
                        FishComputerPlayer temp = (FishComputerPlayer) player;
                        if (temp.isOut() && myTurn) {
                            fState.changeTurn();
                        }
                    } catch (ClassCastException e) {
                        Log.d("From canMove", "Player cast exception");
                        FishHumanPlayer temp = (FishHumanPlayer) player;
                        if (temp.isOut() && myTurn) {
                            fState.changeTurn();
                        }
                    }
                }
            }
        }

        //We send all players the edited game state.
        FishGameState copy = new FishGameState(this.fState);
        copy.setPlayers(players);
        p.sendInfo(copy);
    }


    // This method tests if either player has no moves left
    // if that's the case print to the screen who's the player and the winners score

    /**
     * This method is called whenever an action is received.
     * @return String if null, continue game.
     */
    @Override
    protected String checkIfGameOver() {
        String end = "Game Over";

        // array list to hold scores of each player
        List<Integer> scoreList = new ArrayList<>();

        // get a copy of the boardState
        board = fState.getBoardState();

        //The game can not end in the place penguin phase
        if (fState.getGamePhase() == 0) {
            return null;
        }
        for (GamePlayer p : players) {
            try {
                FishComputerPlayer player = (FishComputerPlayer) p;
                if (!player.isOut()) {
                    return null;
                }
            } catch (ClassCastException e) {
                FishHumanPlayer player = (FishHumanPlayer) p;
                if (!player.isOut()) {
                    return null;
                }
            }

            // at the end of the game gets the score for each player
            int p1Score = fState.getPlayer1Score();
            int p2Score = fState.getPlayer2Score();
            int p3Score = fState.getPlayer3Score();
            int p4Score = fState.getPlayer4Score();

            // add each players score to the arrayList of scores
            scoreList.add(p1Score);
            scoreList.add(p2Score);
            scoreList.add(p3Score);
            scoreList.add(p4Score);


        }
        // this int gets the max score out of the ArrayList which would be the winners score
        int maxScore = Collections.max(scoreList);

        // if the maxScore matches a certain players score then they are the winner
        if (maxScore == fState.getPlayer1Score()) {
            return playerNames[0] + " is the winner with a score of " + maxScore;
        } else if (maxScore == fState.getPlayer2Score()) {
            return playerNames[1] + " is the winner with a score of " + maxScore;
        } else if (maxScore == fState.getPlayer3Score()) {
            return playerNames[2] + " is the winner with a score of " + maxScore;
        } else if (maxScore == fState.getPlayer4Score()) {
            return playerNames[3] + " is the winner with a score of " + maxScore;
        }
        // return Game Over screen if there are scores that are a tie
        return "No one wins there is a tie! " + end;
    }
}
