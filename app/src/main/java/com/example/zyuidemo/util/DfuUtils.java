package com.example.zyuidemo.util;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.util.ToastUtils;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * @author : zhangyong
 * @version :
 * @date : 2025/1/6
 * @desc :
 */
public class DfuUtils {

    private static DfuUtils dfuUtils;
    private DfuServiceController serviceController;
    private DfuServiceInitiator starter;

    public static DfuUtils getInstance() {
        if (dfuUtils == null) {
            synchronized (DfuUtils.class) {
                if (dfuUtils == null) {
                    dfuUtils = new DfuUtils();

                }
            }
        }
        return dfuUtils;
    }

    public void setmDfuProgressListener(Context mContext, DfuProgressListener dfuProgressListener) {
        DfuServiceListenerHelper.registerProgressListener(mContext, dfuProgressListener); //监听升级进度
    }

    //开始升级
    public void startUpdate(Context mContext, String deviceMac, String deviceName, Uri mDeviceZipFilePath) {
        if (mDeviceZipFilePath == null) {
            ToastUtils.showShort("地址错误");
            return;
        }
        //闪退问题解决 兼容   启动前台通知的问题，因为这个库在升级的时候会在通知栏显示进度，
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DfuServiceInitiator.createDfuNotificationChannel(mContext);
//        }

        starter = new DfuServiceInitiator(deviceMac)
                .setDeviceName(deviceName)//设备名称
                .setKeepBond(false)//保持设备绑定 官方demo为false
                .setForceDfu(false)
                .setPacketsReceiptNotificationsEnabled(false)
                .setPacketsReceiptNotificationsValue(12)
                .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);//官方demo为true
        // If you want to have experimental buttonless DFU feature supported call additionally:
        starter.setZip(mDeviceZipFilePath);
        serviceController = starter.start(mContext, DfuService.class); //启动升级服务
    }

    //暂停升级
    public void pauseDevice(Context mContext) {
        if (isDfuServiceRunning(mContext) && serviceController != null && !serviceController.isPaused()) {
            serviceController.pause();
        }
    }

    public void resumeDevice(Context mContext) {
        if (isDfuServiceRunning(mContext) && serviceController != null && serviceController.isPaused()) {
            serviceController.resume();
        }
    }

    //销毁升级
    public void abortDevice(Context mContext) {
        if (isDfuServiceRunning(mContext) && serviceController != null) {
            serviceController.abort();
        }
    }

    /**
     * 判断dfu状态
     *
     * @return
     */
    private boolean isDfuServiceRunning(Context mContext) {
        final ActivityManager manager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DfuService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //退出 dfu
    public void dispose(Context mContext, DfuProgressListener dfuProgressListener) {
        DfuServiceListenerHelper.unregisterProgressListener(mContext, dfuProgressListener);
        if (isDfuServiceRunning(mContext)) {
            if (serviceController != null) {
                serviceController.abort();
                mContext.stopService(new Intent(mContext, DfuService.class));
            }
        }
        if (starter != null) {
            starter = null;
        }
        if (serviceController != null) {
            serviceController = null;
        }
    }

}

