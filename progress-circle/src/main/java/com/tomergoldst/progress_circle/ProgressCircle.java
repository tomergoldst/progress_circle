/*
 Copyright 2016 Tomer Goldstein

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.tomergoldst.progress_circle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Tomer on 19/05/2016.
 *
 */
public class ProgressCircle extends View {

    // Constants
    private static final int DEFAULT_MAX_PROGRESS_VALUE = 100;
    private static final int DEFAULT_MAX_ANIMATION_DURATION_IN_MILLIS = 1200;
    private static final int DEFAULT_MIN_ANIMATION_DURATION_IN_MILLIS = 300;
    private static final int DEFAULT_PROGRESS_WIDTH_DP = 5;
    private static final int DEFAULT_OUTLINE_WIDTH_DP = 5;
    private static final int DEFAULT_PROGRESS_COLOR = 0xff000000;
    private static final int DEFAULT_TEXT_COLOR = 0xff000000;
    private static final int DEFAULT_OUTLINE_COLOR = 0x19000000;
    private static final float DEFAULT_START_ANGLE = -90;

    // progress variable
    private float mProgress;

    // Positioning variables
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private ValueAnimator mValueAnimator;
    private float mXPadBetweenTexts;

    // Style variables
    private Paint mCircleOutlinePaint;
    private Paint mCircleProgressPaint;
    private TextPaint mTextPaint;
    private TextPaint mPercentTextPaint;
    private RectF mValueArcOval;
    private Rect mTextSizeRect;
    private Rect mPercentTextSizeRect;

    // Attributes variables
    private int mTextColor;
    private int mProgressColor;
    private int mOutlineColor;
    private float mProgressWidth;
    private float mOutlineWidth;
    private int mMaxProgressValue;
    private long mMaxAnimationDuration;
    private long mMinAnimationDuration;

    public ProgressCircle(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressCircle,
                0, 0);

        try {
            mTextColor = a.getColor(R.styleable.ProgressCircle_textColor, DEFAULT_TEXT_COLOR);
            mProgressColor = a.getColor(R.styleable.ProgressCircle_progressColor, DEFAULT_PROGRESS_COLOR);
            mOutlineColor = a.getColor(R.styleable.ProgressCircle_outlineColor, DEFAULT_OUTLINE_COLOR);
            mProgressWidth = a.getDimension(R.styleable.ProgressCircle_ProgressWidth,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_WIDTH_DP, getResources().getDisplayMetrics()));
            mOutlineWidth = a.getDimension(R.styleable.ProgressCircle_outlineWidth,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_OUTLINE_WIDTH_DP, getResources().getDisplayMetrics()));
            mMaxProgressValue = a.getInteger(R.styleable.ProgressCircle_maxProgressValue, DEFAULT_MAX_PROGRESS_VALUE);
            mMaxAnimationDuration = a.getInteger(R.styleable.ProgressCircle_maxAnimationDuration, DEFAULT_MAX_ANIMATION_DURATION_IN_MILLIS);
            mMinAnimationDuration = a.getInteger(R.styleable.ProgressCircle_minAnimationDuration, DEFAULT_MIN_ANIMATION_DURATION_IN_MILLIS);
        } finally {
            a.recycle();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        setAttrs(context, attrs);

        // Set initial progress value
        mProgress = 0;
        // Show at preview mode value of 75
        if (isInEditMode()) {
            mProgress = 75;
        }

        // Set outline paint
        initOutlinePaint();

        // Set progress line paint
        initProgressPaint();

        // Set text paint
        initTextPaint();

        // Set percent text paint
        initPercentagePaint();

        // Init rect for finding texts bounds
        mTextSizeRect = new Rect();
        mPercentTextSizeRect = new Rect();

        // Reset values to defaults on illegal values
        if (mMaxAnimationDuration < mMinAnimationDuration) {
            mMaxAnimationDuration = DEFAULT_MAX_ANIMATION_DURATION_IN_MILLIS;
            mMinAnimationDuration = DEFAULT_MIN_ANIMATION_DURATION_IN_MILLIS;
        }
    }

    private void initPercentagePaint() {
        mPercentTextPaint = new TextPaint();
        mPercentTextPaint.setAntiAlias(true);
        mPercentTextPaint.setColor(mTextColor);
    }

    private void refreshPercentagePaint(){
        mPercentTextPaint.setColor(mTextColor);
    }

    private void initTextPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
    }

    private void refreshTextPaint() {
        mTextPaint.setColor(mTextColor);
    }

    private void initProgressPaint() {
        mCircleProgressPaint = new Paint();
        mCircleProgressPaint.setAntiAlias(true);
        mCircleProgressPaint.setStyle(Paint.Style.STROKE);
        mCircleProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mCircleProgressPaint.setStrokeWidth(mProgressWidth);
        mCircleProgressPaint.setColor(mProgressColor);
    }

    private void initOutlinePaint() {
        mCircleOutlinePaint = new Paint();
        mCircleOutlinePaint.setAntiAlias(true);
        mCircleOutlinePaint.setStyle(Paint.Style.STROKE);
        mCircleOutlinePaint.setStrokeWidth(mOutlineWidth);
        mCircleOutlinePaint.setColor(mOutlineColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xPad = (float) (getPaddingLeft() + getPaddingRight());
        float yPad = (float) (getPaddingTop() + getPaddingBottom());

        float circleWidth = mProgressWidth > mOutlineWidth ? mProgressWidth : mOutlineWidth;

        // Figure out how much space we have to draw at.
        float ww = (float) w - xPad - circleWidth;
        float hh = (float) h - yPad - circleWidth;

        // Figure out how big we can make the circle.
        float diameter = Math.min(ww, hh);
        mRadius = diameter / 2;

        // Get center position
        mCenterX = w / 2;
        mCenterY = h / 2;

        // Get text size
        int mTextSize = (int) (mRadius * 0.6);

        // Get percent text size
        int mPercentTextSize = (int) (mRadius * 0.3);

        mTextPaint.setTextSize(mTextSize);
        mPercentTextPaint.setTextSize(mPercentTextSize);
        mValueArcOval = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);

        mXPadBetweenTexts = (float) (mRadius * 0.07);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw outline
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mCircleOutlinePaint);

        // Calculate progress percentage
        float percentageFraction = mProgress / mMaxProgressValue;
        int percentageValue = Math.round(percentageFraction * 100);

        // Draw arc
        float endAngle = 360f * percentageFraction;
        canvas.drawArc(mValueArcOval, DEFAULT_START_ANGLE, endAngle, false, mCircleProgressPaint);

        // Set progress value text
        String valueString = String.valueOf(percentageValue);
        String percentString = "%";

        // Get predicated text measurements
        mTextPaint.getTextBounds(valueString, 0, valueString.length(), mTextSizeRect);
        mPercentTextPaint.getTextBounds(percentString, 0, percentString.length(), mPercentTextSizeRect);

        // Calculate text position
        float startTextXPos = mCenterX - (mTextSizeRect.width() / 2) - (mPercentTextSizeRect.width() / 2) - (mXPadBetweenTexts / 2);
        float startTextYPos = mCenterY + (mTextSizeRect.height() / 2);

        // draw text
        canvas.drawText(valueString, startTextXPos, startTextYPos, mTextPaint);
        canvas.drawText(percentString, startTextXPos + mTextSizeRect.width() + mXPadBetweenTexts, startTextYPos, mPercentTextPaint);

    }

    public void setProgressWithoutAnimation(final int value) {
        mProgress = value;
        invalidate();
        requestLayout();
    }

    public int getProgress() {
        return (int) mProgress;
    }

    public void setProgress(final int value) {
        setProgress(value, 0);
    }

    public void setProgress(final int value, final int startDelay) {
        // On invalid value reset progress to zero
        if (value < 0 || value > mMaxProgressValue) {
            mProgress = 0;
            invalidate();
            requestLayout();
            return;
        }

        // get distance between current progress and new progress
        final float distance = Math.abs(value - mProgress);

        // animate from current progress to new progress
        mValueAnimator = ValueAnimator.ofFloat(mProgress, value);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
                requestLayout();
            }
        });

        // Calculate duration time according to distance
        float durationFraction = distance / (float) mMaxProgressValue;
        long duration = (long) (mMinAnimationDuration +
                ((mMaxAnimationDuration - mMinAnimationDuration) * durationFraction));
        mValueAnimator.setDuration(duration);
        mValueAnimator.setStartDelay(startDelay);

        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        mValueAnimator.start();

    }

    public int getTextColor(){
        return mTextColor;
    }

    public void setTextColor(int color){
        mTextColor = color;
        refreshTextPaint();
        refreshPercentagePaint();
        invalidate();
    }

    public int getProgressColor(){
        return mProgressColor;
    }

    public void setProgressColor(int color){
        mProgressColor = color;
        initProgressPaint();
        invalidate();
    }

    public int getOutlineColor(){
        return mOutlineColor;
    }

    public void setOutlineColor(int color){
        mOutlineColor = color;
        initOutlinePaint();
        invalidate();
    }

    public long getMaxAnimationDuration(){
        return mMaxAnimationDuration;
    }

    public void setMaxAnimationDuration(long duration) {
        mMaxAnimationDuration = duration;
    }

    public long getMinAnimationDuration(){
        return mMinAnimationDuration;
    }

    public void setMinAnimationDuration(long duration) {
        mMinAnimationDuration = duration;
    }

    public int getMaxProgress(){
        return mMaxProgressValue;
    }

    public void setMaxProgress(int value){
        mMaxProgressValue = value;
        invalidate();
    }

    public float getProgressWidth(){
        return mProgressWidth;
    }

    public void setProgressWidth(int width){
        mProgressWidth = width;
        initProgressPaint();
        invalidate();
    }

    public float getOutlineWidth(){
        return mOutlineWidth;
    }

    public void setOutlineWidth(int width){
        mOutlineWidth = width;
        initOutlinePaint();
        invalidate();
    }

    static class SavedState extends BaseSavedState {
        float mProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mProgress = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(mProgress);
        }

        @Override
        public String toString() {
            return "ProgressCircle.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " mProgressValue=" + mProgress + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.mProgress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        mProgress = ss.mProgress;
        requestLayout();
    }


}
