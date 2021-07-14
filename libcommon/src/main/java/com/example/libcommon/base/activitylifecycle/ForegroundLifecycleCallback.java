package com.example.libcommon.base.activitylifecycle;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class ForegroundLifecycleCallback extends BaseActivityLifecycleCallbacks {
    private static ForegroundLifecycleCallback instance = new ForegroundLifecycleCallback();

    public static ForegroundLifecycleCallback getInstance() {
        return instance;
    }

    private WeakReference<Activity> topActivity;

    public Activity getTopActivity() {
        if (topActivity != null) {
            return topActivity.get();
        }
        return null;
    }

    public boolean isAppForeground() {
        return activityStartedCount > 0;
    }


    private int activityStartedCount;

    @Override
    public void onActivityResumed(@NotNull Activity activity) {
        topActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activityStartedCount <= 0) {
            activityStartedCount = 0;
        }
        activityStartedCount++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityStartedCount--;
        if (activityStartedCount <= 0) {
            activityStartedCount = 0;
        }
    }
}
