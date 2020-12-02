package up.edu.heythatsmyfishcs301.fish;

import android.media.MediaPlayer;

import java.util.ArrayList;

import up.edu.heythatsmyfishcs301.R;
import up.edu.heythatsmyfishcs301.game.GameMainActivity;
import up.edu.heythatsmyfishcs301.game.GamePlayer;
import up.edu.heythatsmyfishcs301.game.LocalGame;
import up.edu.heythatsmyfishcs301.game.config.GameConfig;
import up.edu.heythatsmyfishcs301.game.config.GamePlayerType;


/**
 * STATUS OF OUR GAME (BETA RELEASE) :
 * The game is fully functional, and supporst all the rules of play we committed to implement in our
 * requirements. It is possible to play with any combination of players allowed by the requirements
 * and the rules of play.
 *
 * All functionality of the GUI we specified in our requirements.
 *
 * Our "smart" and our "dumb" AI are also fully implemented. Our dumb AI when placing its penguins
 * places them on the first tile it sees has one fish. When it moves its penguin it just moves to
 * the next spot it sees that is valid, meaning there is a straight path to the tile and it isn't
 * being obstructed by another penguin. Our "smart" AI has improved its placing action by randomly
 * placing its pieces on any tile that has one fish. This spreads out the AI's pieces so that it can
 * cover more ground and potentially get a higher score. It's move action is improved because it
 * uses the same search algorithm to find a valid place but what differs from the "dumb" AI is that
 * it searches each direction it can move and if that direction has a tile with 3 fish it moves there
 * first to get the most score. If there aren't any 3 fish it can move to it checks to see if there
 * are 2 fish, then 1 fish. We have seen in tests that on average the smart player scores more due
 * to the improvements we've made
 *
 * All graphic elements are complete and in their final form. All final versions of our penguins and
 * fishTiles are complete.
 *
 * THe GUI is also effective and functional :)
 *
 * We conducted a breadth of tests along with multiple edge case tests and have conlcuded that our
 * game has a few bugs that we know of one of which is that, if the human player loses or wins,
 * their penguins don't get redrawn to the side of the GUI. However this doesn't affect gameplay and
 * is still fully functional with this in the game still. Another bug is that when run on the ASUS
 * tablets if you restart the game multiple times it crashes. We have found this is due to the fact
 * that we have a lot of bitmaps in our project which use a lot of memory. This is causing an out
 * of memory error (~2GB of memory used).
 *
 *
 */




/**
 * This is the main activity, and the program is run from here.
 * This is where the local game is initialized and then the game framework handles the rest.
 * One important thing in this class is the definitions for the types of players: Human and computer.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishMainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 2234;
    // MediaPlayer instance variable
    private MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    public GameConfig createDefaultConfig() {


        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<>();

        // GUI
        playerTypes.add(new GamePlayerType("Local Human Player (human)") {
            public GamePlayer createPlayer(String name) {
                return new FishHumanPlayer(name);
            }
        });

        // dumb computer player
        playerTypes.add(new GamePlayerType("Computer Player (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new FishComputerPlayer1(name);
            }
        });

        // smart computer player
        playerTypes.add(new GamePlayerType("Computer Player (smart)") {
            public GamePlayer createPlayer(String name) {return new FishComputerPlayer2(name);
            }
        });

        // Create a game configuration class
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4, "Hey That's My Fish", PORT_NUMBER);
        // Add the default players
        defaultConfig.addPlayer("Human", 0); // GUI
        defaultConfig.addPlayer("Computer 1", 1); // dumb computer player

        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        //defaultConfig.setRemoteData("Remote Player", "", 0); // remote player GUI
        //done!

        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame(int numPlayers) {
        // start the media player
        startMedia();

        // return new local game
        return new FishLocalGame(numPlayers);
    }

    public void startMedia(){
        // assign mediaplayer
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.penguintheme);

        // make song loop so it doesn't stop playing after the song ends
        mediaPlayer.setLooping(true);
        // make volume equal for headphone users
        mediaPlayer.setVolume(1, 1);
        // start playing the song
        mediaPlayer.start();
    }

    /**
     * External Citation
     * Date:    12/1/20
     * Problem: Want to mute and unmute game with button press using the next 2 methods
     * Resource: https://developer.android.com/reference/android/media/MediaPlayer
     * Solution: Referenced the MediaPlayer api to see what methods are available
     * Created by android developers.
     *
     * We used the isPlaying() method to see if the game is currently playing music. If it is and
     * the player presses the mute button, then the stopMedia() method will be called and the
     * music will stop playing. If the game is not playing music and the player presses the same button,
     * the game will start playing music again
     */
    public boolean getIsPlaying(){
        // returns boolean that is if the mediaplayer is playing or not
        return mediaPlayer.isPlaying();
    }

    public void stopMedia(){
        mediaPlayer.stop();
    }
}
