package com.example.zyuidemo.ui.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
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
    SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ComponentViewModel::class.java) }

    private val data = arrayListOf<ZiRuImageBean>()
    private val adapter = ZiRuAdapter(data)
    private var isActionDown = false
    private var favorHandle: Handler? = null
    private val runnableaaa = Runnable {
        if (isActionDown) {
            binding.likeView.addFavor()
            favorViewPostDelayed()
        } else {
            favorViewRemoveCallback()
        }
    }

    override fun startObserve() {
        viewModel.statusMarkLayoutViewData.observe(this, {
            it?.run {
                binding.statusMarkLayoutView.setViewData(this)
            }
        })
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
                likeView -> {
                    likeView.addFavor()
                }
                else -> {

                }
            }
        }

    }

    override fun initView() {
        initProgressView()
        initStatusMarkLayoutView()
        initZiRuBanner()
        initFavorView()
        initGuideProgressView()
    }

    private fun initGuideProgressView() {
        binding.click = this
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFavorView() {
        Looper.myLooper()?.run {
            favorHandle = Handler(this)
        }
        binding.likeView.setLikeImages(mutableListOf(img_0, img_1, img_2, img_3, img_4, img_5))
        binding.ivFavor.setOnClickListener(this)
        binding.ivFavor.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isActionDown = true
                    favorViewPostDelayed()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                    isActionDown = false
                    favorViewRemoveCallback()
                }
            }
            false
        }
    }

    private fun favorViewPostDelayed() {
        favorHandle?.postDelayed(runnableaaa, 100)
    }

    private fun favorViewRemoveCallback() {
        favorHandle?.removeCallbacksAndMessages(null)
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