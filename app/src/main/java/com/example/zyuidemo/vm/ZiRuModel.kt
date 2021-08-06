package com.example.zyuidemo.vm

import androidx.lifecycle.MutableLiveData
import com.example.libcore.mvvm.BaseViewModel

/**
 * @desc:
 */
class ZiRuModel : BaseViewModel() {
    val data:MutableLiveData<ZiRuSensorData> = MutableLiveData()
}

data class ZiRuSensorData(
    var gyroscopeX: Float = 0f,
    var gyroscopeY: Float = 0f
)

