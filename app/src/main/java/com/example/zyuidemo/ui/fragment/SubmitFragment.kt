package com.example.zyuidemo.ui.fragment

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.router.PagePath
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentSubmitBinding


@Route(path = PagePath.GROUP_UI_SUBMIT_FRAGMENT)
class SubmitFragment : BaseVMFragment<FragmentSubmitBinding>(R.layout.fragment_submit),
    View.OnClickListener {

    override fun initView() {
        binding.click = this
    }

    override fun onClick(p0: View?) {
        binding.run {
            when (p0) {
                guideProgressView3, guideProgressView4,
                guideProgressView5, guideProgressView6
                -> {
                    guideProgressView3.startSuccessAnim()
                    guideProgressView4.startErrorAnim()
                    guideProgressView5.startSuccessAnim()
                    guideProgressView6.startErrorAnim()
                }
                btnSubmitViewReset -> {
                    guideProgressView3.reset()
                    guideProgressView4.reset()
                    guideProgressView5.reset()
                    guideProgressView6.reset()
                }
                else -> {

                }
            }
        }

    }

}