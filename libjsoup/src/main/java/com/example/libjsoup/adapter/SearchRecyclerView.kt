package com.example.libjsoup.adapter

import com.example.libcommon.beans.ItemSearchBean
import com.example.libcommon.router.openWeb
import com.example.libcore.multitype.vu.BaseVu
import com.example.libjsoup.R
import com.example.libjsoup.databinding.ItemSearchBinding

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class SearchRecyclerView : BaseVu<ItemSearchBinding, ItemSearchBean>() {

    override fun getLayoutId(): Int {
        return R.layout.item_search
    }

    override fun bindData(data: ItemSearchBean) {
        binding.tvTitle.text = data.title
        binding.tvHref.text = data.url
        binding.root.setOnClickListener {
            openWeb(data.url)
        }
    }
}