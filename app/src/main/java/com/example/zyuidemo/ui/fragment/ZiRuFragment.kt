package com.example.zyuidemo.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.libcommon.beans.ZiRuImageBean
import com.example.libcommon.router.PagePath
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.R.drawable.*
import com.example.zyuidemo.databinding.FragmentZiRuBinding
import com.example.zyuidemo.ui.adapter.ZiRuAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.transformer.BasePageTransformer

@SuppressLint("SetTextI18n")
@Route(path = PagePath.GROUP_UI_ZI_RU_FRAGMENT)
class ZiRuFragment :
    BaseVMFragment<FragmentZiRuBinding>(R.layout.fragment_zi_ru) {

    private val data = arrayListOf<ZiRuImageBean>()
    private val adapter = ZiRuAdapter(data)

    override fun initView() {
        data.add(ZiRuImageBean(img_bac_1, img_ad_text_1, img_ad_bird_1))
        data.add(ZiRuImageBean(img_bac_2, img_ad_text_2, img_ad_table_2))
        adapter.setDatas(data as List<ZiRuImageBean>?)
        val layoutParams = binding.banner.layoutParams
        layoutParams.height = ScreenUtils.getAppScreenWidth() / 2
        binding.banner.layoutParams = layoutParams
        binding.banner.setAdapter(adapter)
            .setIndicator(binding.indicator, false)
            .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            .setIndicatorWidth(SizeUtils.dp2px(15f), SizeUtils.dp2px(15f))
            .setIndicatorSpace(SizeUtils.dp2px(5f))
            .setIndicatorNormalColorRes(R.color.colorSlGrayLight)
            .setIndicatorSelectedColorRes(R.color.colorSlBlack)
            .setPageTransformer(ZiRuBannerTransformer())
            .addBannerLifecycleObserver(this)
            .start()
    }

}

class ZiRuBannerTransformer : BasePageTransformer() {
    private var mMinAlpha: Float = DEFAULT_MIN_ALPHA

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width //得到view宽
        val frameLayout = view.findViewById<FrameLayout>(R.id.frame_layout)
        when {
            position < -1 -> { // [-Infinity,-1)
                // This page is way off-screen to the left. 出了左边屏幕
                view.alpha = mMinAlpha
            }
            position <= 1 -> { // [-1,1]
                val factor: Float
                val scale: Float
                if (position < 0) {
                    //消失的页面
                    factor = mMinAlpha + (1 - mMinAlpha) * (1 + position)
                    scale = DEFAULT_MIN_SCALE + (1 - DEFAULT_MIN_SCALE) * (1 + position)
                } else {
                    //出现的页面
                    view.translationX = pageWidth.toFloat() //直接设置出现的页面到底
                    factor = mMinAlpha + (1 - mMinAlpha) * (1 - position)
                    scale = DEFAULT_MIN_SCALE + (1 - DEFAULT_MIN_SCALE) * (1 - position)
                }
                //透明度改变Log
                view.alpha = factor
                //阻止出现页面的滑动
                view.translationX = -pageWidth * position

                frameLayout.translationX = pageWidth * position
                frameLayout.scaleX = scale
                frameLayout.scaleY = scale
            }
            else -> { // (1,+Infinity]
                // This page is way off-screen to the right.    出了右边屏幕
                view.alpha = mMinAlpha
            }
        }
    }

    companion object {
        private const val DEFAULT_MIN_ALPHA = 0.0f
        private const val DEFAULT_MIN_SCALE = 0.5f
    }
}