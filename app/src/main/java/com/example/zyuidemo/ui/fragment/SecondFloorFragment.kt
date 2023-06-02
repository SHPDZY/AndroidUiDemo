package com.example.zyuidemo.ui.fragment

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils.getScreenHeight
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.AnimationUtils.translationYView
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentSencondFloorBinding
import com.example.zyuidemo.widget.SecondFloorRefreshHeader

@Route(path = PagePath.GROUP_UI_SECOND_FLOOR_FRAGMENT)
class SecondFloorFragment :
    BaseVMFragment<FragmentSencondFloorBinding>(R.layout.fragment_sencond_floor),
    View.OnClickListener {

    var secondFloorIsOpen: Boolean = false
    private var layoutContentAnim: ObjectAnimator? = null
    private var layoutSecondFloorAnim: ObjectAnimator? = null
    var secondFloorInAnim: Boolean = layoutContentAnim?.isRunning == true
    var height = getScreenHeight().toFloat()
    val animationDuration = 300L
    private var wallDragTop = 0

    override fun onBack(): Boolean {
        if (secondFloorIsOpen){
            onSecondFloorFinished(true)
            return true
        }
        return false
    }

    override fun initView() {
        binding.frameSecondFloor.translationY = -height
        binding.frameSecondFloor.setOnClickListener {
            onSecondFloorFinished(true)
        }
        val refreshHeader = SecondFloorRefreshHeader(requireContext())
        refreshHeader.refreshListener = object : SecondFloorRefreshHeader.SecondFloorListener {
            override fun onRefresh() {
                binding.refreshLayout.finishRefresh(0)
            }

            override fun onSecondFloorOpen() {
                binding.refreshLayout.finishRefresh(0)
                showSecondFloor()
            }

            override fun onSecondFloorRefresh() {
                binding.refreshLayout.finishRefresh(0)
            }

            override fun onPulling(offsetStart: Int, offset: Int) {
                binding.rootLayout.translationY = offset.toFloat()
                binding.secondFloorView.setQuadY((offset*1.3f).toInt())
            }


        }
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setHeaderHeight(120f)
        binding.refreshLayout.setHeaderMaxDragRate(1.8f)
        binding.refreshLayout.setRefreshHeader(refreshHeader)
        binding.refreshLayout.setOnRefreshListener { }
    }

    fun showSecondFloor() {
        if (secondFloorInAnim) {
            return
        }
        secondFloorIsOpen = true
        BarUtils.setStatusBarVisibility(requireActivity(), true)
        BarUtils.setNavBarVisibility(requireActivity(), false)
        handleView(0f, height)
    }

    /**
     * 壁纸点击关闭回调
     */
    fun onSecondFloorFinished(needAnim: Boolean) {
        if (secondFloorInAnim) {
            return
        }
        wallDragTop = 0
        secondFloorIsOpen = false
        handleView(height, 0f, needAnim)
        BarUtils.setStatusBarVisibility(requireActivity(), true)
        BarUtils.setNavBarVisibility(requireActivity(), true)
    }

    fun handleView(contentFrom: Float, contentTo: Float, needAnim: Boolean = true) {
        handleSecondFloorInAnim(true)
        if (!needAnim) {
            binding.contentLayout.translationY = contentTo
            if (contentTo == 0f) {
                binding.frameSecondFloor.translationY = -height
            }
            handleSecondFloorInAnim(false)
            return
        }
        if (layoutContentAnim == null) {
            layoutContentAnim = translationYView(
                binding.contentLayout,
                contentFrom,
                contentTo,
                animationDuration,
                0,
                Animation.RESTART,
                startCallBack = { handleSecondFloorInAnim(true) },
                endCallBack = { handleSecondFloorInAnim(false) }
            )
            layoutSecondFloorAnim?.interpolator = AccelerateDecelerateInterpolator()
            layoutSecondFloorAnim = translationYView(
                binding.frameSecondFloor,
                if (contentFrom == 0f) -height else 0f,
                if (contentTo == 0f) -height else 0f,
                animationDuration,
                0,
                Animation.RESTART,
                startCallBack = { handleSecondFloorInAnim(true) },
                endCallBack = { handleSecondFloorInAnim(false) }
            )
            layoutContentAnim?.interpolator = AccelerateDecelerateInterpolator()
            layoutSecondFloorAnim?.interpolator = AccelerateDecelerateInterpolator()
        } else {
            if (layoutContentAnim?.isRunning == true || layoutContentAnim?.isStarted == true) {
                layoutContentAnim?.end()
            }
            layoutContentAnim?.setFloatValues(contentFrom, contentTo)
            layoutContentAnim?.start()
            if (layoutSecondFloorAnim?.isRunning == true || layoutSecondFloorAnim?.isStarted == true) {
                layoutSecondFloorAnim?.end()
            }
            layoutSecondFloorAnim?.setFloatValues(
                if (contentFrom == 0f) -height else 0f,
                if (contentTo == 0f) -height else 0f
            )
            layoutSecondFloorAnim?.start()
        }
    }

    private fun handleSecondFloorInAnim(boolean: Boolean) {
        secondFloorInAnim = boolean
    }

    override fun isStatusbarLightmode(): Boolean {
        return false
    }


    override fun onClick(v: View?) {
    }


}