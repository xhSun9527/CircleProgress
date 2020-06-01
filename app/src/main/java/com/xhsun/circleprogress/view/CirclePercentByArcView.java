package com.xhsun.circleprogress.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xhsun.circleprogress.R;


/**
 * @time: 2020/6/1
 * @author: xhsun
 * @description: 以圆弧的形式画圆形进度条
 */
public class CirclePercentByArcView extends View {

    //大圈画笔
    private Paint mBgCirclePaint;
    //文字画笔
    private Paint mTxtPaint;

    //圆弧半径
    private int mRadius;
    //圆心坐标
    private int mPointX, mPointY;

    private int mCurPercent;
    private int mBgColor;
    private int mPercentColor;
    private float mTxtSize;
    private int mTxtColor;
    private float mStripWidth;

    //由于onMeasure会调用多次，为了防止半径值改变，设置一个Tag来控制半径值
    private boolean isRadiusFirstChange = true;
    private int mFirstMeasureRadius;

    public CirclePercentByArcView(Context context) {
        this(context, null);
    }

    public CirclePercentByArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public CirclePercentByArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);

        mRadius = array.getInteger(R.styleable.CirclePercentView_radius, 300);
        mBgColor = array.getColor(R.styleable.CirclePercentView_bgColor, Color.GRAY);
        mPercentColor = array.getColor(R.styleable.CirclePercentView_bigColor, Color.YELLOW);
        mTxtColor = array.getColor(R.styleable.CirclePercentView_txtColor, Color.RED);
        mTxtSize = array.getDimension(R.styleable.CirclePercentView_txtSize, 14);
        mStripWidth = array.getDimension(R.styleable.CirclePercentView_stripWidth, 10);

        //使用完后一定要释放资源
        array.recycle();

        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        mBgCirclePaint = new Paint();
        mTxtPaint = new Paint();

        mBgCirclePaint.setStyle(Paint.Style.STROKE);
        mBgCirclePaint.setAntiAlias(true);
        mBgCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 对View进行测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        //对MeasureMode进行判断
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;

            width = widthSize;
            height = heightSize;

            mPointX = mRadius;
            mPointY = mRadius;

            mRadius = (int) (mRadius - mStripWidth);
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            if (isRadiusFirstChange) {
                mFirstMeasureRadius = mRadius;

                mRadius = (int) (mRadius - mStripWidth);
                isRadiusFirstChange = false;
            }
            width = mFirstMeasureRadius * 2;
            height = mFirstMeasureRadius * 2;
            mPointX = mFirstMeasureRadius;
            mPointY = mFirstMeasureRadius;

        }

        setMeasuredDimension(width, height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mEndAngel = (int) (mCurPercent * 3.6);
        //先画背景大圆弧
        mBgCirclePaint.setStrokeWidth(mStripWidth);
        mBgCirclePaint.setColor(mBgColor);
        canvas.drawCircle(mPointX, mPointY, mRadius, mBgCirclePaint);

        //绘制进度圆弧
        mBgCirclePaint.setColor(mPercentColor);
        RectF rectF = new RectF(mPointX - mRadius, mPointY - mRadius, mPointX + mRadius, mPointY + mRadius);
        canvas.drawArc(rectF, -90, mEndAngel, false, mBgCirclePaint);

        //绘制内部文字
        String content = mCurPercent + "%";
        mTxtPaint.setColor(mTxtColor);
        mTxtPaint.setTextSize(mTxtSize);

        //测量文字位置，居中显示
        Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
        mTxtPaint.getFontMetrics(fontMetrics);
        //文字向下偏移量
        float offsetY = (fontMetrics.ascent + fontMetrics.bottom) / 2;

        //测量字符串长度
        float textLength = mTxtPaint.measureText(content);
        int offsetX = (int) (mPointX - textLength / 2);
        canvas.drawText(content, offsetX, mPointY - offsetY, mTxtPaint);
    }

    /**
     * 设置当前进度
     */
    public void setPercent(int percent) {

        if (percent > 100) {
            throw new IllegalArgumentException("the percent can not over 100");
        }

        setCurPercent(percent);
    }

    private void setCurPercent(int percent) {
        mCurPercent = percent;
        this.postInvalidate();
    }
}
