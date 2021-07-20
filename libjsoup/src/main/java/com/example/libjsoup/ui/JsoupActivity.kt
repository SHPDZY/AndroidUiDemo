package com.example.libjsoup.ui

import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.beans.ItemSearchBean
import com.example.libcommon.beans.ItemSearchNavBean
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.launchDelay
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.VuCallBack
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libjsoup.R
import com.example.libjsoup.adapter.SearchNavRecyclerView
import com.example.libjsoup.adapter.SearchRecyclerView
import com.example.libjsoup.databinding.ActivityJsoupBinding
import com.example.libjsoup.vm.JsoupViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

@Route(path = PagePath.GROUP_JSOUP_ACTIVITY)
class JsoupActivity : BaseVMActivity<ActivityJsoupBinding>(R.layout.activity_jsoup),
    View.OnClickListener {
    private val viewModel by lazy { ViewModelProvider(this).get(JsoupViewModel::class.java) }

    private val items: MutableList<ItemSearchBean> = mutableListOf()
    private val itemsNav: MutableList<ItemSearchNavBean> = mutableListOf()
    private val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }
    private val mAdapterNav: MultiTypeAdapter by lazy { MultiTypeAdapter() }


    override fun initView() {
        binding.click = this
        mAdapter.items = items
        mAdapter.register(BaseViewBinder(SearchRecyclerView::class.java))
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerView.adapter = mAdapter

        mAdapterNav.items = itemsNav
        mAdapterNav.register(BaseViewBinder(SearchNavRecyclerView::class.java,object :
            VuCallBack<ItemSearchNavBean> {
            override fun onCallBack(data: ItemSearchNavBean, pos: Int) {
                handleParseHtml(data.url)
            }

        }))
        binding.rvNav.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
        binding.rvNav.adapter = mAdapterNav
        launchDelay {
            KeyboardUtils.showSoftInput(binding.etHtml)
        }
    }

    override fun startObserve() {
        viewModel.htmlData.observe(this, {
            dismissDialog()
            it?.run {
                items.clear()
                items.addAll(this)
                mAdapter.notifyDataSetChanged()
            }
        })
        viewModel.navData.observe(this, {
            it?.run {
                itemsNav.clear()
                itemsNav.addAll(this)
                mAdapterNav.notifyDataSetChanged()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnParseHtml -> {
                KeyboardUtils.hideSoftInput(binding.etHtml)
                handleParseHtml("https://cn.bing.com/search?q=${binding.etHtml.text}")
            }
        }
    }

    private fun handleParseHtml(url: String?) {
        showDialog()
        viewModel.parseHtml(url.toString())
    }

    private var xPopup: BasePopupView? = null

    private fun showDialog() {
        if (xPopup == null) {
            xPopup = XPopup.Builder(this).asLoading()
        }
        xPopup?.show()
    }

    private fun dismissDialog() {
        xPopup?.smartDismiss()
    }

}