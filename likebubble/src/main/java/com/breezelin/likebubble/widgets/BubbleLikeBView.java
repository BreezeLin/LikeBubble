package com.breezelin.likebubble.widgets;

/**
 * Created by Breeze Lin
 * 2016/7/12 15:19
 * breezesummerlin@163.com
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.breezelin.likebubble.R;

import java.util.LinkedList;
import java.util.Random;

/**
 * 以view为基础的点赞冒泡控件
 */
public class BubbleLikeBView extends View implements Runnable {

	/**
	 * 泡泡总量
	 */
	private static final int DEF_BUBBLE_AMOUNT = 200;
	/**
	 * 泡泡飞行速度（像素/帧）
	 */
	private static final int DEF_MOVE_BREADTH = 5;
	/**
	 * 每帧刷新时间
	 */
	private static final int TICK_PER_FLASH = 40;

	/**
	 * 泡泡每帧飞行幅度
	 */
	private int moveBreadth;
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
	/**
	 * 动画是否正在进行
	 */
	private boolean animating;

	public BubbleLikeBView(Context context) {
		super(context);
		init(null);
	}

	public BubbleLikeBView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public BubbleLikeBView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	/**
	 * 初始化
	 */
	private void init(AttributeSet attrs) {
		random = new Random();
		restBubbles = new LinkedList<>();
		flyingBubbles = new LinkedList<>();
		removeBubbles = new LinkedList<>();
		paint = new Paint();
		animating = false;
		int bubbleAmount = DEF_BUBBLE_AMOUNT;

		if (attrs != null) {
			// 属性值解析
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
					R.styleable.BubbleLikeBView);
			// 泡泡总数
			bubbleAmount = typedArray.getInteger(R.styleable.BubbleLikeBView_bubbleAmount,
					DEF_BUBBLE_AMOUNT);
			// 每帧移动幅度
			moveBreadth = typedArray.getDimensionPixelSize(R.styleable.BubbleLikeBView_moveBreadth,
					DEF_MOVE_BREADTH);
			typedArray.recycle();
		}
		// TODO: 2016/7/13 泡泡比例配置
		Bitmap bubbleBitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ic_like_bubble_1);
		for (int i = 0; i < bubbleAmount; i++) {
			restBubbles.add(new Bubble(bubbleBitmap));
		}
	}

	/**
	 * 增加飞行中的泡泡
	 */
	public void addBubble() {
		// 如果还有剩余的泡泡，则从剩余泡泡中获取一个新的泡泡，并加入飞行的泡泡中
		if (restBubbles.size() > 0) {
			// 随机取出泡泡
			Bubble bubble = restBubbles.get(random.nextInt(restBubbles.size()));
			// 设置泡泡初始属性
			bubble.setY(getHeight());
			bubble.setX(getWidth() / 2);
			bubble.getBubbleMatrix().setTranslate(bubble.getX(), bubble.getY());
			bubble.setAlpha(255);
			// 泡泡转移
			restBubbles.remove(bubble);
			flyingBubbles.add(bubble);
			// 若动画没有开始，则让动画开始
			if (!animating) {
				loopAnim();
			}
		}
	}

	/**
	 * 循环动画
	 */
	private void loopAnim() {
		// 如果已经没有在飞行的泡泡，则不再进行计算和剩下的所有的操作
		if (flyingBubbles.size() <= 0) {
			// 标识动画已结束
			animating = false;
			return;
		}
		animating = true;
		// 遍历所有正在飞行的泡泡
		for (Bubble bubble : flyingBubbles) {
			// 如果泡泡已经飞到顶部或边缘，则将其移除
			if (bubble.getY() <= 0 || bubble.getX() == 0 || bubble.getX() == getWidth()) {
				removeBubbles.add(bubble);
				// 跳出循环，不再对此泡泡进行计算
				continue;
			}
			// 尚未到达边缘的泡泡，为其进行位置的计算
			generateBubble(bubble);
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
		// 泡泡重整完成，触发绘制
		invalidate();
		// 准备下一轮的计算
		postDelayed(this, TICK_PER_FLASH);
	}

	/**
	 * 为泡泡计算下一个点的位置等属性
	 *
	 * @param bubble 需要计算的泡泡
	 */
	private void generateBubble(Bubble bubble) {
		// 高度上升
		bubble.setY(bubble.getY() - moveBreadth);
		// TODO: 2016/7/18 X轴的位置应当根据贝塞尔曲线的公式生成
		// 左右方向随机
        for (int i = 0; i < 2; i++) {
            if (random.nextBoolean()) {
                bubble.setX(bubble.getX() + 2 * moveBreadth);
            } else {
                bubble.setX(bubble.getX() - 2 * moveBreadth);
            }
        }
		Matrix bubbleMatrix = bubble.getBubbleMatrix();
		Bitmap bubbleBitmap = bubble.getBubbleBitmap();
		// 位置
		bubbleMatrix.postTranslate(bubble.getX() / bubbleBitmap.getWidth(),
				bubble.getY() / bubbleBitmap.getHeight());
		// 倾斜
		bubbleMatrix.postRotate(bubble.getX());
		// 缩放
		float scale = 2 - Math.abs((float) bubble.getY() / (float) getHeight());
		bubbleMatrix.postScale(scale, scale);
		// 透明度，根据高度而计算
		bubble.setAlpha((int) (255 * Math.abs((float) bubble.getY() / (float) getHeight())));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 如果没有要飞行的泡泡，则直接返回
		if (flyingBubbles.size() == 0) {
			return;
		}
		// 遍历飞行中的泡泡，进行绘制
		for (Bubble bubble : flyingBubbles) {
			// 透明度
			paint.setAlpha(bubble.getAlpha());
			// 取出矩阵
			Matrix bubbleMatrix = bubble.getBubbleMatrix();
			// 进行泡泡的绘制
			Bitmap bubbleBitmap = bubble.getBubbleBitmap();
			// TODO: 2016/7/14 当前矩阵对于泡泡的绘制并不成功，可以使用下边被注释的一句进行暂时的显示。
//            canvas.drawBitmap(bubbleBitmap, bubbleMatrix, paint);
			canvas.drawBitmap(bubbleBitmap, bubble.getX(), bubble.getY(), paint);
			// 重置画笔
			paint.reset();
		}
	}

	@Override
	public void run() {
		// 动画的执行
		loopAnim();
	}
}
