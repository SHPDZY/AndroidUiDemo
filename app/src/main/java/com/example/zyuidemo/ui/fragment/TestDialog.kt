package com.example.zyuidemo.ui.fragment

import android.view.Gravity
import android.view.ViewGroup
import com.example.libcore.mvvm.BaseVMDialog
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.DialogTestBinding

/**
 * @author : zhangyong
 * @version :
 * @date : 2021/8/3
 * @desc :
 */
class TestDialog :BaseVMDialog<DialogTestBinding>(R.layout.dialog_test){
    override fun setGravity(): Int {
        return Gravity.CENTER
    }

    override fun setHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun initView() {

    }

}