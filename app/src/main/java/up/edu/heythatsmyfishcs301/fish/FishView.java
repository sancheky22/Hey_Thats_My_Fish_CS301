package up.edu.heythatsmyfishcs301.fish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import up.edu.heythatsmyfishcs301.R;


/**
 *Descriptions: FishView Class contains all of the components that makes the 1-fish, 2-fish, 3-fish,
 * red-Penguin, and orange-Penguin. It is controlled by the Human Player and draws all of the
 * components such as the board,1-fish, 2-fish, 3-fish, red-Penguin, and orange-Penguin.
 * This class also assigns the hitboxes to the tile objects.
 *
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/
public class FishView extends SurfaceView {

    //instance variables necessary to draw the board state
    private FishGameState gameState;
    private int cWidth;
    private int cHeight;
    private HexagonDrawable hex = new HexagonDrawable(0xFFC3F9FF);
    private HexagonDrawable bigHex = new HexagonDrawable(0xFF5685C5);
    private HexagonDrawable bigHexHighLight = new HexagonDrawable(0xFFFCBA03);
    private final int RESIZE = 120;
    Bitmap redPenguin = null;
    Bitmap resizedRedPenguin = null;
    Bitmap orangePenguin = null;
    Bitmap resizedOrangePenguin = null;
    Bitmap bluePenguin = null;
    Bitmap resizedBluePenguin = null;
    Bitmap greenPenguin = null;
    Bitmap resizedGreenPenguin = null;

//    Bitmap cursedPenguin = null;
//    Bitmap resizedCursedPenguin = null;


    Bitmap oneFish = null;
    Bitmap twoFish = null;
    Bitmap threeFish = null;

    Bitmap rOneFish = null;
    Bitmap rTwoFish = null;
    Bitmap rThreeFish = null;

    private Paint testPaint = new Paint();


    /**
     * FishView constructor
     *
     * @param context
     * @param attrs
     */
    public FishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        //final gamestate,
        gameState = new FishGameState(4);

        /**
         *External Citation
         * Date: 9/18/20
         * Problem: Wanted the background of the surfaceview to be transparent instead of black
         * Resource: https://stackoverflow.com/questions/5391089/how-to-make-surfaceview-transparent
         *
         * Created by: Jens Jensen (4/14/2014)
         *
         * Solution: We copied these lines of code into our constructor to get rid of the black background
         */
        SurfaceView sfvTrack = (SurfaceView) findViewById(R.id.fishView);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        ((SurfaceHolder) sfhTrackHolder).setFormat(PixelFormat.TRANSPARENT);

        testPaint.setColor(Color.WHITE);
        oneFish = BitmapFactory.decodeResource(getResources(), R.drawable.onefishtile);
        twoFish = BitmapFactory.decodeResource(getResources(), R.drawable.twofishtile);
        threeFish = BitmapFactory.decodeResource(getResources(), R.drawable.threefishtile);
        rOneFish = Bitmap.createScaledBitmap(oneFish, 90, 90, false);
        rTwoFish = Bitmap.createScaledBitmap(twoFish, 90, 90, false);
        rThreeFish = Bitmap.createScaledBitmap(threeFish, 90, 90, false);

        redPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.redpenguin);
        orangePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.orangepenguin);
        bluePenguin = BitmapFactory.decodeResource(getResources(), R.drawable.bluepenguin);
        greenPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.greenpenguin);
        //cursedPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.cursed);
    }


    /**
     * Gets width and height of the canvas
     * draws gameState
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();
        drawBoard(canvas, gameState);
    }


    /**
     * This method uses the dimensions of the Canvas to draw a board of an appropriate size
     * Each hexagon can fit into a Rect object as its bounds so we take a rect and draw a hexagon
     * inside of it
     * we also draw any other properties of the tile that need to be drawn (Fish and penguins).
     * We repeat this process for all 8 rows and the end result is a complete board.
     * @param c
     * @param g
     */
    public void drawBoard(Canvas c, FishGameState g) {
        FishTile[][] board = g.getBoardState();

        //For the human player, this method finds their selected penguin
        //We do this to highlight available tiles
        FishPenguin selected = null;
        for (FishPenguin[] arr : g.getPieceArray()){
            for (FishPenguin p : arr){
                if (p.getSelected() != 0) {
                    selected = p;
                }
            }
        }

        int hexHeight = cHeight / 8;
        int hexWidth = cWidth / 8;
        int margin = 15;

        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin, hexWidth - 20,
                hexWidth - 20, false);
        resizedOrangePenguin = Bitmap.createScaledBitmap(orangePenguin, hexWidth - 20,
                hexWidth - 20, false);
        resizedBluePenguin = Bitmap.createScaledBitmap(bluePenguin, hexWidth - 20,
                hexWidth - 20, false);
        resizedGreenPenguin = Bitmap.createScaledBitmap(greenPenguin, hexWidth - 20,
                hexWidth - 20, false);
       // resizedCursedPenguin = Bitmap.createScaledBitmap(cursedPenguin, hexWidth - 20, hexWidth - 20, false);


       /** Here we go through the array and if the tile is null, then it is a placeholder and we
        * skip it.
        * If it is a tile that exists, we draw it.
        * Increment the Rect bound object
        * At the end of the row, reset bound to the start**/

        //Rect object is where the hexagon is drawn
        Rect bound = new Rect(0, 0, hexWidth, hexHeight);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                FishTile tile = board[i][j];

                if (tile == null) {
                    continue;
                }

                //This will draw the hexagon, fish, and then the penguins on each tile
                if (tile.doesExist()) {


                    //If the selected penguin can move to the tile, then highlight it
                    //Or if it is the place phase and it is a legal place tile
                    if ((gameState.getGamePhase() == 0 && tile.getNumFish() == 1 && !tile.hasPenguin())
                            || reachable(selected,tile)){
                        bigHexHighLight.computeHex(bound);
                        bigHexHighLight.draw(c);
                    }
                    else {
                        bigHex.computeHex(bound);
                        bigHex.draw(c);
                    }

                    Rect s = new Rect(bound);

                    s.top += margin;
                    s.bottom -= margin;
                    s.right -= margin;
                    s.left += margin;

                    //We set the hitbox for the Tile
                    tile.setBoundingBox(s);
                    hex.computeHex(s);
                    hex.draw(c);

                    //draw the fish on the hexagon
                    switch (tile.getNumFish()) {
                        case 1:
                            //Draw 1 fish
                            c.drawBitmap(rOneFish, tile.getBoundingBox().left + 10,
                                    tile.getBoundingBox().top + 15, null);
                            break;
                        case 2:
                            //Draw 2 fish
                            c.drawBitmap(rTwoFish, tile.getBoundingBox().left + 10,
                                    tile.getBoundingBox().top + 15, null);
                            break;
                        case 3:
                            //Draw 3 fish
                            c.drawBitmap(rThreeFish, tile.getBoundingBox().left + 10,
                                    tile.getBoundingBox().top + 15, null);
                            break;
                    }


                    //Assigns the appropriate penguin color to the player
                    FishPenguin p = tile.getPenguin();
                    if (p != null){
                        //If the penguin is selected, then this float > 0. Otherwise it equals 0.


                        int selection = p.getSelected()*30;
                        resizedOrangePenguin = Bitmap.createScaledBitmap(orangePenguin,
                                RESIZE+selection,RESIZE+selection,false);

                        resizedRedPenguin = Bitmap.createScaledBitmap(redPenguin,
                                RESIZE+selection,RESIZE+selection,false);

                        resizedBluePenguin = Bitmap.createScaledBitmap(bluePenguin,
                                RESIZE+selection,RESIZE+selection,false);

                        resizedGreenPenguin = Bitmap.createScaledBitmap(greenPenguin,
                                RESIZE+selection,RESIZE+selection,false);

                        if (p.getPlayer() == 0){
                            c.drawBitmap(resizedOrangePenguin,
                                    tile.getBoundingBox().left-selection/2,
                                    tile.getBoundingBox().top-selection/2, null);
                        }
                        else if(p.getPlayer() == 1){
                            c.drawBitmap(resizedRedPenguin,
                                    tile.getBoundingBox().left-selection/2,
                                    tile.getBoundingBox().top-selection/2, null);
                        }
                        else if(p.getPlayer() == 2){
                            c.drawBitmap(resizedBluePenguin,
                                    tile.getBoundingBox().left-selection/2,
                                    tile.getBoundingBox().top-selection/2, null);
                        }
                        else if(p.getPlayer() == 3){
                            c.drawBitmap(resizedGreenPenguin,
                                    tile.getBoundingBox().left-selection/2,
                                    tile.getBoundingBox().top-selection/2, null);
                        }
                    }
                }

                //increment the bounds
                bound.right += hexWidth;
                bound.left += hexWidth;
            }

            //Need to set the bound back to the start and up one more line.
            //If the next line is even, set the left and right bounds to 0
            //If it's odd, offset the tile .
            float spacing = 0.75f;

            bound.left = ((i + 1) % 2) * hexWidth / 2;
            bound.right = ((i + 1) % 2) * hexWidth / 2 + hexWidth;
            bound.bottom += hexHeight * spacing;
            bound.top += hexHeight * spacing;
        }
        // attempt to try to prevent the game from crashing due to Bitmaps causing memory overload
        // reclaims memory used by bitmaps
        resizedGreenPenguin.recycle();
        resizedBluePenguin.recycle();
        resizedRedPenguin.recycle();
        resizedOrangePenguin.recycle();
    }


    /**
     * Returns true if a penguin can move to a tile
     * @param p penguin
     * @param t tile
     * @return true if p can move to t
     */
    private boolean reachable(FishPenguin p, FishTile t){
        FishTile[][] boardState = gameState.getBoardState();
        if (gameState.getGamePhase() == 0) return false;
        if (p == null) return false;

        int px = p.getX();
        int py = p.getY();

        int x = t.getX();
        int y = t.getY();

        //Make sure the penguin is not moving to the same tile
        if(px == x && py == y){
            return false;
        }

        //Make sure that the space you are moving to exists (might be redundant later im not sure)
        if (!boardState[x][y].doesExist()){
            return false;
        }

        //0 means horizontal, 1 means down right diag, 2 means up right diag
        int direction;
        if (py == y){
            direction = 1;
        }
        else if (px == x){
            direction = 0;
        }
        else if (py+px == x+y){
            direction = 2;
        }
        else{
            return false;
        }

        //If the new move is up left diag or down right diag
        if (direction == 1){
            //s is the sign of (new coordinate - old coordinate)
            //if s is positive, then you are moving to the right
            int s = Integer.signum(x-px);
            for (int i = px+s; i != x + s; i+=s){
                if (boardState[i][py].hasPenguin() || !boardState[i][py].doesExist()){
                    return false;
                }
            }
        }

        //If the new move is horizontal (left or right)
        else if (direction == 0){
            int s = Integer.signum(y-py);
            for (int i = py+s; i != y + s; i+=s){
                if (boardState[px][i].hasPenguin() || !boardState[px][i].doesExist()){
                    return false;
                }
            }
        }

        //If the new move is up right diag or down left diag
        else {
            //If s is positive, you are moving upper right diag
            int s = Integer.signum(y-py);
            for (int i = s; i != y-py + s; i+=s){
                if (boardState[px-i][py+i].hasPenguin() || !boardState[px-i][py+i].doesExist()){
                    return false;
                }
            }
        }
        return true;
    }

    public FishGameState getGameState() { return this.gameState; }
    public void setGameState(FishGameState f) { this.gameState = new FishGameState(f); }
} //FishView Class
