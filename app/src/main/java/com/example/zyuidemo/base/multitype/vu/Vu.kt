package com.example.zyuidemo.base.multitype.vu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

/**
 * @date: 2021/4/1
 * @desc:
 */
interface Vu<T> {

    @LayoutRes
    fun getLayoutId(): Int

    fun setLayoutId(@LayoutRes layoutId: Int)

    fun init(inflater: LayoutInflater, container: ViewGroup?)

    fun init(context: Context)

    fun init(viewDataBinding: ViewDataBinding)

    fun afterInit()

    fun onDestroy()

    fun onPause()

    fun onResume()

    fun bindData(data: T)

    fun getView(): View

    fun getContext(): Context?

    fun setAdapterPos(pos: Int)

    fun getAdapterPos(): Int

    fun setVuCallBack(callBack: VuCallBack<T>?)

    fun getVuCallBack(): VuCallBack<T>?

}

interface VuCallBack<T> {

    fun onCallBack(data: T, pos: Int)

}