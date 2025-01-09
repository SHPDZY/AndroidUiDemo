package com.example.libscan.entity

/**
 * @author  : zhangyong
 * @date    : 2023/8/11
 * @desc    :
 * @version :
 */
data class GoodsListData(
    val goodsList: MutableList<GoodsData> = mutableListOf()
)

data class GoodsData(
    var name: String?,
    var code: String?,
    var price: String?,
    var des: String?,
    var img: String?,
)