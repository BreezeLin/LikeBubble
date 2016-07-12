package com.breezelin.likebubble.widgets;

/**
 * Created by Breeze Lin
 * 2016/7/12 15:19
 * breezesummerlin@163.com
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.breezelin.likebubble.R;

import java.util.LinkedList;
import java.util.Random;

/**
 * 尝试写的以view为基础的点赞效果
 */
public class BubbleLikeView2 extends View {

    /**
     * 泡泡总量
     */
    private static final int MAX_BUBBLES = 100;
    /**
     * 泡泡飞行速度（像素/帧）
     */
    private static final int FLASH_RANGE = 3;

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 未飞行的泡泡
     */
    private LinkedList<Bubble> restBubbles;
    /**
     * 飞行中的泡泡
     */
    private LinkedList<Bubble> flyingBubbles;
    /**
     * 应当被移除的泡泡
     */
    private LinkedList<Bubble> removeBubbles;
    /**
     * 随机数产生器
     */
    private Random random;

    public BubbleLikeView2(Context context) {
        super(context);
        init();
    }

    public BubbleLikeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLikeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        random = new Random();
        restBubbles = new LinkedList<>();
        flyingBubbles = new LinkedList<>();
        removeBubbles = new LinkedList<>();
        Bitmap bubbleBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_like_bubble_1);
        for (int i = 0; i < MAX_BUBBLES; i++) {
            restBubbles.add(new Bubble(bubbleBitmap));
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 增加飞行中的泡泡
     */
    public void addBubble() {
        // 如果还有剩余的泡泡，则从剩余泡泡中获取一个新的泡泡，并加入飞行的泡泡中
        // TODO: 2016/7/12 应当以handler处理泡泡过多的情况，比如排队进入
        if (restBubbles.size() > 0) {
            Bubble bubble = restBubbles.get(random.nextInt(restBubbles.size()));
            bubble.setY(getHeight());
            bubble.setX(getWidth() / 2);
            restBubbles.remove(bubble);
            flyingBubbles.add(bubble);
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 遍历飞行中的泡泡，进行绘制
        for (Bubble bubble : flyingBubbles) {
            // 如果泡泡已经飞到顶部或边缘，则将其移除
            if (bubble.getY() <= 0 || bubble.getX() == 0 || bubble.getX() == getWidth()) {
                removeBubbles.add(bubble);
                // 跳出循环，不再对此泡泡进行绘制
                continue;
            }
            // 进行泡泡的绘制
            canvas.drawBitmap(bubble.getBubbleBitmap(), bubble.getX(), bubble.getY(), paint);
            // 重置画笔
            paint.reset();
            // 泡泡属性更新
            // 左右方向随机
            if (random.nextBoolean()) {
                bubble.setX(bubble.getX() + FLASH_RANGE);
            } else {
                bubble.setX(bubble.getX() - FLASH_RANGE);
            }
            // 高度上升概率随机
            if (random.nextBoolean()) {
                bubble.setY(bubble.getY() - FLASH_RANGE);
            }
        }
        // 存在需要移除的泡泡
        if (removeBubbles.size() > 0) {
            // 将需要移除的泡泡转移至闲余泡泡中
            flyingBubbles.removeAll(removeBubbles);
            restBubbles.addAll(removeBubbles);
            // 重置移除的泡泡
            for (Bubble bubble : removeBubbles) {
                bubble.reset();
            }
            // 清空需移除的泡泡集合
            removeBubbles.clear();
        }
        invalidate();
    }
}
