package com.example.libcommon.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.blankj.utilcode.util.BarUtils
import com.example.libcommon.R
import com.example.libcommon.utils.setOnAvoidMultipleClickListener
import kotlinx.android.synthetic.main.common_app_bar_default.view.*

/**
 * @author  : zhangyong
 * @date    : 2023/8/4
 * @desc    :
 * @version :
 */
class CommonAppBarDefaultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.common_app_bar_default, this)
        initView(context)
    }

    private fun initView(context: Context) {
        view_statusbar?.layoutParams?.height = BarUtils.getStatusBarHeight()
    }

    fun setOnBackListener(unit: () -> Unit) {
        iv_back.setOnAvoidMultipleClickListener {
            unit.invoke()
        }
    }

    fun setTitle(title: CharSequence?) {
        tv_title.text = title?.toString()
    }

}