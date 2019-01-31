package com.wqiao.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wqiao.view.util.DensityUtils;

/**
 * Created by 909940 on 2017/9/5.
 */

public class ArrowKeyView extends View {

    public static final int DIR_UNDEFINE = 0x100;
    public static final int DIR_LEFT = 0x101;//点击左边
    public static final int DIR_UP = 0x102;//点击上边
    public static final int DIR_RIGHT = 0x103;//点击右边
    public static final int DIR_DOWN = 0x104;//点击下边

    private final static int PART_ONE = 1;
    private final static int PART_TWO = 2;
    private final static int PART_THREE = 3;
    private final static int PART_FOUR = 4;

    private final static int START_DEGREE = -135;

    private int blueCircleWidth = 0;//蓝色圆环的宽度

    private int center = 0;
    private int innerRadius = 0;

    private int dir = DIR_UNDEFINE;

    private int blueCircleColor = Color.rgb(136, 203, 246);
    private int grayCircleColor = Color.rgb(207, 234, 252);
    private int backgroundColor = Color.rgb(255, 255, 255);

    private Paint outerCirclepaint = new Paint();
    private Paint paint = new Paint();
    private Paint innerCiclepaint = new Paint();
    private Paint linepaint = new Paint();
    private Paint bitmapPaint = new Paint();

    private RectF mRectF = new RectF();

    private Bitmap ptzLeft;
    private Bitmap ptzUp;
    private Bitmap ptzRight;
    private Bitmap ptzDown;

    private Context context;

    public ArrowKeyView(Context context) {
        this(context, null);
        init();
    }

    public ArrowKeyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public ArrowKeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowKeyView, defStyleAttr, 0);
    }

    /**
     * 初始化绘制弧形所在矩形的四点坐标
     **/
    private void init(){
        blueCircleWidth = DensityUtils.dp2px(context, 2);
        center = DensityUtils.dp2px(context, 90);
        innerRadius = center / 3;

        outerCirclepaint.setStyle(Paint.Style.STROKE);
        outerCirclepaint.setAntiAlias(true);
        outerCirclepaint.setColor(blueCircleColor);
        outerCirclepaint.setStrokeWidth(blueCircleWidth);

        paint.setAntiAlias(true);

        linepaint.setStyle(Paint.Style.FILL);
        linepaint.setAntiAlias(true);
        linepaint.setColor(grayCircleColor);
        linepaint.setStrokeWidth(DensityUtils.dp2px(context, 1));

        mRectF.left = blueCircleWidth;
        mRectF.top = blueCircleWidth;
        mRectF.right = 2 * center - blueCircleWidth;
        mRectF.bottom = 2 * center - blueCircleWidth;

        ptzLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.lechange_ptz_left);
        ptzUp = BitmapFactory.decodeResource(getResources(), R.mipmap.lechange_ptz_up);
        ptzRight = BitmapFactory.decodeResource(getResources(), R.mipmap.lechange_ptz_right);
        ptzDown = BitmapFactory.decodeResource(getResources(), R.mipmap.lechange_ptz_down);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画最外面的蓝色圆环
        canvas.drawCircle(center, center, center - blueCircleWidth, outerCirclepaint);
        //画两条分割线
        canvas.drawLine((float)((1 - Math.sqrt(2) / 2) * center + blueCircleWidth), (float)((1 - Math.sqrt(2) / 2) * center + blueCircleWidth),
                (float)((1 + Math.sqrt(2) / 2) * center - blueCircleWidth), (float)((1 + Math.sqrt(2) / 2) * center - blueCircleWidth), linepaint);
        canvas.drawLine((float)((1 - Math.sqrt(2) / 2) * center + blueCircleWidth), (float)((1 + Math.sqrt(2) / 2) * center - blueCircleWidth),
                (float)((1 + Math.sqrt(2) / 2) * center - blueCircleWidth), (float)((1 - Math.sqrt(2) / 2) * center + blueCircleWidth), linepaint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(blueCircleColor);
        paint.setStrokeWidth(blueCircleWidth);

        switch (dir){
            case DIR_LEFT:
                //画被选中的区域
                canvas.drawArc(mRectF, START_DEGREE + 270, 90, true, paint);
                break;
            case DIR_UP:
                //画被选中的区域
                canvas.drawArc(mRectF, START_DEGREE, 90, true, paint);
                break;
            case DIR_RIGHT:
                //画被选中的区域
                canvas.drawArc(mRectF, START_DEGREE + 90, 90, true, paint);
                break;
            case DIR_DOWN:
                //画被选中的区域
                canvas.drawArc(mRectF, START_DEGREE + 180, 90, true, paint);
                break;

            case DIR_UNDEFINE:
                //未点击状态
//                paint.setColor(backgroundColor);
//                canvas.drawCircle(center, center, center - blueCircleWidth, paint);
                break;
        }

        paint.setColor(backgroundColor);

        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
        paint.setXfermode(xfermode);

        //先画实心的白色圆
        canvas.drawCircle(center, center, innerRadius, paint);

        //再画蓝色的圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(blueCircleColor);
        paint.setStrokeWidth(blueCircleWidth);
        canvas.drawCircle(center, center, innerRadius, paint);

        //画里面的灰色小圆
        paint.setColor(grayCircleColor);
        paint.setStrokeWidth(blueCircleWidth / 2);
        canvas.drawCircle(center, center, innerRadius - 20, paint);

        canvas.drawBitmap(ptzLeft, center / 4, center - ptzLeft.getHeight()/2, paint);
        canvas.drawBitmap(ptzUp, center - ptzUp.getWidth()/2, center / 4, paint);
        canvas.drawBitmap(ptzRight, 7 * center / 4 - ptzRight.getWidth(), center - ptzRight.getHeight()/2, paint);
        canvas.drawBitmap(ptzDown, center - ptzDown.getWidth()/2, 7 * center / 4 - ptzDown.getHeight(), paint);

        paint.setXfermode(null);
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        super.setOnLongClickListener(l);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float eventX = event.getX();
                float eventY = event.getY();

                double alfa = 0;
                //点击的位置到圆心距离的平方
                double distance = Math.pow(eventX - center, 2) + Math.pow(eventY - center, 2);
                if( Math.sqrt(distance) < center && Math.sqrt(distance) > innerRadius ){
                    //点击范围在可点击圆环内
                    int which = touchOnWhichPart(event);
                    switch (which) {
                        case PART_ONE:
                            Log.i("wenqiao", "onTouchEvent--->PART_ONE");

                            alfa = Math.atan2(eventX - center, center - eventY) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_ONE--->DIR_UP");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickUp();
                                }
                                dir = DIR_UP;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_ONE--->DIR_RIGHT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickRight();
                                }
                                dir = DIR_RIGHT;
                            }
                            break;
                        case PART_TWO:
                            Log.i("wenqiao", "onTouchEvent--->PART_TWO");

                            alfa = Math.atan2(eventY - center, eventX - center) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_TWO--->DIR_RIGHT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickRight();
                                }
                                dir = DIR_RIGHT;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_TWO--->DIR_DOWN");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickDown();
                                }
                                dir = DIR_DOWN;
                            }
                            break;
                        case PART_THREE:
                            Log.i("wenqiao", "onTouchEvent--->PART_THREE");

                            alfa = Math.atan2(center - eventX, eventY - center) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_THREE--->DIR_DOWN");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickDown();
                                }
                                dir = DIR_DOWN;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_THREE--->DIR_LEFT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickLeft();
                                }
                                dir = DIR_LEFT;
                            }
                            break;
                        case PART_FOUR:
                            Log.i("wenqiao", "onTouchEvent--->PART_FOUR");

                            alfa = Math.atan2(center - eventY, center - eventX) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_FOUR--->DIR_LEFT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickLeft();
                                }
                                dir = DIR_LEFT;

                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i("wenqiao", "onTouchEvent--->PART_FOUR--->DIR_UP");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickUp();
                                }
                                dir = DIR_UP;
                            }
                            break;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                dir = DIR_UNDEFINE;
                break;
        }

        invalidate();

//        return super.onTouchEvent(event);
        return true;
    }

    /**
     *    4 |  1
     * -----|-----
     *    3 |  2
     * 圆被分成四等份，判断点击在园的哪一部分
     */
    private int touchOnWhichPart(MotionEvent event) {
        if (event.getX() > center) {
            if (event.getY() > center) return PART_TWO;
            else return PART_ONE;
        } else {
            if (event.getY() > center) return PART_THREE;
            else return PART_FOUR;
        }
    }

    public interface OnclickPtzListener{
        void clickLeft();
        void clickUp();
        void clickRight();
        void clickDown();
    }

    private OnclickPtzListener onclickPtzListener;

    public void setOnclickPtzListener(OnclickPtzListener onclickPtzListener){
        this.onclickPtzListener = onclickPtzListener;
    }
}
