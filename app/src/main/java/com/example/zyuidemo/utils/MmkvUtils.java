package com.example.zyuidemo.utils;

import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2021-03-01 3:41 PM Monday
 */
public class MmkvUtils {

    private static final String MMKVID_DEFAULT = "mmkvid_default";
    private static final String MMKVID_OBJECT = "mmkvid_obj";

    private ConcurrentHashMap<String, MMKV> mmkvMap = new ConcurrentHashMap<>();

    private MmkvUtils() {
        mmkvMap.put(MMKVID_DEFAULT, MMKV.mmkvWithID(MMKVID_DEFAULT, MMKV.MULTI_PROCESS_MODE));
        mmkvMap.put(MMKVID_OBJECT, MMKV.mmkvWithID(MMKVID_OBJECT, MMKV.MULTI_PROCESS_MODE));
    }

    public static MmkvUtils getInstance() {
        return Inner.instance;
    }

    private static class Inner {
        private static final MmkvUtils instance = new MmkvUtils();
    }

    private MMKV getMmkv(String mmkvId) {
        if (TextUtils.isEmpty(mmkvId)) {
            return null;
        } else {
            if (mmkvMap.containsKey(mmkvId)) {
                return mmkvMap.get(mmkvId);
            } else {
                MMKV newMmkv = MMKV.mmkvWithID(mmkvId, MMKV.MULTI_PROCESS_MODE);
                mmkvMap.put(mmkvId, newMmkv);
                return newMmkv;
            }
        }
    }

    /**
     * @param key
     * @param object:String/Integer/Boolean/Float/Long/Double/byte[]
     */
    public void put(String key, Object object) {
        putIml(MMKVID_DEFAULT, key, object);
    }

    public void putWithUserKey(String userKey, String key, Object object) {
        putIml(userKey, key, object);
    }

    private void putIml(String mmkvId, String key, Object object) {
        MMKV mmkv = getMmkv(mmkvId);
        if (object instanceof String) {
            mmkv.encode(key, (String) object);
        } else if (object instanceof Integer) {
            mmkv.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mmkv.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            mmkv.encode(key, (Float) object);
        } else if (object instanceof Long) {
            mmkv.encode(key, (Long) object);
        } else if (object instanceof Double) {
            mmkv.encode(key, (Double) object);
        } else if (object instanceof byte[]) {
            mmkv.encode(key, (byte[]) object);
        }
    }

    public void putStringSet(String key, Set<String> value) {
        getMmkv(MMKVID_DEFAULT).putStringSet(key, value);
    }

    public void putStringSetWithUserKey(String userKey, String key, Set<String> value) {
        getMmkv(userKey).putStringSet(key, value);
    }

    public void putObject(String key, Object object) {
        putObjectIml(MMKVID_OBJECT, key, object);
    }

    public void putObjectWithUserKey(String userKey, String key, Object object) {
        putObjectIml(userKey, key, object);
    }

    public String getString(String key, String defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getString(key, defaultVal);
    }

    public int getInt(String key, int defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getInt(key, defaultVal);
    }

    public float getFloat(String key, float defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getFloat(key, defaultVal);
    }

    public long getLong(String key, long defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getLong(key, defaultVal);
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getBoolean(key, defaultVal);
    }


    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return getMmkv(MMKVID_DEFAULT).getStringSet(key, defaultVal);
    }

    public Set<String> getStringSetWithUserKey(String userKey, String key, Set<String> defaultVal) {
        return getMmkv(userKey).getStringSet(key, defaultVal);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return getObjectIml(MMKVID_OBJECT, key, clazz);
    }

    public <T> T getObjectWithUserKey(String userKey, String key, Class<T> clazz) {
        return getObjectIml(userKey, key, clazz);
    }

    private void putObjectIml(String mmkvId, String key, Object object) {
        if (object instanceof Parcelable) {
            getMmkv(mmkvId).encode(key, (Parcelable) object);
        } else {
            getMmkv(mmkvId).encode(key, new Gson().toJson(object));
        }
    }

    private <T> T getObjectIml(String mmkvId, String key, Class<T> clazz) {
        if (Parcelable.class.isAssignableFrom(clazz)) {
            return (T) getMmkv(mmkvId).decodeParcelable(key, (Class<? extends Parcelable>) clazz);
        } else {
            String content = getMmkv(mmkvId).decodeString(key, (String) null);
            if (!TextUtils.isEmpty(content)) {
                return new Gson().fromJson(content, clazz);
            }
        }
        return null;
    }

    public MmkvUtils remove(String key) {
        getMmkv(MMKVID_DEFAULT).remove(key);
        return this;
    }

    public MmkvUtils removeWithUserKey(String userKey, String key) {
        getMmkv(userKey).remove(key);
        return this;
    }

    public MmkvUtils removeObject(String key) {
        getMmkv(MMKVID_OBJECT).remove(key);
        return this;
    }


}

