package com.neo.dan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import com.neo.dan.R;
import com.neo.dan.anima.PathAnimation;
import com.neo.dan.mesh.InhaleMesh;

/**
 * 实现路径动画的View
 *
 * @author Hitoha
 * @version 1.00 2015/08/11 新建
 */
public class SampleView extends View {

	/** 网格宽度均分数量 */
	private static final int WIDTH = 40;
	/** 网格高度均分数量 */
	private static final int HEIGHT = 40;

	/** 实现吸入动画的位图 */
	private Bitmap bitmap;
	/** 变换矩阵 */
	private final Matrix matrix = new Matrix();
	/** 反向变换矩阵 */
	private final Matrix inverse = new Matrix();

	/** 是否为调试模式 */
	private boolean isDebug = true;

	/** 画笔 */
	private Paint paint = new Paint();
	/** 吸入点 */
	private int[] inhalePoint = new int[] { 0, 0 };
	/** 吸入动画网格 */
	private InhaleMesh inhaleMesh = null;

	public void setInhalePoint(int[] inhalePoint) {
		this.inhalePoint = inhalePoint;
		// 创建路径
		buildPaths(inhalePoint[0], inhalePoint[1]);

		// 重绘该view
		invalidate();
	}

	/**
	 * 构造函数
	 *
	 * @param context
	 *            Context
	 */
	public SampleView(Context context) {
		this(context, null);
	}

	/**
	 * 构造函数
	 *
	 * @param context
	 *            Context
	 * @param attrs
	 *            属性
	 */
	public SampleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造函数
	 *
	 * @param context
	 *            Context
	 * @param attrs
	 *            属性
	 * @param defStyleAttr
	 *            默认风格
	 */
	public SampleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// 初始化
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		// View可以获得焦点
		setFocusable(true);

		// 加载位图
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_zly);

		// 新建吸入动画网格
		inhaleMesh = new InhaleMesh(WIDTH, HEIGHT);
		// 设定设置位图尺寸
		inhaleMesh.setBitmapSize(bitmap.getWidth(), bitmap.getHeight());
		// 设置吸入方向为向下吸入
		inhaleMesh.setInhaleDir(InhaleMesh.InhaleDir.UP);
	}

	/**
	 * 设定是否为调试模式
	 *
	 * @param isDebug
	 *            是否为调试模式
	 */
	public void setIsDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// 获取位图宽高
		float bmpW = bitmap.getWidth();
		float bmpH = bitmap.getHeight();

		// 图片移动到合适的位置
		matrix.setTranslate(10, 10);

		// 设定matrix的逆矩阵inverse
		matrix.invert(inverse);

		// 设定画笔颜色为红色
		paint.setColor(Color.RED);
		// 设定画笔线宽
		paint.setStrokeWidth(2);
		// 设定抗锯齿
		paint.setAntiAlias(true);

		// 构建路径
		buildPaths(bmpW / 2, h - 20);
		// 构建网格
		buildMesh(bmpW, bmpH);
	}

	/**
	 * 动画开始
	 *
	 * @param reverse
	 *            是否可以反向动画
	 * @return 动画是否在执行
	 */
	public boolean startAnimation(boolean reverse) {

		// 获取动画
		Animation anim = this.getAnimation();

		// 当没有动画或动画已经结束时
		if (null != anim && !anim.hasEnded()) {
			// 返回false
			return false;
		}

		// 新建动画
		PathAnimation animation = new PathAnimation(0, HEIGHT + 1, reverse,
				new PathAnimation.AnimationUpdateListener() {
					@Override
					public void onAnimUpdate(int index) {
						// 创建网格
						inhaleMesh.buildMeshes(index);
						// // 重绘该view
						invalidate();
					}
				});

		if (null != animation) {
			// 持续时间
			animation.setDuration(1000);
			// 执行动画
			this.startAnimation(animation);
		}

		// 返回true
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// 画布颜色填充
		canvas.drawColor(0xFFCCCCCC);

		// 设置矩阵
		canvas.concat(matrix);

		// 绘制网格图片
		canvas.drawBitmapMesh(bitmap, inhaleMesh.getWidth(),
				inhaleMesh.getHeight(), inhaleMesh.getVertices(), 0, null, 0,
				paint);

		// 绘制吸入点
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(inhalePoint[0], inhalePoint[1], 5, paint);

		// 调试模式下
		if (isDebug) {

			// 绘制网格顶点
			canvas.drawPoints(inhaleMesh.getVertices(), paint);

			// 绘制路径
			paint.setColor(Color.BLUE);
			paint.setStyle(Style.STROKE);
			Path[] paths = inhaleMesh.getPaths();
			for (Path path : paths) {
				canvas.drawPath(path, paint);
			}
		}
	}

	/**
	 * 创建网格
	 *
	 * @param w
	 *            位图宽度
	 * @param h
	 *            位图高度
	 */
	private void buildMesh(float w, float h) {
		// 创建网格
		inhaleMesh.buildMeshes(w, h);
	}

	/**
	 * 创建路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	private void buildPaths(float endX, float endY) {
		// 吸入点坐标重设定
		inhalePoint[0] = (int) endX;
		inhalePoint[1] = (int) endY;

		// 创建路径
		inhaleMesh.buildPaths(endX, endY);
	}

}
