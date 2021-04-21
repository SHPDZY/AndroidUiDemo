package com.example.zyuidemo.ui.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.example.zyuidemo.R
import com.example.zyuidemo.base.multitype.vu.BaseVu
import com.example.zyuidemo.databinding.ItemImageSingleViewBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.Postprocessor
import com.facebook.imageutils.BitmapUtil

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
class ImageSingleView : BaseVu<ItemImageSingleViewBinding, String>(), View.OnClickListener {

    private lateinit var mImage: String
    var isSingle: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.item_image_single_view
    }

    override fun bindData(data: String) {
        mImage = data
        binding.sdvSingle.tag = mImage + adapterPosition
        binding.sdvSingle.let {
            if (isSingle) {
                it.aspectRatio = 1.33f
            } else {
                it.aspectRatio = 1f
            }
            val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data))
                .setResizeOptions(ResizeOptions(100, 100))
                .setRequestPriority(Priority.LOW)
                .build()
            val lowResImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data))
                .setResizeOptions(ResizeOptions(10, 10))
                .setRequestPriority(Priority.LOW)
                .setLocalThumbnailPreviewsEnabled(true)
                .setPostprocessor(IterativeBoxBlurPostProcessor(10, 10))
                .build()
            val hierarchy = GenericDraweeHierarchyBuilder.newInstance(it.resources)
                .setFailureImage(R.drawable.picture_icon_data_error)
            val controller = Fresco.newDraweeControllerBuilder()
            controller.imageRequest = request
//            controller.setUri(Uri.parse(data))
            controller.autoPlayAnimations = true
            controller.lowResImageRequest = lowResImageRequest
            controller.oldController = it.controller
            controller.controllerListener = object : BaseControllerListener<ImageInfo?>() {

                override fun onFinalImageSet(
                    id: String?,
                    imageInfo: ImageInfo?,
                    animatable: Animatable?
                ) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    if (imageInfo == null || !isSingle) {
                        return
                    }
                    if (imageInfo.height > imageInfo.width) {
                        it.aspectRatio = 0.75f
                    } else {
                        it.aspectRatio = 1.33f
                    }
                }

            }
            it.hierarchy = hierarchy.build()
            it.controller = controller.build()
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        mVuCallBack?.onCallBack(mImage, adapterPosition)
    }
}