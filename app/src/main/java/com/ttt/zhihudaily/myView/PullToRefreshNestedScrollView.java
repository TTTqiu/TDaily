package com.ttt.zhihudaily.myView;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PullToRefreshNestedScrollView extends PullToRefreshBase<NestedScrollView> {

    private int[] outLocation=new int[2];
    private int startLocation;
    private Context context;

    public PullToRefreshNestedScrollView(Context context) {
        super(context);
        this.context=context;
    }

    public PullToRefreshNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode) {
        super(context, mode);
        this.context=context;
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
        this.context=context;
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
        getRefreshableView().getLocationOnScreen(outLocation);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=pref.edit();
        startLocation=pref.getInt("PTRStartLocation",0);
        if (startLocation==0){
            editor.putInt("PTRStartLocation",outLocation[1]);
            editor.apply();
            startLocation=outLocation[1];
        }
        return outLocation[1]== startLocation&&getRefreshableView().getScrollY()==0;
    }
}
