package com.example.zyuidemo.util;

import android.app.Activity;

import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * @author : zhangyong
 * @version :
 * @date : 2025/1/6
 * @desc :
 */
public class DfuService extends DfuBaseService {


    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return NotificationActivity.class;
    }

    @Override
    protected boolean isDebug() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}




