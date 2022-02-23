package com.example.zyuidemo.vm

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils
import com.example.libcommon.router.jsoupService
import com.example.libcore.mvvm.BaseViewModel
import okhttp3.*
import java.io.IOException
import kotlin.random.Random


/**
 * @desc:
 */
class MainViewModel : BaseViewModel() {

    fun toLoginPage() {
        RouterUtils.goFragment(PagePath.LOGIN)
    }

    fun toImageListPage() {
        RouterUtils.goFragment(PagePath.IMAGE_LIST)
    }

    fun toWechat() {
        RouterUtils.goFragment(PagePath.WE_CHAT)
    }

    fun toShort() {
        RouterUtils.navigation(PagePath.SHORT_CUTS_ACTIVITY)
    }

    fun toCustomComponent() {
        RouterUtils.navigation(PagePath.CUSTOM_COMPONENT_ACTIVITY)
    }

    fun toJsoupActivity() {
        jsoupService()?.openJsoupActivity()
    }

    fun toJsoupWebActivity() {
        jsoupService()?.openJsoupWebActivity()
    }

    fun toCoordinatorFragment() {
        RouterUtils.goFragment(PagePath.GROUP_UI_COORDINATOR_LAYOUT_FRAGMENT)
    }

    fun toMonitorManagerFragment() {
        RouterUtils.goFragment(PagePath.GROUP_TOOLS_MONITOR_MANAGER_FRAGMENT)
    }

    fun toSensor() {
        RouterUtils.goFragment(PagePath.GROUP_UI_SENSOR_FRAGMENT)
    }

    fun toZiRu() {
        RouterUtils.goFragment(PagePath.GROUP_UI_ZI_RU_FRAGMENT)
    }

    fun toSubmit() {
        RouterUtils.goFragment(PagePath.GROUP_UI_SUBMIT_FRAGMENT)
    }

    val courseId =
        arrayListOf("ff8080817be3a3ed017bf2ee559e1b91", "ff8080817be3a3ed017bf2ee3ac51b6f")
    val cookie =
        "homeUrl=https://shlg.o-learn.cn; SESSION=3a51b1e5-3dd9-41df-872b-72bc9e1b2a90"

    fun slg() {
        courseId.forEach { courseId ->
            aotuCheckStudyList(courseId)
        }
    }

    private fun aotuCheckStudyList(courseId: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("courseId", courseId)
            .build()
        val request = Request.Builder()
            .url("https://usst.ct-edu.com.cn/learning/student/studentDataAPI.action?functionCode=queryCourseItemList")
            .post(requestBody)
            .header("Cookie", cookie)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtils.e(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                LogUtils.d("body $string")
                val data = GsonUtils.fromJson(string, SlgListEntity::class.java)
                if ("E000P" == data.returnCode) {
                    ToastUtils.showShort(data.returnMessage)
                    return
                }
                val stringBuilder = StringBuilder()
                data.chapterList?.forEach { chapter ->
                    stringBuilder.append(chapter.title).append("\n")
                    chapter.childList.forEach {
                        val get = it.childList[0]
                        val id = get.id
                        val status = get.status
                        stringBuilder.append(it.title).append("\n").append(" ———— id:$id")
                            .append("  状态：${handleStatus(status)}").append("\n")
                        if (status != 2) {
                            autoStudy(id, courseId)
                        }
                    }
                }
                LogUtils.d(stringBuilder)
            }
        })
    }

    private fun autoStudy(id: String, courseId: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("courseId", courseId)
            .add("itemId", id)
            .add("playbackRate", "1")
            .add("key", "${System.currentTimeMillis()}")
            .add("playPosition", Random.nextInt(100).toString())
            .add("recordCount", Random.nextInt(1800).toString())
            .build()
        val request = Request.Builder()
            .url("https://usst.ct-edu.com.cn/learning/student/studentDataAPI.action?functionCode=sendVideoLearnRecord")
            .post(requestBody)
            .header("Cookie", cookie)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtils.e(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                val data = GsonUtils.fromJson(string, LookStatusEntity::class.java)
                if (data.learnRecord.status == 2) {
                    LogUtils.d("课程id：$id 已看完")
                } else {
                    LogUtils.d("课程id：$id 未看完")
                    autoStudy(id, courseId)
                }
            }
        })
    }

    fun handleStatus(status: Int): String {
        return when (status) {
            1 -> {
                "没看完"
            }
            2 -> {
                "看完了"
            }
            else -> "还没看"
        }
    }

}