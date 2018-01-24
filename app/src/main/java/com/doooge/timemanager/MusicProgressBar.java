package com.doooge.timemanager;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 2018/1/21.
 */

public class MusicProgressBar extends View {
    /**
     * The paint
     */
    private Paint paint;

    /**
     * The color of progress circle
     */
    private int roundColor;

    /**
     * The color of progress bar
     */
    private int roundProgressColor;

    /**
     * The width of progress circle
     */
    private float roundWidth;

    /**
     * The maximum process
     */
    private int max;

    /**
     * The current process of start thumb
     */
    private int progressStart;

    /**
     * The current process of end thumb
     */
    private int progressEnd;

    /**
     * The color of the string of intermediate progress percentage.
     */
    private int textColor;

    /**
     * The font of the middle progress percentage string.
     */
    private float textSize;


    /**
     * the start thumb
     */
    private Drawable thumbStart, thumbStartPress;

    /**
     * the end thumb
     */
    private Drawable thumbEnd, thumbEndPress;

    private int startTime;
    private int endTime;

    private String time;

    private Handler handler;

    private Context ctx;


    public MusicProgressBar(Context context) {
        this(context, null);
    }




    public MusicProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
         ctx = context;
        paint = new Paint();



        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //Gets the custom property and default values.
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLUE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 50);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_imageMax, 1440);
        mTypedArray.recycle();

        // Loading the picture of thumb
        thumbStart = getResources().getDrawable(R.drawable.a1);
        int thumbHalfheight = thumbStart.getIntrinsicHeight() / 2;
        int thumbHalfWidth = thumbStart.getIntrinsicWidth() / 2;
        thumbStart.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);

        thumbStartPress = getResources().getDrawable(R.drawable.a2);
        thumbHalfheight = thumbStartPress.getIntrinsicHeight() / 2;
        thumbHalfWidth = thumbStartPress.getIntrinsicWidth() / 2;
        thumbStartPress.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        paddingOuterThumb = thumbHalfheight;

        thumbEnd = getResources().getDrawable(R.drawable.a1);
        int thumbHalfheight1 = thumbEnd.getIntrinsicHeight() / 2;
        int thumbHalfWidth1 = thumbEnd.getIntrinsicWidth() / 2;
        thumbEnd.setBounds(-thumbHalfWidth1, -thumbHalfheight1, thumbHalfWidth1, thumbHalfheight1);

        thumbEndPress = getResources().getDrawable(R.drawable.a2);
        thumbHalfheight1 = thumbEndPress.getIntrinsicHeight() / 2;
        thumbHalfWidth1 = thumbEndPress.getIntrinsicWidth() / 2;
        thumbEndPress.setBounds(-thumbHalfWidth1, -thumbHalfheight1, thumbHalfWidth1, thumbHalfheight1);


    }

    @Override
    public void onDraw(Canvas canvas) {
        /**
         * 画最外层的大圆环
         */
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出圆环

        /**
         * 画文字
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);

        String textTime = getTimeText(progressStart, progressEnd);
        float textWidth = paint.measureText(textTime);

        View view = LayoutInflater.from(ctx).inflate(R.layout.taskcreator,null);
        EditText view1 = view.findViewById(R.id.time1);
        view1.setText(textTime);
        System.out.println((handler==null)+"!!!!");
        if(handler!=null) {
            System.out.println("!!!!!!!!!");

            Message message = handler.obtainMessage();
            message.what = 0;
            message.obj = textTime;
            handler.sendMessage(message);
        }

        System.out.println("===============");
       // String x = (String) view1.getText();
       // System.out.println(x+"!!!!!");





        canvas.drawText(textTime, centerX - textWidth / 2, centerY + textSize / 2, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundWidth);
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        paint.setStyle(Paint.Style.STROKE);
        if (progressStart >= progressEnd) {

            canvas.drawArc(oval, 360 * progressEnd / max + 270, 360 * (progressStart - progressEnd) / max, false, paint);
        } else if (progressStart < progressEnd) {
            canvas.drawArc(oval, 360 * progressStart / max + 270, 360 * (progressEnd - progressStart) / max, false, paint);
        }


        PointF progressStartPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progressStart / max, 270);
        PointF progressEndPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progressEnd / max, 270);


        // 画Thumb
        canvas.save();

        pointStartX = progressStartPoint.x;
        pointStartY = progressStartPoint.y;
        pointEndX = progressEndPoint.x;
        pointEndY = progressEndPoint.y;

        if (downOnArc && !downOnArc1) {
            canvas.translate(progressStartPoint.x, progressStartPoint.y);
            thumbStartPress.draw(canvas);

        } else if (downOnArc1 && !downOnArc) {
            canvas.translate(progressEndPoint.x, progressEndPoint.y);
            thumbEndPress.draw(canvas);

        } else {
            canvas.translate(progressEndPoint.x, progressEndPoint.y);
            thumbEnd.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(progressStartPoint.x, progressStartPoint.y);
            thumbStart.draw(canvas);
        }


        canvas.restore();
    }

    /**
     * the boolean type of check if touch on screen
     */
    private boolean downOnArc = false;
    private boolean downOnArc1 = false;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchPot(x, y)) {
                    downOnArc = true;
                    updateArc(x, y, true);
                    return true;
                } else if (isTouchPot1(x, y)) {
                    downOnArc1 = true;
                    updateArc(x, y, false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnArc) {
                    updateArc(x, y, true);
                    return true;
                } else if (downOnArc1) {
                    updateArc(x, y, false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downOnArc = false;
                downOnArc1 = false;
                invalidate();
                if (changeListener != null) {
                    changeListener.onProgressChangeEnd(max, progressStart);
                    changeListener.onProgressChangeEnd(max, progressEnd);
                }
                if(timeListener!=null){
                    timeListener.execute();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int centerX, centerY;
    private int radius;
    private int paddingOuterThumb;

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);

        radius = (int) (minCenter - roundWidth / 2 - paddingOuterThumb); //圆环的半径

        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb * 1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb * 1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }

    // 根据点的位置，更新进度
    private void updateArc(int x, int y, boolean isStart) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        // 计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
        double angle = Math.atan2(cy, cx) / Math.PI;
        // 将角度转换成（0->2）之间的值，然后加上90°的偏移量
        angle = ((2 + angle) % 2 + (90 / 180f)) % 2;
        // 用（0->2）之间的角度值乘以总进度，等于当前进度
        time = getTime(progressStart);
        if (isStart) {

            progressStart = (int) (angle * max / 2);
            if (changeListener != null) {
                changeListener.onProgressChange(max, progressStart);
            }
            if(timeListener!=null){
                timeListener.execute();
            }

        } else {
            progressEnd = (int) (angle * max / 2);

            if (changeListener != null) {
                changeListener.onProgressChange(max, progressEnd);
            }
            if(timeListener!=null){
                timeListener.execute();
            }


        }
        invalidate();
    }

    private int minValidateTouchArcRadius; // 最小有效点击半径
    private int maxValidateTouchArcRadius; // 最大有效点击半径

    private float pointStartX;
    private float pointStartY;
    private float pointEndX;
    private float pointEndY;

    // 判断是否按在圆边上
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {
            return true;
        }
        return false;
    }

    /**  To check if touch on the start thumb
     *
     * @param x the X coordinate of touch on view
     * @param y the Y coordinate of touch on view
     * @return whether touch success.
     */
    private boolean isTouchPot(int x, int y) {
        int X = (int) pointStartX - x;
        int Y = (int) pointStartY - y;
        double d = Math.sqrt(X ^ 2 + Y ^ 2);
        if (d <= 10) {
            return true;
        }
        return false;

    }

    /**  To check if touch on the end thumb
     *
     * @param x the X coordinate of touch on view
     * @param y the Y coordinate of touch on view
     * @return whether touch success.
     */
    private boolean isTouchPot1(int x, int y) {
        int X = (int) pointEndX - x;
        int Y = (int) pointEndY - y;
        double d = Math.sqrt(X ^ 2 + Y ^ 2);
        if (d <= 10) {
            return true;
        }
        return false;

    }


    // 计算某点到圆点的距离
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    private String getTime(int progress){
        int minute = progress/60;
        int second = progress%60;
        String result = (minute<10?"0":"")+minute+":"+(second<10?"0":"")+second;
        return result;



    }




    private String getTimeText(int progress, int progress1) {
        int minute = progress / 60;
        int second = progress % 60;
        int minute1 = progress1 / 60;
        int second1 = progress1 % 60;
        String result;
        if (minute >= minute1) {
            result = (minute - minute1 < 10 ? "0" : "") + (minute - minute1) + ":";
            if (second < second1) {
                result += (second + (60 - second1)) / 10 + "" + ((second + (60 - second1)) % 10 < 5 ? "0" : "5");
            } else {
                result += (second - second1) / 10 + "" + ((second - second1) % 10 < 5 ? "0" : "5");
            }

        } else {
            result = (minute1 - minute < 10 ? "0" : "") + (minute1 - minute) + ":";
            if (second1 < second) {
                result += (second1 + (60 - second)) / 10 + "" + ((second1 + (60 - second)) % 10 < 5 ? "0" : "5");
            } else {
                result += (second1 - second) / 10 + "" + ((second1 - second) % 10 < 5 ? "0" : "5");
            }
        }





        return result;
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progressStart;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progressStart not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progressStart = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public static class ChartUtil {

        /**
         * According to the coordinates of the center, the radius and the fan Angle,
         * the x and y coordinates of the end ray and the arc intersection are calculated.
         *
         * @param cirX
         * @param cirY
         * @param radius
         * @param cirAngle
         * @return
         */
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle) {
            float posX = 0.0f;
            float posY = 0.0f;
            //将角度转换为弧度
            float arcAngle = (float) (Math.PI * cirAngle / 180.0);
            if (cirAngle < 90) {
                posX = cirX + (float) (Math.cos(arcAngle)) * radius;
                posY = cirY + (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 90) {
                posX = cirX;
                posY = cirY + radius;
            } else if (cirAngle > 90 && cirAngle < 180) {
                arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
                posX = cirX - (float) (Math.cos(arcAngle)) * radius;
                posY = cirY + (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 180) {
                posX = cirX - radius;
                posY = cirY;
            } else if (cirAngle > 180 && cirAngle < 270) {
                arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
                posX = cirX - (float) (Math.cos(arcAngle)) * radius;
                posY = cirY - (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 270) {
                posX = cirX;
                posY = cirY - radius;
            } else {
                arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
                posX = cirX + (float) (Math.cos(arcAngle)) * radius;
                posY = cirY - (float) (Math.sin(arcAngle)) * radius;
            }
            return new PointF(posX, posY);
        }

        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle) {
            cirAngle = (orginAngle + cirAngle) % 360;
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
        }
    }

    private OnProgressChangeListener changeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);

        void onProgressChangeEnd(int duration, int progress);
    }

    public interface OnTimeChangeListener{
        void onStartTimeChange(int a);
    }

    public Callback timeListener;


    public interface Callback {
        Handler execute();
    }

    public void Test(Callback callback) {

        handler=callback.execute();//进行回调
        System.out.println(handler==null);




    }



}
