package com.example.libscan.ui

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.constant.DataSaverConstants
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterConstants
import com.example.libcommon.utils.BitmapUtils
import com.example.libcommon.utils.DataSaver
import com.example.libcommon.utils.isVisible
import com.example.libcommon.utils.toast
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libscan.LibScanUtils
import com.example.libscan.R
import com.example.libscan.databinding.ActivityAddBinding
import com.example.libscan.entity.GoodsData
import com.example.libscan.entity.GoodsListData
import com.example.libscan.vm.ScanViewModel
import com.google.zxing.integration.android.IntentIntegrator


@Route(path = PagePath.GROUP_ADD_ACTIVITY)
class AddActivity : BaseVMActivity<ActivityAddBinding>(R.layout.activity_add),
    View.OnClickListener {

    @Autowired(name = RouterConstants.SCAN_CODE)
    @JvmField
    public var clickCode: String? = null

    private lateinit var listData: GoodsListData
    private var goodsCode: String? = ""
    private var isEdit = false
    private var bitmapToBase64: String? = ""
    private val viewModel by lazy { ViewModelProvider(this).get(ScanViewModel::class.java) }
    var goodsData = GoodsData("", "", "", "", "")

    override fun initView() {
        listData =
            DataSaver.getObject<GoodsListData>(DataSaverConstants.KEY_SCAN_GOODS_LIST)
                ?: GoodsListData()
        LogUtils.e("clickCode:$clickCode")
        LogUtils.e("listData:${GsonUtils.toJson(listData)}")
        if (!TextUtils.isEmpty(clickCode)) {
            goodsCode = clickCode
            val findGoods = findGoodsData(clickCode)
            if (findGoods != null) {
                goodsData = findGoods
                isEdit = true
                handleSaveEditTextView()
                handleGoodsData()
            } else {
                isEdit = false
                handleSaveEditTextView()
                binding.tvScan.text = goodsCode
            }
        }
        binding.click = this
        binding.commonAppBar.setTitle("上传商品信息")
        binding.commonAppBar.setOnBackListener {
            onBackPressed()
        }
    }

    override fun startObserve() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivSave -> {
                val name = binding.etName.text?.trim()?.toString()
                val price = binding.etPrice.text?.trim()?.toString()
                val description = binding.etDescr.text?.trim()?.toString()
                if (TextUtils.isEmpty(goodsCode)) {
                    toast("请扫描商品条形码")
                    return
                }
                if (TextUtils.isEmpty(name)) {
                    toast("请输入商品名称")
                    return
                }
                if (TextUtils.isEmpty(price)) {
                    toast("请输入商品价格")
                    return
                }
                goodsData.code = goodsCode
                goodsData.name = name
                goodsData.price = price
                goodsData.des = description
                goodsData.img = bitmapToBase64.toString()
                val findGoods = findGoodsData(goodsCode)
                if (findGoods != null) {
                    listData.goodsList.remove(findGoods)
                    listData.goodsList.add(0, goodsData)
                    toast("该商品已存在，已为您更新商品信息")
                } else {
                    listData.goodsList.add(0, goodsData)
                    toast("保存成功")
                }
                DataSaver.saveObject(DataSaverConstants.KEY_SCAN_GOODS_LIST, listData)
                onBackPressed()
            }
            binding.tvScan -> {
                initScan()
            }
            binding.ivDelete -> {
                AlertDialog.Builder(this)
                    .setMessage("是否确认删除？")
                    .setPositiveButton("删除") { _, _ ->
                        val findGoods = findGoodsData(goodsCode)
                        if (findGoods != null) {
                            listData.goodsList.remove(findGoods)
                            toast("删除成功")
                            DataSaver.saveObject(DataSaverConstants.KEY_SCAN_GOODS_LIST, listData)
                            onBackPressed()
                        }
                    }
                    .setNegativeButton("取消", null)
                    .show()
            }
            binding.ivSelectImg -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, 201)
                }
            }
        }
    }

    fun initScan() {
        LibScanUtils.initScan(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
            if (scanResult != null && scanResult.contents != null) {
                goodsCode = scanResult.contents
                val findGoods = findGoodsData(goodsCode)
                if (findGoods != null) {
                    goodsData = findGoods
                    isEdit = true
                    toast("该商品已存在")
                    handleSaveEditTextView()
                    handleGoodsData()
                } else {
                    isEdit = false
                    handleSaveEditTextView()
                }
            }
        } else if (requestCode == 201 && resultCode == RESULT_OK) {
            val imageBitmap = intent?.extras?.get("data") as? Bitmap ?: return
            bitmapToBase64 = BitmapUtils.convertBitmapToBase64(imageBitmap)
            val base64ToBitmap = BitmapUtils.convertBase64ToBitmap(bitmapToBase64)
            binding.ivSelectImg.setImageBitmap(base64ToBitmap)
        }
    }

    private fun handleSaveEditTextView() {
        binding.ivSave.text = if (isEdit) "更新商品" else "添加商品"
        binding.ivDelete.isVisible = isEdit
        binding.tvScan.text = goodsCode
    }

    private fun findGoodsData(goodsCode: String?) = listData.goodsList.find {
        it.code == goodsCode
    }

    private fun handleGoodsData() {
        binding.etName.setText(goodsData.name)
        binding.etPrice.setText(goodsData.price)
        binding.etDescr.setText(goodsData.des)
        binding.tvScan.text = goodsData.code
        bitmapToBase64 = goodsData.img
        if (TextUtils.isEmpty(bitmapToBase64)) {
            binding.ivSelectImg.setImageResource(R.drawable.picture_image_placeholder)
        } else {
            val base64ToBitmap = BitmapUtils.convertBase64ToBitmap(bitmapToBase64)
            binding.ivSelectImg.setImageBitmap(base64ToBitmap)
        }
    }

    private fun handleParseHtml(url: String?) {
        showDialog()
    }

}