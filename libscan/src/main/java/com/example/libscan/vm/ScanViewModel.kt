package com.example.libscan.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.beans.ItemSearchBean
import com.example.libcommon.beans.ItemSearchNavBean
import com.example.libcommon.utils.launch
import com.example.libcore.mvvm.BaseViewModel
import org.jsoup.Jsoup

/**
 * @desc:
 */
class ScanViewModel : BaseViewModel() {

    val htmlData = MutableLiveData<MutableList<ItemSearchBean>>()
    val navData = MutableLiveData<MutableList<ItemSearchNavBean>>()

    init {

    }

}