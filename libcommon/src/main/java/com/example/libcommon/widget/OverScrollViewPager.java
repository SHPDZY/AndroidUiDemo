package com.example.libcommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.List;

public class OverScrollViewPager extends NoScrollViewPager {

    private static final String TAG = "OverScrollViewPager";

    private static final boolean DEBUG = false;

    private GestureDetectorCompat mDetector;
    private List<OnPageOverScrollListener> mOnPageOverScrollListeners;
    private OnPageOverScrollListener mOnPageOverScrollListener;
    private STATE mState = STATE.IDLE;

    public OverScrollViewPager(Context context) {
        super(context, null);
        this.init(context);
    }

    public OverScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        mDetector = new GestureDetectorCompat(context, new GestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_UP) {
            if (mState != STATE.IDLE) {
                dispatchOnPageOverScrolled(mState);
                mState = STATE.IDLE;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void addOnPageOverScrollListener(OnPageOverScrollListener listener) {
        if (mOnPageOverScrollListeners == null) {
            mOnPageOverScrollListeners = new ArrayList<>();
        }
        mOnPageOverScrollListeners.add(listener);
    }

    public void removeOnPageOverScrollListener(OnPageOverScrollListener listener) {
        if (mOnPageOverScrollListeners != null) {
            mOnPageOverScrollListeners.remove(listener);
        }
    }

    public void clearOnPageOverScrollListeners() {
        if (mOnPageOverScrollListeners != null) {
            mOnPageOverScrollListeners.clear();
        }
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                if (getCurrentItem() == 0) {
                    if (e1 != null && e2 != null && e1.getX() < e2.getX()) {
                        onPageOverScroll(STATE.LEFT);
                    } else {
                        onPageOverScroll(STATE.IDLE);
                    }
                } else if (getAdapter() != null && getCurrentItem() == getAdapter().getCount() - 1) {
                    if (e1 != null && e2 != null && e1.getX() > e2.getX()) {
                        onPageOverScroll(STATE.RIGHT);
                    } else {
                        onPageOverScroll(STATE.IDLE);
                    }
                } else {
                    onPageOverScroll(STATE.IDLE);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    }

    private void onPageOverScroll(STATE state) {
        if (mState != state) {
            if (DEBUG) Log.i(TAG, "onScroll: " + state);
            mState = state;
        }
    }

    private void dispatchOnPageOverScrolled(STATE state) {
        if (mOnPageOverScrollListeners != null) {
            for (int i = 0, z = mOnPageOverScrollListeners.size(); i < z; i++) {
                OnPageOverScrollListener listener = mOnPageOverScrollListeners.get(i);
                if (listener != null) {
                    listener.onPageOverScrolled(state);
                }
            }
        }
    }

    public enum STATE {
        /**
         * This means page was scrolled from left to right direction
         */
        LEFT,
        /**
         * This means page was scrolled from right to left direction
         */
        RIGHT,
        /**
         * Generally this state can be ignored
         */
        IDLE,
    }

    public interface OnPageOverScrollListener {

        /**
         * This method will be invoked when the current page is overscroll.
         *
         * @param state Overscroll state of the current page
         */
        public void onPageOverScrolled(STATE state);

    }
}