package com.example.libscan.ui

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.libcommon.router.scanService
import com.example.libcommon.utils.BitmapUtils
import com.example.libcommon.utils.setOnAvoidMultipleClickListener
import com.example.libcommon.utils.toast
import com.example.libcore.mvvm.BaseVMDialog
import com.example.libscan.LibScanUtils
import com.example.libscan.R
import com.example.libscan.databinding.DialogItemGoodsBinding
import com.example.libscan.entity.GoodsData


/**
 * @author : zhangyong
 * @version :
 * @date : 2021/8/3
 * @desc :
 */
class GoodsDialog(val data: GoodsData) :
    BaseVMDialog<DialogItemGoodsBinding>(R.layout.dialog_item_goods) {

    override fun setGravity(): Int {
        return Gravity.CENTER
    }

    override fun setCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun setHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.tvName.text = data.name
        binding.tvCode.text = data.code + (if (TextUtils.isEmpty(data.des)) "" else "\n" + data.des)
        binding.tvPrice.text = "￥${data.price}"
        binding.tvEdit.setOnAvoidMultipleClickListener {
            scanService()?.openAddActivity(data.code)
            dismiss()
        }
        val speakStr = "  ${data.name} \n ￥${data.price}"
        binding.ivClose.setOnAvoidMultipleClickListener {
            dismiss()
        }
        binding.tvDelete.setOnAvoidMultipleClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("是否确认删除？")
                .setPositiveButton("删除") { _, _ ->
                    toast("删除成功")
                    dismiss()
                }
                .setNegativeButton("取消", null)
                .show()
        }
        val bitmapToBase64 = data.img
        if (TextUtils.isEmpty(bitmapToBase64)) {
            binding.ivImg.setImageResource(R.drawable.picture_icon_data_error)
        } else {
            val base64ToBitmap = BitmapUtils.convertBase64ToBitmap(bitmapToBase64)
            binding.ivImg.setImageBitmap(base64ToBitmap)
        }
        LibScanUtils.speak(speakStr)
    }

    companion object {
        fun newInstance(goodsData: GoodsData) = GoodsDialog(goodsData)
    }

}