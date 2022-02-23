package com.example.zyuidemo.vm

/**
 * @author  : zhangyong
 * @date    : 2021/11/23
 * @desc    :
 * @version :
 */
data class SlgListEntity(
    val chapterList: List<Chapter>? = arrayListOf(),
    val lastRecord: LastRecord,
    val returnCode: String,
    val returnMessage: String,
    val title: String
)

data class Chapter(
    val childList: List<Child>,
    val endDate: String,
    val id: String,
    val startDate: String,
    val title: String,
    val wareTypeName: String
)

data class LastRecord(
    val lastChapterId: String,
    val lastItemId: String,
    val lastSectionId: String
)

data class Child(
    val childList: List<ChildX>,
    val id: String,
    val status: Int,
    val title: String,
    val wareTypeName: String
)

data class ChildX(
    val id: String,
    val isGuide: Boolean,
    val status: Int,
    val title: String,
    val wareType: String,
    val wareTypeName: String
)

data class LookStatusEntity(
    val learnRecord: LearnRecord,
    val returnCode: String,
    val returnMessage: String
)

data class LearnRecord(
    val key: String,
    val state: Int,
    val status: Int,
    val studyTime: Int
)