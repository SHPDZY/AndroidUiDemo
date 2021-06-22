package com.example.zyuidemo.ui

import android.graphics.Color
import android.net.Uri
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMFragment
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.databinding.FragmentLoginBinding
import com.example.zyuidemo.databinding.FragmentWeChatBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.fragmentPage
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

@Route(path = PagePath.WE_CHAT)
class WeChatFragment : BaseVMFragment<FragmentWeChatBinding>(R.layout.fragment_we_chat),
    View.OnClickListener {

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

    override fun initView() {

    }

    override fun isStatusbarLightmode(): Boolean {
        return false
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

    fun changeStatusBackgroundAlphaValue(alphaValue: Int) {
        binding.statusBar.setBackgroundColor(Color.argb(alphaValue, 239, 239, 239))
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}