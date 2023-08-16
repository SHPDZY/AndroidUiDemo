package com.example.libcore.mvvm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.example.libcore.activity.StatusBarEnhanceActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

abstract class BaseVMActivity<T : ViewDataBinding>(@LayoutRes val layoutId: Int) : StatusBarEnhanceActivity() {

    lateinit var binding: T

    protected fun <T : ViewDataBinding> binding(@LayoutRes resId: Int): T =
            DataBindingUtil.setContentView<T>(this, resId)
                    .apply { }


    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
        binding = binding(layoutId)
        startObserve()
        initView()
        initData()
    }

    fun getLayoutResId(): Int = 0
    open fun startObserve() {
    }

    open fun initView() {
    }

    open fun initData() {
    }

    private var xPopup: BasePopupView? = null

    fun showDialog() {
        if (xPopup == null) {
            xPopup = XPopup.Builder(this).asLoading()
        }
        xPopup?.show()
    }

    fun dismissDialog() {
        xPopup?.smartDismiss()
    }
}