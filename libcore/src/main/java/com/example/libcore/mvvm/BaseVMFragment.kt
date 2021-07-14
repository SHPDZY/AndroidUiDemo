package com.example.libcore.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.example.libcore.fragment.StatusBarEnhanceFragment

abstract class BaseVMFragment<T : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    StatusBarEnhanceFragment() {

    lateinit var binding: T

    protected fun <T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes layoutId: Int,
        container: ViewGroup?
    ): T =
        DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
            lifecycleOwner = this@BaseVMFragment
        }

    override fun getLayoutResId(): Int {
        return layoutId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = binding(inflater, layoutId, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        startObserve()
        initView()
        initData()
        super.onViewCreated(view, savedInstanceState)
    }


    open fun startObserve() {

    }

    open fun initView() {

    }

    open fun initData() {

    }

    open fun finish() {
        activity?.finish()
    }

}