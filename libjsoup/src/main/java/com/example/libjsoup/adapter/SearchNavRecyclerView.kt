package com.example.libjsoup.adapter

import com.example.libcommon.beans.ItemSearchNavBean
import com.example.libcommon.router.openWeb
import com.example.libcommon.utils.getColor
import com.example.libcore.multitype.vu.BaseVu
import com.example.libjsoup.R
import com.example.libjsoup.databinding.ItemNavBinding

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class SearchNavRecyclerView : BaseVu<ItemNavBinding, ItemSearchNavBean>() {

    override fun getLayoutId(): Int {
        return R.layout.item_nav
    }

    override fun bindData(data: ItemSearchNavBean) {
        binding.tvIndex.text = data.index
        binding.tvIndex.setTextColor(if (data.isCurrent)R.color.colorSlRed.getColor() else R.color.colorSlBlack.getColor())
        binding.root.setOnClickListener {
            if(data.isCurrent) return@setOnClickListener
            mVuCallBack?.onCallBack(data,getAdapterPos())
        }
    }
}