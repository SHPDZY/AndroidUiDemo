package com.example.libcommon.router


import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider

interface IAppLifeService : IProvider {

    fun getTopActivity(): Activity?

}