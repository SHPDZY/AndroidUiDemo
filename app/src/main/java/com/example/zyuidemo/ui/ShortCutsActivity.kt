package com.example.zyuidemo.ui

import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseVMActivity
import com.example.zyuidemo.databinding.ActivityShortCutsBinding
import com.example.libcommon.router.PagePath
import com.example.zyuidemo.vm.ShortCutsViewModel

@Route(path = PagePath.SHORT_CUTS_ACTIVITY)
class ShortCutsActivity : BaseVMActivity<ActivityShortCutsBinding>(R.layout.activity_short_cuts){
    private val viewModel by lazy { ViewModelProvider(this).get(ShortCutsViewModel::class.java) }

    override fun initView() {
        binding.vm = viewModel
    }

}