package com.example.heythatsmyfishcs301.fish;

import android.view.View;

import com.example.heythatsmyfishcs301.game.GameHumanPlayer;
import com.example.heythatsmyfishcs301.game.GameMainActivity;
import com.example.heythatsmyfishcs301.game.infoMsg.GameInfo;

public class FishHumanPlayer extends GameHumanPlayer {
    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }
}
