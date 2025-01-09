package com.example.libcommon.router


import com.alibaba.android.arouter.facade.template.IProvider

interface IScanService : IProvider {

    fun openScanActivity()
    fun openAddActivity(code: String? = "")

}