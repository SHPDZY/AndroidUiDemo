package com.example.zyuidemo.ui


import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseVMActivity
import com.example.zyuidemo.databinding.FragmentSplashBinding
import com.example.libcommon.router.ILoginService
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.fragmentPage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Route(path = PagePath.ACTIVITY_SPLASH)
class SplashActivity : BaseVMActivity<FragmentSplashBinding>(R.layout.fragment_splash) {

    @Autowired
    lateinit var loginService: ILoginService

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
        super.initView()
        val userInfo = loginService.getUserInfo()
        val userNo = userInfo?.userNo
        if (!TextUtils.isEmpty(userNo)) {
            ARouter.getInstance()
                .build(PagePath.MAIN)
                .navigation(this)
        } else {
            ARouter.getInstance()
                .fragmentPage(PagePath.LOGIN)
                .withTransition(0, 0)
                .navigation(this)

        }
        GlobalScope.launch {
            delay(300)
            finish()
        }

    }
}