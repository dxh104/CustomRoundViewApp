package com.example.customroundviewapp.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.customroundviewapp.R;

/**
 * Created by XHD on 2020/12/11
 */
public class CustomRoundView extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {
    //半径建议不能设置超过屏幕宽度一半
    private Context mContext;
    private int mRadius;//半径 -----直径(半径2倍)比容器宽度/高度大就选择(直径+最大子view宽/高) 作为容器宽高
    private float mRotateValue;//旋转角度值
    private ValueAnimator mValueAnimator;

    public CustomRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomRoundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initAttrs(attrs);//初始化配置
    }

    private int childViewMaxWidth = 0, childViewMaxHeight = 0;

    //容器设置合适的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewGroupWidth = 0, viewGroupHeight = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);//测量子view
        for (int i = 0; i < getChildCount(); i++) {//获取子view最大宽高
            View childView = getChildAt(i);
            childViewMaxWidth = childViewMaxWidth < childView.getMeasuredWidth() ? childView.getMeasuredWidth() : childViewMaxWidth;
            childViewMaxHeight = childViewMaxHeight < childView.getMeasuredHeight() ? childView.getMeasuredHeight() : childViewMaxHeight;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY://match_parent 100dp 确切值 父决定子的确切大小，子被限定在给定的边界里，忽略本身想要的大小
                viewGroupWidth = widthSize > childViewMaxWidth + mRadius * 2 ? widthSize : childViewMaxWidth + mRadius * 2;//选择较大的值
                break;
            case MeasureSpec.AT_MOST://wrap_content  子最大可以达到的指定大小
                viewGroupWidth = childViewMaxWidth + mRadius * 2;
                break;
            case MeasureSpec.UNSPECIFIED:// 父容器不对子View的大小做限制.
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY://match_parent 100dp 确切值 父决定子的确切大小，子被限定在给定的边界里，忽略本身想要的大小
                viewGroupHeight = heightSize > childViewMaxHeight + mRadius * 2 ? heightSize : childViewMaxHeight + mRadius * 2;//选择较大的值
                break;
            case MeasureSpec.AT_MOST://wrap_content  子最大可以达到的指定大小
                viewGroupHeight = childViewMaxHeight + mRadius * 2;
                break;
            case MeasureSpec.UNSPECIFIED:// 父容器不对子View的大小做限制.
                break;
        }
        setMeasuredDimension(viewGroupWidth, viewGroupHeight);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomRoundView);
        mRotateValue = typedArray.getFloat(R.styleable.CustomRoundView_rotateValue, 0) - 90;
        mRadius = typedArray.getLayoutDimension(R.styleable.CustomRoundView_radius, 0);
        typedArray.recycle();
    }

    private double childViewCenterX;//子view中心位置
    private double childViewCenterY;//子view中心位置
    private float averageAngle = 0;//平均角度差

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, right, top, bottom;
        if (getChildCount() != 0) {
            averageAngle = 360 / getChildCount();
        }
        int circleX = getMeasuredWidth() / 2;//圆心坐标x
        int circleY = getMeasuredHeight() / 2;//圆心坐标y
        for (int i = 0; i < getChildCount(); i++) {
            childViewCenterX = circleX + mRadius * Math.cos(Math.toRadians(averageAngle * i + mRotateValue));
            childViewCenterY = circleY + mRadius * Math.sin(Math.toRadians(averageAngle * i + mRotateValue));
            View childView = getChildAt(i);
            left = (int) (childViewCenterX - childView.getMeasuredWidth() / 2);
            right = (int) (childViewCenterX + childView.getMeasuredWidth() / 2);
            top = (int) (childViewCenterY - childView.getMeasuredHeight() / 2);
            bottom = (int) (childViewCenterY + childView.getMeasuredHeight() / 2);
            childView.layout(left, top, right, bottom);//所有childView居中叠加显示
        }
    }

    public void setmRotateValue(float mRotateValue) {
        this.mRotateValue = mRotateValue - 90;
        if (getChildCount() > 0)
            requestLayout();
    }

    private boolean mOrder;//顺序 true=正序
    private float mFromRotateValue;//从哪个角度值开始转

    //开启动画
    public void startAnimal(long duration, boolean order) {
        if (getChildCount() == 0)
            return;
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, 360);
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.setInterpolator(new LinearInterpolator());//线性插值器
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);//播放结束重头播放
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);//重复次数循环

        }
        mFromRotateValue = mRotateValue + 90;//从当前角度开始转
        mOrder = order;
        mValueAnimator.setDuration(duration);//一次动画时间
        mValueAnimator.start();
    }

    //暂停动画
    @SuppressLint("NewApi")
    public void pauseAnimal() {
        if (mValueAnimator == null)
            return;
        if (!mValueAnimator.isPaused()) {
            mValueAnimator.pause();
        }
    }

    //恢复动画
    @SuppressLint("NewApi")
    public void resumeAnimal() {
        if (mValueAnimator == null)
            return;
        if (mValueAnimator.isPaused()) {
            mValueAnimator.resume();
        }
    }

    //停止动画
    public void stopAnimal() {
        if (mValueAnimator == null)
            return;
        if (mValueAnimator.isStarted()) {
            mValueAnimator.cancel();
        }
        mValueAnimator.removeUpdateListener(this);
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator = null;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float currentValue = (float) animation.getAnimatedValue();
        if (mOrder)
            setmRotateValue((mFromRotateValue + currentValue) % 360);
        else
            setmRotateValue((mFromRotateValue - currentValue) % 360);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimal();//防止内存泄漏
        super.onDetachedFromWindow();
    }
}
