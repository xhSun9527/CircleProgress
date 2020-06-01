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
 * @time:  2020/6/1
 * @author:  xhsun
 * @description:  以扇形的方式画圆形进度条
 */
public class CirclePercentView extends View {

    private float mStripWidth;
    public int mCurPercent;
    private int mBigColor;
    private int mLittleColor;
    private float mTxtSize;
    private int mRadius;
    private int mTxtColor;

    private Paint mOuterPaint;
    private Paint mOvalPaint;
    private Paint mTxtPaint;

    //圆心坐标
    private int mPointX;
    private int mPointY;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);

        mStripWidth = array.getDimension(R.styleable.CirclePercentView_stripWidth, 10);
        mCurPercent = array.getInteger(R.styleable.CirclePercentView_percent, 0);
        mBigColor = array.getColor(R.styleable.CirclePercentView_bigColor, Color.BLUE);
        mLittleColor = array.getColor(R.styleable.CirclePercentView_littleColor, Color.CYAN);
        mTxtSize = array.getDimension(R.styleable.CirclePercentView_txtSize, 14);
        mRadius = array.getInteger(R.styleable.CirclePercentView_radius, 100);
        mTxtColor = array.getColor(R.styleable.CirclePercentView_txtColor, Color.RED);

        array.recycle();

        initPaint();
    }

    private void initPaint() {

        mOuterPaint = new Paint();
        mOvalPaint = new Paint();
        mTxtPaint = new Paint();

        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStyle(Paint.Style.FILL);

        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setStyle(Paint.Style.FILL);

        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            width = widthSize;
            height = heightSize;

        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = mRadius * 2;
            height = mRadius * 2;
        }

        mPointX = mRadius;
        mPointY = mRadius;


        setMeasuredDimension(width, height);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        //角度
        int mEndAngle = (int) (mCurPercent * 3.6);
        //画大圆
        mOuterPaint.setColor(mBigColor);
        canvas.drawCircle(mPointX, mPointY, mRadius, mOuterPaint);

        //画扇形
        mOvalPaint.setColor(mLittleColor);
        mOvalPaint.setStrokeCap(Paint.Cap.ROUND);
        RectF arcRectF = new RectF(mPointX - mRadius, mPointY - mRadius, mPointX + mRadius, mPointY + mRadius);
        canvas.drawArc(arcRectF, -90, mEndAngle, true, mOvalPaint);

        //画小圆，遮住扇形
        mOuterPaint.setColor(mBigColor);
        canvas.drawCircle(mPointX, mPointY, mRadius - mStripWidth, mOuterPaint);

        //画文字
        String text = mCurPercent + " %";
        mTxtPaint.setColor(mTxtColor);
        mTxtPaint.setTextSize(mTxtSize);

        Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
        mTxtPaint.getFontMetrics(fontMetrics);
        float offsetY = (fontMetrics.ascent + fontMetrics.bottom) / 2;
        //测量字符串长度
        float textLength = mTxtPaint.measureText(text);
        canvas.drawText(text, mPointX - (textLength / 2), mPointY - offsetY, mTxtPaint);
    }

    public void setPercent(int percent) {

        if (percent > 100) {
            throw new IllegalArgumentException("percent can not above 100");
        }

        setCurPercent(percent);
    }

    private void setCurPercent(int percent) {
        mCurPercent = percent;

        this.postInvalidate();
    }
}
