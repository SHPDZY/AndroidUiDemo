package com.example.libcommon.beans

/**
 *  author: zy
 *  time  : 2021/4/12
 *  desc  :
 */
data class TestPostsListBean(
        var list: ArrayList<TestPostBean>
)

data class TestPostBean(
        var isLiked: Int,
        val pictures: ArrayList<String>?,
        val title: String?,
        val nick: String?,
        val avatar: String?,
        val type: Int, // 帖子类型（0-长图文, 1-轻发布（动态）2视频帖
        val commentCount: Int,
        var likeCount: Int,
        var shareCount: Int,
        val userId: String?,
        val userNo: String?,
        val description: String?,
        val location: String?,
        val videoUrl: String?,
        var tags: ArrayList<String>?,
        val publishedTime: String?
        )

data class SquareListBean(
    val list: List<PostsListBean>?,
    val pageNo: Int,
    val total: Int
)

data class PostsListBean(
    val commentCount: Int,
    val coverPictures: ArrayList<String>?,
    var tags: ArrayList<TagsBean>?,
    val globalTopped: String?, //社区置顶标识
    val id: String?,
    var isLiked: Int,
    var likeCount: Int,
    var shareCount: Int,
    val pictures: ArrayList<String>?,
    val publishedTime: String?,
    val threadDescription: ThreadDescription?,
    val title: String?,
    val nick: String?,
    val avatar: String?,
    val type: Int, // 帖子类型（0-长图文, 1-轻发布（动态）2视频帖
    val userId: String?,
    val userNo: String?,
    val videoSize: String?,
    val location: String?,
    val videoUrl: String?
)

data class ThreadDescription(
        val description: String?,
        val id: String?,
        val picture: String?
)

data class TagsBean(
        val tagId: String?,
        val tagName: String?
)