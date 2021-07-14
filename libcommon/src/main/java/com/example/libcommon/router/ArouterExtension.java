package com.example.libcommon.router;

import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @date: 2021-02-02 3:58 PM Tuesday
 */
public class ArouterExtension {
    public static Postcard fragmentPage(String fragmentPath) {
        return ARouter.getInstance().build(PagePath.NEVBASE_FRAGMENT_CONTAINER).withString(RouterConstants.EXTRA_KEY_FRAGMENT_PATH, fragmentPath);
    }

    public static Postcard page(String pagePath) {
        if (TextUtils.isEmpty(pagePath)) {
            return ARouter.getInstance().build(pagePath);
        }
        if (pagePath.startsWith("http") || pagePath.startsWith("https")) {
            return fragmentPage(PagePath.WEB_FRAGMENT).withString(RouterConstants.WEB_URL, pagePath);
        }
        int index = pagePath.indexOf("/", 1);
        String subPath = pagePath.substring(index + 1, pagePath.length());
        if (subPath.startsWith(PagePath.ACTIVITY_PREFIX)) {
            return ARouter.getInstance().build(pagePath);
        } else if (subPath.startsWith(PagePath.FRAGMENT_PREFIX)) {
            return fragmentPage(pagePath);
        }
        return ARouter.getInstance().build(pagePath);
    }


}
