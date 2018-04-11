package cn.teachcourse;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Created by http://teachcourse.cn on 2018/4/11.
 */

public class DrawingLineBoard extends DrawingRectangleBoard {
    public DrawingLineBoard(Context context) {
        super(context);
    }

    public DrawingLineBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                firstX = x;
                firstY = y;
                mPath.moveTo(firstX, firstY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - firstX);
                float dy = Math.abs(y - firstY);
                if (dx >= 3 || dy >= 3) {
                    mPath.quadTo(firstX, firstY, (firstX + x) / 2, (firstY + y) / 2);
                }
                firstX = x;
                firstY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
