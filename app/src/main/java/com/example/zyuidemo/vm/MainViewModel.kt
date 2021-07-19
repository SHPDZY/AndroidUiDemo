package com.example.zyuidemo.vm

import com.example.libcore.mvvm.BaseViewModel
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils

/**
 * @desc:
 */
class MainViewModel : BaseViewModel() {

    fun toLoginPage(){
        RouterUtils.goFragment(PagePath.LOGIN)
    }

    fun toImageListPage(){
        RouterUtils.goFragment(PagePath.IMAGE_LIST)
    }

    fun toWechat(){
        RouterUtils.goFragment(PagePath.WE_CHAT)
    }

    fun toShort(){
        RouterUtils.goFragment(PagePath.SHORT_CUTS_ACTIVITY)
    }

    fun toCustomComponent(){
        RouterUtils.goFragment(PagePath.CUSTOM_COMPONENT_ACTIVITY)
    }

}