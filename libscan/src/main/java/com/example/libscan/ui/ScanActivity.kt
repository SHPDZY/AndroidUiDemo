package com.example.libscan.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.constant.DataSaverConstants
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.scanService
import com.example.libcommon.utils.DataSaver
import com.example.libcommon.utils.isVisible
import com.example.libcommon.utils.removeItemDecorationAtV2
import com.example.libcommon.utils.toast
import com.example.libcommon.widget.CommonGridItemDecoration
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libscan.LibScanUtils
import com.example.libscan.R
import com.example.libscan.adapter.GoodsItemView
import com.example.libscan.databinding.ActivityScanBinding
import com.example.libscan.entity.GoodsData
import com.example.libscan.entity.GoodsListData
import com.example.libscan.vm.ScanViewModel
import com.google.zxing.integration.android.IntentIntegrator
import java.io.IOException


@Route(path = PagePath.GROUP_SCAN_ACTIVITY)
class ScanActivity : BaseVMActivity<ActivityScanBinding>(R.layout.activity_scan),
    View.OnClickListener {
    private val viewModel by lazy { ViewModelProvider(this).get(ScanViewModel::class.java) }
    private lateinit var listData: GoodsListData

    private val items: MutableList<GoodsData> = mutableListOf()
    private val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.click = this
        mAdapter.items = items
        mAdapter.register(BaseViewBinder(GoodsItemView::class.java))
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.removeItemDecorationAtV2(0)
        binding.recyclerView.addItemDecoration(CommonGridItemDecoration(0, 5, 8, 4))
        binding.recyclerView.adapter = mAdapter

        binding.etHtml.doAfterTextChanged {
            binding.ivClear.isVisible = !TextUtils.isEmpty(it)
            if (TextUtils.isEmpty(it)) {
                items.clear()
                items.addAll(listData.goodsList)
                mAdapter.notifyDataSetChanged()
            } else {
                val bySearch = findGoodsDataBySearch(it.toString())
                items.clear()
                items.addAll(bySearch)
                mAdapter.notifyDataSetChanged()
            }
        }
        binding.etHtml.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                doSearch()
            }
            true
        }
        LibScanUtils.initSpeech(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LibScanUtils.onDestroy()
    }

    override fun startObserve() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivClear -> {
                binding.etHtml.setText("")
            }
            binding.tvSearch -> {
                doSearch()
            }
            binding.tvScan -> {
                LibScanUtils.initScan(this)
            }
            binding.ivAdd -> {
                scanService()?.openAddActivity()
            }
            binding.tvOutPut -> {
                LibScanUtils.shareFile(this)
            }
            binding.tvInput -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*" // 设置文件类型为任意类型
                intent.addCategory(Intent.CATEGORY_OPENABLE) // 设置可打开的文件
                startActivityForResult(Intent.createChooser(intent, "选择文件"), 201)
            }

        }
    }

    private fun doSearch() {
        val code = binding.etHtml.text?.trim()?.toString()
        KeyboardUtils.hideSoftInput(binding.etHtml)
        if (code.isNullOrEmpty()) {
            toast("请输入商品条形码或商品名称")
            return
        }
        doSearchGoods(code)
    }

    override fun onResume() {
        super.onResume()
        getGoodsData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getGoodsData(list: GoodsListData? = null) {
        if (list == null) {
            listData =
                DataSaver.getObject<GoodsListData>(DataSaverConstants.KEY_SCAN_GOODS_LIST)
                    ?: GoodsListData()
        }
        items.clear()
        items.addAll(listData.goodsList)
        mAdapter.notifyDataSetChanged()
        LibScanUtils.writeGoodsList(this, listData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
            if (scanResult != null && scanResult.contents != null) {
                val result = scanResult.contents
                doSearchGoods(result)
            }
        } else if (requestCode == 201 && resultCode == RESULT_OK && intent != null) {
            val uri = intent.data ?: return
            val filePath: String = uri.path.toString()
            try {
                listData = LibScanUtils.writeGoodsList(this, uri)
                getGoodsData(listData)
                // 在这里使用文件内容进行后续操作
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun doSearchGoods(code: String?) {
        var findGoodsData = findGoodsDataByCode(code)
        if (findGoodsData == null) {
            findGoodsData = findGoodsDataByName(code)
        }
        if (findGoodsData != null) {
            GoodsDialog.newInstance(findGoodsData).show(this)
        } else {
            AlertDialog.Builder(this)
                .setMessage("未查询到商品，是否去添加？")
                .setPositiveButton("取消", null)
                .setNegativeButton("去添加") { _, _ ->
                    scanService()?.openAddActivity(code)
                }
                .show()
        }
    }


    private fun handleParseHtml(url: String?) {
        showDialog()
    }

    private fun findGoodsDataByCode(goodsCode: String?) = listData.goodsList.find {
        it.code == goodsCode
    }

    private fun findGoodsDataByName(goodsName: String?) = listData.goodsList.find {
        it.name == goodsName
    }

    private fun findGoodsDataBySearch(search: String) = listData.goodsList.filter {
        it.name?.contains(search) == true || it.code?.contains(search) == true || it.price?.contains(
            search
        ) == true || it.des?.contains(search) == true
    }

}