package com.example.zyuidemo.vm

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.blankj.utilcode.util.StringUtils.*
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseViewModel
import com.example.zyuidemo.constant.AutoWiredKey
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.topActivity
import com.example.zyuidemo.ui.MainActivity

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
        }
    }

    fun deleteShort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.removeDynamicShortcuts(listOf("short_cuts_activity"))
        }
    }

}