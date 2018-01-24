package com.doooge.timemanager;

/**
 * Created by user on 2018/1/21.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/10/26.
 */

public class ArcShapeView4Top extends View {
    //startAngle：圆弧的起始角度。 sweepAngle：圆弧的角度。
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float storkeWidth;
    private float startAngle;
    private float sweepAngle;
    private float startAngle2;
    private float sweepAngle2;
    private int storkColor;
    private boolean isArc;
    private TypedArray arr;
    private float width;
    private float height;
    private float cx;
    private float cy;
    private RectF rect;
    /**
     * 上半弧进度改变是调用该接口方法
     */
    private OnPregressChangedDown pregressDown;
    /**
     * 下半弧进度改变是调用该接口方法
     */
    private OnPregressChangedUp pregressUp;
    /**
     * The radius of the inner circle
     */
    private float innerRadius;
    /**
     * The X coordinate for 12 O'Clock
     */
    private float startPointX;

    /**
     * The Y coordinate for 12 O'Clock
     */
    private float startPointY;
    /**
     * The X coordinate for 12 O'Clock
     */
    private float startPointX2;

    /**
     * The Y coordinate for 12 O'Clock
     */
    private float startPointY2;

    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointX;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointY;
    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointX2;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointY2;

    /**
     * The adjustment factor. This adds an adjustment of the specified size to
     * both sides of the progress bar, allowing touch events to be processed
     * more user friendlily (yes, I know that's not a word)
     */
    private float adjustmentFactor = 4;

    /**
     * The progress mark when the view isn't being progress modified
     */
    private Bitmap progressMark;


    /**
     * The flag to see if the setProgress() method was called from our own
     * View's setAngle() method, or externally by a user.
     */
    private boolean CALLED_FROM_ANGLE = false;
    /**
     * The X coordinate for the top left corner of the marking drawable
     */
    private float dx;

    /**
     * The Y coordinate for the top left corner of the marking drawable
     */
    private float dy;
    /**
     * The X coordinate for the top left corner of the marking drawable
     */
    private float dx2;

    /**
     * The Y coordinate for the top left corner of the marking drawable
     */
    private float dy2;
    /**
     * The maximum progress amount
     */
    private int maxProgress = 100;

    /**
     * The current progress
     */
    private int progress;

    /**
     * The progress percent
     */
    private int progressPercent;
    private float padding = 20;

    /**
     * The radius of the outer circle
     */
    private float outerRadius;
    /**
     * The listener to listen for changes
     */
    private ArcShapeView4Top.OnSeekChangeListener mListener;
    /**
     * The progress circle ring background
     */
    private Paint circleRing;
    /**
     * The angle of progress
     */
    private int angle = 120;
    /**
     * The angle of progress
     */
    private int angle2 = -30;
    private Context context;

    public ArcShapeView4Top(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        arr = context.obtainStyledAttributes(attrs, R.styleable.ArcShapeView);
        inits();

    }

    private void inits() {
        storkeWidth = arr.getDimension(R.styleable.ArcShapeView_storkeWidth, 1);
        startAngle = arr.getInt(R.styleable.ArcShapeView_startAngle, 90);
        sweepAngle = arr.getInt(R.styleable.ArcShapeView_sweepAngle, 90);
        startAngle2 = 120;
        sweepAngle2 = -60;
        storkColor = arr.getColor(R.styleable.ArcShapeView_storkeColor, Color.BLACK);
        isArc = arr.getBoolean(R.styleable.ArcShapeView_isArc, true);
        rect = new RectF();
        progressMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.a1);
        mListener = new OnSeekChangeListener() {
            @Override
            public void onProgressChange(ArcShapeView4Top view, int newProgress) {

            }
        };
    }

    protected void onDraw(Canvas canvas) {
        dx = getXFromAngle(markPointX);
        dy = getYFromAngle(markPointY);
        dx2 = getXFromAngle(markPointX2);
        dy2 = getYFromAngle(markPointY2);
        circleRing = new Paint();
        circleRing.setAntiAlias(true);                       //设置画笔为无锯齿
        circleRing.setColor(storkColor);                    //设置画笔颜色
//        canvas.drawColor(storkColor);                  //白色背景
        circleRing.setStrokeWidth(storkeWidth);              //线宽
        circleRing.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rect, startAngle2, angle2, isArc, circleRing);    //绘制圆弧
        canvas.drawArc(rect, startAngle, angle, isArc, circleRing);    //绘制圆弧
        circleRing.setColor(Color.GRAY);
        canvas.drawArc(rect, startAngle2 + angle2, sweepAngle2 - angle2, isArc, circleRing);    //绘制圆弧
        canvas.drawArc(rect, startAngle + angle, sweepAngle - angle, isArc, circleRing);    //绘制圆弧
        canvas.drawBitmap(progressMark, dx, dy, null);
        canvas.drawBitmap(progressMark, dx2, dy2, null);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getWidth(); // Get View Width
        height = getHeight();// Get View Height

        float size = (width > height) ? height : width; // Choose the smaller
        // between width and
        // height to make a
        // square

        cx = width / 2; // Center X for circle
        cy = height / 2; // Center Y for circle
        outerRadius = size / 2 - padding; // Radius of the outer circle
        innerRadius = outerRadius - storkeWidth; // Radius of the inner circle

        left = cx - outerRadius; // Calculate left bound of our rect
        right = cx + outerRadius;// Calculate right bound of our rect
        top = cy - outerRadius;// Calculate top bound of our rect
        bottom = cy + outerRadius;// Calculate bottom bound of our rect
//        startPointX = (float) (1-(float)Math.cos((180-startAngle)*Math.PI/180))*(outerRadius-storkeWidth/2);
//        startPointX =(1-(float)Math.sqrt(3)/2)*(outerRadius+storkeWidth+padding) ; // 12 O'clock X coordinate
//        startPointY = (float) (1+(float)Math.sin((180-startAngle)*Math.PI/180))*(outerRadius);
//        startPointY =(float) 1.5*(outerRadius+storkeWidth/2);// 12 O'clock Y coordinate
        startPointX = cx; // 12 O'clock X coordinate
        startPointY = cy - outerRadius;// 12 O'clock Y coordinate
        startPointX2 = cx; // 12 O'clock X coordinate
        startPointY2 = cy + outerRadius;// 12 O'clock Y coordinate
        markPointX = startPointX;// Initial locatino of the marker X coordinate
        markPointY = startPointY;// Initial locatino of the marker Y coordinate
        markPointX2 = startPointX2;// Initial locatino of the marker X coordinate
        markPointY2 = startPointY2;// Initial locatino of the marker Y coordinate

        rect.set(left, top, right, bottom); // assign size to rect
    }
    //float hudu = (float) Math.abs(Math.PI * currentDegreeFlag / 180)
//    /*
// * (non-Javadoc)
// *
// * @see android.view.View#onDraw(android.graphics.Canvas)
// */
//    @Override
//    protected void onDraw(Canvas canvas) {
//        dx = getXFromAngle();
//        dy = getYFromAngle();
//
//        canvas.drawCircle(cx, cy, outerRadius, circleRing);
//        canvas.drawArc(rect, startAngle, angle, true, circleColor);
//        canvas.drawCircle(cx, cy, innerRadius, innerColor);
//
//
//        super.onDraw(canvas);
//    }

//    /**
//     * Draw marker at the current progress point onto the given canvas.
//     *
//     * @param canvas
//     *            the canvas
//     */
//    public void drawMarkerAtProgress(Canvas canvas) {
//        if (IS_PRESSED) {
//            canvas.drawBitmap(progressMarkPressed, dx, dy, null);
//        } else {
//            canvas.drawBitmap(progressMark, dx, dy, null);
//        }
//    }

    /**
     * Gets the X coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the X coordinate
     */
    public float getXFromAngle(float markPointX) {
        int size1 = progressMark.getWidth();
        float x = markPointX - (size1 / 2);
        return x;
    }

    /**
     * Gets the Y coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the Y coordinate
     */
    public float getYFromAngle(float markPointY) {
        int size1 = progressMark.getHeight();
        float y = markPointY - (size1 / 2);
        return y;
    }

    /**
     * Get the angle.
     *
     * @return the angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Set the angle.
     *
     * @param angle the new angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        float donePercent = (((float) this.angle) / 360) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        setProgressPercent(Math.round(donePercent));
        CALLED_FROM_ANGLE = true;
        setProgress(Math.round(progress));
    }

    /**
     * Get the angle.
     *
     * @return the angle
     */
    public int getAngle2() {
        return angle2;
    }

    /**
     * Set the angle.
     *
     * @param angle the new angle
     */
    public void setAngle2(int angle) {
        this.angle2 = angle;
        float donePercent = (((float) this.angle2) / 360) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        setProgressPercent(Math.round(donePercent));
        CALLED_FROM_ANGLE = true;
        setProgress(Math.round(progress));
    }

    /**
     * Sets the seek bar change listener.
     *
     * @param listener the new seek bar change listener
     */
    public void setSeekBarChangeListener(ArcShapeView4Top.OnSeekChangeListener listener) {
        mListener = listener;
    }

    /**
     * Gets the seek bar change listener.
     *
     * @return the seek bar change listener
     */
    public ArcShapeView4Top.OnSeekChangeListener getSeekBarChangeListener() {
        return mListener;
    }

//    /**
//     * Gets the bar width.
//     *
//     * @return the bar width
//     */
//    public int getBarWidth() {
//        return barWidth;
//    }
//
//    /**
//     * Sets the bar width.
//     *
//     * @param barWidth
//     *            the new bar width
//     */
//    public void setBarWidth(int barWidth) {
//        this.barWidth = barWidth;
//    }

    /**
     * The listener interface for receiving onSeekChange events. The class that
     * is interested in processing a onSeekChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>setSeekBarChangeListener(OnSeekChangeListener)<code> method. When
     * the onSeekChange event occurs, that object's appropriate
     * method is invoked.
     */
    public interface OnSeekChangeListener {

        /**
         * On progress change.
         *
         * @param view        the view
         * @param newProgress the new progress
         */
        public void onProgressChange(ArcShapeView4Top view, int newProgress);
    }

    /**
     * Gets the max progress.
     *
     * @return the max progress
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the max progress.
     *
     * @param maxProgress the new max progress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    public void setProgress(int progress) {
        if (this.progress != progress) {
            this.progress = progress;
            if (!CALLED_FROM_ANGLE) {
                int newPercent = (this.progress / this.maxProgress) * 100;
                int newAngle = (newPercent / 100) * 360;
                this.setAngle(newAngle);
                this.setProgressPercent(newPercent);
            }
            mListener.onProgressChange(this, this.getProgress());
            CALLED_FROM_ANGLE = false;
        }
    }

    /**
     * Gets the progress percent.
     *
     * @return the progress percent
     */
    public int getProgressPercent() {
        return progressPercent;
    }

    /**
     * Sets the progress percent.
     *
     * @param progressPercent the new progress percent
     */
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }


    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }


    public float Dp2Px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
    }
    /**
     * Moved.
     *
     * @param x  the x
     * @param y  the y
     * @param up the up
     */
    private void moved(float x, float y, boolean up) {
        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (distance < outerRadius + Dp2Px(context, adjustmentFactor) && distance > innerRadius - Dp2Px(context, adjustmentFactor) && !up) {

//            Log.d("120", "moved:calculate4Y(120)========== "+calculate4Y(120)+":cx===="+cx);
//            Log.d("150", "moved:calculate4Y(150)========== "+calculate4Y(150)+":cy===="+cy);
//            Log.d("y,x", "moved: y=========="+y+":x============"+x);
            if (y >= calculate4Y(120)) {
                float degrees1 = 0.0f;
//                degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 270 - startAngle)) % 360.0);
                if (x >= cx) {
                    degrees1 = ((float) (30 + 180 * (Math.atan2(x - cx, y - cy)) / Math.PI));
//                    Log.d("degrees2", "moved: degrees2="+degrees1);
//                    Log.d("x,y", "moved:x - cx= "+(x - cx)+":y-cy="+(y-cy)+":atan="+Math.atan2(x - cx, y-cy)*180/3.14);
                }
                if (x < cx) {
                    degrees1 = ((float) (30 - 180 * (Math.atan2(cx - x, y - cy)) / Math.PI));
//                    Log.d("degrees1", "moved: degrees1="+degrees1);
//                    Log.d("x,y", "moved:cx-x= "+(cx-x)+":y-cy="+(y-cy)+":atan="+Math.atan2(cx-x, y-cy)*180/3.14);
                }
                markPointX2 = calculate4X(startAngle2 - degrees1);
                markPointY2 = calculate4Y(startAngle2 - degrees1);
                setAngle2((int) (0 - degrees1));
                if (pregressDown != null) {
                    pregressDown.onPregressChangedDown(Math.abs(degrees1 / sweepAngle2));
                }
//                if (degrees>0 ) {
//                    degrees -= 2 * Math.PI;
//
//                }
//                angle2=Math.round(degrees);
            } else if (y <= calculate4Y(150)) {
//               degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 270 - startAngle)) % 360.0);
                float degrees = 0.0f;
                if (y >= cy && x < cx) {
                    degrees = ((float) (180 * (Math.atan2(cx - x, y - cy)) / Math.PI) - 60);
//                    Log.d("degrees1", "moved: degrees1="+degrees);
//                    Log.d("x,y", "moved:cx-x= "+(cx-x)+":y-cy="+(y-cy)+":atan="+Math.atan2(cx-x, y-cy)*180/3.14);

                }
                if (y >= cy && x >= cx) {
                    degrees = ((float) (300 - 180 * (Math.atan2(x - cx, y - cy)) / Math.PI));
//                    Log.d("degrees2", "moved: degrees2="+degrees);
//                    Log.d("x,y", "moved:x - cx= "+(x - cx)+":y-cy="+(y-cy)+":atan="+Math.atan2(x - cx, y-cy));

                }
                if (y < cy && x >= cx) {
                    degrees = ((float) (180 * (Math.atan2(x - cx, cy - y)) / Math.PI + 120));
//                    Log.d("degrees3", "moved: degrees3="+degrees);
//                    Log.d("x,y", "moved:x - cx= "+(x - cx)+":cy-y="+(cy-y)+":atan="+Math.atan2(x - cx, cy-y));
                }
                if (y < cy && x < cx) {
                    degrees = ((float) (90 - 180 * (Math.atan2(cx - x, cy - y)) / Math.PI + 30));
//                    Log.d("degrees4", "moved: degrees4="+degrees);
//                    Log.d("x,y", "moved:cx-x= "+(cx-x)+":cy-y="+(cy-y)+":atan="+Math.atan2(cx-x, cy-y));
                }
                markPointX = calculate4X(startAngle + degrees);
                markPointY = calculate4Y(startAngle + degrees);
                setAngle((int)(degrees));
                if (pregressUp != null) {
                    pregressUp.onPregressChangedUp(degrees / sweepAngle);
                }
            }
            invalidate();

        } else

        {
            invalidate();
        }
    }

    /**
     * Gets the adjustment factor.
     *
     * @return the adjustment factor
     */
    public float getAdjustmentFactor() {
        return adjustmentFactor;
    }

    /**
     * Sets the adjustment factor.
     *
     * @param adjustmentFactor the new adjustment factor
     */
    public void setAdjustmentFactor(float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    /**
     * 根据半径和角度计算x坐标
     */
    private float calculateX(float r, double angle) {
        angle = angle * ((2 * Math.PI) / 360);
        double x = r * Math.sin(angle);

        double xFinal = cx + x;
        return (float) xFinal;
    }

    /**
     * 根据半径和角度计算y坐标
     */
    private float calculateY(float r, double angle) {
        angle = angle * ((2 * Math.PI) / 360);
        double y = r * Math.cos(angle);

        double yFinal = cy - y;
        return (float) yFinal;
    }

    /**
     * 根据半径和角度计算x坐标
     */
    private float calculate4X(double angle) {
        angle = angle * Math.PI / 180;
        double x = (innerRadius + storkeWidth) * Math.cos(angle);
        double xFinal = cx + x;
        return (float) xFinal;
    }

    /**
     * 根据半径和角度计算y坐标
     */
    private float calculate4Y(double angle) {
        angle = angle * Math.PI / 180;
        double y = (innerRadius + storkeWidth) * Math.sin(angle);
        double yFinal = cy + y;
        return (float) yFinal;
    }


    public void setLeft(float left) {
        this.left = left;
    }

    /**
     * 但进度发送改变的时候调用该方法(下半弧)
     *
     * @param pregressDown （0.0~1.0）
     */
    public void setPregressDown(OnPregressChangedDown pregressDown) {
        this.pregressDown = pregressDown;

    }

    /**
     * 但进度发送改变的时候调用该方法(上半弧)
     *
     * @param pregressUp （0.0~1.0）
     */
    public void setPregressUp(OnPregressChangedUp pregressUp) {
        this.pregressUp = pregressUp;
    }

    public interface OnPregressChangedUp {
        /**
         * 但进度发送改变的时候调用该方法(上半弧)
         *
         * @param progress （0.0~1.0）
         */
        public void onPregressChangedUp(double progress);

    }

    public interface OnPregressChangedDown {
        /**
         * 但进度发送改变的时候调用该方法(下半弧)
         *
         * @param progress （0.0~1.0）
         */
        public void onPregressChangedDown(double progress);
    }
}
