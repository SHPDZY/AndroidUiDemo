package com.example.zyuidemo.ui

import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.launchDelay
import com.example.libcore.mvvm.BaseVMActivity
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.ActivityCustomComponentBinding
import com.example.zyuidemo.databinding.ActivityShortCutsBinding

@Route(path = PagePath.CUSTOM_COMPONENT_ACTIVITY)
class CustomComponentActivity :
    BaseVMActivity<ActivityCustomComponentBinding>(R.layout.activity_custom_component),
    SeekBar.OnSeekBarChangeListener {

    override fun initView() {
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