package com.example.libcommon.utils;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author : zhangyong
 * @version :
 * @date : 2021/8/17
 * @desc :
 */
public class GlideUtils {
    /**
     * 加载Gif动图
     *
     * @param view         imageview
     * @param url          图片路径  "https://imgjike.ui.cn/data/singles/7e47416df391c7bc0426105ecfd943e9.gif";
     * @param maxLoopCount 循环次数
     * @param delay        单帧播放时长
     */
    public static void loadGif(ImageView view, String url, int maxLoopCount, int delay) {
        if (view == null) return;
        Glide.with(view.getContext())
                .asGif()
                .load(url)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            Field gifStateField = GifDrawable.class.getDeclaredField("state");
                            gifStateField.setAccessible(true);
                            Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                            gifFrameLoaderField.setAccessible(true);
                            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                            gifDecoderField.setAccessible(true);
                            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                            getDelayMethod.setAccessible(true);
                            //设置只播放一次
                            resource.setLoopCount(maxLoopCount);

                            Field field = GifDecoder.class.getDeclaredField("header");
                            field.setAccessible(true);
                            GifHeader header = (GifHeader) field.get(gifDecoder);
                            Field field2 = GifHeader.class.getDeclaredField("frames");
                            field2.setAccessible(true);
                            List frames = (List) field2.get(header);
                            if (frames.size() > 0) {
                                Field delay = frames.get(0).getClass().getDeclaredField("delay");
                                delay.setAccessible(true);
                                for (Object frame : frames) {
                                    delay.set(frame, delay);
                                }
                            }
                        } catch (ClassCastException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .into(view);
    }

}
