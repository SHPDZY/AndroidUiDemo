package com.example.libcore.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class StatusBarEnhanceActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleStatusbar();
    }

    protected void handleStatusbar() {
        if (isStatusbarImmersive()) {
            transparentStatusBar(this);
            if (isStatusbarLightmode()) {
                StatusBarUtil.setLightMode(this);
            } else {
                StatusBarUtil.setDarkMode(this);
            }
        }
    }

    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public boolean isStatusbarImmersive() {
        return true;
    }

    public boolean isStatusbarLightmode() {
        return true;
    }

    /**
     * 点击空白处隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                List<View> viewList = new ArrayList<>();
                if (v != null) {
                    viewList.add(v);
                }
                if (isShouldHideKeyboard(getNoHideKeyboardViews(viewList), ev)) {
                    touchActivity();
                    if (v != null) {
                        KeyboardUtils.hideSoftInput(v);
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }
    }

    protected void touchActivity() {

    }

    /**
     * @param viewList 此集合中在此基类中只有一个子项即为当前有焦点的view
     * @return View的集合，这些View的区域被点击后不会隐藏软键盘（如果不重写此方法，除了当前有焦点的view，其它view被点击都会默认隐藏软键盘）
     */
    protected List<View> getNoHideKeyboardViews(List<View> viewList) {
        return viewList;
    }

    public static boolean isShouldHideKeyboard(List<View> views, MotionEvent ev) {
        boolean hide = true;
        if (views != null && views.size() > 0) {
            for (View v : views) {
                if (v != null) {
                    int[] l = {0, 0};
                    v.getLocationInWindow(l);
                    int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
                    if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                        hide = false;
                        break;
                    }
                }
            }
        }
        return hide;
    }
}
