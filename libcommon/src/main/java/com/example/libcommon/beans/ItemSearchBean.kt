package com.example.libcommon.beans

/**
 * @author  : zhangyong
 * @date    : 2021/7/20
 * @desc    :
 * @version :
 */
data class ItemSearchBean(
    val title:String?,
    val url:String?,
    val img:String?
)

data class ItemSearchNavBean(
    val index:String?,
    val url:String?,
    val isCurrent:Boolean
)
