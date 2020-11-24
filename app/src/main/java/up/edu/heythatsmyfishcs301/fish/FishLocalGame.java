package up.edu.heythatsmyfishcs301.fish;

import android.util.Log;

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


    // Constructor for the local game that creates a new GameState
    public FishLocalGame(int numPlayers){
        super();
        this.fState = new FishGameState(numPlayers);
    }


    /**
     * This method is called whenever an action is received.
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return
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
                /**
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
        if (playerIdx != fState.getPlayerTurn()) return false;
        return true;
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
     * @return
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
                Log.d("Place penguin", "penguin is now on tile (" + dest.getX() + ", " + dest.getY() + ")");

                this.fState.changeTurn();
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
     * @param p
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

        //Local variables that will be useful later.
        boolean myTurn;
        int nextTurn = fState.getPlayerTurn();

        //If it is not the place phase
        if (fState.getGamePhase() == 1) {

            /**
             * Checks if the next player has any legal moves.
             * If they do, then give everyone the info.
             */
            for (FishPenguin penguin : fState.getPieceArray()[nextTurn]) {
                if (fState.testMove(penguin)) {
                    FishGameState copy = new FishGameState(this.fState);
                    p.sendInfo(copy);
                    return;
                }
            }

            /**
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
                //fState.addScore(nextTurn,board[px][py].getNumFish());
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

            /**
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
        p.sendInfo(copy);
    }




    // This method tests if either player has no moves left
    // if that's the case print to the screen who's the player and the winners score

    /**
     * This method is called whenever an action is received.
     * @return
     */
    @Override
    protected String checkIfGameOver() {
        String end = "Game Over";

        // get a copy of the boardState
        board = fState.getBoardState();
        int playerTurn = fState.getPlayerTurn();
        int nextTurn = playerTurn;
        //The game can not end in the place penguin phase
        if (fState.getGamePhase() == 0){
            return null;
        }
        for (GamePlayer p : players){
            try {
                FishComputerPlayer player = (FishComputerPlayer) p;
                if (!player.isOut()){
                    return null;
                }
            }
            catch (ClassCastException e) {
                FishHumanPlayer player = (FishHumanPlayer) p;
                if (!player.isOut()){
                    return null;
                }
            }
        }

        return end;
        //get a copy of the piece array
        //pieces = fState.getPieceArray();

        //test if human player has any valid moves left. Does so by going through the whole board and checking
        //if it has a penguin on it then checks if the penguin belongs to the human player. Then it calls the
        //testMove function and if there's no legal moves left, it returns who won. If there is a legal move left
        //it checks if the computer has any valid moves left
    }

}
