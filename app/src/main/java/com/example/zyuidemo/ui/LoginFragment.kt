package com.example.zyuidemo.ui

import android.net.Uri
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMFragment
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.databinding.FragmentLoginBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.loginService
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Route(path = PagePath.LOGIN)
class LoginFragment : BaseVMFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    private var isComplete = false
    private var mLoginType = ""
    private var xPopup: BasePopupView? = null
    override fun onBack(): Boolean {
        return false
    }


    override fun initData() {
        super.initData()
    }

    override fun startObserve() {
        super.startObserve()
    }

    var mPlayer: SimpleExoPlayer? = null

    override fun initView() {
        binding.run {
            ivLoginWx.setOnClickListener(this@LoginFragment)
            ivLoginZfb.setOnClickListener(this@LoginFragment)
            ivWelcome.setOnClickListener(this@LoginFragment)
            mPlayer = SimpleExoPlayer.Builder(exoPlay.context).build()
            mPlayer?.run {
                val uri =
                    Uri.parse("https://bj-saloon.oss-cn-beijing.aliyuncs.com/android/app_resource/login_bg.mp4")
                val mediaSource: MediaSource =
                    ProgressiveMediaSource.Factory(DefaultDataSourceFactory(exoPlay.context))
                        .createMediaSource(uri)
                setMediaSource(mediaSource)
                prepare()
                repeatMode = REPEAT_MODE_ONE
                exoPlay.player = this
                exoPlay.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                volume = 0f
                playWhenReady = true
            }
        }
    }

    override fun isStatusbarLightmode(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        if (mPlayer != null) {
            mPlayer!!.play()
        }
        GlobalScope.launch {
            delay(500)
            if (!isComplete) dismissDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPlayer != null) {
            mPlayer!!.release()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mPlayer != null) {
            mPlayer!!.pause()
        }
    }

    override fun onClick(v: View?) {
        isComplete = false
        when (v?.id) {
            R.id.iv_login_wx,
            R.id.iv_login_zfb -> {
                showDialog()
                val userInfoBean = UserInfoBean()
                userInfoBean.accessToken = "token"
                userInfoBean.userNo = "no"
                userInfoBean.beanId = "id"
                userInfoBean.nickName = "nickName"
                loginService().setUserInfo(userInfoBean)
                GlobalScope.launch {
                    delay(500)
                    dismissDialog()
                    ARouter.getInstance().build(PagePath.MAIN).navigation(activity)
                    delay(300)
                    finish()
                }
            }
        }
    }

    private fun showDialog() {
        if (xPopup == null) {
            xPopup = XPopup.Builder(activity).asLoading()
        }
        xPopup?.show()
    }

    private fun dismissDialog() {
        xPopup?.smartDismiss()
    }


}