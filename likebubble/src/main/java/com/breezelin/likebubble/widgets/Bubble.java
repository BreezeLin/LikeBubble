package com.breezelin.likebubble.widgets;

/**
 * Created by Breeze Lin
 * 2016/7/12 15:21
 * breezesummerlin@163.com
 */

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 泡泡实体
 */
public class Bubble {

    /**
     * 图片
     */
    private Bitmap bubbleBitmap;
    /**
     * 绘制矩阵，包含位置、倾斜和缩放等信息
     */
    private Matrix bubbleMatrix;
    /**
     * 坐标x
     */
    private int x;
    /**
     * 坐标y
     */
    private int y;
    /**
     * 透明度
     */
    private int alpha;

    /**
     * 点赞泡泡
     *
     * @param bubbleBitmap 泡泡的图片
     */
    public Bubble(Bitmap bubbleBitmap) {
        this.bubbleBitmap = bubbleBitmap;
        bubbleMatrix = new Matrix();
        reset();
    }

    /**
     * 泡泡属性重置
     */
    public void reset() {
        x = 0;
        y = 0;
        alpha = 255;
        // 重置绘制矩阵属性
        bubbleMatrix.setTranslate(0, 0);
        bubbleMatrix.setScale(1, 1);
        bubbleMatrix.setRotate(0);
    }

    public Bitmap getBubbleBitmap() {
        return bubbleBitmap;
    }

    /**
     * @return 泡泡的横向坐标
     */
    public int getX() {
        return x;
    }

    /**
     * @param x 泡泡的横向坐标
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return 泡泡的纵向坐标
     */
    public int getY() {
        return y;
    }

    /**
     * @param y 泡泡的纵向坐标
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return 泡泡的透明度
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * @param alpha 泡泡的透明度
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    /**
     * @return 泡泡的矩阵
     */
    public Matrix getBubbleMatrix() {
        return bubbleMatrix;
    }
}
