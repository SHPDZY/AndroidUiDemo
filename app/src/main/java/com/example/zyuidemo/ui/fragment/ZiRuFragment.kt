package com.example.zyuidemo.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.beans.ZiRuImageBean
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.fori
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentSensorManagerBinding
import com.example.zyuidemo.databinding.FragmentZiRuBinding
import com.example.zyuidemo.ui.adapter.ZiRuAdapter
import com.example.zyuidemo.vm.MainViewModel
import com.example.zyuidemo.vm.ZiRuModel
import com.example.zyuidemo.vm.ZiRuSensorData

@SuppressLint("SetTextI18n")
@Route(path = PagePath.GROUP_UI_ZI_RU_FRAGMENT)
class ZiRuFragment :
    BaseVMFragment<FragmentZiRuBinding>(R.layout.fragment_zi_ru),
    SensorEventListener {

    private val mVm by lazy { ViewModelProvider(requireActivity()).get(ZiRuModel::class.java) }

    val TAG = "SensorManagerFragment"
    private val data = arrayListOf<ZiRuImageBean>()
    private val adapter = ZiRuAdapter(data)
    private var mSensorManager: SensorManager? = null
    private var mSensorGyroscope: Sensor? = null
    private var timestamp = 0f
    private val angle = FloatArray(3)
    private val NS2S = 1.0f / 1000000000.0f
    private var gyroscopeX = 0f
    private var gyroscopeY = 0f
    private var gyroscopeZ = 0f
    private val gyroscopeMaxX = 80f
    private val gyroscopeMaxY = 40f
    private val lightMaxValue = 400f
    private var lightMoonMaxTop = 0
    private var lightMoonMaxLeft = 0

    override fun initView() {
        3.fori {
            val ziRuImageBean = ZiRuImageBean()
            data.add(ziRuImageBean)
        }
        adapter.setDatas(data as List<ZiRuImageBean>?)
        binding.banner.adapter = adapter
        binding.banner.start()

        //SensorManager实例
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        //获取设备支持的传感器
        val sensorList = mSensorManager?.getSensorList(Sensor.TYPE_ALL)
        mSensorGyroscope = mSensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        //设置传感器监听，灵敏度设置为game就足够
        mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)

    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.accuracy == 0) {
            return
        }
        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                if (timestamp != 0f) {
                    val dT = (sensorEvent.timestamp - timestamp) * NS2S
                    angle[0] += sensorEvent.values[0] * dT
                    angle[1] += sensorEvent.values[1] * dT
                    val angleX = Math.toDegrees(angle[0].toDouble()).toFloat()
                    val angleY = Math.toDegrees(angle[1].toDouble()).toFloat()
                    mVm.data.postValue(ZiRuSensorData(angleY,angleX))
                }
                timestamp = sensorEvent.timestamp.toFloat()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}