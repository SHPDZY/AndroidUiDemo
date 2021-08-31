package com.example.zyuidemo.widget.likeview;

import java.util.List;

/**
 */
public interface IAnimationLayout {

    /**
     * 添加 资源文件
     *
     * @param resId resId
     */
    IAnimationLayout addLikeImage(int resId);

    /**
     * 添加 资源文件组
     *
     * @param resIds resIds
     */
    IAnimationLayout addLikeImages(Integer... resIds);

    /**
     * 添加 资源文件列表
     *
     * @param resIds resIds
     */
    IAnimationLayout addLikeImages(List<Integer> resIds);

    /**
     * 设置 资源文件列表
     *
     * @param resIds resIds
     */
    IAnimationLayout setLikeImages(List<Integer> resIds);

    /**
     * 添加 发送
     */
    IAnimationLayout addFavor();
}