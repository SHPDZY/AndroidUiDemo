package com.example.zyuidemo.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.example.libcommon.router.PagePath
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentSensorManagerBinding
import java.lang.Math.abs
import java.lang.Math.toDegrees
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

@SuppressLint("SetTextI18n")
@Route(path = PagePath.GROUP_UI_SENSOR_FRAGMENT)
class SensorManagerFragment :
    BaseVMFragment<FragmentSensorManagerBinding>(R.layout.fragment_sensor_manager),
    SensorEventListener {

    val TAG = "SensorManagerFragment"
    private var mSensorManager: SensorManager? = null
    private var mSensorGyroscope: Sensor? = null
    private var mSensorLight: Sensor? = null
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
        //SensorManager实例
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        //获取设备支持的传感器
        val sensorList = mSensorManager?.getSensorList(Sensor.TYPE_ALL)
        val sb = StringBuilder()
        sb.append("当前设备支持${sensorList?.size ?: 0}个传感器:\n")
        sensorList?.forEach { sensor ->
            sb.append("设备名称:").append(sensor.name).append("\n")
                .append("设备版本:").append(sensor.version).append("\n")
                .append("供应商:").append(sensor.vendor).append("\n\n")
        }
        LogUtils.d(sb)
        mSensorGyroscope = mSensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mSensorLight = mSensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        //设置传感器监听，灵敏度设置为game就足够
        mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)
        mSensorManager?.registerListener(this, mSensorLight, SENSOR_DELAY_GAME)
        (binding.ivImage.layoutParams as? ViewGroup.MarginLayoutParams)?.run {
            topMargin = ScreenUtils.getAppScreenHeight() * 1 / 3
            leftMargin = ScreenUtils.getAppScreenWidth() / 2
            lightMoonMaxTop = topMargin
            lightMoonMaxLeft = leftMargin
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.accuracy == 0) {
            return
        }
        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                handleGyroscope(sensorEvent)
            }
            Sensor.TYPE_LIGHT -> {
                handleLight(sensorEvent)
            }
        }
    }

    private fun handleLight(sensorEvent: SensorEvent) {
        val light: Float = sensorEvent.values[0]
        val ratio = 1.1f - min(light / lightMaxValue, 1f)
        binding.run {
            ivImage.alpha = ratio
            ivStar.alpha = ratio
            ivDark.alpha = 0.6f - min(light / lightMaxValue, 0.6f)
            (ivImage.layoutParams as? ViewGroup.MarginLayoutParams)?.run {
                topMargin = (lightMoonMaxTop.toFloat() * (1.5f - ratio)).toInt()
                leftMargin = (lightMoonMaxLeft.toFloat() * (1.5f - ratio)).toInt()
            }
            ivImage.scaleX = max(2f * ratio, 1f)
            ivImage.scaleY = max(2f * ratio, 1f)
            LogUtils.d("TYPE_LIGHT $light ratio $ratio ${ivImage.marginTop} ${ivImage.marginLeft}")
        }
    }

    private fun handleGyroscope(sensorEvent: SensorEvent) {
        binding.run {
            if (timestamp == 0f) {
                return@run
            }
            val dT = (sensorEvent.timestamp - timestamp) * NS2S
            angle[0] += sensorEvent.values[0] * dT
            angle[1] += sensorEvent.values[1] * dT
            angle[2] += sensorEvent.values[2] * dT
            val angleX = toDegrees(angle[0].toDouble()).toFloat()
            val angleY = toDegrees(angle[1].toDouble()).toFloat()
            val angleZ = toDegrees(angle[2].toDouble()).toFloat()
            tvContent.text = "angleX $angleX \nangleY $angleY \nangleZ $angleZ"
            handleViewTranslationY(angleX)
            handleViewTranslationX(angleY)
            gyroscopeZ = angleZ
        }
        timestamp = sensorEvent.timestamp.toFloat()
    }

    private fun FragmentSensorManagerBinding.handleViewTranslationY(angleX: Float) {
        if (gyroscopeX == 0f) {
            gyroscopeX = angleX
            return
        }
        val diffValue = gyroscopeX - angleX
        if (abs(diffValue) < 0.1) return
        gyroscopeX = angleX
        val tranY2 = ivImage.translationY + diffValue * 2
        val tranY4 = ivStar.translationY + diffValue * 4
        ivImage.translationY = handleY(tranY2)
        ivStar.translationY = handleY(tranY4)
    }

    private fun FragmentSensorManagerBinding.handleViewTranslationX(angleY: Float) {
        if (gyroscopeY == 0f) {
            gyroscopeY = angleY
            return
        }
        val diffValue = gyroscopeY - angleY
        if (abs(diffValue) < 0.1) return
        gyroscopeY = angleY
        val tranX1 = ivBg.translationX - diffValue
        val tranX2 = ivImage.translationX + diffValue * 2
        val tranX4 = ivStar.translationX + diffValue * 4
        ivBg.translationX = handleX(tranX1)
        ivImage.translationX = handleX(tranX2)
        ivStar.translationX = handleX(tranX4)
    }

    private fun handleY(tranY: Float) =
        if (tranY > 0) min(tranY, gyroscopeMaxY) else max(tranY, -gyroscopeMaxY)

    private fun handleX(tranX: Float) =
        if (tranX > 0) min(tranX, gyroscopeMaxX) else max(tranX, -gyroscopeMaxX)

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)
        mSensorManager?.registerListener(this, mSensorLight, SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}