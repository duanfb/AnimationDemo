package com.neo.dan.mesh;

/**
 * 网格抽象类
 *
 * @author Hitoha
 * @version 1.00 2015/08/11 新建
 */
public abstract class Mesh {

	/** 网格宽度均分数量 */
	protected int WIDTH = 40;

	/** 网格高度均分数量 */
	protected int HEIGHT = 40;

	/** 位图宽度 */
	protected int bitmapWidth = -1;

	/** 位图高度 */
	protected int bitmapHeight = -1;

	/** 网格每一格顶点坐标数组，行优先 */
	protected final float[] verts;

	/**
	 * 构造方法
	 *
	 * @param width
	 *            网格宽度均分数量
	 * @param height
	 *            网格高度均分数量
	 */
	public Mesh(int width, int height) {
		// 设置网格宽度均分数量
		WIDTH = width;
		// 设置网格高度均分数量
		HEIGHT = height;

		// 初始化网格每一格顶点坐标数组，数量为(WIDTH + 1) * (HEIGHT + 1) * 2个
		verts = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
	}

	/**
	 * 获取网格每一格顶点坐标数组
	 *
	 * @return 网格每一格顶点坐标数组
	 */
	public float[] getVertices() {
		return verts;
	}

	/**
	 * 获取网格宽度均分数量
	 *
	 * @return 网格宽度均分数量
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * 网格高度均分数量
	 *
	 * @return 网格高度均分数量
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * 设置某一网格的顶点
	 *
	 * @param array
	 *            网格每一格顶点坐标数组
	 * @param index
	 *            要设置的顶点的索引，从0开始
	 * @param x
	 *            顶点x值
	 * @param y
	 *            顶点y值
	 */
	public static void setXY(float[] array, int index, float x, float y) {
		// array以(x, y)的形式储存顶点坐标，偶数下标储存顶点x值，奇数下标储存顶点y值
		// 设置顶点x值
		array[index * 2 + 0] = x;
		// 设置顶点y值
		array[index * 2 + 1] = y;
	}

	/**
	 * 设置位图尺寸
	 *
	 * @param w
	 *            位图宽度
	 * @param h
	 *            位图高度
	 */
	public void setBitmapSize(int w, int h) {
		// 设置位图宽度
		bitmapWidth = w;
		// 设置位图高度
		bitmapHeight = h;
	}

	/**
	 * 创建到吸入点的路径
	 *
	 * @param endX
	 *            吸入点x坐标
	 * @param endY
	 *            吸入点y坐标
	 */
	public abstract void buildPaths(float endX, float endY);

	/**
	 * 创建网格
	 *
	 * @param index
	 *            网格索引
	 */
	public abstract void buildMeshes(int index);

	/**
	 * 创建网格
	 *
	 * @param w
	 *            位图宽度
	 * @param h
	 *            位图高度
	 */
	public void buildMeshes(float w, float h) {

		// 初始索引
		int index = 0;

		// 竖直方向的的顶点为HEIGHT + 1个
		for (int y = 0; y <= HEIGHT; y++) {

			// 竖直方向网格的y值
			float fy = y * h / HEIGHT;

			// 水平方向的的顶点为WIDTH + 1个
			for (int x = 0; x <= WIDTH; x++) {

				// 水平方向网格的x值
				float fx = x * w / WIDTH;

				// 设置该网格顶点的坐标值，存储到verts中
				setXY(verts, index, fx, fy);

				// 索引+1
				index++;
			}
		}
	}
}
