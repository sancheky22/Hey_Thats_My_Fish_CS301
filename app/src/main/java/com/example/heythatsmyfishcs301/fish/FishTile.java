package com.example.heythatsmyfishcs301.fish;

import android.graphics.Rect;

/**
 * @author Kyle Sanchez
 * @author Ryan Enslow
 * @author Carina Pineda
 * @author Linda Nguyen
 **/

public class FishTile {


    private boolean exists;
    private boolean hasPenguin;
    private FishPenguin penguin;
    private int value;
    private int x;
    private int y;
    private Rect boundingBox;

    private int numFish;

    public FishTile(int x, int y){
        this.x = x;
        this.y = y;
        this.exists = true;
        this.hasPenguin = false;
        this.penguin = null;
        //needs to be [1-3] not just 3 but we can work on that later
        this.value = 3;
        this.boundingBox = null;
    }

    public FishTile(FishTile t){
        this.x = t.x;
        this.y = t.y;
        this.exists = t.exists;
        this.hasPenguin = t.hasPenguin;
        this.penguin = new FishPenguin(t.penguin);
        this.boundingBox = new Rect(t.boundingBox);
    }

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean doesExist(){
        return this.exists;
    }

    public boolean hasPenguin(){
        return hasPenguin;
    }

    public FishPenguin getPenguin(){
        return this.penguin;
    }

    public int getValue(){
        return this.value;
    }

    public Rect getBoundingBox(){
        return this.boundingBox;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setExists(boolean b){
        this.exists = b;
    }

    public void setHasPenguin(boolean b){
        this.hasPenguin = b;
    }

    public void setPenguin(FishPenguin p){
        this.penguin = new FishPenguin(p);
    }

    public void setValue(int i){
        this.value = i;
    }

    public void setBoundingBox(Rect r){
        this.boundingBox = new Rect(r);
    }

    public void setNumFish(int i){
        this.numFish = i;
    }

    public int getNumFish(){
        return this.numFish;
    }

}
