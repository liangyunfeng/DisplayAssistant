package com.lyf.display.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lyf.display.R;

/**
 * Created by yunfeng.l on 2018/3/6.
 */

public class CloseView extends View {
    private Paint mPaint;
    protected float startX1, startY1,startX2, startY2,endX1, endY1, endX2, endY2;
    protected int centerX, centerY;
    private int mPaintColor = Color.BLACK;
    private int mPaintSize = 3;

    public CloseView(Context context) {
        super(context);
        init();
    }

    public CloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaintColor = Color.BLACK;
        mPaintSize = 3;

        mPaint = new Paint();
        mPaint.setStrokeWidth(mPaintSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawLine(startX1,startY1,endX1,endY1,mPaint);
        canvas.drawLine(startX2,startY2,endX2,endY2,mPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        centerX = width / 2;
        centerY = height / 2;
        startX1 = width * 0.3f;
        startY1 = height * 0.3f;
        endX1 = width * 0.9f;
        endY1 = height * 0.9f;
        startX2 = width * 0.3f;
        startY2 = height * 0.9f;
        endX2 = width * 0.9f;
        endY2 = height * 0.3f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
