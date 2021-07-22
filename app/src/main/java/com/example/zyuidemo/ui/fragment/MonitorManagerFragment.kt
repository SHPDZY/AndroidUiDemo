package com.example.zyuidemo.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.MonitorManager
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentMonitorManagerBinding


@Route(path = PagePath.GROUP_TOOLS_MONITOR_MANAGER_FRAGMENT)
class MonitorManagerFragment : BaseVMFragment<FragmentMonitorManagerBinding>(R.layout.fragment_monitor_manager) {

    override fun initView() {
        MonitorManager.instance.observe(this,{
            binding.tvTest.text = it.toString()
        })
        binding.btnTest.setOnClickListener {
            Thread.sleep(1000)
        }
    }

}