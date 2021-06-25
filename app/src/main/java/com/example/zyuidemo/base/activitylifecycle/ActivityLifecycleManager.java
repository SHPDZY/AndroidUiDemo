package com.example.zyuidemo.base.activitylifecycle;


import com.blankj.utilcode.util.Utils;

public class ActivityLifecycleManager {
    private static final ActivityLifecycleManager ourInstance = new ActivityLifecycleManager();

    public static ActivityLifecycleManager getInstance() {
        return ourInstance;
    }

    private ActivityLifecycleManager() {
    }

    public void init() {
        Utils.getApp().registerActivityLifecycleCallbacks(ForegroundLifecycleCallback.getInstance());
    }
}
