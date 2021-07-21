package com.example.libcommon.beans

data class StatusMarkNote(
    var name: String?,
    val questions: ArrayList<StatusMarkNoteQuestion?>?,
)

data class StatusMarkNoteQuestion(
    var name: String?,
    val tip: String?
)