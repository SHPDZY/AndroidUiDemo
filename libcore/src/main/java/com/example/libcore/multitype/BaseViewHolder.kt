package com.example.libcore.multitype

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libcore.multitype.vu.Vu

class BaseViewHolder<T> : RecyclerView.ViewHolder {
    var itemVu: Vu<T>? = null

    internal constructor(view: View) : super(view)
    internal constructor(itemVu: Vu<T>) : super(itemVu.getView()) {
        this.itemVu = itemVu
    }

    fun bindData(componentsBean: T) {
        itemVu?.run {
            itemVu!!.setAdapterPos(adapterPosition)
            try {
                itemVu!!.bindData(componentsBean)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onViewDetachedFromWindow() {
        if (itemVu != null) {
            itemVu!!.onPause()
        }
    }

    fun onViewAttachedToWindow() {
        if (itemVu != null) {
            itemVu!!.onResume()
        }
    }

    fun onViewRecycled() {
        if (itemVu != null) {
            itemVu!!.onDestroy()
        }
    }
}