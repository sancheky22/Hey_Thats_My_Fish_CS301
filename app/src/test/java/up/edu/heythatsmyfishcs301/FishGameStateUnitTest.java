package up.edu.heythatsmyfishcs301;

import up.edu.heythatsmyfishcs301.fish.FishGameState;

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
        //f.addScore(f.getPlayerTurn(),44);
        assertEquals(f.getPlayer1Score(),60);
    }

    @Test
    public void setPlayer1Score(){
        FishGameState f = new FishGameState();
        f.setPlayer1Score(12);
        assertEquals(f.getPlayer1Score(), 12);
    }

    @Test
    public void setPlayer2Score(){
        FishGameState f = new FishGameState();
        f.setPlayer1Score(10);
        assertEquals(f.getPlayer1Score(), 10);
    }

    @Test
    public void getPlayer1SCore(){
        FishGameState f = new FishGameState();
        f.setPlayer1Score(10);
        int score = f.getPlayer1Score();
        assertEquals(score, 10);
    }

    @Test
    public void movePenguin(){
//        FishGameState f = new FishGameState();
//        FishPenguin p = new FishPenguin(0);
//        p.movePenguin(p, 2, 4);
//        int x = p.getX();
//        int y = p.getY();
//        assertEquals(x, 2);
//        asssertEquals(y, 4);
    }


}
