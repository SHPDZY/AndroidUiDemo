package com.example.zyuidemo.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.libcommon.beans.ZiRuImageBean
import com.example.libcore.multitype.BaseViewHolder
import com.example.libcore.multitype.vu.BaseVu
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.ItemZiRuViewBinding
import com.example.zyuidemo.vm.ZiRuModel
import com.example.zyuidemo.widget.ZiRuLayout.Companion.DIRECTION_LEFT
import com.example.zyuidemo.widget.ZiRuLayout.Companion.DIRECTION_RIGHT
import com.youth.banner.adapter.BannerAdapter
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

    override fun getLayoutId(): Int {
        return R.layout.item_zi_ru_view
    }

    override fun bindData(data: ZiRuImageBean) {
        binding.zrBac.scaleX = 1.1f
        binding.zrBac.setDirection(DIRECTION_LEFT)
        binding.zrText.setDirection(DIRECTION_RIGHT)
        binding.sdvSingle.setImageResource(data.url_bac)
        binding.ivAdText.setImageResource(data.top_ad)
        binding.ivAdIcon.setImageResource(data.top_ad_ic)
    }

    override fun onClick(p0: View?) {

    }

}

