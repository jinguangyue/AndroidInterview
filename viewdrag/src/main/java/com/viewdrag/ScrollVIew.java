package com.viewdrag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

public class ScrollVIew extends AppCompatImageView {

    private Context context;
    private Scroller scroller;

    public ScrollVIew(Context context) {
        super(context);
        this.context = context;
        scroller = new Scroller(context);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);

    }

    public ScrollVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
    }

    public ScrollVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        scroller = new Scroller(context);
    }


    public void smoothScrollTo(int destX, int dextY){
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;

        scroller.startScroll(scrollX, 0, deltaX, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
