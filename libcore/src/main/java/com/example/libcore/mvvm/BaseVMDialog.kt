package com.example.libcore.mvvm

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.*

/**
 *  @author: zhangyong
 *  @time  : 6/01/2021
 *  @desc  : dataBinding baseDialog
 */
abstract class BaseVMDialog<T : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    AppCompatDialogFragment(), OnClickListener {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
    }

    private fun initWindow() {
        //初始化window相关表现
        dialog?.setCanceledOnTouchOutside(setCanceledOnTouchOutside())
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
        val window = dialog?.window
        //隐藏导航栏
        if (isShowNavigation()) {
            window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_HIDE_NAVIGATION
            window?.decorView?.setOnSystemUiVisibilityChangeListener {
                val uiOptions = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_HIDE_NAVIGATION or 0x00001000
                dialog?.window?.decorView?.systemUiVisibility = uiOptions
            }
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(setGravity())
        window?.attributes = window?.attributes.also {
            if (setWindowAnimation() != 0) {
                it?.windowAnimations = setWindowAnimation()
            }
        }
        window?.setLayout(setWidth(), setHeight())
    }

    abstract fun setGravity(): Int
    abstract fun initView()
    open fun observeData() {}
    open fun setCanceledOnTouchOutside(): Boolean = false
    open fun setWindowAnimation(): Int = 0
    open fun setWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT
    open fun setHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT
    open fun isShowNavigation() = false

    fun show(context: Context?) {
        if (context !is FragmentActivity) {
            return
        }
        show(context, context.supportFragmentManager)
    }

    fun show(f: FragmentManager?) {
        f?.apply {
            super.show(f, this@BaseVMDialog.javaClass.simpleName)
        }
    }

    fun show(activity: FragmentActivity?, f: FragmentManager?) {
        if (activity == null || activity.isFinishing) return
        f?.apply {
            super.show(f, this@BaseVMDialog.javaClass.simpleName)
        }
    }

    override fun onClick(v: View?) {
    }

}




