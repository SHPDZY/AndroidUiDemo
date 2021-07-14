package com.example.zyuidemo.ui

import android.graphics.Color
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.databinding.FragmentWeChatBinding
import com.example.libcommon.router.PagePath
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

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