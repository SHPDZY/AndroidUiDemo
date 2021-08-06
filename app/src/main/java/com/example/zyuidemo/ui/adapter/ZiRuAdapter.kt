package com.example.zyuidemo.ui.adapter

import android.view.View
import com.example.libcommon.beans.ZiRuImageBean
import com.youth.banner.adapter.BannerAdapter
import com.example.libcore.multitype.BaseViewHolder
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.libcore.multitype.vu.BaseVu
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentSensorManagerBinding
import com.example.zyuidemo.databinding.ItemImageSingleViewBinding
import com.example.zyuidemo.databinding.ItemZiRuViewBinding
import com.example.zyuidemo.vm.ZiRuModel
import kotlin.math.max
import kotlin.math.min

/**
 * @author : zhangyong
 * @version :
 * @date : 2021/8/6
 * @desc :
 */
class ZiRuAdapter(datas: List<ZiRuImageBean>?) :
    BannerAdapter<ZiRuImageBean?, BaseViewHolder<ZiRuImageBean>?>(datas) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ZiRuImageBean> {
        val imageItemView = ImageItemView()
        imageItemView.init(parent.context)
        imageItemView.getView().layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return BaseViewHolder(imageItemView)
    }

    override fun onBindView(
        holder: BaseViewHolder<ZiRuImageBean>?,
        data: ZiRuImageBean?,
        position: Int,
        size: Int
    ) {
        if (data != null) {
            holder?.bindData(data)
        }
    }
}

class ImageItemView : BaseVu<ItemZiRuViewBinding, ZiRuImageBean>(), View.OnClickListener {

    private val mVm by lazy { ViewModelProvider(getContext() as FragmentActivity).get(ZiRuModel::class.java) }
    private var gyroscopeX = 0f
    private var gyroscopeY = 0f

    override fun getLayoutId(): Int {
        return R.layout.item_zi_ru_view
    }

    override fun bindData(data: ZiRuImageBean) {
        binding.sdvSingle.scaleX = 1.5f
        binding.sdvSingle.scaleY = 1.5f
        binding.sdvSingle.setImageResource(R.drawable.ic_sensor_bac)
        mVm.data.observe(getContext() as FragmentActivity,{
            binding.handleViewTranslationX(it.gyroscopeX)
            binding.handleViewTranslationY(it.gyroscopeY)
        })
    }

    private fun ItemZiRuViewBinding.handleViewTranslationY(angleX: Float) {
        if (gyroscopeX == 0f) {
            gyroscopeX = angleX
            return
        }
        val diffValue = gyroscopeX - angleX
        if (Math.abs(diffValue) < 0.1) return
        gyroscopeX = angleX
        val tranY2 = ivMoon.translationY + diffValue * 2
        val tranY4 = ivStar.translationY + diffValue * 4
        ivMoon.translationY = handleY(tranY2)
        ivStar.translationY = handleY(tranY4)
    }

    private fun ItemZiRuViewBinding.handleViewTranslationX(angleY: Float) {
        if (gyroscopeY == 0f) {
            gyroscopeY = angleY
            return
        }
        val diffValue = gyroscopeY - angleY
        if (Math.abs(diffValue) < 0.1) return
        gyroscopeY = angleY
        val tranX1 = sdvSingle.translationX - diffValue
        val tranX2 = ivMoon.translationX + diffValue * 2
        val tranX4 = ivStar.translationX + diffValue * 4
        sdvSingle.translationX = handleX(tranX1)
        ivMoon.translationX = handleX(tranX2)
        ivStar.translationX = handleX(tranX4)
    }

    private val gyroscopeMaxX = 80f
    private val gyroscopeMaxY = 40f

    private fun handleY(tranY: Float) =
        if (tranY > 0) min(tranY, gyroscopeMaxY) else max(tranY, -gyroscopeMaxY)

    private fun handleX(tranX: Float) =
        if (tranX > 0) min(tranX, gyroscopeMaxX) else max(tranX, -gyroscopeMaxX)

    override fun onClick(p0: View?) {

    }

}

