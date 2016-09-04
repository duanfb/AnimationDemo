package com.neo.dan.mesh;

import android.graphics.Path;
import android.graphics.PathMeasure;


/**
 * 吸入动画网格
 *
 * @author Hitoha
 * @version 1.00 2015/08/11 新建
 */
public class InhaleMesh extends Mesh {

	/**
	 * 吸入方向
	 *
	 * @author Hitoha
	 * @version 1.00 2015/08/11 新建
	 *
	 */
	public enum InhaleDir {
		UP, DOWN, LEFT, RIGHT,
	}

	/** 吸入动画第一条路径 */
	private Path firstPath = new Path();

	/** 吸入动画第二条路径 */
	private Path secondPath = new Path();

	/** 吸入动画第一条路径测量 */
	private PathMeasure firstPathMeasure = new PathMeasure();

	/** 吸入动画第二条路径测量 */
	private PathMeasure secondPathMeasure = new PathMeasure();

	/** 吸入芳香，默认是向下吸入 */
	private InhaleDir inhaleDir = InhaleDir.DOWN;

	/**
	 * 构造方法
	 *
	 * @param width
	 *            网格宽度均分数量
	 * @param height
	 *            网格高度均分数量
	 */
	public InhaleMesh(int width, int height) {
		super(width, height);
	}

	/**
	 * 设置吸入方向
	 *
	 * @param inhaleDir
	 *            吸入方向
	 */
	public void setInhaleDir(InhaleDir inhaleDir) {
		this.inhaleDir = inhaleDir;
	}

	/**
	 * 获取吸入方向
	 *
	 * @return 吸入方向
	 */
	public InhaleDir getInhaleDir() {
		return inhaleDir;
	}

	/**
	 * 创建到吸入点的路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	@Override
	public void buildPaths(float endX, float endY) {

		// 当没有设置位图时
		if (bitmapWidth <= 0 || bitmapHeight <= 0) {

			// 抛出异常
			throw new IllegalArgumentException(
					"Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
		}

		// 查看吸入方向
		switch (inhaleDir) {

			// 向上吸入
			case UP:
				// 创建向上吸入路径
				buildPathsUp(endX, endY);
				break;

			// 向下吸入
			case DOWN:
				// 创建向下吸入路径
				buildPathsDown(endX, endY);
				break;

			// 向右吸入
			case RIGHT:
				// 创建向右吸入路径
				buildPathsRight(endX, endY);
				break;

			// 向左吸入
			case LEFT:
				// 创建向左吸入路径
				buildPathsLeft(endX, endY);
				break;
		}
	}

	/**
	 * 创建网格
	 *
	 * @param index
	 *            网格索引
	 */
	@Override
	public void buildMeshes(int index) {

		// 当没有设置位图时
		if (bitmapWidth <= 0 || bitmapHeight <= 0) {

			// 抛出异常
			throw new IllegalArgumentException(
					"Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
		}

		// 查看吸入方向
		switch (inhaleDir) {

			// 向上/下吸入
			case UP:
			case DOWN:
				// 创建竖直方向吸入的网格
				buildMeshByPathOnVertical(index);
				break;

			// 向左/右吸入
			case RIGHT:
			case LEFT:
				// 创建水平方向吸入的网格
				buildMeshByPathOnHorizontal(index);
				break;
		}
	}

	/**
	 * 获取吸入动画路径
	 *
	 * @return 吸入动画路径
	 */
	public Path[] getPaths() {
		return new Path[] { firstPath, secondPath };
	}

	/**
	 * 创建向下吸入路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	private void buildPathsDown(float endX, float endY) {

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 图片宽高设定
		float w = bitmapWidth;
		float h = bitmapHeight;

		// 路径复位
		firstPath.reset();
		secondPath.reset();

		// 第一条路径从图片左上角开始画
		firstPath.moveTo(0, 0);
		// 第二条路径从图片右上角开始画
		secondPath.moveTo(w, 0);

		// 紧贴图片左边，向下画一条和图片高度相等的直线
		firstPath.lineTo(0, h);
		// 紧贴图片右边，向下画一条和图片高度相等的直线
		secondPath.lineTo(w, h);

		// 从图片左下角画一条到吸入点的曲线
		firstPath.quadTo(0, (endY + h) / 2, endX, endY);
		// 从图片右下角画一条到吸入点的曲线
		secondPath.quadTo(w, (endY + h) / 2, endX, endY);
	}

	/**
	 * 创建向上吸入路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	private void buildPathsUp(float endX, float endY) {

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 图片宽高设定
		float w = bitmapWidth;
		float h = bitmapHeight;

		// 路径复位
		firstPath.reset();
		secondPath.reset();

		// 第一条路径从图片左下角开始画
		firstPath.moveTo(0, h);
		// 第二条路径从图片右下角开始画
		secondPath.moveTo(w, h);

		// 紧贴图片左边，向上画一条和图片高度相等的直线
		firstPath.lineTo(0, 0);
		// 紧贴图片右边，向上画一条和图片高度相等的直线
		secondPath.lineTo(w, 0);

		// 从图片左上角画一条到吸入点的曲线
		firstPath.quadTo(0, (endY - h) / 2, endX, endY);
		// 从图片右上角画一条到吸入点的曲线
		secondPath.quadTo(w, (endY - h) / 2, endX, endY);
	}

	/**
	 * 创建向右吸入路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	private void buildPathsRight(float endX, float endY) {

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 图片宽高设定
		float w = bitmapWidth;
		float h = bitmapHeight;

		// 路径复位
		firstPath.reset();
		secondPath.reset();

		// 第一条路径从图片左上角开始画
		firstPath.moveTo(0, 0);
		// 第一条路径从图片左下角开始画
		secondPath.moveTo(0, h);

		// 紧贴图片上边，向右画一条和图片宽度相等的直线
		firstPath.lineTo(w, 0);
		// 紧贴图片下边，向右画一条和图片宽度相等的直线
		secondPath.lineTo(w, h);

		// 从图片右上角画一条到吸入点的曲线
		firstPath.quadTo((endX + w) / 2, 0, endX, endY);
		// 从图片右下角画一条到吸入点的曲线
		secondPath.quadTo((endX + w) / 2, h, endX, endY);
	}

	/**
	 * 创建向左吸入路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	private void buildPathsLeft(float endX, float endY) {

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 图片宽高设定
		float w = bitmapWidth;
		float h = bitmapHeight;

		// 路径复位
		firstPath.reset();
		secondPath.reset();

		// 第一条路径从图片右上角开始画
		firstPath.moveTo(w, 0);
		// 第一条路径从图片右下角开始画
		secondPath.moveTo(w, h);

		// 紧贴图片上边，向左画一条和图片宽度相等的直线
		firstPath.lineTo(0, 0);
		// 紧贴图片下边，向左画一条和图片宽度相等的直线
		secondPath.lineTo(0, h);

		// 从图片左上角画一条到吸入点的曲线
		firstPath.quadTo((endX - w) / 2, 0, endX, endY);
		// 从图片左下角画一条到吸入点的曲线
		secondPath.quadTo((endX - w) / 2, h, endX, endY);
	}

	/**
	 * 创建竖直方向吸入的网格
	 *
	 * @param timeIndex
	 *            网格索引
	 */
	private void buildMeshByPathOnVertical(int timeIndex) {

		// 此时绘制的图片可能已经变为一个梯形

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 初始索引
		int index = 0;

		// 左右点初始坐标
		float[] pos1 = { 0.0f, 0.0f };
		float[] pos2 = { 0.0f, 0.0f };

		// 获取两条路径的长度
		float firstLen = firstPathMeasure.getLength();
		float secondLen = secondPathMeasure.getLength();

		// 路径均分
		float len1 = firstLen / HEIGHT;
		float len2 = secondLen / HEIGHT;

		// 两条路径的第一个网格
		float firstPointDist = timeIndex * len1;
		float secondPointDist = timeIndex * len2;

		// 网格总高度
		float height = bitmapHeight;

		// 图片左上角的坐标，存入pos1中
		firstPathMeasure.getPosTan(firstPointDist, pos1, null);
		// 图片左下角的坐标，存入pos2中
		firstPathMeasure.getPosTan(firstPointDist + height, pos2, null);

		// (x1, y1)图片左上角坐标
		// (x2, y2)图片左下角坐标
		float x1 = pos1[0];
		float x2 = pos2[0];
		float y1 = pos1[1];
		float y2 = pos2[1];

		// 图片左上角到左下角的直线距离
		float FIRST_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));

		// 均分图片左上角到左下角的直线
		float FIRST_H = FIRST_DIST / HEIGHT;

		// 图片右上角的坐标，存入pos1中
		secondPathMeasure.getPosTan(secondPointDist, pos1, null);
		// 图片右下角的坐标，存入pos1中
		secondPathMeasure.getPosTan(secondPointDist + height, pos2, null);

		// (x1, y1)图片右上角坐标
		// (x2, y2)图片右下角坐标
		x1 = pos1[0];
		x2 = pos2[0];
		y1 = pos1[1];
		y2 = pos2[1];

		// 图片右上角到右下角的直线距离
		float SECOND_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));
		// 均分图片右上角到右下角的直线
		float SECOND_H = SECOND_DIST / HEIGHT;

		// 向下吸入
		if (inhaleDir == InhaleDir.DOWN) {
			// 从上到下计算出网格顶点坐标
			// 双重循环求出每一个网格顶点的坐标
			for (int y = 0; y <= HEIGHT; y++) {
				// 左侧斜边网格坐标存入pos1中
				firstPathMeasure.getPosTan(y * FIRST_H + firstPointDist, pos1,
						null);
				// 右侧斜边网格坐标存入pos2中
				secondPathMeasure.getPosTan(y * SECOND_H + secondPointDist,
						pos2, null);

				// 两点之间水平距离
				float w = pos2[0] - pos1[0];

				// (fx1, fy1)左边斜边顶点坐标
				// (fx2, fy2)右边斜边顶点坐标
				float fx1 = pos1[0];
				float fx2 = pos2[0];
				float fy1 = pos1[1];
				float fy2 = pos2[1];

				// 两点之间竖直距离
				float dy = fy2 - fy1;
				// 两点之间水平距离
				float dx = fx2 - fx1;

				// 水平方向顶点坐标
				for (int x = 0; x <= WIDTH; ++x) {
					// 求出(fx1, fy1)与(fx2, fy2)连线上的所有网格顶点坐标，并储存在verts中

					// 每一个点的横坐标
					float fx = x * w / WIDTH;
					// 每一个点的纵坐标
					float fy = fx * dy / dx;

					// 将顶点坐标储存在verts中
					verts[index * 2 + 0] = fx + fx1;
					verts[index * 2 + 1] = fy + fy1;

					// 下一个顶点的索引
					index++;
				}
			}
		} else if (inhaleDir == InhaleDir.UP) {
			// 向上吸入
			// 从下到上计算出网格顶点坐标
			// 双重循环求出每一个网格顶点的坐标
			for (int y = HEIGHT; y >= 0; y--) {
				// 左侧斜边网格坐标存入pos1中
				firstPathMeasure.getPosTan(y * FIRST_H + firstPointDist, pos1,
						null);
				// 右侧斜边网格坐标存入pos2中
				secondPathMeasure.getPosTan(y * SECOND_H + secondPointDist,
						pos2, null);

				// 两点之间水平距离
				float w = pos2[0] - pos1[0];

				// (fx1, fy1)左边斜边顶点坐标
				// (fx2, fy2)右边斜边顶点坐标
				float fx1 = pos1[0];
				float fx2 = pos2[0];
				float fy1 = pos1[1];
				float fy2 = pos2[1];

				// 两点之间竖直距离
				float dy = fy2 - fy1;
				// 两点之间水平距离
				float dx = fx2 - fx1;

				// 水平方向顶点坐标
				for (int x = 0; x <= WIDTH; ++x) {
					// 求出(fx1, fy1)与(fx2, fy2)连线上的所有网格顶点坐标，并储存在verts中

					// 每一个点的横坐标
					float fx = x * w / WIDTH;
					// 每一个点的纵坐标
					float fy = fx * dy / dx;

					// 将顶点坐标储存在verts中
					verts[index * 2 + 0] = fx + fx1;
					verts[index * 2 + 1] = fy + fy1;

					// 下一个顶点的索引
					index++;
				}
			}
		}
	}

	/**
	 * 创建水平方向吸入的网格
	 *
	 * @param timeIndex
	 *            网格索引
	 */
	private void buildMeshByPathOnHorizontal(int timeIndex) {

		// 此时绘制的图片可能已经变为一个梯形

		// 路径测量设定
		firstPathMeasure.setPath(firstPath, false);
		secondPathMeasure.setPath(secondPath, false);

		// 初始索引
		int index = 0;

		// 左右点初始坐标
		float[] pos1 = { 0.0f, 0.0f };
		float[] pos2 = { 0.0f, 0.0f };

		// 获取两条路径的长度
		float firstLen = firstPathMeasure.getLength();
		float secondLen = secondPathMeasure.getLength();

		// 路径均分
		float len1 = firstLen / WIDTH;
		float len2 = secondLen / WIDTH;

		// 两条路径的第一个网格
		float firstPointDist = timeIndex * len1;
		float secondPointDist = timeIndex * len2;

		// 网格总宽度
		float width = bitmapWidth;

		// 图片左上角的坐标，存入pos1中
		firstPathMeasure.getPosTan(firstPointDist, pos1, null);
		// 图片右上角的坐标，存入pos2中
		firstPathMeasure.getPosTan(firstPointDist + width, pos2, null);

		// (x1, y1)图片左上角坐标
		// (x2, y2)图片右上角坐标
		float x1 = pos1[0];
		float x2 = pos2[0];
		float y1 = pos1[1];
		float y2 = pos2[1];

		// 图片左上角到右上角的直线距离
		float FIRST_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));
		// 均分图片左上角到右上角的直线
		float FIRST_X = FIRST_DIST / WIDTH;

		// 图片左下角的坐标，存入pos1中
		secondPathMeasure.getPosTan(secondPointDist, pos1, null);
		// 图片右下角的坐标，存入pos2中
		secondPathMeasure.getPosTan(secondPointDist + width, pos2, null);

		// (x1, y1)图片左下角坐标
		// (x2, y2)图片右下角坐标
		x1 = pos1[0];
		x2 = pos2[0];
		y1 = pos1[1];
		y2 = pos2[1];

		// 图片左下角到右下角的直线距离
		float SECOND_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));
		// 均分图片左下角到右下角的直线
		float SECOND_X = SECOND_DIST / WIDTH;

		// 向右吸入
		if (inhaleDir == InhaleDir.RIGHT) {
			// 从左到右计算出网格顶点坐标
			// 双重循环求出每一个网格顶点的坐标
			for (int x = 0; x <= WIDTH; x++) {
				// 上侧斜边网格坐标存入pos1中
				firstPathMeasure.getPosTan(x * FIRST_X + firstPointDist, pos1,
						null);
				// 下侧斜边网格坐标存入pos2中
				secondPathMeasure.getPosTan(x * SECOND_X + secondPointDist,
						pos2, null);

				// 两点之间竖直距离
				float h = pos2[1] - pos1[1];

				// (fx1, fy1)上边斜边顶点坐标
				// (fx2, fy2)下边斜边顶点坐标
				float fx1 = pos1[0];
				float fx2 = pos2[0];
				float fy1 = pos1[1];
				float fy2 = pos2[1];

				// 两点之间竖直距离
				float dy = fy2 - fy1;
				// 两点之间水平距离
				float dx = fx2 - fx1;

				// 竖直方向顶点坐标
				for (int y = 0; y <= HEIGHT; ++y) {
					// 求出(fx1, fy1)与(fx2, fy2)连线上的所有网格顶点坐标，并储存在verts中

					// 每一个点的纵坐标
					float fy = y * h / HEIGHT;
					// 每一个点的横坐标
					float fx = fy * dx / dy;

					// verts行优先，计算出该顶点在verts的为存储位置
					index = y * (WIDTH + 1) + x;

					// 将顶点坐标储存在verts中
					verts[index * 2 + 0] = fx + fx1;
					verts[index * 2 + 1] = fy + fy1;
				}
			}
		} else if (inhaleDir == InhaleDir.LEFT) {
			// 向左吸入
			// 从右到左计算出网格顶点坐标
			// 双重循环求出每一个网格顶点的坐标
			for (int x = WIDTH; x >= 0; x--) {
				// 上侧斜边网格坐标存入pos1中
				firstPathMeasure.getPosTan(x * FIRST_X + firstPointDist, pos1,
						null);
				// 下侧斜边网格坐标存入pos2中
				secondPathMeasure.getPosTan(x * SECOND_X + secondPointDist,
						pos2, null);

				// 两点之间竖直距离
				float h = pos2[1] - pos1[1];

				// (fx1, fy1)上边斜边顶点坐标
				// (fx2, fy2)下边斜边顶点坐标
				float fx1 = pos1[0];
				float fx2 = pos2[0];
				float fy1 = pos1[1];
				float fy2 = pos2[1];

				// 两点之间竖直距离
				float dy = fy2 - fy1;
				// 两点之间水平距离
				float dx = fx2 - fx1;

				// 竖直方向顶点坐标
				for (int y = 0; y <= HEIGHT; ++y) {
					// 求出(fx1, fy1)与(fx2, fy2)连线上的所有网格顶点坐标，并储存在verts中

					// 每一个点的纵坐标
					float fy = y * h / HEIGHT;
					// 每一个点的横坐标
					float fx = fy * dx / dy;

					// verts行优先，计算出该顶点在verts的为存储位置
					index = y * (WIDTH + 1) + WIDTH - x;

					// 将顶点坐标储存在verts中
					verts[index * 2 + 0] = fx + fx1;
					verts[index * 2 + 1] = fy + fy1;
				}
			}
		}
	}
}
