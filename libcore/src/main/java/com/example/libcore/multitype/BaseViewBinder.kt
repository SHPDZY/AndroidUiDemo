package com.example.libcore.multitype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.drakeet.multitype.ItemViewBinder
import com.example.libcore.multitype.vu.Vu
import com.example.libcore.multitype.vu.VuCallBack
import java.lang.reflect.ParameterizedType


/**
 * @author: yutao
 * @date: 2021/4/1
 * @desc:
 */
open class BaseViewBinder<V : Vu<T>, T> : ItemViewBinder<T, BaseViewHolder<T>> {

    var vuClass: Class<V>? = null
    var itemVu: V? = null
    var mVuCallBack: VuCallBack<T>? = null

    private var mCreatedHolder = 0

    constructor()

    constructor(vuClass: Class<V>, vuCallBack: VuCallBack<T>? = null) {
        this.vuClass = vuClass
        mVuCallBack = vuCallBack
    }

    private fun createVu() {
        try {
            if (vuClass != null) {
                itemVu = vuClass!!.newInstance()
            } else {
                val c: Class<*> = this.javaClass
                val t = c.genericSuperclass
                if (t is ParameterizedType) {
                    val p = t.actualTypeArguments
                    itemVu = (p[0] as Class<V>).newInstance()
                }
            }
            itemVu?.setVuCallBack(mVuCallBack)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, itemData: T) {
        if (itemData != null) {
            holder.bindData(itemData)
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<T> {
        itemVu = null
        createVu()
        return if (itemVu != null) {
            itemVu!!.init(inflater, parent)
            BaseViewHolder(itemVu!!)
        } else {
            BaseViewHolder(View(parent.context))
        }
    }

    override fun onViewAttachedToWindow(@NonNull holder: BaseViewHolder<T>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(@NonNull holder: BaseViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}