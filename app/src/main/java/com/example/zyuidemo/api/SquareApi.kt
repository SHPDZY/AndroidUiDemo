package com.example.zyuidemo.api

import com.example.libcommon.beans.SquareListBean
import com.example.libcommon.network.Response
import retrofit2.http.POST

interface SquareApi {

    /**
     * 获取帖子列表
     */
    @POST("/post/v1/getAppPostList")
    suspend fun getPostsData(): Response<SquareListBean>

}