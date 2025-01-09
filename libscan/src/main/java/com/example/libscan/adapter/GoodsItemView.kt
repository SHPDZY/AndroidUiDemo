package com.example.libscan.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import com.example.libcommon.router.scanService
import com.example.libcommon.utils.BitmapUtils
import com.example.libcommon.utils.setOnAvoidMultipleClickListener
import com.example.libcore.multitype.vu.BaseVu
import com.example.libscan.R
import com.example.libscan.databinding.ItemGoodsBinding
import com.example.libscan.entity.GoodsData

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class GoodsItemView : BaseVu<ItemGoodsBinding, GoodsData>() {

    override fun getLayoutId(): Int {
        return R.layout.item_goods
    }

    @SuppressLint("SetTextI18n")
    override fun bindData(data: GoodsData) {
        handleGoodsData(binding, data)
    }

    companion object {
        fun handleGoodsData(binding: ItemGoodsBinding, data: GoodsData) {
            binding.tvName.text = data.name
//            binding.tvCode.text = data.code
            binding.tvPrice.text = "￥${data.price}"
            val bitmapToBase64 = data.img
            if (TextUtils.isEmpty(bitmapToBase64)) {
                binding.ivImg.setImageResource(R.drawable.picture_image_placeholder)
            } else {
                val base64ToBitmap = BitmapUtils.convertBase64ToBitmap(bitmapToBase64)
                binding.ivImg.setImageBitmap(base64ToBitmap)
            }
            binding.root.setOnAvoidMultipleClickListener {
                scanService()?.openAddActivity(data.code)
            }
        }
    }
}