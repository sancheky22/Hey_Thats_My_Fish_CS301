package com.example.heythatsmyfishcs301.fish;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.example.heythatsmyfishcs301.R;
import com.example.heythatsmyfishcs301.game.GameHumanPlayer;
import com.example.heythatsmyfishcs301.game.GameMainActivity;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

import java.util.logging.Logger;

public class FishHumanPlayer extends GameHumanPlayer {
    //Tag for logging
    private static final String TAG = "FishHumanPlayer";

    // the most recent game state, given to us by the FishLocalGame
    private FishGameState state;

    // the android activity that we are running
    private GameMainActivity myActivity;

    // the surface view
    private FishView surfaceView;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public FishHumanPlayer(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    //This method is called when the human player receives the copy of the game state.
    //This method is in charge of updating FishView I think
    @Override
    public void receiveInfo(GameInfo info) {

        if (surfaceView == null) return;


        // ignore the message if it's not a FishGameState message
        if (!(info instanceof FishGameState)){
            return;
        } else {
            // update the state
            state = (FishGameState)info;
            //surfaceView.setState(state);
            //surfaceView.invalidate();
            //Logger.log(TAG, "receiving");

        }






    }


    //I don't know when this is called or what it does.
    @Override
    public void setAsGui(GameMainActivity activity) {
        // remember the activity
        myActivity = activity;


        activity.setContentView(R.layout.activity_main);


        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }
    }
}
