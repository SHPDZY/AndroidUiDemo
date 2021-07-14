package com.example.libcore.multitype.vu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @date: 2021/4/1
 * @desc:
 */
open class BaseVu<D : ViewDataBinding, T> : Vu<T> {

    val TAG = javaClass.name

    constructor()

    lateinit var binding: D

    var mVuCallBack: VuCallBack<T>? = null
    var adapterPosition = 0
    var mContext: Context? = null
    var layoutRes = 0

    override fun getLayoutId(): Int {
        return layoutRes
    }

    override fun setLayoutId(layoutId: Int) {
        this.layoutRes = layoutId
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mContext = binding.root.context
        afterInit()
    }

    override fun init(context: Context) {
        mContext = context
        init(LayoutInflater.from(context), null)
    }

    override fun init(viewDataBinding: ViewDataBinding) {
        binding = viewDataBinding as D
        mContext = binding.root.context
        afterInit()
    }

    override fun onDestroy() {
    }

    override fun onPause() {
    }

    override fun onResume() {
    }


    override fun getContext(): Context? {
        return mContext
    }

    override fun getView(): View {
        return binding.root
    }

    override fun setAdapterPos(pos: Int) {
        adapterPosition = pos
    }

    override fun getAdapterPos(): Int {
        return adapterPosition
    }

    override fun bindData(data: T) {
    }

    override fun setVuCallBack(vuCallBack: VuCallBack<T>?) {
        mVuCallBack = vuCallBack
    }

    override fun getVuCallBack(): VuCallBack<T>? {
        return mVuCallBack
    }

    override fun afterInit() {

    }
}