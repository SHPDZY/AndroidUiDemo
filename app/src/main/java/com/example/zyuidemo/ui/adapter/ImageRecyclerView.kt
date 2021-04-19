package com.example.zyuidemo.ui.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.zyuidemo.R
import com.example.zyuidemo.base.multitype.BaseViewBinder
import com.example.zyuidemo.base.multitype.vu.BaseVu
import com.example.zyuidemo.base.multitype.vu.VuCallBack
import com.example.zyuidemo.databinding.IncludeImageRecyclerViewBinding
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.tools.ScreenUtils

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class ImageRecyclerView : BaseVu<IncludeImageRecyclerViewBinding, ArrayList<String>>(),
    VuCallBack<String> {

    var imageList: ArrayList<String>? = null

    private val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }

    override fun getLayoutId(): Int {
        return R.layout.include_image_recycler_view
    }

    override fun bindData(data: ArrayList<String>) {
        imageList = data
        val size = data.size
        if (size in 2..3) {
            mAdapter.items = data.subList(0, 2)
        } else {
            mAdapter.items = data.subList(0, 4)
        }
        mAdapter.register(BaseViewBinder(ImageSingleView::class.java, this))
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2,
                ScreenUtils.dip2px(binding.root.context, 5f), false))
        binding.recyclerView.adapter = mAdapter
        if (size > 2) binding.count = "+${size - mAdapter.itemCount}"
        binding.tvCount.isVisible = if (size == 3) true else size > 4
    }

    override fun onCallBack(data: String, pos: Int) {
        imageList?.let { mVuCallBack?.onCallBack(it, pos) }
    }
}