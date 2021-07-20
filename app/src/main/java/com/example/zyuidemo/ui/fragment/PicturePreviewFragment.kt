package com.example.zyuidemo.ui.fragment

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.app.SharedElementCallback
import android.graphics.Color
import android.view.View
import android.view.ViewTreeObserver
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.beans.UgcPictureChangeEvent
import com.example.libcommon.constant.AutoWiredKey
import com.example.libcommon.constant.DataSaverConstants
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.DataSaver
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.VuCallBack
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentPicturePreviewBinding
import com.example.zyuidemo.ui.adapter.PageListView
import com.example.zyuidemo.ui.adapter.PictureItem
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.DraweeTransition
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.interfaces.OnDragChangeListener
import com.youth.banner.util.BannerUtils

@Route(path = PagePath.FRAGMENT_PICTURE_PREVIEW)
open class PicturePreviewFragment : BaseVMFragment<FragmentPicturePreviewBinding>(R.layout.fragment_picture_preview)
    , OnDragChangeListener, VuCallBack<PictureItem> {

    private var isSharedElement: Boolean = false

    @Autowired(name = AutoWiredKey.picList)
    @JvmField
    var list: ArrayList<String>? = null

    @Autowired(name = AutoWiredKey.picIdx)
    @JvmField
    var idx: Int = 0

    val mAdapter: MultiTypeAdapter by lazy { MultiTypeAdapter() }

    private var argbEvaluator = ArgbEvaluator()
    private var bgColor = Color.rgb(0, 0, 0)

    override fun initView() {
        super.initView()
        if (list.isNullOrEmpty()) {
            finish()
            return
        }
        activity?.window?.sharedElementEnterTransition = DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.FIT_CENTER) // 进入
        activity?.window?.sharedElementReturnTransition = DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.CENTER_CROP) // 返回

        activity?.supportPostponeEnterTransition()

        binding.imgBack.setOnClickListener {
            startExitAnim()
        }
        binding.photoViewContainer.setOnDragChangeListener(this)
        initViewPager()
    }

    override fun isStatusbarLightmode(): Boolean {
        return false
    }

    override fun onBack(): Boolean {
        startExitAnim()
        return true
    }

    private fun getPictures(): List<PictureItem> {
        val pictures = mutableListOf<PictureItem>()
        list?.run {
            this.forEachIndexed { index, s ->
                if (!s.isNullOrBlank()) {
                    pictures.add(PictureItem(index, s))
                }
            }
        }
        return pictures
    }

    @SuppressLint("SetTextI18n")
    private fun initViewPager() {
        val list = getPictures()
        binding.rIndicator.indicatorConfig.run {
            selectedWidth = BannerUtils.dp2px(16f).toInt()
            indicatorSpace = BannerUtils.dp2px(4f).toInt()
            height = BannerUtils.dp2px(4f).toInt()
            radius = 0
            normalColor = resources.getColor(R.color.white)
            selectedColor = resources.getColor(R.color.colorSlBrand)
            binding.rIndicator.onPageChanged(list.size, idx)
        }
        val setIndicator = { position: Int, size: Int ->
            currentUrl = list[position].url + position
            binding.indicator.text = "${position + 1}/$size"
            binding.rIndicator.onPageSelected(position)
        }
        setIndicator(idx, list.size)
        mAdapter.items = list
        val baseViewBinder = BaseViewBinder(PageListView::class.java, this)
        mAdapter.register(baseViewBinder)
        binding.pager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            adapter = mAdapter
            setCurrentItem(idx, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setIndicator(position, list.size)
                }
            })
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    LogUtils.e("onGlobalLayout")
                    activity?.supportStartPostponedEnterTransition()
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
        setEnterShareCallback()
    }

    private fun startExitAnim() {
        //判断是否需要动画
        val currentPosition = binding.rIndicator.indicatorConfig.currentPosition
        val indicatorSize = binding.rIndicator.indicatorConfig.indicatorSize
        //isSharedElement = true 说明有共享元素
        //然后判断pager有没有在帖子展示的图片范围内，超过范围就不做动画
        if (isSharedElement) {
            if (indicatorSize in 1..3) {
                isSharedElement = currentPosition in 0..1
            } else if (indicatorSize in 1..9) {
                isSharedElement = currentPosition in 0..3
            }
        }
        if (isSharedElement) {
            DataSaver.saveString(DataSaverConstants.KEY_PREVIEW_URL, currentUrl)
            activity?.supportFinishAfterTransition()
        } else {
            activity?.finish()
            activity?.overridePendingTransition(0, R.anim.picture_anim_fade_out)
        }
    }

    override fun onRelease() {
        startExitAnim()
    }

    var isPostChangeItem = false
    override fun onDragChange(dy: Int, scale: Float, fraction: Float) {
        if (!isPostChangeItem)
            LiveEventBus.get(UgcPictureChangeEvent.ugc_pic_key).post(UgcPictureChangeEvent())
        isPostChangeItem = true
        binding.clBox.setBackgroundColor(argbEvaluator.evaluate(fraction * .8f, bgColor, Color.TRANSPARENT) as Int)
    }

    override fun onCallBack(data: PictureItem, pos: Int) {
        startExitAnim()
    }

    var currentUrl = ""
    private fun setEnterShareCallback() {
        //设置共享元素们的回调
        activity?.setEnterSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(names: MutableList<String?>, sharedElements: MutableMap<String?, View?>) {
                isSharedElement = true
                sharedElements.clear()
                names.clear()
                names.add(currentUrl)
                sharedElements[currentUrl] = binding.pager.findViewWithTag<View>(binding.pager.currentItem)
            }

        })
    }

}