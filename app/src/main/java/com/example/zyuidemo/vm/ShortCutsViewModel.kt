package com.example.zyuidemo.vm

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils.*
import com.blankj.utilcode.util.ToastUtils
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseViewModel
import com.example.libcommon.constant.AutoWiredKey
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.topActivity
import com.example.zyuidemo.ui.activity.MainActivity
import com.example.zyuidemo.ui.fragment.TestDialog

/**
 * @desc:
 */
class ShortCutsViewModel : BaseViewModel() {

    private var shortcutManager: ShortcutManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = topActivity()?.getSystemService(ShortcutManager::class.java)
        }
    }

    fun addShort() {
        TestDialog().show(ActivityUtils.getTopActivity())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val intent = Intent(topActivity(), MainActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(AutoWiredKey.shortCutsRoute, PagePath.SHORT_CUTS_ACTIVITY)
            val shortcut = ShortcutInfo.Builder(topActivity(), "short_cuts_activity")
                .setIcon(Icon.createWithResource(topActivity(), R.drawable.ic_login_wx))
                .setShortLabel(getString(R.string.short_cut_home))
                .setLongLabel(getString(R.string.short_cut_home))
                .setIntent(intent)
                .build()
            shortcutManager?.addDynamicShortcuts(listOf(shortcut))
            ToastUtils.showShort("已成功")
        }
    }

    fun deleteShort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.removeDynamicShortcuts(listOf("short_cuts_activity"))
            ToastUtils.showShort("已删除")
        }
    }

}