package com.neo.dan.anima;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

/**
 * 路径动画
 *
 * @author Hitoha
 * @version 1.00 2015/08/11 新建
 */
public class PathAnimation extends Animation {

	/**
	 * 动画状态更新监听
	 *
	 * @author Hitoha
	 * @version 1.00 2015/08/11 新建
	 */
	public interface AnimationUpdateListener {

		/**
		 * 动画状态更新
		 *
		 * @param index
		 *            动画的帧
		 */
		public void onAnimUpdate(int index);
	}

	/** 起始帧 */
	private int fromIndex = 0;

	/** 终止帧 */
	private int endIndex = 0;

	/** 是否反向执行动画 */
	private boolean reverse = false;

	/** 动画状态更新监听 */
	private AnimationUpdateListener listener = null;

	/**
	 * 构造函数
	 *
	 * @param fromIndex
	 *            起始帧
	 * @param endIndex
	 *            终止帧
	 * @param reverse
	 *            是否反向执行动画
	 * @param listener
	 *            动画状态更新监听
	 */
	public PathAnimation(int fromIndex, int endIndex, boolean reverse,
						 AnimationUpdateListener listener) {
		this.fromIndex = fromIndex;
		this.endIndex = endIndex;
		this.reverse = reverse;
		this.listener = listener;
	}

	/**
	 * 获取当前动画变换
	 *
	 * @param currentTime
	 *            当前时间
	 * @param outTransformation
	 *            装载获取的动画变换
	 * @return 动画继续执行，返回true；动画停止，返回false
	 */
	@Override
	public boolean getTransformation(long currentTime,
									 Transformation outTransformation) {

		// 获取当前动画
		boolean more = super.getTransformation(currentTime, outTransformation);

		// 返回动画是否继续执行
		return more;
	}

	/**
	 * 自定义动画效果实现
	 *
	 * @param interpolatedTime
	 *            动画渲染时间
	 * @param t
	 *            装载当前动画变换
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		// 当前帧
		int curIndex = 0;

		// 获取动画渲染
		Interpolator interpolator = this.getInterpolator();

		// 该动画有渲染时
		if (null != interpolator) {
			// 获取当前时间片
			float value = interpolator.getInterpolation(interpolatedTime);
			interpolatedTime = value;
		}

		// 该动画支持反向执行
		if (reverse) {
			// 反向时间片
			interpolatedTime = 1.0f - interpolatedTime;
		}

		// 计算当前帧
		curIndex = (int) (fromIndex + (endIndex - fromIndex) * interpolatedTime);

		// 有监听
		if (null != listener) {
			// 动画刷新
			listener.onAnimUpdate(curIndex);
		}
	}
}
