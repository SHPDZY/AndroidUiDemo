package com.example.zyuidemo.ui.activity

import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.libcommon.beans.ZiRuImageBean
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.launchDelay
import com.example.libcore.mvvm.BaseVMActivity
import com.example.zyuidemo.R
import com.example.zyuidemo.R.drawable.*
import com.example.zyuidemo.databinding.ActivityCustomComponentBinding
import com.example.zyuidemo.ui.adapter.ZiRuAdapter
import com.example.zyuidemo.ui.fragment.ZiRuBannerTransformer
import com.example.zyuidemo.vm.ComponentViewModel
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.RectangleIndicator

@Route(path = PagePath.CUSTOM_COMPONENT_ACTIVITY)
class CustomComponentActivity :
    BaseVMActivity<ActivityCustomComponentBinding>(R.layout.activity_custom_component),
    SeekBar.OnSeekBarChangeListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ComponentViewModel::class.java) }

    private val data = arrayListOf<ZiRuImageBean>()
    private val adapter = ZiRuAdapter(data)

    override fun startObserve() {
        viewModel.statusMarkLayoutViewData.observe(this, {
            it?.run {
                binding.statusMarkLayoutView.setViewData(this)
            }
        })
    }

    override fun initView() {
        initProgressView()
        initStatusMarkLayoutView()
        initZiRuBanner()
    }

    private fun initZiRuBanner() {
        data.add(ZiRuImageBean(img_bac_1, img_ad_text_1, img_ad_bird_1))
        data.add(ZiRuImageBean(img_bac_2, img_ad_text_2, img_ad_table_2))
        adapter.setDatas(data as List<ZiRuImageBean>?)
        val layoutParams = binding.banner.layoutParams
        layoutParams.height = ScreenUtils.getAppScreenWidth() / 2
        binding.banner.layoutParams = layoutParams
        binding.banner.setAdapter(adapter)
            .setIndicator(RectangleIndicator(this))
            .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            .setIndicatorWidth(SizeUtils.dp2px(15f), SizeUtils.dp2px(15f))
            .setIndicatorSpace(SizeUtils.dp2px(5f))
            .setIndicatorNormalColorRes(R.color.colorSlGrayLight)
            .setIndicatorSelectedColorRes(R.color.colorSlBlack)
            .setPageTransformer(ZiRuBannerTransformer())
            .addBannerLifecycleObserver(this)
            .start()
    }

    private fun initStatusMarkLayoutView() {
        viewModel.getStatusMarkLayoutData()
    }

    private fun initProgressView() {
        binding.progressView.setLatestWeight(65.8f)
        binding.seekBar.setOnSeekBarChangeListener(this)
        launchDelay {
            binding.seekBar.progress = 50
            handleProgress(50)
            binding.progressView.start()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        handleProgress(progress)
    }

    private fun handleProgress(progress: Int) {
        var finalProgress = progress.toFloat()
        if (progress < 10) {
            finalProgress = -(50 - progress).toFloat()
        }
        if (progress > 90) {
            finalProgress = progress * 1.4f
        }
        val fl = finalProgress / 100f
        LogUtils.d("progress $fl")
        binding.progressView.setProgress(fl)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        binding.progressView.pause()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        binding.progressView.start()
    }
}