package com.example.zyuidemo.ui.adapter

import com.example.libcore.multitype.vu.BaseVu
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.ItemCoordinatorViewBinding

/**
 *  author: zy
 *  desc  :
 */
class ItemCoordinatorView : BaseVu<ItemCoordinatorViewBinding, String>() {
    override fun getLayoutId(): Int {
        return R.layout.item_coordinator_view
    }

    override fun bindData(data: String) {
        binding.tv.text = data
    }
}