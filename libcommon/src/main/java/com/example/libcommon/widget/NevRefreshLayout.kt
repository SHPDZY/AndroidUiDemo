package com.example.libcommon.widget

import android.content.Context
import android.util.AttributeSet
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @date: 2021-03-08 10:10 AM Monday
 */
open class NevRefreshLayout : SmartRefreshLayout {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun init() {
        setRefreshHeader(RefreshHeader(context))
        setRefreshFooter(LoadFooter(context))
        setEnableOverScrollBounce(false)
        setEnableOverScrollDrag(false)
    }
}