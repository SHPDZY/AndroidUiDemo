package com.example.zyuidemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMActivity
import com.example.zyuidemo.databinding.ActivityMainBinding
import com.example.zyuidemo.databinding.ActivityShortCutsBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.vm.MainViewModel
import com.example.zyuidemo.vm.ShortCutsViewModel

@Route(path = PagePath.SHORT_CUTS_ACTIVITY)
class ShortCutsActivity : BaseVMActivity<ActivityShortCutsBinding>(R.layout.activity_short_cuts){
    private val viewModel by lazy { ViewModelProvider(this).get(ShortCutsViewModel::class.java) }

    override fun initView() {
        binding.vm = viewModel
    }

}