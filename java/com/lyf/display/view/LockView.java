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

public class LockView extends View {
    private static final int LOCKED = 0;
    private static final int LOCKING = 1;
    private static final int UNLOCKED = 2;
    private static final int UNLOCKING = 3;
    private final int DEFAULT_SPEED = 10;
    private Paint mPaint;
    protected int centerX, centerY;
    protected Camera mCamera;
    private float count = 0;
    private int mCurState = LOCKED;
    private boolean canDrawCoorLine = false;
    private int mLockPaintColor = Color.BLACK;
    private int mLockPaintSize = 3;
    private float speed = 0;
    private CallBack mCallBack;
    private RectF mRect;
    private RectF mOval;
    private float mLockBodyCenX, mLockBodyCenY, mLockBodySart, mLockBodyEnd;
    private Matrix mMatrix;

    public LockView(Context context) {
        super(context);
        init();
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockView);
        mLockPaintColor = a.getColor(R.styleable.LockView_lockColor, Color.BLACK);
        mLockPaintSize = a.getInt(R.styleable.LockView_lockSize, 3);
        speed = a.getFloat(R.styleable.LockView_speed, DEFAULT_SPEED);
        canDrawCoorLine = a.getBoolean(R.styleable.LockView_showCoordinateLine, false);
        mCurState = a.getInt(R.styleable.LockView_state, LOCKED);
        if (mCurState != LOCKED && mCurState != UNLOCKED) {
            mCurState = UNLOCKED;
        }
        if (mCurState == LOCKED) {
            count = 0;
        } else if (mCurState == UNLOCKED) {
            count = 180;
        }
        mPaint.setColor(mLockPaintColor);
        mPaint.setStrokeWidth(mLockPaintSize);
        a.recycle();
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mLockPaintColor = Color.BLACK;
        mLockPaintSize = 3;

        mPaint = new Paint();
        mPaint.setStrokeWidth(mLockPaintSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLockPaintColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mMatrix = new Matrix();
        mCamera = new Camera();

        mCurState = LOCKED;
        count = 0;
        speed = DEFAULT_SPEED;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurState == LOCKED) {
                    unlock();
                } else if (mCurState == UNLOCKED) {
                    lock();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canDrawCoorLine) {
            canvas.save();
            canvas.drawLine(0, centerY, getMeasuredWidth(), centerY, mPaint);
            canvas.drawLine(centerX, 0, centerY, getMeasuredHeight(), mPaint);
            canvas.restore();
        }

        canvas.save();
        canvas.drawRect(mRect, mPaint);
        canvas.restore();

        if (mCurState == UNLOCKING) {
            count = count + speed;
            if (count >= 180) {
                count = 180;
                mCurState = UNLOCKED;
                if (mCallBack != null) {
                    mCallBack.onUnLocked();
                }
            }
        } else if (mCurState == LOCKING) {
            count = count - speed;
            if (count <= 0) {
                count = 0;
                mCurState = LOCKED;
                if (mCallBack != null) {
                    mCallBack.onLocked();
                }
            }
        }

        canvas.save();
        canvas.rotate(count / 2, mLockBodyCenX, mLockBodyCenY);
        canvas.drawLine(mLockBodyCenX, mLockBodySart, mLockBodyCenX, mLockBodyEnd, mPaint);
        canvas.restore();

        mCamera.save();
        mCamera.rotateY((count));
        mCamera.getMatrix(mMatrix);
        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);
        canvas.setMatrix(mMatrix);
        canvas.drawArc(mOval, -180, 180, false, mPaint);
        mCamera.restore();

        if (mCurState == LOCKING || mCurState == UNLOCKING) {
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        centerX = width / 2;
        centerY = height / 2;
        mRect = new RectF(centerX * 0.15f, centerY, centerX * 1.15f, centerY * 1.8f);
        mOval = new RectF(centerX * 0.3f, centerY * 0.5f, centerX, centerY * 1.5f);
        mLockBodyCenX = centerX * 0.65f;
        mLockBodyCenY = centerY * 1.4f;
        mLockBodySart = centerY * 1.35f;
        mLockBodyEnd = centerY * 1.45f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void lock() {
        if (mCurState == UNLOCKED) {
            mCurState = LOCKING;
            invalidate();
        }
    }

    public void unlock() {
        if (mCurState == LOCKED) {
            mCurState = UNLOCKING;
            invalidate();
        }
    }

    public boolean isLocked() {
        return mCurState == LOCKED;
    }

    public void setState(int state) {
        if (state == LOCKED || state == UNLOCKED) {
            mCurState = state;
            if (mCurState == LOCKED) {
                count = 0;
            } else if (mCurState == UNLOCKED) {
                count = 180;
            }
            invalidate();
        }
    }

    public int getState() {
        return mCurState;
    }

    public void drawCoordinateLine(boolean enable) {
        canDrawCoorLine = enable;
        invalidate();
    }

    public void setLockColor(int color) {
        mLockPaintColor = color;
        if (mPaint != null) {
            mPaint.setColor(mLockPaintColor);
            invalidate();
        }
    }

    public void setLockSize(int size) {
        mLockPaintSize = size;
        if (mPaint != null) {
            mPaint.setStrokeWidth(mLockPaintSize);
            invalidate();
        }
    }

    public void setSpeed(float angle) {
        speed = angle;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        public void onLocked();
        public void onUnLocked();
    }
}
