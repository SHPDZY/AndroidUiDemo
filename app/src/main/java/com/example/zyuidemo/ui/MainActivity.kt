package com.example.zyuidemo.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMActivity
import com.example.zyuidemo.constant.AutoWiredKey
import com.example.zyuidemo.databinding.ActivityMainBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.RouterUtils
import com.example.zyuidemo.vm.MainViewModel
import com.example.zyuidemo.vm.RegisterViewModel

@Route(path = PagePath.MAIN)
class MainActivity : BaseVMActivity<ActivityMainBinding>(R.layout.activity_main){
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun initView() {
        binding.vm = mainViewModel
    }

    override fun initData() {
        super.initData()
        handleRoute(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleRoute(intent)
    }

    private fun handleRoute(intent: Intent?) {
        intent?.run {
            val routePath = getStringExtra(AutoWiredKey.shortCutsRoute)
            LogUtils.d("routePath:$routePath")
            LogUtils.d("data:${data?.path}")
            if (!StringUtils.isEmpty(routePath)){
                RouterUtils.navigation(routePath)
            }else if (ObjectUtils.isNotEmpty(data)){
                RouterUtils.goFragment(data?.path.toString())
            }
        }
    }

}