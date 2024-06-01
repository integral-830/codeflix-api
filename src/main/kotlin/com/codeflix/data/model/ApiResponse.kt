package com.codeflix.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse (
    val success:Boolean,
    val courses : List<Course> = emptyList()
)

@Serializable
data class FolderResponse (
    val success:Boolean,
    val folders : List<Folder> = emptyList()
)

@Serializable
data class VideoResponse (
    val success:Boolean,
    val videos : List<Video> = emptyList()
)

