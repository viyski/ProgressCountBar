package com.gm.afloat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.gm.afloat.R;

/**
 * Created by lgm on 2017/3/18.
 */
public class ProgressCountBar extends View {

    private static final String TAG = ProgressCountBar.class.getSimpleName();
    private final float mTextSize;
    private float mStrokeWidth;
    private float mCircleRadius;
    private int mCircleColor;
    private int mProgressColor;
    private int mBackgroundColor;
    private int mProgressDuration;
    private int mCurrentDuration;
    private Paint mCirclePaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private float mCenterX;
    private float mCenterY;
    private RectF mCircleBounds;
    private float mSwipeAngle;
    private float mRadio;
    private ProgressUpdateListener mListener;
    private CountTimer countTimer;

    private int mState = STATE_INIT;
    private static final int STATE_INIT = 0;
    private static final int STATE_DRAWING = 1;
    private static final int STATE_FINISH = 2;

    public ProgressCountBar(Context context) {
        this(context, null);
    }

    public ProgressCountBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressCountBar);
        try {
            mStrokeWidth = typedArray.getDimension(R.styleable.ProgressCountBar_strokeWidth, 4);
            mCircleRadius = typedArray.getDimension(R.styleable.ProgressCountBar_circleRadius, 22);
            mCircleColor = typedArray.getColor(R.styleable.ProgressCountBar_circleColor, Color.WHITE);
            mProgressColor = typedArray.getColor(R.styleable.ProgressCountBar_progressColor, Color.BLUE);
            mBackgroundColor = typedArray.getColor(R.styleable.ProgressCountBar_backgroundColor,Color.parseColor("#99000000"));
            mProgressDuration = typedArray.getInt(R.styleable.ProgressCountBar_progressDuration, 300);
            mTextSize = typedArray.getDimension(R.styleable.ProgressCountBar_countTextSize, 16);
        } finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mStrokeWidth+1);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);

        mRadio = (float) 360 / (float) mProgressDuration;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2f;
        mCenterY = h / 2f;

        mCircleBounds = new RectF();
        mCircleBounds.top = h / 2f - mCircleRadius;
        mCircleBounds.left = w / 2f - mCircleRadius;
        mCircleBounds.bottom = h / 2f + mCircleRadius;
        mCircleBounds.right = w / 2f + mCircleRadius;

    }

    public void start() {
        countTimer = new CountTimer(mProgressDuration * 1000, 1000);
        countTimer.start();
    }

    public void setProgressUpdateListener(ProgressUpdateListener listener) {
        mListener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float lenText = mTextPaint.measureText(mCurrentDuration + "");
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float heightText = fontMetrics.bottom - fontMetrics.top;

        switch (mState){
            case STATE_INIT:
                break;
            case STATE_DRAWING:
                canvas.drawCircle(mCenterX, mCenterY, mCircleRadius - 2 , mBackgroundPaint);
                canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);

                mSwipeAngle = 360 - mRadio * mCurrentDuration;
                canvas.drawArc(mCircleBounds, -90, mSwipeAngle, false, mProgressPaint);
                canvas.drawText(String.valueOf(mCurrentDuration), mCenterX - lenText / 2, mCenterY + heightText / 3, mTextPaint);
                break;
            case STATE_FINISH:
                canvas.drawCircle(mCenterX, mCenterY, mCircleRadius - 2 , mBackgroundPaint);
                canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);

                canvas.drawArc(mCircleBounds, 0, 360, false, mProgressPaint);
                canvas.drawText("", mCenterX - lenText / 2, mCenterY + heightText / 3, mTextPaint);
                break;
        }
    }

    public void reset(){
        mState = STATE_INIT;
    }

    class CountTimer extends CountDownTimer {

        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mState = STATE_DRAWING;
            mCurrentDuration = (int) (millisUntilFinished / 1000);
            if (mListener != null) mListener.onTick(mCurrentDuration);
            invalidate();
        }

        @Override
        public void onFinish() {
            mState = STATE_FINISH;
            if (mListener != null) mListener.onFinish();
            invalidate();
        }
    }

    public interface ProgressUpdateListener {
        void onTick(int duration);

        void onFinish();
    }
}
