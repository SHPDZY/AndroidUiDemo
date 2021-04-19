package com.example.zyuidemo.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMActivity
import com.example.zyuidemo.databinding.ActivityMainBinding
import com.example.zyuidemo.router.PagePath

@Route(path = PagePath.MAIN)
class MainActivity : BaseVMActivity<ActivityMainBinding>(R.layout.activity_main)