package com.example.zyuidemo.ui.activity

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libcommon.constant.AutoWiredKey
import com.example.zyuidemo.databinding.ActivityMainBinding
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils
import com.example.libcommon.widget.floatview.FloatView
import com.example.libcommon.widget.floatview.FloatViewManager
import com.example.libcommon.widget.floatview.FloatViewManagerListener
import com.example.zyuidemo.vm.MainViewModel

@Route(path = PagePath.MAIN)
class MainActivity : BaseVMActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun initView() {
        binding.vm = mainViewModel
    }

    override fun initData() {
        super.initData()
        handleRoute(intent)
        FloatViewManager.setClickListener(object : FloatViewManagerListener {
            override fun onClick(floatView: FloatView?) {
                mainViewModel.toShort()
            }
        }).addNotDisplayActivity(
            CustomComponentActivity::class.java.name,
            ShortCutsActivity::class.java.name
        )
//            .init()
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
            if (!StringUtils.isEmpty(routePath)) {
                RouterUtils.navigation(routePath)
            } else if (ObjectUtils.isNotEmpty(data)) {
                RouterUtils.goFragment(data?.path.toString())
            }
        }
    }

}