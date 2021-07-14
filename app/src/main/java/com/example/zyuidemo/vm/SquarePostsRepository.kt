package com.example.zyuidemo.vm

import com.example.zyuidemo.api.SquareApi
import com.example.libcommon.beans.SquareListBean
import com.example.libcommon.network.BaseRepository
import com.example.libcommon.network.Result
import com.example.libcommon.network.RetrofitClient

class SquarePostsRepository : BaseRepository() {
    private val activitiesApi by lazy { RetrofitClient.getService(SquareApi::class.java) }

    suspend fun getPostsData(): Result<SquareListBean> {
        return safeApiCall(
            call = { executeResponse(
                activitiesApi.getPostsData()
            ) },
            errorMessage = "网络错误"
        )
    }

}