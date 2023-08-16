package com.example.libscan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.constant.DataSaverConstants
import com.example.libcommon.utils.DataSaver
import com.example.libscan.entity.GoodsListData
import com.example.libscan.ui.MyCaptureActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


/**
 * @author  : zhangyong
 * @date    : 2023/8/11
 * @desc    :
 * @version :
 */
object LibScanUtils {

    private var speech: TextToSpeech? = null
    const val BOOHEE_DIR = "zy_scan"
    const val GOODS_LIST_FILE_NAME = "goods_list.txt"
    const val GOODS_LIST_FILE_START_TEXT = "goods_list="

    fun initSpeech(context: Context): TextToSpeech? {
        if (speech == null) {
            speech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    speech?.language = Locale.CHINA
                }
            }
        }
        return speech
    }

    fun speak(text: String) {
        speech?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    fun initScan(activity: Activity) {
        val integrator = IntentIntegrator(activity)
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats()
        integrator.setPrompt("请将摄像头对准") //底部的提示文字，设为""可以置空
        integrator.setCameraId(0) //前置或者后置摄像头
        integrator.captureActivity = MyCaptureActivity::class.java //设置打开摄像头的Activity
        integrator.setBeepEnabled(true) //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    fun getImageKey(code: String?) =
        DataSaverConstants.KEY_SCAN_GOODS_IMAGE + "_" + code.toString()

    fun writeGoodsList(context: Context, list: GoodsListData): GoodsListData {
        val directory = ScanFileUtils.getFileDirectory(context, "zytest", true)
        val file = File(directory, GOODS_LIST_FILE_NAME)
        LogUtils.e("zy_directory $file")
        FileUtils.createOrExistsFile(file)
        val json = GsonUtils.toJson(list)
        DataSaver.saveObject(DataSaverConstants.KEY_SCAN_GOODS_LIST, list)
        FileIOUtils.writeFileFromString(file, GOODS_LIST_FILE_START_TEXT + json)
        return list
    }

    fun writeGoodsList(context: Context, uri: Uri): GoodsListData {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        reader.close()
        inputStream?.close()
        val fileContent = stringBuilder.toString()
        if (!TextUtils.isEmpty(fileContent) && fileContent.startsWith(GOODS_LIST_FILE_START_TEXT)) {
            val substring = fileContent.substring(GOODS_LIST_FILE_START_TEXT.length)
            val directory = ScanFileUtils.getFileDirectory(context, "zytest", true)
            val file = File(directory, GOODS_LIST_FILE_NAME)
            LogUtils.e("zy_directory $file")
            FileUtils.createOrExistsFile(file)
            FileIOUtils.writeFileFromString(
                file,
                GOODS_LIST_FILE_START_TEXT + substring
            )
            val listData = GsonUtils.fromJson(substring, GoodsListData::class.java)
            DataSaver.saveObject(DataSaverConstants.KEY_SCAN_GOODS_LIST, listData)
            return listData
        }
        return GoodsListData()
    }

    fun readGoodsList(context: Context): GoodsListData {
        val directory = ScanFileUtils.getFileDirectory(context, "zytest", true)
        val file = File(directory, GOODS_LIST_FILE_NAME)
        LogUtils.e("zy_directory $file")
        FileUtils.createOrExistsFile(file)
        val string = FileIOUtils.readFile2String(file)
        return if (!TextUtils.isEmpty(string) && string.startsWith(GOODS_LIST_FILE_START_TEXT)) {
            GsonUtils.fromJson(
                string.substring(GOODS_LIST_FILE_START_TEXT.length),
                GoodsListData::class.java
            )
        } else {
            GoodsListData()
        }
    }

    fun shareFile(context: Context) {
        // 创建分享的Intent
        val directory = ScanFileUtils.getFileDirectory(context, "zytest", true)
        val file = File(directory, GOODS_LIST_FILE_NAME)
        val contentUri =
            FileProvider.getUriForFile(context, "com.example.zyuidemo.fileprovider", file)
        var intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.type = "*/*"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent = Intent.createChooser(intent, "")
        context.startActivity(intent)
    }

    fun onDestroy() {
        speech?.stop()
        speech?.shutdown()
        speech = null
    }
}