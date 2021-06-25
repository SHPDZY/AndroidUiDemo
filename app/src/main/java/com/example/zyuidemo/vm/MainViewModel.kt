package com.example.zyuidemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.zyuidemo.base.BaseViewModel
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.RouterUtils
import com.example.zyuidemo.router.go
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

}