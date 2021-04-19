package com.example.zyuidemo.network

import android.util.Log
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.utils.MmkvUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap
import kotlin.random.Random

/**
 * @date: 2021/2/2
 * @desc:
 */
object RetrofitClient : BaseRetrofitClient() {
    val BASE_URL: String = "www.baidu.com"

    private const val OAUTH_URL = "https://api.weixin.qq.com"
    private const val APP_KEY = ""
    private const val SECRET_KEY = ""

    fun <S> getService(serviceClass: Class<S>): S {
        return getService(serviceClass, BASE_URL)
    }

    fun <S> acquireService(serviceClass: Class<S>): S {
        return getService(serviceClass, OAUTH_URL)
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        addCommonHeader(builder)

        builder.addInterceptor { chain ->
            chain.run {
                val builder = calculateSign(chain)
                proceed(builder.build())
            }
        }
    }

    private fun addCommonHeader(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            chain.run {
                val request = chain.request()
                val builder = request.newBuilder()
                val headerParams: MutableMap<String, String> = HashMap<String, String>()

                headerParams["platform"] = "Android"

                val userInfoBean = MmkvUtils.getInstance()
                    .getObject(UserInfoBean.USER_KEY, UserInfoBean::class.java)
                userInfoBean?.run {
                    headerParams["x-user-token"] = userInfoBean.accessToken
                }
                for ((key, value) in headerParams.entries) {
                    builder.addHeader(key, value)
                }
                proceed(builder.build())
            }
        }
    }

    private fun calculateSign(chain: Interceptor.Chain): Request.Builder {
        val request = chain.request()
        val url = request.url
        val nonce = Random.nextInt(1, Int.MAX_VALUE).toString()
        val timestamp = System.currentTimeMillis().toString()
        val requestId = UUID.randomUUID().toString()

        val segment = createSegment(url.pathSegments)  //api
        val sortParams: MutableMap<String, String> = TreeMap<String, String>()
        val headerParams: MutableMap<String, String> = TreeMap<String, String>()

        headerParams["appkey"] = APP_KEY
        headerParams["nonce"] = nonce
        headerParams["timestamp"] = timestamp

        if (request.method == "GET") {
            for (paramName in url.queryParameterNames) {
                sortParams[paramName] = url.queryParameter(paramName).toString()
            }
        } else if (request.method == "POST") {
            val body = getRequestBody(request)
            if (!body.isNullOrEmpty()) {
                sortParams["json"] = android.util.Base64.encodeToString(
                    body.encodeToByteArray(),
                    android.util.Base64.NO_WRAP
                )
            }
        }

        val signatureUrl = createUrl(createUrl(segment, headerParams, false), sortParams, true)
        Log.d("http", "handleBuilder: $signatureUrl")
//        val signature = android.util.Base64.encodeToString(HMAC_SHA1.genHMACByte(signatureUrl, SECRET_KEY), android.util.Base64.NO_WRAP)
        val signature = encyptSign(signatureUrl.trim(), SECRET_KEY).trim()
        headerParams["signature"] = signature
        headerParams["requestid"] = requestId

        val builder = request.newBuilder()
        for ((key, value) in headerParams.entries) {
            builder.addHeader(key, value)
        }
        return builder
    }

    fun encyptSign(base: String, key: String): String {
        val type = "HmacSHA1"
        val secret = SecretKeySpec(key.encodeToByteArray(), type)
        val mac = Mac.getInstance(type)
        mac.init(secret)
        val digest = mac.doFinal(base.encodeToByteArray())
        return android.util.Base64.encodeToString(digest, android.util.Base64.NO_WRAP)
    }

    fun getRequestBody(request: Request): String? {
        val requestBuilder = request.newBuilder().build()
        requestBuilder.body?.run {
            val buffer = Buffer()
            writeTo(buffer)
            return buffer.readUtf8()
        }
        return null
    }

    private fun createSegment(pathSegments: List<String>): String {
        val seSize = pathSegments.size
        val sb = StringBuilder()
        sb.append("/")
        for ((index, se) in pathSegments.withIndex()) {
            sb.append(se)
            if (index == seSize - 1) {
                sb.append("?")
            } else {
                sb.append("/")
            }
        }
        return sb.toString()

    }

    private fun createUrl(
        segment: String,
        sortParams: MutableMap<String, String>,
        appendSymbol: Boolean
    ): String {
        val signatureSb = StringBuilder(segment)
        val paramSize = sortParams.size
        for ((index, entry) in sortParams.entries.withIndex()) {
            if (index == 0) {
                if (appendSymbol) {
                    signatureSb.append("&")
                }
            } else {
                signatureSb.append("&")
            }
            signatureSb.append(entry.key).append("=").append(entry.value)
        }
        return signatureSb.toString()
    }
}