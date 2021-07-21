package com.example.zyuidemo.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.example.libcommon.beans.StatusMarkNote
import com.example.libcore.mvvm.BaseViewModel

/**
 * @desc:
 */
class ComponentViewModel : BaseViewModel() {

    val statusMarkLayoutViewData = MutableLiveData<StatusMarkNote>()

    fun getStatusMarkLayoutData() {
        val data = "{\n" +
                "                \"name\": \"\uD83D\uDE0B进食前的饥饱感\",\n" +
                "                \"questions\": [\n" +
                "                    {\n" +
                "                        \"name\": \"1\",\n" +
                "                        \"tip\": \"饿坏了、浑身无力\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"2\",\n" +
                "                        \"tip\": \"很饿、感觉前胸贴后背\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"3\",\n" +
                "                        \"tip\": \"饥饿感明显\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"4\",\n" +
                "                        \"tip\": \"有点儿饿\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"5\",\n" +
                "                        \"tip\": \"不饿不饱\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"6\",\n" +
                "                        \"tip\": \"有点儿饱\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"7\",\n" +
                "                        \"tip\": \"略饱，但还能吃点\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"8\",\n" +
                "                        \"tip\": \"饱腹感明显\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"9\",\n" +
                "                        \"tip\": \"有点吃撑了\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"10\",\n" +
                "                        \"tip\": \"肚子撑得快炸了\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }"
        val fromJson = GsonUtils.fromJson(data, StatusMarkNote::class.java)
        statusMarkLayoutViewData.postValue(fromJson)
    }

}