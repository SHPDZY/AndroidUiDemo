package com.example.zyuidemo.vm

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils
import com.example.libcommon.router.jsoupService
import com.example.libcore.mvvm.BaseViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
        "af89c49fe2f040a883a5c7d85bbc1244=WyIzODYzNjMxMTQ3Il0; SESSION_NEW=ODg2OTk1ZjItOWFlNy00OWIxLWIwZmQtZDc2NTJlMmU3Yjlj; student-access-token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoic3R1ZGVudCIsInVzZXJfbmFtZSI6IkoyMjQ0MTMzMTIzIiwic2NvcGUiOlsiYWxsIl0sInNlc3Npb25JZCI6Ijg4Njk5NWYyLTlhZTctNDliMS1iMGZkLWQ3NjUyZTJlN2I5YyIsImF1dGhvcml0aWVzIjpbIlNUVURFTlQiXSwianRpIjoiNTBlYTQ2ZmUtMzYxYS00M2M4LWFkMTMtMTJhMGIwNWVhMzkyIiwiY2xpZW50X2lkIjoid3Mtc3R1ZGVudCJ9.9pUHNtD9B9jE_AVORt0zM9kHVJH5KIvR-P6Bp89HqxM"
    fun slg() {
        courseId.forEach { courseId ->
            aotuCheckStudyList(courseId)
        }
    }

    fun kf() {
        aotuCheckStudyListKF0()
    }

    private fun aotuCheckStudyListKF0() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://lms.ouchn.cn/api/my-courses?conditions={\"status\":[\"ongoing\"],\"keyword\":\"\"}&fields=id,name,course_code,department(id,name),grade(id,name),klass(id,name),course_type,cover,small_cover,start_date,end_date,is_started,is_closed,academic_year_id,semester_id,credit,compulsory,second_name,display_name,created_user(id,name),org(is_enterprise_or_organization),org_id,public_scope,course_attributes(teaching_class_name,copy_status,tip,data),audit_status,audit_remark,can_withdraw_course,imported_from,allow_clone,is_instructor,is_team_teaching,academic_year(id,name),semester(id,name),instructors(id,name,email,avatar_small_url),is_master,is_child,has_synchronized,master_course(name)&page=1&page_size=10")
            .get()
            .header("Cookie", "HWWAFSESID=23641106739121b5901; HWWAFSESTIME=1665971405326; _ga=GA1.2.1490979779.1665971409; _gat=1; session=V2-70000000002-b4e9bc90-37e6-4a8f-bbe5-949ed26f6d8e.NzAwMDA2Mzg4NjM.1666058022194.B3X8EKkhXJ0-ZKcgZyAChu12C50")
            .build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                val data = GsonUtils.fromJson(string, KF::class.java)
                LogUtils.d("body ${GsonUtils.toJson(data)}")
                val ids = arrayListOf<String>()
                data.courses.forEach {
                    aotuCheckStudyListKF(it.id)
                }
            }

        })
    }
    private fun aotuCheckStudyListKF(id: Long) {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://lms.ouchn.cn/api/courses/${id}/modules")
            .get()
            .header("Cookie", "HWWAFSESID=23641106739121b5901; HWWAFSESTIME=1665971405326; _ga=GA1.2.1490979779.1665971409; _gat=1; session=V2-70000000002-b4e9bc90-37e6-4a8f-bbe5-949ed26f6d8e.NzAwMDA2Mzg4NjM.1666058022194.B3X8EKkhXJ0-ZKcgZyAChu12C50")
            .build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                val data = GsonUtils.fromJson(string, KfDx::class.java)
                LogUtils.d("body ${GsonUtils.toJson(data)}")
                val ids = arrayListOf<String>()
                data.modules.forEach {
                    ids.add(it.id.toString())
                }
                aotuCheckStudyListKF2(ids.toString(),id)
            }

        })
    }
    private fun aotuCheckStudyListKF2(ids: String, id: Long) {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://lms.ouchn.cn/api/course/${id}/all-activities?module_ids=${ids}&activity_types=learning_activities,exams,classrooms,live_records,rollcalls&no-loading-animation=true")
            .get()
            .header("Cookie", "HWWAFSESID=23641106739121b5901; HWWAFSESTIME=1665971405326; _ga=GA1.2.1490979779.1665971409; _gat=1; session=V2-70000000002-b4e9bc90-37e6-4a8f-bbe5-949ed26f6d8e.NzAwMDA2Mzg4NjM.1666058022194.B3X8EKkhXJ0-ZKcgZyAChu12C50")
            .build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                val data = GsonUtils.fromJson(string, KfDx2::class.java)
                LogUtils.d("body ${GsonUtils.toJson(data)}")
                data.learning_activities.forEach {
                    LogUtils.d("title ${it.title} id ${it.id}")
                    if (it.type.equals("online_video")){

                        aotuCheckStudyListKF3(it.id.toString(),it.uploads[0].videos[0].duration)
                    }
                }
            }

        })
    }
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

    private fun aotuCheckStudyListKF3(ids: String, duration: Float) {
        val okHttpClient = OkHttpClient()
        val body: RequestBody = RequestBody.create(JSON, "{\n" +
                "  \"start\": 0,\n" +
                "  \"end\": ${duration-duration*0.1f}\n" +
                "}")
        val request = Request.Builder()
            .url("https://lms.ouchn.cn/api/course/activities-read/${ids}")
            .post(body)
            .header("Content-Type","application/json")
            .header("Cookie", "HWWAFSESID=23641106739121b5901; HWWAFSESTIME=1665971405326; _ga=GA1.2.1490979779.1665971409; _gat=1; session=V2-70000000002-b4e9bc90-37e6-4a8f-bbe5-949ed26f6d8e.NzAwMDA2Mzg4NjM.1666058022194.B3X8EKkhXJ0-ZKcgZyAChu12C50")
            .build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string() ?: ""
                LogUtils.d("body ${string}")
            }

        })
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