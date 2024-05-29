package com.codeflix.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Course(
    @BsonId
    val id: String = ObjectId().toString(),
    val courseName: String,
    val thumbnail: String,
    val description: String,
    val tutor: String,
)

@Serializable
data class Folder(
    @BsonId
    val folderId: String = ObjectId().toString(),
    val courseId: String,
    val folderName: String,
)

@Serializable
data class Video(
    @BsonId
    val videoId: String = ObjectId().toString(),
    val folderId: String,
    val title: String,
    val url: String,
)

@Serializable
data class SimpleResponse(
    val success: Boolean,
    val message: String,
)
