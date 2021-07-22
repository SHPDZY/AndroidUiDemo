package com.example.zyuidemo.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.ColorUtils.getColor
import com.example.libcommon.utils.abs
import com.example.libcommon.utils.fori
import com.example.libcommon.utils.getColor
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.R.color.*
import com.example.zyuidemo.databinding.FragmentCoordinatorLayoutBinding
import com.example.zyuidemo.ui.adapter.ItemCoordinatorView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*


@Route(path = PagePath.GROUP_UI_COORDINATOR_LAYOUT_FRAGMENT)
class CoordinatorFragment :
    BaseVMFragment<FragmentCoordinatorLayoutBinding>(R.layout.fragment_coordinator_layout),
    AppBarLayout.OnOffsetChangedListener {
    private val items: MutableList<String> = mutableListOf()
    private val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }

    private var isPlaying = true

    override fun initView() {
        50.fori { items.add("test$it") }
        mAdapter.items = items
        mAdapter.register(BaseViewBinder(ItemCoordinatorView::class.java))
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerView.adapter = mAdapter

        binding.appBarLayout.addOnOffsetChangedListener(this)
        val layoutParams =
            binding.collapsingToolbarLayout.layoutParams as? AppBarLayout.LayoutParams
        binding.btnAction.setOnClickListener {
            isPlaying = !isPlaying
            layoutParams?.scrollFlags = if (isPlaying) SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or SCROLL_FLAG_SNAP else SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            binding.collapsingToolbarLayout.layoutParams = layoutParams
            binding.btnAction.setImageResource(if (isPlaying) R.drawable.exo_icon_pause else R.drawable.exo_icon_play)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val totalScrollRange = appBarLayout?.totalScrollRange?.toFloat() ?: 0f
        var radio = (verticalOffset.toFloat() / totalScrollRange).abs()
        LogUtils.d("verticalOffset $verticalOffset totalScrollRange $totalScrollRange radio $radio")
        val color = getColor(colorSlWhite.getColor(), colorSlBlack.getColor(), radio)
        radio = if (radio < 0.5f) 0f else (radio - 0.5f) * 2
        val textColor = getColor(colorSlBlack0.getColor(), colorSlBlack.getColor(), radio)
        binding.ivBack.setColorFilter(color)
        binding.tvTitle.setTextColor(textColor)
    }
}