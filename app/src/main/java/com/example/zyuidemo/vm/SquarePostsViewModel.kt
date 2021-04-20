package com.example.zyuidemo.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.example.zyuidemo.base.BaseViewModel
import com.example.zyuidemo.beans.SquareListBean
import com.example.zyuidemo.beans.TestPostBean
import com.example.zyuidemo.beans.TestPostsListBean
import com.example.zyuidemo.constant.MmkvConstants
import com.example.zyuidemo.utils.MmkvUtils

class SquarePostsViewModel : BaseViewModel() {

    private val activitiesRepository = SquarePostsRepository()

    var postsData = MutableLiveData<TestPostsListBean>()

    /**
     * 获取帖子网络数据
     */
    fun getPostsData() {
        val list = ArrayList<TestPostBean>()
        for (i in 0..10) {
            list.add(getTestList(i))
        }
        val testPostsListBean = TestPostsListBean(list)
        postsData.postValue(testPostsListBean)
        MmkvUtils.getInstance().putObject(MmkvConstants.KEY_SQUARE_POSTS_LIST, testPostsListBean)
    }

    private fun getTestList(pos: Int): TestPostBean {
        val pictures = ArrayList<String>()
        pictures.add("imageurl")
        pictures.add("imageurl")
        pictures.add("imageurl")

        val tags = ArrayList<String>()
        pictures.add("imageurl")
        pictures.add("imageurl")
        pictures.add("imageurl")
        return TestPostBean(
            0,
            pictures,
            "test",
            "test",
            "url",
            0,
            0,
            0,
            0,
            "123",
            "123",
            "123",
            "上海",
            "url",
            tags,
            "2021-4-19 15:41:17"
        )
    }


    /**
     * 获取帖子缓存数据
     */
    fun getCachePostsData(pageIndex: Int) {
        val squareListBean = MmkvUtils.getInstance()
            .getObject(MmkvConstants.KEY_SQUARE_POSTS_LIST, TestPostsListBean::class.java)
        if (squareListBean != null) {
            postsData.postValue(squareListBean)
        }
        getPostsData()
    }

}