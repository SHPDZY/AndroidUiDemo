package com.example.zyuidemo.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;

public abstract class StatusBarEnhanceFragment extends VisibilityEnhanceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleStatusbar();
    }

    protected void handleStatusbar() {
        if (isStatusbarImmersive()) {
            transparentStatusBar(getActivity());
            if (isStatusbarLightmode()) {
                StatusBarUtil.setLightMode(getActivity());
            } else {
                StatusBarUtil.setDarkMode(getActivity());
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
}
