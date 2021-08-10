package com.example.zyuidemo.widget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * ZhangYong
 */
class ZiRuLayout : FrameLayout, SensorEventListener, ZiRuLifecycleObserver {

    companion object {
        const val DIRECTION_LEFT = 1f
        const val DIRECTION_RIGHT = -1f
        const val maxMoveX = 40f
        const val maxMoveY = 20f
        const val NS2S = 1.0f / 1000000000.0f
    }

    private lateinit var mScroller: Scroller
    private var mDirection = DIRECTION_LEFT
    private var mSensorManager: SensorManager? = null
    private var mSensorGyroscope: Sensor? = null
    private var mSensorAccelerometer: Sensor? = null
    private var timestamp = 0f
    private val angle = FloatArray(3)
    private val useGyroscope = true
    private var totalY = 0f
    private var totalX = 0f

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        mScroller = Scroller(context)
        //SensorManager实例
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        mSensorGyroscope = mSensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mSensorAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //设置传感器监听，灵敏度设置为game就足够
        if (useGyroscope) {
            mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)
        } else {
            mSensorManager?.registerListener(this, mSensorAccelerometer, SENSOR_DELAY_GAME)
        }
        if (context is FragmentActivity) {
            addZiRuLifecycleObserver(context)
        }
    }

    fun addZiRuLifecycleObserver(owner: LifecycleOwner?) {
        owner?.lifecycle?.addObserver(ZiRuLifecycleObserverAdapter(owner, this))
    }

    fun setDirection(direction: Float) {
        mDirection = direction
    }

    fun isNegativeDirection(boolean: Boolean) {
        mDirection = if (boolean) DIRECTION_LEFT else DIRECTION_RIGHT
    }

    fun isNegativeDirection(): Boolean {
        return mDirection == DIRECTION_RIGHT
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                if (timestamp != 0f) {
                    val dT = (sensorEvent.timestamp - timestamp) * NS2S
                    angle[0] += sensorEvent.values[0] * dT
                    angle[1] += sensorEvent.values[1] * dT
                    val angleY = Math.toDegrees(angle[0].toDouble()).toFloat()
                    val angleX = Math.toDegrees(angle[1].toDouble()).toFloat()
                    if (totalY == 0f) {
                        totalY = angleY; return
                    }
                    if (totalX == 0f) {
                        totalX = angleX; return
                    }
                    var scrollX = 0f
                    var scrollY = 0f
                    val dx = totalX - angleX
                    val dy = totalY - angleY
                    if (abs(dx) >= 0.1) scrollX = handleX(dx) * mDirection * 1.5f
                    if (abs(dy) >= 0.1) scrollY = handleY(dy) * mDirection * 1f
                    if (scrollX != 0f) totalX = angleX
                    if (scrollY != 0f) totalY = angleY
                    if (scrollX != 0f || scrollY != 0f)
                        smoothScrollBy(scrollX.toInt(), scrollY.toInt())
                }
                timestamp = sensorEvent.timestamp.toFloat()
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val angleY = sensorEvent.values[1]
                val angleX = sensorEvent.values[0]
                if (totalY == 0f) {
                    totalY = angleY
                    return
                }
                if (totalX == 0f) {
                    totalX = angleX
                    return
                }
                var scrollX = 0f
                var scrollY = 0f
                val dx: Float = totalX - angleX
                val dy: Float = totalY - angleY
                if (abs(dx) > 0.2 && abs(dx) < 2) scrollX = handleX(dx) * mDirection * 5
                if (abs(dy) > 0.2 && abs(dy) < 2) scrollY = handleY(dy) * mDirection * 2f
                if (scrollX != 0f) totalX = angleX
                if (scrollY != 0f) totalY = angleY
                if (scrollX != 0f || scrollY != 0f)
                    smoothScrollBy(-scrollX.toInt(), -scrollY.toInt())
            }
        }
    }

    private fun handleY(tranY: Float) =
        if (tranY > 0) min(tranY, maxMoveY) else max(tranY, -maxMoveY)

    private fun handleX(tranX: Float) =
        if (tranX > 0) min(tranX, maxMoveX) else max(tranX, -maxMoveX)

    fun smoothScrollTo(fx: Int, fy: Int) {
        val dx = fx - mScroller.finalX
        val dy = fy - mScroller.finalY
        smoothScrollBy(dx, dy)
    }

    fun smoothScrollBy(dx: Int, dy: Int) {
        // 参数一：startX 参数二：startY为开始滚动的位置，dx,dy为滚动的偏移量
        mScroller.startScroll(mScroller.finalX, mScroller.finalY, dx, dy, 200)
        invalidate()
    }

    override fun computeScroll() {
        // 判断滚动是否完成 true就是未完成
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
        super.computeScroll()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume(owner: LifecycleOwner?) {
        if (useGyroscope) {
            mSensorManager?.registerListener(this, mSensorGyroscope, SENSOR_DELAY_GAME)
        } else {
            mSensorManager?.registerListener(this, mSensorAccelerometer, SENSOR_DELAY_GAME)
        }
    }

    override fun onPause(owner: LifecycleOwner?) {
        mSensorManager?.unregisterListener(this)
    }

    override fun onDestroy(owner: LifecycleOwner?) {
    }


}

interface ZiRuLifecycleObserver : LifecycleObserver {
    fun onResume(owner: LifecycleOwner?)
    fun onPause(owner: LifecycleOwner?)
    fun onDestroy(owner: LifecycleOwner?)
}

class ZiRuLifecycleObserverAdapter(
    private val mLifecycleOwner: LifecycleOwner,
    private val mObserver: ZiRuLifecycleObserver
) :
    LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        LogUtils.d("ZiRuLifecycleObserverAdapter onResume")
        mObserver.onResume(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        LogUtils.d("ZiRuLifecycleObserverAdapter onPause")
        mObserver.onPause(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtils.d("ZiRuLifecycleObserverAdapter onDestroy")
        mObserver.onDestroy(mLifecycleOwner)
    }
}
