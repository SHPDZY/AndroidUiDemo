package com.example.zyuidemo.ui.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import com.alibaba.android.arouter.facade.annotation.Route
import com.busi.square.ui.adapter.ItemPostView
import com.drakeet.multitype.MultiTypeAdapter
import com.example.zyuidemo.R
import com.example.libcore.mvvm.BaseVMFragment
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.VuCallBack
import com.example.libcommon.beans.TestPostBean
import com.example.libcommon.beans.UserInfoBean
import com.example.zyuidemo.databinding.FragmentSquareBinding
import com.example.libcommon.router.PagePath
import com.example.zyuidemo.vm.SquarePostsViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * @author: zhangyong
 * @desc:
 */
@Route(path = PagePath.SQUARE)
class SquareFragment : BaseVMFragment<FragmentSquareBinding>(R.layout.fragment_square),
    OnRefreshListener, OnLoadMoreListener, VuCallBack<TestPostBean> {

    private val mViewModel by lazy { ViewModelProvider(this).get(SquarePostsViewModel::class.java) }

    private val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }

    private val mPostsListData = mutableListOf<TestPostBean>()

    private var isLoadMore = false

    override fun onBack(): Boolean {
        return false
    }

    override fun startObserve() {
        mViewModel.postsData.observe(this, {
            if (it == null) {
                if (isLoadMore) {
                    binding.refreshLayout.finishLoadMore()
                } else {
                    binding.refreshLayout.finishRefresh()
                }
                return@observe
            }
            if (!isLoadMore) {
                mPostsListData.clear()
                binding.refreshLayout.finishRefresh()
                binding.refreshLayout.setEnableLoadMore(true)
                it.list?.run {
                    mPostsListData.addAll(this)
                    mAdapter.notifyDataSetChanged()
                }
            } else {
                binding.refreshLayout.finishLoadMore()
                if (it.list?.isNullOrEmpty() == true) {
                    binding.refreshLayout.setEnableLoadMore(false)
                }
                val start = mPostsListData.size
                it.list?.run {
                    mPostsListData.addAll(this)
                    mAdapter.notifyItemRangeInserted(start, this.size)
                }
            }

        })
    }

    override fun initView() {
        binding.apply {
            mAdapter.items = mPostsListData
            mAdapter.register(BaseViewBinder(ItemPostView::class.java, this@SquareFragment))
            refreshLayout.setRefreshFooter(ClassicsFooter(context))
            refreshLayout.setOnRefreshListener(this@SquareFragment)
            refreshLayout.setOnLoadMoreListener(this@SquareFragment)
            binding.rvSquare.layoutManager = LinearLayoutManager(activity)
            binding.rvSquare.adapter = mAdapter
            //快速滑动暂停fresco图片加载
            binding.rvSquare.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_SETTLING) {
                        imagePause()
                    } else if (newState == SCROLL_STATE_IDLE) {
                        imageResume()
                    }
                }

            })
            binding.pageStateLayout.setOnClickListener {
                isLoadMore = false
                getPostsData()
            }
        }
    }

    override fun initData() {
        super.initData()
        initCacheData()
    }

    private var pageIndex = 1
    private var from = ""

    /**
     * 首次加载缓存数据，缓存没数据加载网络数据
     */
    private fun initCacheData() {
        isLoadMore = false
        mViewModel.getCachePostsData(pageIndex)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isLoadMore = false
        getPostsData()
    }

    private fun getPostsData() {
        pageIndex = 1
        mViewModel.getPostsData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        pageIndex++
        mViewModel.getPostsData()
    }

    // 暂停图片请求
    fun imagePause() {
        Fresco.getImagePipeline().pause();
    }

    // 恢复图片请求
    fun imageResume() {
        Fresco.getImagePipeline().resume();
    }

    private var mUserInfo: UserInfoBean? = null

    override fun onCallBack(data: TestPostBean, pos: Int) {
    }
}
