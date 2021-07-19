package com.example.libcommon.widget.floatview

import android.view.MotionEvent

/**
 * @author  zhangyong
 */
interface FloatViewManagerListener:FloatViewListener{
    override fun onRemove(magnetView: FloatView?) {}

    override fun onTouch(event: MotionEvent?) {}

    fun isEnable():Boolean{ return true }
}