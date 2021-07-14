package com.example.zyuidemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.libcore.mvvm.BaseViewModel
import com.example.libcommon.router.PagePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @desc:
 */
class ImageListViewModel : BaseViewModel() {
    private val _tabInfoList: MutableLiveData<List<TabInfoBean>> = MutableLiveData()
    val tabInfoList: LiveData<List<TabInfoBean>>
        get() = _tabInfoList

    fun getTabList() {
        viewModelScope.launch(Dispatchers.Main) {
            _tabInfoList.value = mutableListOf(
                    TabInfoBean().apply { id = "1"; name = "列表1";routePath = PagePath.SQUARE },
                    TabInfoBean().apply { id = "2"; name = "列表2";routePath = PagePath.SQUARE },
                    TabInfoBean().apply { id = "3"; name = "列表3";routePath = PagePath.SQUARE },
                    )
        }
    }
}

class TabInfoBean {
    var id: String? = null
    var name: String? = null
    var url: String? = null
    var routePath: String? = null
}