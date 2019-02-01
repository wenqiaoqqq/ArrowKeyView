package com.wqiao.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    public static final String TAG = ArrowKeyView.class.getName();

    public static final int DIR_UNDEFINE = 0x100;
    public static final int DIR_LEFT = 0x101;//点击左边
    public static final int DIR_UP = 0x102;//点击上边
    public static final int DIR_RIGHT = 0x103;//点击右边
    public static final int DIR_DOWN = 0x104;//点击下边

    //当前点击的方向
    private int dir = DIR_UNDEFINE;

    private final static int PART_ONE = 1;
    private final static int PART_TWO = 2;
    private final static int PART_THREE = 3;
    private final static int PART_FOUR = 4;

    private final static int START_DEGREE = -135;

    private int center = 0;
    private int innerRadius = 0;

    //外环颜色(大圆环和小圆环)
    private int outerRingColor = Color.rgb(136, 203, 246);
    //内环颜色(最里面的圆环)
    private int innerRingColor = Color.rgb(207, 234, 252);
    //背景颜色
    private int backgroundColor = Color.rgb(255, 255, 255);

    //外环宽度
    private int outerRingWidth;
    //内环宽度
    private int innerRingWidth;

    private int DEFUALT_VIEW_WIDTH;
    private int DEFUALT_VIEW_HEIGHT;

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
        DEFUALT_VIEW_WIDTH = DEFUALT_VIEW_HEIGHT = DensityUtils.dp2px(context, 180);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowKeyView, defStyleAttr, 0);
        outerRingColor = a.getColor(R.styleable.ArrowKeyView_outerRingColor, Color.rgb(136, 203, 246));
        outerRingWidth = a.getDimensionPixelSize(R.styleable.ArrowKeyView_outerRingWidth, DensityUtils.dp2px(context, 2));
        innerRingColor = a.getColor(R.styleable.ArrowKeyView_innerRingColor, Color.rgb(207, 234, 252));
        innerRingWidth = a.getDimensionPixelSize(R.styleable.ArrowKeyView_innerRingWidth, outerRingWidth / 2);
        ptzLeft = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.ArrowKeyView_leftArrowIcon, R.mipmap.lechange_ptz_left));
        ptzUp = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.ArrowKeyView_upArrowIcon, R.mipmap.lechange_ptz_up));
        ptzRight = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.ArrowKeyView_rightArrowIcon, R.mipmap.lechange_ptz_right));
        ptzDown = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.ArrowKeyView_downArrowIcon, R.mipmap.lechange_ptz_down));

        a.recycle();

        init();
    }

    /**
     * 初始化绘制弧形所在矩形的四点坐标
     **/
    private void init(){
        outerCirclepaint.setStyle(Paint.Style.STROKE);
        outerCirclepaint.setAntiAlias(true);
        outerCirclepaint.setColor(outerRingColor);
        outerCirclepaint.setStrokeWidth(outerRingWidth);

        paint.setAntiAlias(true);

        linepaint.setStyle(Paint.Style.FILL);
        linepaint.setAntiAlias(true);
        linepaint.setColor(innerRingColor);
        linepaint.setStrokeWidth(DensityUtils.dp2px(context, 1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureDimension(DEFUALT_VIEW_WIDTH,widthMeasureSpec);
        int height = measureDimension(DEFUALT_VIEW_HEIGHT,heightMeasureSpec);
        //将计算的宽和高设置进去，保存，最后一步一定要有
        setMeasuredDimension(width,height);

        center = width / 2;
        innerRadius = center / 3;
        mRectF.left = outerRingWidth;
        mRectF.top = outerRingWidth;
        mRectF.right = 2 * center - outerRingWidth;
        mRectF.bottom = 2 * center - outerRingWidth;
    }

    /**
     * @param defualtSize   设置的默认大小
     * @param measureSpec   父控件传来的widthMeasureSpec，heightMeasureSpec
     * @return  结果
     */
    public int measureDimension(int defualtSize,int measureSpec){
        int result = defualtSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //1,layout中自定义组件给出来确定的值，比如100dp
        //2,layout中自定义组件使用的是match_parent，但父控件的size已经可以确定了，比如设置的具体的值或者match_parent
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        //layout中自定义组件使用的wrap_content
        else if(specMode == MeasureSpec.AT_MOST){
            result = Math.min(defualtSize,specSize);//建议：result不能大于specSize
        }
        //UNSPECIFIED,没有任何限制，所以可以设置任何大小
        else {
            result = defualtSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画最外面的蓝色圆环
        canvas.drawCircle(center, center, center - outerRingWidth, outerCirclepaint);
        //画两条分割线
        canvas.drawLine((float)((1 - Math.sqrt(2) / 2) * center + outerRingWidth), (float)((1 - Math.sqrt(2) / 2) * center + outerRingWidth),
                (float)((1 + Math.sqrt(2) / 2) * center - outerRingWidth), (float)((1 + Math.sqrt(2) / 2) * center - outerRingWidth), linepaint);
        canvas.drawLine((float)((1 - Math.sqrt(2) / 2) * center + outerRingWidth), (float)((1 + Math.sqrt(2) / 2) * center - outerRingWidth),
                (float)((1 + Math.sqrt(2) / 2) * center - outerRingWidth), (float)((1 - Math.sqrt(2) / 2) * center + outerRingWidth), linepaint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(outerRingColor);
        paint.setStrokeWidth(outerRingWidth);

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
        paint.setColor(outerRingColor);
        paint.setStrokeWidth(outerRingWidth);
        canvas.drawCircle(center, center, innerRadius, paint);

        //画里面的灰色小圆
        paint.setColor(innerRingColor);
        paint.setStrokeWidth(innerRingWidth);
        canvas.drawCircle(center, center, innerRadius * 2 /3, paint);

        canvas.drawBitmap(ptzLeft, center / 4, center - ptzLeft.getHeight()/2, paint);
        canvas.drawBitmap(ptzUp, center - ptzUp.getWidth()/2, center / 4, paint);
        canvas.drawBitmap(ptzRight, 7 * center / 4 - ptzRight.getWidth(), center - ptzRight.getHeight()/2, paint);
        canvas.drawBitmap(ptzDown, center - ptzDown.getWidth()/2, 7 * center / 4 - ptzDown.getHeight(), paint);

        paint.setXfermode(null);
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
                            Log.i(TAG, "onTouchEvent--->PART_ONE");

                            alfa = Math.atan2(eventX - center, center - eventY) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i(TAG, "onTouchEvent--->PART_ONE--->DIR_UP");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickUp();
                                }
                                dir = DIR_UP;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i(TAG, "onTouchEvent--->PART_ONE--->DIR_RIGHT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickRight();
                                }
                                dir = DIR_RIGHT;
                            }
                            break;
                        case PART_TWO:
                            Log.i(TAG, "onTouchEvent--->PART_TWO");

                            alfa = Math.atan2(eventY - center, eventX - center) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i(TAG, "onTouchEvent--->PART_TWO--->DIR_RIGHT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickRight();
                                }
                                dir = DIR_RIGHT;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i(TAG, "onTouchEvent--->PART_TWO--->DIR_DOWN");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickDown();
                                }
                                dir = DIR_DOWN;
                            }
                            break;
                        case PART_THREE:
                            Log.i(TAG, "onTouchEvent--->PART_THREE");

                            alfa = Math.atan2(center - eventX, eventY - center) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i(TAG, "onTouchEvent--->PART_THREE--->DIR_DOWN");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickDown();
                                }
                                dir = DIR_DOWN;
                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i(TAG, "onTouchEvent--->PART_THREE--->DIR_LEFT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickLeft();
                                }
                                dir = DIR_LEFT;
                            }
                            break;
                        case PART_FOUR:
                            Log.i(TAG, "onTouchEvent--->PART_FOUR");

                            alfa = Math.atan2(center - eventY, center - eventX) * 180 / Math.PI;

                            if( alfa > 0 && alfa < 45 ){
                                Log.i(TAG, "onTouchEvent--->PART_FOUR--->DIR_LEFT");

                                if( onclickPtzListener != null ){
                                    onclickPtzListener.clickLeft();
                                }
                                dir = DIR_LEFT;

                            }
                            else if( alfa >= 45 && alfa < 90 ){
                                Log.i(TAG, "onTouchEvent--->PART_FOUR--->DIR_UP");

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
