package com.example.zyuidemo.vm

import com.example.zyuidemo.api.SquareApi
import com.example.zyuidemo.beans.SquareListBean
import com.example.zyuidemo.network.BaseRepository
import com.example.zyuidemo.network.Result
import com.example.zyuidemo.network.RetrofitClient

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