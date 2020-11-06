package com.example.heythatsmyfishcs301;

import com.example.heythatsmyfishcs301.fish.FishGameState;

import org.junit.Test;
import static org.junit.Assert.*;

public class FishGameStateUnitTest {

    @Test
    public void setGamePhase(){
        FishGameState f = new FishGameState();
        f.setGamePhase(2);
        assertEquals(f.getGamePhase(),2);
    }

    @Test public void addScore(){
        FishGameState f = new FishGameState();
        f.setPlayer1Score(16);
        f.setPlayerTurn(0);
        f.addScore(f.getPlayerTurn(),44);
        assertEquals(f.getPlayer1Score(),60);
    }
}
