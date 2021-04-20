package com.busi.square.ui.adapter

import android.app.SharedElementCallback
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NumberUtils
import com.blankj.utilcode.util.TimeUtils
import com.example.zyuidemo.R
import com.example.zyuidemo.base.multitype.vu.BaseVu
import com.example.zyuidemo.base.multitype.vu.Vu
import com.example.zyuidemo.base.multitype.vu.VuCallBack
import com.example.zyuidemo.beans.PostsListBean
import com.example.zyuidemo.beans.TagsBean
import com.example.zyuidemo.beans.TestPostBean
import com.example.zyuidemo.beans.UgcPictureChangeEvent
import com.example.zyuidemo.constant.AutoWiredKey
import com.example.zyuidemo.constant.MmkvConstants.KEY_PREVIEW_URL
import com.example.zyuidemo.databinding.ItemPostViewBinding
import com.example.zyuidemo.router.*
import com.example.zyuidemo.ui.adapter.ImageRecyclerView
import com.example.zyuidemo.ui.adapter.ImageSingleView
import com.example.zyuidemo.utils.MmkvUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.jeremyliao.liveeventbus.LiveEventBus
import com.noober.background.view.BLTextView
import java.util.*
import kotlin.collections.ArrayList

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class ItemPostView : BaseVu<ItemPostViewBinding, TestPostBean>(), VuCallBack<ArrayList<String>>, View.OnClickListener {
    companion object {
        const val TAG_TV_COUNT_LIKE = ""
    }

    private val mImageSingleView: ImageSingleView by lazy { ImageSingleView() }
    private val mImageRecyclerView: ImageRecyclerView by lazy { ImageRecyclerView() }
    private lateinit var mData: TestPostBean
    override fun getLayoutId(): Int {
        return R.layout.item_post_view
    }

    override fun bindData(data: TestPostBean) {
        mData = data
        binding.includeHead.nick = data.nick
        binding.includeHead.time = TimeUtils.getFriendlyTimeSpanByNow(data.publishedTime.toString())
        binding.includeBottom.countLike = formatCount(data.likeCount)
        binding.includeBottom.countComment = formatCount(data.commentCount)
        binding.includeBottom.countShare = formatCount(data.shareCount)
        binding.includeBottom.tvCountLike.isSelected = data.isLiked == 1
        binding.includeBottom.tvCountLike.tag = TAG_TV_COUNT_LIKE + adapterPosition
        binding.includeContent.ivTopic.isVisible = !data.tags.isNullOrEmpty()
        binding.includeContent.layoutTopic.isVisible = !data.tags.isNullOrEmpty()
        binding.includeContent.layoutTopic.tagTypeface = Typeface.DEFAULT_BOLD
        binding.includeContent.ivLocation.isVisible = !data.location.isNullOrEmpty()
        binding.includeContent.tvLocation.isVisible = !data.location.isNullOrEmpty()
        binding.includeContent.tvLocation.text = data.location
        binding.includeContent.layoutTopic.removeAllTags()
        data.tags?.run {
            if (size > 5) {
                data.tags = data.tags!!.subList(0, 5) as ArrayList<String>
            }
            this.forEach {
                binding.includeContent.layoutTopic.addTag(it)
            }
        }
        //单张图单独加载，多图直接recyclerview加载
        binding.includeImage.removeAllViews()
        binding.includeImage.isVisible = data.pictures?.size != 0
        if (data.pictures?.size == 0) {
            setContentLinesAndVisable(5, data.description, !TextUtils.isEmpty(data.description))
        } else {
            setContentLinesAndVisable(2, data.description, !TextUtils.isEmpty(data.description))
            data.pictures?.run { setPictures() }
        }
        loadImageUrl(data.avatar, binding.includeHead.ivHead)
        binding.root.setOnClickListener(this)
        binding.includeBottom.click = this
        binding.includeHead.click = this
    }

    private fun formatCount(count: Int): String? {
        return when {
            count in 1000..9999 -> {
                String.format("%sk", NumberUtils.format(count / 1000f, 1, false))
            }
            count in 10000..999999 -> {
                String.format("%sw", NumberUtils.format(count / 10000f, 1, false))
            }
            count >= 1000000 -> {
                String.format("%sw", count / 10000)
            }
            else -> {
                count.toString()
            }
        }
    }

    private fun setContentLinesAndVisable(lines: Int, description: String?, isVisble: Boolean) {
        binding.includeContent.tvContent.maxLines = lines
        binding.includeContent.content = description
        binding.includeContent.tvContent.isVisible = isVisble
    }

    private fun loadImageUrl(img: String?, it: SimpleDraweeView) {
        if (img.isNullOrEmpty()) {
            it.setActualImageResource(R.mipmap.ic_launcher)
            return
        }
        val controller = Fresco.newDraweeControllerBuilder()
        val hierarchy = GenericDraweeHierarchyBuilder.newInstance(it.resources)
                .setPlaceholderImage(R.mipmap.ic_launcher)
                .setFailureImage(R.drawable.picture_icon_data_error)
        controller.setUri(Uri.parse(img))
        controller.autoPlayAnimations = true
        controller.lowResImageRequest = ImageRequest.fromUri("$img?x-oss-process=image/resize,w_10/quality,Q_30")
        controller.oldController = it.controller
        it.hierarchy = hierarchy.build()
        it.controller = controller.build()
    }

    private fun ArrayList<String>.setPictures() {
        when (size) {
            0 -> {
            }
            1 -> {
                firstOrNull()?.setSinglePicture()
            }
            else -> {
                setPictures(this, mImageRecyclerView)
                mImageRecyclerView.mVuCallBack = this@ItemPostView
            }
        }
    }

    private fun String.setSinglePicture() {
        mImageSingleView.init(binding.root.context)
        mImageSingleView.isSingle = true
        binding.includeImage.addView(mImageSingleView.getView())
        mImageSingleView.bindData(this)
        mImageSingleView.mVuCallBack = mOneViewVuCallBack
    }

    private fun setPictures(data: ArrayList<String>, view: Vu<ArrayList<String>>) {
        view.init(binding.root.context)
        binding.includeImage.addView(view.getView())
        view.bindData(data)
    }

    private fun jumpPicturePreview(imageList: ArrayList<String>, index: Int) {
        ActivityUtils.getTopActivity()?.run {
            //通过tag找到需要共享的view
            val transitionName = imageList[index] + index
            var sharedElement = binding.includeImage.findViewWithTag<View>(transitionName)
            ViewCompat.setTransitionName(sharedElement, transitionName)
            val makeSceneTransitionAnimation = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, sharedElement, transitionName)
            //设置监听 预览页item切换位置后，更新共享view
            LiveEventBus.get(UgcPictureChangeEvent.ugc_pic_key).observeForever {
                sharedElement.alpha = 1f
            }
            this.setExitSharedElementCallback(object : SharedElementCallback() {

                override fun onSharedElementEnd(sharedElementNames: List<String>, sharedElements: List<View>, sharedElementSnapshots: List<View>) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                    for (view in sharedElements) {
                        view.visibility = View.VISIBLE
                        view.alpha = 1f
                    }
                }

                override fun onCaptureSharedElementSnapshot(sharedElement: View?, viewToGlobalMatrix: Matrix?, screenBounds: RectF?): Parcelable {
                    sharedElement?.alpha = 1f
                    return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds)
                }

                override fun onMapSharedElements(names: MutableList<String?>, sharedElements: MutableMap<String?, View?>) {
                    val currentUrl = MmkvUtils.getInstance().getString(KEY_PREVIEW_URL, "")
                    if (!TextUtils.isEmpty(currentUrl)) {
                        names.clear()
                        sharedElements.clear()
                        names.add(currentUrl)
                        sharedElements[currentUrl] = binding.includeImage.findViewWithTag<View>(currentUrl)
                        MmkvUtils.getInstance().put(KEY_PREVIEW_URL, "")
                    }
                }

            })
            ARouter.getInstance().transFragmentPage(PagePath.FRAGMENT_PICTURE_PREVIEW)
                    .withStringArrayList(AutoWiredKey.picList, imageList)
                    .withInt(AutoWiredKey.picIdx, index)
                    .withOptionsCompat(makeSceneTransitionAnimation)
                    .navigation(this)
        }

    }

    private val mOneViewVuCallBack = object : VuCallBack<String> {
        override fun onCallBack(data: String, pos: Int) {
            jumpPictureOrVideo(arrayListOf(data), pos)
        }
    }

    override fun onCallBack(data: ArrayList<String>, pos: Int) {
        jumpPictureOrVideo(data,pos)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_count_like -> {
                val blTextView = v as BLTextView
                val isLike = mData.isLiked.xor(1) == 1
                val likeCount = mData.likeCount
                blTextView?.run {
                    text = "${
                        if (!isLike) {
                            likeCount - 1
                        } else {
                            likeCount + 1
                        }
                    }"
                    isSelected = isLike
                }
                mVuCallBack?.onCallBack(mData, adapterPosition)
            }
            R.id.tv_count_share -> {
            }
            R.id.iv_head, R.id.tv_name, R.id.tv_time -> {

                mData.userNo?.run {
                    ViewCompat.setTransitionName(binding.includeHead.ivHead, "transition_name_head$this")
                    ViewCompat.setTransitionName(binding.includeHead.tvName, "transition_name_name$this")
                    val makeSceneTransitionAnimation = ActivityUtils.getTopActivity()?.let {
                        ActivityOptionsCompat.makeSceneTransitionAnimation(it,
                                Pair.create(binding.includeHead.ivHead, "transition_name_head$this"),
                                Pair.create(binding.includeHead.tvName, "transition_name_name$this"))
                    }
                    val selfUserNo = loginService().getUserInfo()?.userNo
                    ARouter.getInstance()
                            .fragmentPage(if (this == selfUserNo) PagePath.PERSONAL_CENTER else PagePath.FRAGMENT_OTHERS_ZONE)
                            .withString(AutoWiredKey.userNo, this)
                            .withString(AutoWiredKey.userAvatar, mData.avatar)
                            .withString(AutoWiredKey.userName, mData.nick)
                            .withOptionsCompat(makeSceneTransitionAnimation)
                            .navigation(ActivityUtils.getTopActivity())
                }
            }
            else -> {
                if (routeVideoDetail()) return
            }
        }
    }

    private fun jumpPictureOrVideo(data: ArrayList<String>, pos: Int) {
        if (routeVideoDetail()) return
        jumpPicturePreview(data, pos)
    }

    private fun routeVideoDetail(): Boolean {
        if (mData.type == 2) {
            return true
        }
        return false
    }

}