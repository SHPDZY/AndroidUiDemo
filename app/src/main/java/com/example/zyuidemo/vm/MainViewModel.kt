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
        RouterUtils.fragmentPage(PagePath.LOGIN).go()
    }

    fun toImageListPage(){
        RouterUtils.fragmentPage(PagePath.IMAGE_LIST).go()
    }

    fun toWechat(){
        RouterUtils.fragmentPage(PagePath.WE_CHAT).go()
    }

}