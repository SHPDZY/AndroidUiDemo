package com.example.zyuidemo.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.router.PagePath
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.BaseVu
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentDfuBinding
import com.example.zyuidemo.databinding.FragmentJktBinding
import com.example.zyuidemo.databinding.ItemJtkViewBinding
import com.example.zyuidemo.util.DfuUtils
import no.nordicsemi.android.dfu.DfuProgressListener
import kotlin.random.Random


@Route(path = PagePath.GROUP_UI_DFU_FRAGMENT)
class DFUFragment : BaseVMFragment<FragmentDfuBinding>(R.layout.fragment_dfu),
    View.OnClickListener, DfuProgressListener {

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.click = this
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.tvDfu) {
            openFileChooser()
        }
    }

    val REQUEST_CODE_SELECT_FILE = 1002

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // 选择所有类型的文件
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == RESULT_OK && data != null) {
            val selectedFileUri = data.data ?: return
            // 在这里可以对selectedFileUri进行处理，获取文件路径等信息
            DfuUtils.getInstance().setmDfuProgressListener(requireActivity(), this)
            DfuUtils.getInstance().startUpdate(
                requireActivity(),
                "50:E4:52:D4:E4:3A",
                "BH", selectedFileUri
            )
        }
    }

    override fun onDeviceConnecting(deviceAddress: String) {
        LogUtils.d("DFU ---> onDeviceConnecting deviceAddress $deviceAddress")
    }

    override fun onDeviceConnected(deviceAddress: String) {
        LogUtils.d("DFU ---> onDeviceConnected deviceAddress $deviceAddress")
    }

    override fun onDfuProcessStarting(deviceAddress: String) {
        LogUtils.d("DFU ---> onDfuProcessStarting deviceAddress $deviceAddress")
    }

    override fun onDfuProcessStarted(deviceAddress: String) {
        LogUtils.d("DFU ---> onDfuProcessStarted deviceAddress $deviceAddress")
    }

    override fun onEnablingDfuMode(deviceAddress: String) {
        LogUtils.d("DFU ---> onEnablingDfuMode deviceAddress $deviceAddress")
    }

    override fun onProgressChanged(
        deviceAddress: String,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    ) {
        //percent 是进度 在这里可以展示进度条
        LogUtils.d("DFU ---> onEnablingDfuMode deviceAddress $deviceAddress percent $percent speed $speed avgSpeed $avgSpeed currentPart $currentPart partsTotal $partsTotal")
//        if (percent >= 100) {
//            launchDelay(200){
//                DfuUtils.getInstance().dispose(this, this)
//                scanLog("DFU:更新完成")
//            }
//        }
    }

    override fun onFirmwareValidating(deviceAddress: String) {
        LogUtils.d("DFU ---> onFirmwareValidating deviceAddress $deviceAddress")
    }

    override fun onDeviceDisconnecting(deviceAddress: String) {
        LogUtils.d("DFU ---> onDeviceDisconnecting deviceAddress $deviceAddress")
    }

    override fun onDeviceDisconnected(deviceAddress: String) {
        LogUtils.d("DFU ---> onDeviceDisconnected deviceAddress $deviceAddress")
    }

    override fun onDfuCompleted(deviceAddress: String) {
        //升级成功
        LogUtils.d("DFU ---> onDfuCompleted deviceAddress $deviceAddress")
    }

    override fun onDfuAborted(deviceAddress: String) {
        //升级流产，失败
        // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
        LogUtils.d("DFU ---> onDfuAborted deviceAddress $deviceAddress")
    }

    override fun onError(
        deviceAddress: String,
        error: Int,
        errorType: Int,
        message: String
    ) {
        //升级错误
        LogUtils.d("DFU ---> onFirmwareValidating deviceAddress $deviceAddress error $error errorType $errorType message $message")
    }

}