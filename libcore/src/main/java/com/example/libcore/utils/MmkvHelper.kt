package com.example.libcore.utils

import android.os.Parcelable
import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: zhangyong
 * @date: 2021-04-20 10:01 AM Tuesday
 */
object MmkvHelper {
    const val MMKVID_DEFAULT = "mmkvid_default"
    const val MMKVID_OBJECT = "mmkvid_obj"

    private val mmkvMap = ConcurrentHashMap<String, MMKV>()

    init {
        LogUtils.e("MmkvHelper init")
        mmkvMap[MMKVID_DEFAULT] = MMKV.mmkvWithID(MMKVID_DEFAULT, MMKV.MULTI_PROCESS_MODE)!!
        mmkvMap[MMKVID_OBJECT] = MMKV.mmkvWithID(MMKVID_OBJECT, MMKV.MULTI_PROCESS_MODE)!!
    }

    fun getMmkv(mmkvId: String): MMKV? {
        return if (TextUtils.isEmpty(mmkvId)) {
            null
        } else {
            if (mmkvMap.containsKey(mmkvId)) {
                mmkvMap[mmkvId]
            } else {
                val newMmkv = MMKV.mmkvWithID(mmkvId, MMKV.MULTI_PROCESS_MODE)
                if (newMmkv != null) {
                    mmkvMap[mmkvId] = newMmkv
                }
                newMmkv
            }
        }
    }

    /********************* 以下为对外公开 api *********************/

    /**
     * @param key
     * @param obj:String/Integer/Boolean/Float/Long/Double/byte[]
     */
    fun put(key: String, obj: Any) {
        putIml(MMKVID_DEFAULT, key, obj)
    }

    fun putWithUserKey(userKey: String, key: String, obj: Any) {
        putIml(userKey, key, obj)
    }

    private fun putIml(mmkvId: String, key: String, obj: Any) {
        val mmkv = getMmkv(mmkvId)
        if (mmkv == null) {
            LogUtils.e("can not get mmkv instance, return")
            return
        }
        if (obj is String) {
            mmkv.encode(key, obj)
        } else if (obj is Int) {
            mmkv.encode(key, obj)
        } else if (obj is Boolean) {
            mmkv.encode(key, obj)
        } else if (obj is Float) {
            mmkv.encode(key, obj)
        } else if (obj is Long) {
            mmkv.encode(key, obj)
        } else if (obj is Double) {
            mmkv.encode(key, obj)
        } else if (obj is ByteArray) {
            mmkv.encode(key, obj)
        }
    }

    fun putStringSet(key: String, value: Set<String>) {
        val mmkv = getMmkv(MMKVID_DEFAULT)
        if (mmkv == null) {
            LogUtils.e("can not get mmkv instance, return")
            return
        }
        mmkv.putStringSet(key, value)
    }

    fun putStringSetWithUserKey(userKey: String, key: String, value: Set<String>) {
        val mmkv = getMmkv(userKey)
        if (mmkv == null) {
            LogUtils.e("can not get mmkv instance, return")
            return
        }
        mmkv.putStringSet(key, value)
    }

    fun putObject(key: String, obj: Any) {
        putObjectIml(MMKVID_OBJECT, key, obj)
    }

    fun putObjectWithUserKey(userKey: String, key: String, obj: Any) {
        putObjectIml(userKey, key, obj)
    }

    private fun putObjectIml(mmkvId: String, key: String, obj: Any) {
        val mmkv = getMmkv(mmkvId)
        if (mmkv == null) {
            LogUtils.e("can not get mmkv instance, return")
            return
        }
        if (obj is Parcelable) {
            mmkv.encode(key, obj)
        } else {
            mmkv.encode(key, Gson().toJson(obj))
        }
    }

    fun getString(key: String, defaultVal: String?): String? {
        return getMmkv(MMKVID_DEFAULT)!!.getString(key, defaultVal)
    }

    fun getStringWithUserKey(userKey: String, key: String, defaultVal: String?): String? {
        return getMmkv(userKey)!!.getString(key, defaultVal)
    }

    fun getInt(key: String, defaultVal: Int): Int {
        return getMmkv(MMKVID_DEFAULT)!!.getInt(key, defaultVal)
    }

    fun getIntWithUserKey(userKey: String, key: String, defaultVal: Int): Int {
        return getMmkv(userKey)!!.getInt(key, defaultVal)
    }

    fun getFloat(key: String, defaultVal: Float): Float {
        return getMmkv(MMKVID_DEFAULT)!!.getFloat(key, defaultVal)
    }

    fun getFloatWithUserKey(userKey: String, key: String, defaultVal: Float): Float {
        return getMmkv(userKey)!!.getFloat(key, defaultVal)
    }

    fun getLong(key: String, defaultVal: Long): Long {
        return getMmkv(MMKVID_DEFAULT)!!.getLong(key, defaultVal)
    }

    fun getLongWithUserKey(userKey: String, key: String, defaultVal: Long): Long {
        return getMmkv(userKey)!!.getLong(key, defaultVal)
    }


    fun getBoolean(key: String, defaultVal: Boolean): Boolean {
        return getMmkv(MMKVID_DEFAULT)!!.getBoolean(key, defaultVal)
    }

    fun getBooleanWithUserKey(userKey: String, key: String, defaultVal: Boolean): Boolean {
        return getMmkv(userKey)!!.getBoolean(key, defaultVal)
    }


    fun getStringSet(key: String, defaultVal: Set<String>?): Set<String>? {
        return getMmkv(MMKVID_DEFAULT)!!.getStringSet(key, defaultVal)
    }

    fun getStringSetWithUserKey(userKey: String, key: String, defaultVal: Set<String>?): Set<String>? {
        return getMmkv(userKey)!!.getStringSet(key, defaultVal)
    }

    inline fun <reified T> getObject(key: String): T? {
        val json = getMmkv(MMKVID_OBJECT)!!.decodeString(key, null)
        if (!TextUtils.isEmpty(json)) {
            return Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)
        }
        return null
    }

    inline fun <reified T> getObjectWithUserKey(userKey: String, key: String): T? {
        val json = getMmkv(userKey)!!.decodeString(key, null)
        if (!TextUtils.isEmpty(json)) {
            return Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)
        }
        return null
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return getObjectIml(MMKVID_OBJECT, key, clazz)
    }

    fun <T> getObjectWithUserKey(userKey: String, key: String, clazz: Class<T>): T? {
        return getObjectIml(userKey, key, clazz)
    }

    private fun <T> getObjectIml(mmkvId: String, key: String, clazz: Class<T>): T? {
        if (Parcelable::class.java.isAssignableFrom(clazz)) {
            return getMmkv(mmkvId)!!.decodeParcelable(key, clazz as Class<out Parcelable>) as? T
        } else {
            val content = getMmkv(mmkvId)!!.decodeString(key, null)
            if (!TextUtils.isEmpty(content)) {
                return Gson().fromJson(content, clazz)
            }
        }
        return null
    }

    fun remove(key: String) {
        getMmkv(MMKVID_DEFAULT)!!.remove(key)
    }

    fun removeWithUserKey(userKey: String, key: String) {
        getMmkv(userKey)!!.remove(key)
    }

    fun removeObject(key: String) {
        getMmkv(MMKVID_OBJECT)!!.remove(key)
    }
}