package com.ttt.zhihudaily.myView;


import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PullToRefreshNestedScrollView extends PullToRefreshBase<NestedScrollView> {

    private int[] outLocation=new int[2];
    private int startLocation=0;

    public PullToRefreshNestedScrollView(Context context) {
        super(context);
    }

    public PullToRefreshNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected NestedScrollView createRefreshableView(Context context, AttributeSet attrs) {
        return new MyNestedScrollView(context, attrs);
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        getRefreshableView().getLocationInWindow(outLocation);
        if (startLocation==0){
            startLocation=outLocation[1];
        }
        return outLocation[1]== startLocation&&getRefreshableView().getScrollY()==0;
    }
}
