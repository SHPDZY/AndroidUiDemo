package com.example.libcommon.router


import com.alibaba.android.arouter.facade.template.IProvider

interface IJsoupService : IProvider {

    fun openJsoupActivity()
    fun openJsoupWebActivity()

}