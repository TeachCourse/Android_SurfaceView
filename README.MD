SurfaceView继承自View，对View进行优化，适合在子线程频繁地刷新界面，并实现了双缓存机制

`SurfaceHolder.Callback`提供管理SurfaceView生命周期的三个方法：`surfaceCreated()`、`surfaceChanged()`、`surfaceDestroyed()`，在相应的回调方法中处理操作

*Note that：*不用画布，直接在窗口上进行绘图叫做无缓冲绘图。用了一个画布，将所有内容都先画到画布上，在整体绘制到窗口上，就该叫做单缓冲绘图，那个画布就是一个缓冲区。用了一个画布和一个Bitmap，画布保存临时的绘图，Bitmap保存绘图的历史记录，这样就叫做双缓冲绘图。

```java
public class DrawingRectangleBoard extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean isDrawing;
    Paint mPaint;
    Path mPath;
    float firstX;
    float firstY;

    public DrawingRectangleBoard(Context context) {
        this(context, null);
    }

    public DrawingRectangleBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(6f);
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        Log.e("surfaceCreated", "--" + isDrawing);

        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        Log.e("surfaceDestroyed", "--" + isDrawing);
    }

    @Override
    public void run() {
        while (isDrawing) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                if (mCanvas != null) {
                    mCanvas.drawColor(Color.WHITE);
                    mCanvas.drawPath(mPath, mPaint);
                }
            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = x;
                firstY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //绘制矩形时，要先清除前一次的结果
                mPath.reset();
                if (firstX < x && firstY < y) {
                    //↘方向
                    mPath.addRect(firstX, firstY, x, y, Path.Direction.CCW);
                } else if (firstX > x && firstY > y) {
                    //↖方向
                    mPath.addRect(x, y, firstX, firstY, Path.Direction.CCW);
                } else if (firstX > x && firstY < y) {
                    //↙方向
                    mPath.addRect(x, firstY, firstX, y, Path.Direction.CCW);
                } else if (firstX < x && firstY > y) {
                    //↗方向
                    mPath.addRect(firstX, y, x, firstY, Path.Direction.CCW);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }
}
```

[例子源码1](/app/src/main/java/cn/teachcourse/)

[例子源码2](https://github.com/TeachCourse/BlogSource/blob/master/app/src/main/java/cn/teachcourse/app/VideoMainActivity.java)
