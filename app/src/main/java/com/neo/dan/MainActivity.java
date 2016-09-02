package com.neo.dan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";


    private static final String ALPHA = "alpha";
    private static final String ROTATION = "rotation";
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";


    private List<ObjectAnimator> animatorList = new ArrayList<>();

    View mIvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvImage = findViewById(R.id.iv_image);

        //第一列
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);

        //第二列
        findViewById(R.id.btn11).setOnClickListener(this);
        findViewById(R.id.btn12).setOnClickListener(this);
        findViewById(R.id.btn13).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mIvImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAnimators();
            }
        },500);
    }

    private void initAnimators() {
        //渐变:从1到0，再到1
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, ALPHA, 1, 0, 1));
        //旋转:从0度旋转到360度
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, ROTATION, 0f, 360));
        //x平移:水平平移自身宽度
        float curTranslationX = mIvImage.getTranslationX();
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, TRANSLATION_X, curTranslationX,
                mIvImage.getMeasuredWidth(), curTranslationX));
        //y平移：竖直平移自身高度
        float curTranslationY = mIvImage.getTranslationY();
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, TRANSLATION_Y, curTranslationY,
                mIvImage.getMeasuredHeight(), curTranslationY));
        //x缩放：从1，到0，再到1
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, SCALE_X, 1f, 0.0f, 1f));
        //Y缩放：从1，到0，再到1
        animatorList.add(ObjectAnimator.ofFloat(mIvImage, SCALE_Y, 1f, 0.2f, 1f));

    }

    @Override
    public void onClick(View view) {
        anim1(view);
        switch (view.getId()) {
            case R.id.btn11: //2种随机组合
                anim11(view);
                break;
            case R.id.btn12: //3种随机组合
                anim12(view);
                break;
            case R.id.btn13: //4种随机组合
                anim13(view);
                break;
        }
    }

    /**
     * 第一列动画
     *
     * @param view
     */
    private void anim1(View view) {
        //com.neo.dan:id/btn1
        String resourceName = view.getResources().getResourceName(view.getId());
        //btn1
        String entryName = view.getResources().getResourceEntryName(view.getId());

        Log.e(TAG, "resourceName===" + resourceName);
        Log.e(TAG, "EntryName===" + entryName);

        String indexStr = entryName.substring(3, entryName.length());
        Integer index = Integer.valueOf(indexStr);

        if (index >= animatorList.size()) {
            return;
        }

        ObjectAnimator animator = animatorList.get(index);
        if (animator.isRunning()) {
            return;
        }

        if (animator != null) {
            //监听属性值变化
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    Log.e(TAG, "===" + valueAnimator.getAnimatedValue());
                }
            });

            //AnimatorListenerAdapter实现了AnimatorListener
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            animator.setDuration(1000);
            animator.start();
        }
    }

    private void anim11(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        int random = getRandom();
        ObjectAnimator animator1 = animatorList.get(random);
        ObjectAnimator animator2 = animatorList.get((random + 1) % animatorList.size());

        animator1.setDuration(500);
        animator2.setDuration(500);


        //先执行1，再执行2
        animatorSet.play(animator2).after(animator1);
        //每个动画各1s
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    private void anim12(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        int random = getRandom();
        ObjectAnimator animator1 = animatorList.get(random);
        ObjectAnimator animator2 = animatorList.get((random + 1) % animatorList.size());
        ObjectAnimator animator3 = animatorList.get((random + 2) % animatorList.size());

        animator1.setDuration(500);
        animator2.setDuration(500);
        animator3.setDuration(500);

        //先执行1，再执行(2和3同时)
        animatorSet.play(animator2).with(animator3).after(animator1);
        animatorSet.start();
    }

    private void anim13(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        int random = getRandom();
        ObjectAnimator animator1 = animatorList.get(random);
        ObjectAnimator animator2 = animatorList.get((random + 1) % animatorList.size());
        ObjectAnimator animator3 = animatorList.get((random + 2) % animatorList.size());
        ObjectAnimator animator4 = animatorList.get((random + 3) % animatorList.size());

        animator1.setDuration(500);
        animator2.setDuration(500);
        animator3.setDuration(500);
        animator4.setDuration(500);

        //1,2,3,4按顺序执行
        animatorSet.play(animator4).after(animator3).after(animator2).after(animator1);
        //每个动画各1s
        animatorSet.start();
    }

    private int getRandom() {
        Random random = new Random();
        return random.nextInt(animatorList.size());
    }

}
