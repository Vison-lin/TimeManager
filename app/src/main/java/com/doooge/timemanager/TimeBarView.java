package com.doooge.timemanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 2018/1/21.
 */

public class TimeBarView extends View {
    /**
     * instance of handler
     */
    private static Handler handler;
    /**
     * instance of colorType
     */
    private static Type colorType;
    /**
     * The current process of start thumb
     */
    private static int progressStart = 0;
    /**
     * The current process of end thumb
     */
    private static int progressEnd = 0;
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
     * The color of the string of intermediate progress percentage.
     */
    private int textColor;
    /**
     * The font of the middle progress percentage string.
     */
    private float textSize;
    /**
     * The start thumb
     */
    private Drawable thumbStart, thumbStartPress;
    private Bitmap thumbS;
    /**
     * The end thumb
     */
    private Drawable thumbEnd, thumbEndPress;
    private Bitmap thumbE;
    /**
     * record the X,Y coordinate of current place of thumb
     */
    private Point startPoint;
    private Point endPoint;
    /**
     * the boolean type of check if touch on screen
     */
    private boolean downOnStart = false;
    private boolean downOnEnd = false;
    /**
     * the coordinate of centre of circle
     */
    private int centerX, centerY;
    /**
     * the radius of circle
     */
    private int radius;
    private int paddingOuterThumb;

    /**
     * for improving
     */
    private Message message;
    private RectF oval;
    private PointF progressStartPoint;
    private PointF progressEndPoint;
    private String textTime;
    private LocalDatabaseHelper ldh;
    /**
     * create the constructor of this context
     *
     * @param context
     */
    public TimeBarView(Context context) {
        this(context, null);
    }
//    public TimeBarView(Context context,int progressStart,int progressEnd) {
//        this(context, null);
//        paint = new Paint();
//        textTime = getTimeText(progressStart, progressEnd);
//        TimeBarView.progressStart = progressStart;
//        TimeBarView.progressEnd = progressEnd;
//        runHandler();
//    }

    public TimeBarView(Context context, int progressStart, int progressEnd, Type type) {
        super(context, null, 0);
        textTime = getTimeText(progressStart, progressEnd);
        TimeBarView.progressStart = progressStart;
        TimeBarView.progressEnd = progressEnd;
        TimeBarView.colorType = type;
    }


    public TimeBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        startPoint = new Point(0, 0);
        endPoint = new Point(0, 0);
        progressStart  =0;
        progressEnd  = 720;
        colorType = null;
        textTime = getTimeText(progressStart, progressEnd);
        ldh = LocalDatabaseHelper.getInstance(context);
        Type def = ldh.findTypeByPrimaryKey(-999);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //Gets the customer property and default values.
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, getResources().getColor(R.color.gray));
        roundProgressColor = Integer.parseInt(def.getColor());
        // roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, getResources().getColor(R.color.btn_bkgd_def));
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 70);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLACK);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize_round, 60);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_imageMax, 1440);
        mTypedArray.recycle();

        // Loading the picture of thumb
        thumbStart = getResources().getDrawable(R.drawable.a1);
        Bitmap bitmap = ((BitmapDrawable) thumbStart).getBitmap();
        // Scale it to 50 x 50
        thumbStart = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        //thumbS = BitmapFactory.decodeResource(getResources(), R.drawable.a1);
        int thumbHalfheight = thumbStart.getIntrinsicHeight() / 2;
        int thumbHalfWidth = thumbStart.getIntrinsicWidth() / 2;
        //thumbStart.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        int left = (int)(25-roundWidth);
        int top = (int)(25-roundWidth);
        int right = (int)(roundWidth-15);
        int bottom = (int)(roundWidth-15);
        thumbStart.setBounds(left, top, -left, -top);


        thumbStartPress = getResources().getDrawable(R.drawable.a2);
        bitmap = ((BitmapDrawable) thumbStartPress).getBitmap();
        // Scale it to 50 x 50
        thumbStartPress = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        thumbHalfheight = thumbStartPress.getIntrinsicHeight() / 2;
        thumbHalfWidth = thumbStartPress.getIntrinsicWidth() / 2;
        thumbStartPress.setBounds(left, top, -left, -top);
        paddingOuterThumb = thumbHalfheight;

        thumbEnd = getResources().getDrawable(R.drawable.a1);
        bitmap = ((BitmapDrawable) thumbEnd).getBitmap();
        // Scale it to 50 x 50
        thumbEnd = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        //thumbE = BitmapFactory.decodeResource(getResources(), R.drawable.a1);
        int thumbHalfheight1 = thumbEnd.getIntrinsicHeight() / 2;
        int thumbHalfWidth1 = thumbEnd.getIntrinsicWidth() / 2;
        thumbEnd.setBounds(left, top, -left, -top);

        thumbEndPress = getResources().getDrawable(R.drawable.a2);
        bitmap = ((BitmapDrawable) thumbEndPress).getBitmap();
        // Scale it to 50 x 50
        thumbEndPress = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        thumbHalfheight1 = thumbEndPress.getIntrinsicHeight() / 2;
        thumbHalfWidth1 = thumbEndPress.getIntrinsicWidth() / 2;
        thumbEndPress.setBounds(left, top, -left, -top);



    }

    @Override
    public void onDraw(Canvas canvas) {
        System.out.println("paingting");
        if (colorType != null) {
            roundProgressColor = Integer.parseInt(colorType.getColor());
        }
        /**
         * draw the circle
         */
        //setLayerType(LAYER_TYPE_SOFTWARE, null);
        //LinearGradient shader = new LinearGradient(0, 0, 800, 800, Color.BLACK, Color.BLACK, Shader.TileMode.REPEAT);
        //paint.setShader(shader);
        // paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER));
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawCircle(centerX, centerY, radius, paint);
//        paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.blue));
//        canvas.drawCircle(centerX, centerY, radius - roundWidth + 40, paint);

        /**
         * draw the text of time
         */
        paint = new Paint();
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        float textWidth = paint.measureText(textTime);
        canvas.drawText(textTime, centerX - textWidth / 2, centerY + textSize / 2, paint);


        /**
         * draw arc and the process bar
         */
        paint = new Paint();
        paint.setMaskFilter(new BlurMaskFilter(60, BlurMaskFilter.Blur.INNER));
        paint.setStrokeWidth(roundWidth);
        paint.setColor(roundProgressColor);
        oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        paint.setStyle(Paint.Style.STROKE);
        if (progressStart > progressEnd) {

            canvas.drawArc(oval, -360 * (1440-progressStart) / max + 270, 360 * (1440-progressStart + progressEnd) / max, false, paint);
        } else if (progressStart < progressEnd) {
            canvas.drawArc(oval, 360 * progressStart / max + 270, 360 * (progressEnd - progressStart) / max, false, paint);
        }
        if(getTime(progressStart).equals(getTime(progressEnd))){
            canvas.drawArc(oval, 360 * progressStart / max + 270, 360 , false, paint);
        }
        progressStartPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progressStart / max, 270);
        progressEndPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progressEnd / max, 270);


        // draw thumb
        canvas.save();

        startPoint.setX(progressStartPoint.x);
        startPoint.setY(progressStartPoint.y);
        endPoint.setX(progressEndPoint.x);
        endPoint.setY(progressEndPoint.y);

        if (downOnStart && !downOnEnd) {
            canvas.translate(endPoint.getX(), endPoint.getY());
            thumbEnd.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(startPoint.getX(), startPoint.getY());
            thumbStartPress.draw(canvas);

        } else if (downOnEnd && !downOnStart) {
            canvas.translate(endPoint.getX(), endPoint.getY());
            thumbEndPress.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(startPoint.getX(), startPoint.getY());
            thumbStart.draw(canvas);


        } else {
            canvas.translate(endPoint.getX(), endPoint.getY());
            thumbEnd.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(startPoint.getX(), startPoint.getY());
            thumbStart.draw(canvas);
        }
        runHandler();
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                textTime = getTimeText(progressStart, progressEnd);

                if (isTouchPot(x, y)) {
                    downOnStart = true;
                    updateArc(x, y, true);
                    return true;
                } else if (isTouchPot1(x, y)) {
                    downOnEnd = true;
                    updateArc(x, y, false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                textTime = getTimeText(progressStart, progressEnd);

                if (downOnStart) {
                    updateArc(x, y, true);
                    return true;
                } else if (downOnEnd) {
                    updateArc(x, y, false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                textTime = getTimeText(progressStart, progressEnd);
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                downOnStart = false;
                downOnEnd = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);

        radius = (int) (minCenter - roundWidth / 2 - paddingOuterThumb);

        super.onSizeChanged(width, height, oldw, oldh);
    }

    // update the process according to the coordinate of touch point
    private void updateArc(int x, int y, boolean isStart) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;

        double angle = Math.atan2(cy, cx) / Math.PI;

        angle = ((2 + angle) % 2 + (90 / 180f)) % 2;

        if (isStart) {

            progressStart = (int) (angle * max / 2);
//            if (changeListener != null) {
//                changeListener.onProgressChange(max, progressStart);
//            }
//            if (timeListener != null) {
//                timeListener.execute();
//            }

        } else {
            progressEnd = (int) (angle * max / 2);

//            if (changeListener != null) {
//                changeListener.onProgressChange(max, progressEnd);
//            }
//            if (timeListener != null) {
//                timeListener.execute();
//            }


        }
        invalidate();
    }


//    public void update() {
//
//        roundProgressColor = getResources().getColor(R.color.btn_bkgd_purple);
//        invalidate();
//
//    }


    /**
     * To check if touch on the start thumb
     *
     * @param x the X coordinate of touch on view
     * @param y the Y coordinate of touch on view
     * @return whether touch success.
     */
    private boolean isTouchPot(int x, int y) {
        System.out.println("startX: " + startPoint.getX());
        System.out.println("startY: " + startPoint.getY());
        System.out.println("x :" + x);
        System.out.println("y: " + y);
        int X = (int) startPoint.getX() - x;
        System.out.println("X: " + X);
        int Y = (int) startPoint.getY() - y;
        System.out.println("Y: " + Y);
        System.out.println("XY: " + (X * X + Y * Y));
        Double d = Math.sqrt(X * X + Y * Y);
        System.out.println("d: " + d);
        return d <= 60;

    }

    /**
     * To check if touch on the end thumb
     *
     * @param x the X coordinate of touch on view
     * @param y the Y coordinate of touch on view
     * @return whether touch success.
     */
    private boolean isTouchPot1(int x, int y) {
        int X = (int) endPoint.getX() - x;
        int Y = (int) endPoint.getY() - y;
        double d = Math.sqrt(X * X + Y * Y);
        return d <= 60;

    }


    /**
     * To caculate the time with the current process
     *
     * @param process the current process
     * @return
     */
    private String getTime(int process) {
        int minute = process / 60;
        int second = process % 60;
        String result;

        result = (minute < 10 ? "0" : "") + minute + ":" + second / 10 + "" + (second % 10 < 5 ? "0" : "5");
        return result;

    }

    private boolean isOverDay(int processStart, int processEnd) {
        if(getTime(processStart).equals(getTime(processEnd))){
            return true;
        }
        return processStart > processEnd;
    }





    /**
     * To caculate the time difference between start time and end time.
     *
     * @param processStart the current process of start point
     * @param processEnd   the current process of end point
     * @return
     */


    private String getTimeText(int processStart, int processEnd) {
        int minute = processStart / 60;
        int second = Integer.parseInt((processStart % 60)/10+""+((processStart%60)%10<5?"0":"5"));

        int minute1 = processEnd / 60;
        int second1 = Integer.parseInt((processEnd% 60)/10+""+((processEnd%60)%10<5?"0":"5"));

        String result;

        if (minute > minute1) {
            if(second<second1) {
                result = (24 - minute + minute1 < 10 ? "0" : "") + (24 - minute + minute1) + ":";
                result += (second1 - second) < 10 ? "0" + (second1 - second) : second1 - second;
            }else if(second>second1){
                result = (23 - minute + minute1 < 10 ? "0" : "") + (23 - minute + minute1) + ":";
                result += (60+second1-second)<10 ?"0"+(60+second1-second):60+second1-second;
            }else{
                result = (24 - minute + minute1 < 10 ? "0" : "") + (24 - minute + minute1) + ":"+"00";
            }

        } else if (minute < minute1) {


            if (second1 < second) {
                result = (minute1 - minute-1 < 10 ? "0" : "") + (minute1 - minute-1) + ":";
                result += (60+second1-second)<10 ?"0"+(60+second1-second):60+second1-second;
            } else {
                result = (minute1 - minute < 10 ? "0" : "") + (minute1 - minute) + ":";
                result += (second1-second)<10 ?"0"+(second1-second):second1-second;
            }
        } else {
            if (second > second1) {
                result = "23:" + ((60+second1-second)<10 ?"0"+(60+second1-second):60+second1-second);
            } else if (second < second1) {
                result = "00:" + ((second1-second)<10 ?"0"+(second1-second):second1-second);
            } else {
                result = "24:00";

            }
        }


        return result;
    }


//    private OnProgressChangeListener changeListener;
//
//    public interface OnProgressChangeListener {
//        void onProgressChange(int duration, int progress);
//
//        void onProgressChangeEnd(int duration, int progress);
//    }

//    public Callback timeListener;

    public void runHandler() {
        if (handler != null) {
            String result = getTime(progressStart) + "@" + getTime(progressEnd) + "@" + isOverDay(progressStart, progressEnd) + "@" + progressStart + "@" + progressEnd;
            message = handler.obtainMessage();
            message.what = 0;
            message.obj = result;
            handler.sendMessage(message);
        }


    }


    public void Test(Callback callback) {
        handler = callback.execute();

    }

    /**
     * Those methods are used for achieving data call-back between this view and SpecificTaskCreator
     * activity
     */
    public interface Callback {
        Handler execute();
    }

    private static class ChartUtil {

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
        private static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle) {
            float posX = 0.0f;
            float posY = 0.0f;
            //Convert angles to radians
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

        private static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle) {
            cirAngle = (orginAngle + cirAngle) % 360;
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
        }
    }

    private class Point {
        private float x;
        private float y;

        private Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        private float getX() {
            return x;
        }

        private void setX(float x) {
            this.x = x;
        }

        private float getY() {
            return y;
        }

        private void setY(float y) {
            this.y = y;
        }

    }


}
