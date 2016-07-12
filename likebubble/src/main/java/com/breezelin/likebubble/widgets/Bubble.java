package com.breezelin.likebubble.widgets;

/**
 * Created by Breeze Lin
 * 2016/7/12 15:21
 * breezesummerlin@163.com
 */

import android.graphics.Bitmap;

/**
 * 泡泡实体
 */
public class Bubble {

    private Bitmap bubbleBitmap;
    private int x;
    private int y;
    private int rotation;
    private int scaleX;
    private int scaleY;

    public Bubble(Bitmap bubbleBitmap) {
        this.bubbleBitmap = bubbleBitmap;
        reset();
    }

    public void reset() {
        x = 0;
        y = 0;
        rotation = 0;
        scaleX = 0;
        scaleY = 0;
    }

    public Bitmap getBubbleBitmap() {
        return bubbleBitmap;
    }

    public void setBubbleBitmap(Bitmap bubbleBitmap) {
        this.bubbleBitmap = bubbleBitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getScaleX() {
        return scaleX;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public int getScaleY() {
        return scaleY;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }
}
