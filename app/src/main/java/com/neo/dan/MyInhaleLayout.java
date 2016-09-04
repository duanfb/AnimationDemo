package com.neo.dan;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.neo.dan.anima.PathAnimation;
import com.neo.dan.mesh.InhaleMesh;

/**
 * @author : neo.duan
 * @date : 	 2016/9/3
 * @desc : 请描述这个文件
 */
public class MyInhaleLayout extends RelativeLayout {
    /** 网格宽度均分数量 */
    private static final int WIDTH = 40;
    /** 网格高度均分数量 */
    private static final int HEIGHT = 40;

    /** 吸入动画网格 */
    private InhaleMesh inhaleMesh = null;

    /** 变换矩阵 */
    private final Matrix matrix = new Matrix();
    /** 反向变换矩阵 */
    private final Matrix inverse = new Matrix();

    private Bitmap bitmap;

    public MyInhaleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //绘制完成，初始化各项
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                init();
            }
        });
    }

    public MyInhaleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyInhaleLayout(Context context) {
        this(context,null);
    }

    private void init() {
        // 新建吸入动画网格
        inhaleMesh = new InhaleMesh(WIDTH, HEIGHT);
        // 设定设置位图尺寸
        inhaleMesh.setBitmapSize(getMeasuredWidth(), getMeasuredHeight());
        // 设置吸入方向为向下吸入
        inhaleMesh.setInhaleDir(InhaleMesh.InhaleDir.UP);

        // 图片移动到合适的位置
        matrix.setTranslate(10, 10);

        // 设定matrix的逆矩阵inverse
        matrix.invert(inverse);

        // 构建路径
        buildPaths(getMeasuredWidth() / 2, getMeasuredHeight() - 20);
        // 构建网格
        buildMesh(getMeasuredWidth(), getMeasuredHeight());

        // 加载位图
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_zly);
    }

    /**
     * 创建路径
     *
     * @param endX
     *            吸入点x坐标
     * @param endY
     *            吸入点y坐标
     */
    public void buildPaths(float endX, float endY) {
        if (inhaleMesh != null) {
            // 创建路径
            inhaleMesh.buildPaths(endX, endY);
        }
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
        super.onDraw(canvas);
        // 画布颜色填充
        canvas.drawColor(0xFFCCCCCC);

        // 设置矩阵
        canvas.concat(matrix);

        // 绘制网格图片
        canvas.drawBitmapMesh(bitmap, inhaleMesh.getWidth(),
                inhaleMesh.getHeight(), inhaleMesh.getVertices(), 0, null, 0,
                null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 获取位图宽高
        float bmpW = getMeasuredWidth();
        float bmpH = getMeasuredHeight();

        // 图片移动到合适的位置
        matrix.setTranslate(10, 10);

        // 设定matrix的逆矩阵inverse
        matrix.invert(inverse);

        // 构建路径
        buildPaths(bmpW / 2, h - 20);
        // 构建网格
        buildMesh(bmpW, bmpH);
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
        if (inhaleMesh != null) {
            inhaleMesh.buildMeshes(w, h);
        }
    }
}
