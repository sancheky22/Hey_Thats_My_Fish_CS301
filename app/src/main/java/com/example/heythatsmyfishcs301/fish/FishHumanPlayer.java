package com.example.heythatsmyfishcs301.fish;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.heythatsmyfishcs301.R;
import com.example.heythatsmyfishcs301.game.GameHumanPlayer;
import com.example.heythatsmyfishcs301.game.GameMainActivity;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

public class FishHumanPlayer extends GameHumanPlayer {

    private GameMainActivity myActivity;

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
        if (!(info instanceof FishGameState)) {
            flash(0xFFFF0000, 1000);
            return;
        }


    }


    //I don't know when this is called or what it does.
    @Override
    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;
        activity.setContentView(R.layout.activity_main);

    }
}
