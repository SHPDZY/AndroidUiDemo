package com.example.libcore.utils

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils

/**
 *  time  : 2021/4/1
 *  desc  : 点击输入框外隐藏软键盘工具类
 */
object KeyBoardClick2HideUtil {

    /**
     * 在fragment的onViewCreated方法中调用
     */
    fun click2Hide(view: View, fragment: Fragment?,listener: OnKeyboardHiddenByOtherViewListener? = null) {
        fragment?.activity?.run {
            view.setOnTouchListener { v, ev ->
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (isShouldHideKeyboard(this.currentFocus, ev)) {
                        KeyboardUtils.hideSoftInput(this)
                        listener?.onKeyBordHidden()
                    }
                }
                false
            }
        }
    }

    /**
     * 在activity的dispatchTouchEvent方法中调用
     */
    fun click2Hide(activity: Activity?, ev: MotionEvent?,listener: OnKeyboardHiddenByOtherViewListener? = null) {
        activity?.run {
            ev?.run {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (isShouldHideKeyboard(activity.currentFocus, ev)) {
                        KeyboardUtils.hideSoftInput(activity)
                        listener?.onKeyBordHidden()
                    }
                }
            }
        }
    }

    // Return whether touch the view.
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationOnScreen(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.rawX > left && event.rawX < right && event.rawY > top && event.rawY < bottom)
        }
        return false
    }
}

interface OnKeyboardHiddenByOtherViewListener{
    fun onKeyBordHidden()
}