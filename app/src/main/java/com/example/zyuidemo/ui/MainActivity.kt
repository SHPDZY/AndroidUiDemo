package com.example.zyuidemo.ui

import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMActivity
import com.example.zyuidemo.databinding.ActivityMainBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.vm.MainViewModel
import com.example.zyuidemo.vm.RegisterViewModel

@Route(path = PagePath.MAIN)
class MainActivity : BaseVMActivity<ActivityMainBinding>(R.layout.activity_main){
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun initView() {
        binding.vm = mainViewModel
    }

}