package com.example.zyuidemo.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseViewModel
import com.example.zyuidemo.beans.SquareListBean
import com.example.zyuidemo.constant.MmkvConstants
import com.example.zyuidemo.utils.MmkvUtils

class SquarePostsViewModel : BaseViewModel() {

    private val activitiesRepository = SquarePostsRepository()

    var postsData = MutableLiveData<SquareListBean>()

    /**
     * 获取帖子网络数据
     */
    fun getPostsData() {
    }

    /**
     * 获取帖子缓存数据
     */
    fun getCachePostsData(pageIndex: Int) {
        val squareListBean = MmkvUtils.getInstance()
            .getObject(MmkvConstants.KEY_SQUARE_POSTS_LIST, SquareListBean::class.java)
        if (squareListBean != null) {
            postsData.postValue(squareListBean)
        }
        getPostsData()
    }

}