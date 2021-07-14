package com.example.libcommon.utils

import com.example.libcore.utils.MmkvHelper

/**
 * @author: zhangyong
 * @date: 2021-04-20 10:55 AM Tuesday
 */
object DataSaver {
    fun saveString(key: String, value: String) {
        MmkvHelper.put(key, value)
    }

    fun saveStringWithUserKey(userKey: String, key: String, value: String) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveInt(key: String, value: Int) {
        MmkvHelper.put(key, value)
    }

    fun saveIntWithUserKey(userKey: String, key: String, value: Int) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveBoolean(key: String, value: Boolean) {
        MmkvHelper.put(key, value)
    }

    fun saveBooleanWithUserKey(userKey: String, key: String, value: Boolean) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveFloat(key: String, value: Float) {
        MmkvHelper.put(key, value)
    }

    fun saveFloatWithUserKey(userKey: String, key: String, value: Float) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveLong(key: String, value: Long) {
        MmkvHelper.put(key, value)
    }

    fun saveLongWithUserKey(userKey: String, key: String, value: Long) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveDouble(key: String, value: Double) {
        MmkvHelper.put(key, value)
    }

    fun saveDoubleWithUserKey(userKey: String, key: String, value: Double) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveByteArray(key: String, value: ByteArray) {
        MmkvHelper.put(key, value)
    }

    fun saveByteArrayWithUserKey(userKey: String, key: String, value: ByteArray) {
        MmkvHelper.putWithUserKey(userKey, key, value)
    }

    fun saveStringSet(key: String, value: Set<String>) {
        MmkvHelper.putStringSet(key, value)
    }

    fun saveStringSetWithUserKey(userKey: String, key: String, value: Set<String>) {
        MmkvHelper.putStringSetWithUserKey(userKey, key, value)
    }

    fun saveObject(key: String, obj: Any) {
        MmkvHelper.putObject(key, obj)
    }

    fun saveObjectWithUserKey(userKey: String, key: String, obj: Any) {
        MmkvHelper.putObjectWithUserKey(userKey, key, obj)
    }

    fun getString(key: String, defaultVal: String?): String? {
        return MmkvHelper.getString(key, defaultVal)
    }

    fun getStringWithUserKey(userKey: String, key: String, defaultVal: String?): String? {
        return MmkvHelper.getStringWithUserKey(userKey, key, defaultVal)
    }

    fun getInt(key: String, defaultVal: Int): Int {
        return MmkvHelper.getInt(key, defaultVal)
    }

    fun getIntWithUserKey(userKey: String, key: String, defaultVal: Int): Int {
        return MmkvHelper.getIntWithUserKey(userKey, key, defaultVal)
    }

    fun getFloat(key: String, defaultVal: Float): Float {
        return MmkvHelper.getFloat(key, defaultVal)
    }

    fun getFloatWithUserKey(userKey: String, key: String, defaultVal: Float): Float {
        return MmkvHelper.getFloatWithUserKey(userKey, key, defaultVal)
    }

    fun getLong(key: String, defaultVal: Long): Long {
        return MmkvHelper.getLong(key, defaultVal)
    }

    fun getLongWithUserKey(userKey: String, key: String, defaultVal: Long): Long {
        return MmkvHelper.getLongWithUserKey(userKey, key, defaultVal)
    }


    fun getBoolean(key: String, defaultVal: Boolean): Boolean {
        return MmkvHelper.getBoolean(key, defaultVal)
    }

    fun getBooleanWithUserKey(userKey: String, key: String, defaultVal: Boolean): Boolean {
        return MmkvHelper.getBooleanWithUserKey(userKey, key, defaultVal)
    }

    fun getStringSet(key: String, defaultVal: Set<String>?): Set<String>? {
        return MmkvHelper.getStringSet(key, defaultVal)
    }

    fun getStringSetWithUserKey(userKey: String, key: String, defaultVal: Set<String>?): Set<String>? {
        return MmkvHelper.getStringSetWithUserKey(userKey, key, defaultVal)
    }

    inline fun <reified T> getObject(key: String): T? {
        return MmkvHelper.getObject<T>(key)
    }

    inline fun <reified T> getObjectWithUserKey(userKey: String, key: String): T? {
        return MmkvHelper.getObjectWithUserKey<T>(userKey, key)
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return MmkvHelper.getObject(key, clazz)
    }

    fun <T> getObjectWithUserKey(userKey: String, key: String, clazz: Class<T>): T? {
        return MmkvHelper.getObjectWithUserKey(userKey, key, clazz)
    }

    fun delete(key: String) {
        MmkvHelper.remove(key)
    }

    fun deleteWithUserKey(userKey: String, key: String) {
        MmkvHelper.removeWithUserKey(userKey, key)
    }

    fun deleteObject(key: String) {
        MmkvHelper.removeObject(key)
    }

}