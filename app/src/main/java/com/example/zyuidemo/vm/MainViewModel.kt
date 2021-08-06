package com.example.zyuidemo.vm

import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils
import com.example.libcommon.router.jsoupService
import com.example.libcore.mvvm.BaseViewModel

/**
 * @desc:
 */
class MainViewModel : BaseViewModel() {

    fun toLoginPage() {
        RouterUtils.goFragment(PagePath.LOGIN)
    }

    fun toImageListPage() {
        RouterUtils.goFragment(PagePath.IMAGE_LIST)
    }

    fun toWechat() {
        RouterUtils.goFragment(PagePath.WE_CHAT)
    }

    fun toShort() {
        RouterUtils.navigation(PagePath.SHORT_CUTS_ACTIVITY)
    }

    fun toCustomComponent() {
        RouterUtils.navigation(PagePath.CUSTOM_COMPONENT_ACTIVITY)
    }

    fun toJsoupActivity() {
        jsoupService()?.openJsoupActivity()
    }

    fun toCoordinatorFragment() {
        RouterUtils.goFragment(PagePath.GROUP_UI_COORDINATOR_LAYOUT_FRAGMENT)
    }

    fun toMonitorManagerFragment() {
        RouterUtils.goFragment(PagePath.GROUP_TOOLS_MONITOR_MANAGER_FRAGMENT)
    }

    fun toSensor() {
        RouterUtils.goFragment(PagePath.GROUP_UI_SENSOR_FRAGMENT)
    }

    fun toZiRu() {
        RouterUtils.goFragment(PagePath.GROUP_UI_ZI_RU_FRAGMENT)
    }

}