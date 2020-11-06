package com.example.heythatsmyfishcs301.fish;

import com.example.heythatsmyfishcs301.R;
import com.example.heythatsmyfishcs301.game.GameMainActivity;
import com.example.heythatsmyfishcs301.game.GamePlayer;
import com.example.heythatsmyfishcs301.game.LocalGame;
import com.example.heythatsmyfishcs301.game.config.GameConfig;
import com.example.heythatsmyfishcs301.game.config.GamePlayerType;

import java.util.ArrayList;

public class FishMainActivity extends GameMainActivity {

    // media player can go here for sounds

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 2234;

    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

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
    public LocalGame createLocalGame() {
        return new FishLocalGame();
    }
}
