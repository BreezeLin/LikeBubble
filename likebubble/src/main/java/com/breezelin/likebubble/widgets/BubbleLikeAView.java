package com.breezelin.likebubble.widgets;

/**
 * Created by Breeze Lin
 * 2016/4/26 12:19
 * breezesummerlin@163.com
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.breezelin.likebubble.R;
import com.breezelin.likebubble.utils.ScreenUtil;

import java.util.LinkedList;
import java.util.Random;

/**
 * 以ViewGroup和ObjectAnimation为基础的点赞冒泡控件
 */
public class BubbleLikeAView extends FrameLayout {

    /**
     * 预存的泡泡数
     */
    private static final int MAX_BUBBLES = 200;
    /**
     * 泡泡动画时长
     */
    private static final long ANIMATION_DURATION = 4000L;
    /**
     * 泡泡动画触发时间间隔
     */
    private static final long ANIMATION_INTERVAL = ANIMATION_DURATION / MAX_BUBBLES;
    /**
     * 泡泡动画触发时间间隔
     */
    private static final long BUBBLE_INTERVAL = 100L;
    /**
     * 连续动画最大时长
     */
    private static final long DURATION_MAX = 10000L;
    /**
     * 随机数
     */
    private final Random mRandom = new Random();
    /**
     * 上一次动画触发的时间
     */
    private long lastAnimationTime = 0L;
    /**
     * 需要显示的泡泡个数
     */
    private int count;
    /**
     * 动画截止时间
     */
    private long endTime = 0L;
    /**
     * 正在动画中
     */
    private boolean animating = false;
    /**
     * 泡泡的gravity
     */
    private int bubbleGravity = Gravity.CENTER_HORIZONTAL;
    /**
     * 泡泡在底部的位置偏移值
     */
    private int bubbleTranslation = 0;
    /**
     * 泡泡飞行时的左侧边距
     */
    private int bubbleLeftPadding = 0;
    /**
     * 泡泡飞行时的右侧边距
     */
    private int bubbleRightPadding = 0;
    /**
     * 泡泡速度插值器
     */
    private BubbleInterpolator interpolator = new BubbleInterpolator();

    /**
     * 点赞冒泡控件
     */
    public BubbleLikeAView(Context context) {
        super(context);
        initBubbles();
    }

    /**
     * 点赞冒泡控件
     */
    public BubbleLikeAView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBubbles();
    }

    /**
     * 点赞冒泡控件
     */
    public BubbleLikeAView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBubbles();
    }

    /**
     * 点赞泡泡的集合
     */
    private LinkedList<ImageView> bubbles;

    /**
     * 初始化泡泡
     */
    private void initBubbles() {
        // 泡泡集合初始化
        bubbles = new LinkedList<>();
        // 按照限定的泡泡总数来创建泡泡
        for (int i = 0; i < MAX_BUBBLES; i++) {
            ImageView bubble = new ImageView(getContext());
            // 泡泡拥有统一的布局参数
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | bubbleGravity);
            bubble.setLayoutParams(layoutParams);
            bubble.setTranslationX(bubbleTranslation);
            // TODO: 2016/4/26 设置好图片的比例
            if (i >= 0 && i < MAX_BUBBLES / 4) {
                bubble.setImageResource(R.mipmap.ic_like_bubble_1);
            } else if (i >= MAX_BUBBLES / 4 && i < MAX_BUBBLES / 4 * 2) {
                bubble.setImageResource(R.mipmap.ic_like_bubble_2);
            } else if (i >= MAX_BUBBLES / 4 * 2 && i < MAX_BUBBLES / 4 * 3) {
                bubble.setImageResource(R.mipmap.ic_like_bubble_3);
            } else if (i > MAX_BUBBLES / 4 * 3 && i < MAX_BUBBLES) {
                bubble.setImageResource(R.mipmap.ic_like_bubble_4);
            }
            bubbles.add(bubble);
        }
    }

    /**
     * 设置泡泡在底部的位置<br/>
     * 默认在中间，偏移值为0
     *
     * @param gravity     泡泡预置bottom的gravity，传入left/right/center_horizontal即可
     * @param translation 左右位置偏移值。右为正，左为负。
     */
    public void setBubbleGravity(int gravity, int translation) {
        bubbleGravity = Gravity.BOTTOM | gravity;
        bubbleTranslation = translation;
        initBubbles();
    }

    /**
     * 设置泡泡飞行范围<br/>
     * 默认为整个控件范围
     *
     * @param left  左侧边距
     * @param right 右侧边距
     */
    public void setBubbleFlyPadding(int left, int right) {
        bubbleLeftPadding = left;
        bubbleRightPadding = right;
    }

    /**
     * 显示单个泡泡动画
     */
    public void showBubble() {
        long currentTime = System.currentTimeMillis();
        // 当还有剩余的泡泡可用，则进行泡泡的使用
        if (bubbles.size() > 0 && currentTime - lastAnimationTime > ANIMATION_INTERVAL) {
            lastAnimationTime = currentTime;
            ImageView bubble = bubbles.get(mRandom.nextInt(bubbles.size()));
            bubbles.remove(bubble);
            addView(bubble);
            setBubbleAnimation(bubble);
        }
    }

    /**
     * 显示固定个数的泡泡
     *
     * @param count 泡泡个数
     */
    public void showBubble(int count) {
        if (count <= 0) {
            return;
        }
        this.count = count;
        showCertainBubbles();
    }


    /**
     * 显示固定个数泡泡的循环执行体
     */
    private void showCertainBubbles() {
        if (count > 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    showBubble();
                }
            });
            count--;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCertainBubbles();
                }
            }, BUBBLE_INTERVAL);
        }
    }

    /**
     * 按时长显示动画
     *
     * @param duration 需要显示的动画尺长
     */
    public void showBubbleForDuration(long duration) {
        // 获取当前系统时间
        long currentTimeMillis = System.currentTimeMillis();
        // 如果之前并未做过时长动画或之前的时长动画已结束，则重置动画时间
        if (endTime < currentTimeMillis) {
            endTime = currentTimeMillis;
        }
        // 结束时间确认
        endTime += duration;
        // 如果时长超过了最大限制，则使用最大时长值
        if (endTime - currentTimeMillis > DURATION_MAX) {
            endTime = currentTimeMillis + DURATION_MAX;
        }
        // 如果当前未在动画中，则开启动画
        // 如果当前在动画中，则不重复开启动画
        if (!animating) {
            animating = true;
            continueBubbleAnimation();
        }
    }

    /**
     * 时长动画的循环调用体
     */
    private void continueBubbleAnimation() {
        if (endTime > System.currentTimeMillis()) {
            // 每个泡泡之间有100ms的延时
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bubbles.size() > 0) {
                        // 显示泡泡的同时，会将泡泡移出集合，置入视图
                        ImageView bubble = bubbles.get(mRandom.nextInt(bubbles.size()));
                        bubbles.remove(bubble);
                        addView(bubble);
                        setBubbleAnimation(bubble);
                        // 时间到，动画结束
                        // 时间未到，继续触发新一次动画
                        if (endTime - System.currentTimeMillis() < ANIMATION_INTERVAL) {
                            animating = false;
                        } else {
                            continueBubbleAnimation();
                        }
                    }
                }
            }, BUBBLE_INTERVAL);
        }
    }

    int bubbleRange = ScreenUtil.dip2px(70, getContext());

    /**
     * 为泡泡设置随机的动画
     *
     * @param bubble 泡泡
     */
    @SuppressLint("RtlHardcoded")
    private void setBubbleAnimation(final ImageView bubble) {

        // 计算横向偏移位置
        float[] translations = new float[5];
        // 活动范围
        int range = getWidth() - ScreenUtil.dip2px(40f, getContext());
        // 初始位置应带有偏移值
        translations[0] = 0.0f + bubbleTranslation;
        // 根据初始位置计算随机的目标位置
        if (bubbleGravity == (Gravity.BOTTOM | Gravity.LEFT)
                || bubbleGravity == (Gravity.BOTTOM | Gravity.START)) {
            translations[1] = mRandom.nextFloat();
        } else if (bubbleGravity == (Gravity.BOTTOM | Gravity.RIGHT)
                || bubbleGravity == (Gravity.BOTTOM | Gravity.END)) {
            translations[1] = -mRandom.nextFloat();
        } else {
            translations[1] = (mRandom.nextFloat() - 0.5f);
        }
        // 每次将飞行的横向范围与上一个点的位置有关
        translations[1] = (translations[1] * range) / 1.8f;
        translations[2] = translations[1] + (mRandom.nextFloat() - 0.5f) * bubbleRange;
        translations[3] = translations[2] + (mRandom.nextFloat() - 0.5f) * bubbleRange;
        translations[4] = translations[3] + (mRandom.nextFloat() - 0.5f) * bubbleRange;
        // 限制活动范围，此范围包括左右边距
        // 计算泡泡的倾斜
        float rotation = 0.0f;
        if (bubbleGravity == (Gravity.BOTTOM | Gravity.LEFT)
                || bubbleGravity == (Gravity.BOTTOM | Gravity.START)) {
            for (int i = 0; i < translations.length; i++) {
                if (i != 0) {
                    if (translations[i] > range - bubbleRightPadding) {
                        translations[i] = range - bubbleRightPadding;
                    }
                    if (translations[i] < bubbleLeftPadding) {
                        translations[i] = bubbleLeftPadding;
                    }
                }
            }
            rotation = 45f * mRandom.nextFloat();
        } else if (bubbleGravity == (Gravity.BOTTOM | Gravity.RIGHT)
                || bubbleGravity == (Gravity.BOTTOM | Gravity.END)) {
            for (int i = 0; i < translations.length; i++) {
                if (i != 0) {
                    if (translations[i] < -range + bubbleLeftPadding) {
                        translations[i] = -range + bubbleLeftPadding;
                    }
                    if (translations[i] > -bubbleRightPadding) {
                        translations[i] = -bubbleRightPadding;
                    }
                }
            }
            rotation = -45f * mRandom.nextFloat();
        } else {
            for (int i = 0; i < translations.length; i++) {
                if (i != 0) {
                    if (translations[i] < -range / 2 + bubbleLeftPadding) {
                        translations[i] = -range / 2 + bubbleLeftPadding;
                    }
                    if (translations[i] > range / 2 - bubbleRightPadding) {
                        translations[i] = range / 2 - bubbleRightPadding;
                    }
                }
            }
        }
        // 设置动画
        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(bubble,
                PropertyValuesHolder.ofFloat("translationX", translations[0], translations[1],
                        translations[2], translations[3], translations[4]),
                PropertyValuesHolder.ofFloat("translationY",
                        -getHeight() + ScreenUtil.dip2px(15, getContext())),
                PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.0f, 1.0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.0f, 1.0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.8f, 0.5f, 0.0f),
                PropertyValuesHolder.ofFloat("rotation", rotation / 4, rotation * 2 / 4, rotation * 3 / 4, rotation)
        ).setDuration(ANIMATION_DURATION);
        animator.setInterpolator(interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetBubble(bubble);
            }
        });
        animator.start();
    }

    /**
     * 重置泡泡参数
     *
     * @param bubble 泡泡
     */
    private void resetBubble(ImageView bubble) {
        // 回置泡泡参数
        bubble.setTranslationX(0.0f + bubbleTranslation);
        bubble.setTranslationY(0.0f);
        bubble.setScaleX(1.0f);
        bubble.setScaleY(1.0f);
        bubble.setAlpha(1.0f);
        // 将泡泡置回数组中待回收使用
        bubbles.addLast(bubble);
        // 将泡泡移出当前视图
        removeView(bubble);
    }

    /**
     * 泡泡加速器，截取了一段开方的函数，先快后慢
     */
    private class BubbleInterpolator implements Interpolator {

        private double m = 1.0d / (Math.sqrt(2.0d) - 1.0d);

        @Override
        public float getInterpolation(float input) {
            return (float) (m * (Math.sqrt(input + 1.0d) - 1.0d));
        }
    }
}