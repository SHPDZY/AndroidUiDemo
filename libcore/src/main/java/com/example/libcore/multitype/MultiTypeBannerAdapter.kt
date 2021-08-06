package com.example.libcore.multitype

import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.libcore.multitype.vu.Vu
import com.youth.banner.adapter.BannerAdapter

/**
 * @author  : zhangyong
 * @date    : 2021/8/6
 * @desc    :
 * @version :
 */
class MultiTypeBannerAdapter<T>(datas: List<T>?) :
    BannerAdapter<T, BaseViewHolder<T>>(datas) {

    var vuCls: Class<out Vu<T>>? = null
    var vu: Vu<T>? = null
    fun registerViewBinder(vuCls: Class<out Vu<T>>) {
        this.vuCls = vuCls
    }

    fun registerViewBinder(vu: Vu<T>) {
        this.vu = vu
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        if (vuCls != null) {
            vu = vuCls!!.newInstance()
        }
        if (vu != null) {
            vu!!.init(parent.context)
            vu!!.getView().layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return BaseViewHolder(vu!!)
        }
        return BaseViewHolder(FrameLayout(parent.context))
    }

    override fun onBindView(
        holder: BaseViewHolder<T>?,
        data: T?,
        position: Int,
        size: Int
    ) {
        if (data != null) {
            holder?.bindData(data)
        }
    }
}