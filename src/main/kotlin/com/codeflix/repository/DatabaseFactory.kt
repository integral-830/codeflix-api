package com.codeflix.repository

import com.codeflix.data.model.*
import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class DatabaseFactory {
    private val client = KMongo
        .createClient(ConnectionString(System.getenv("MONGODB_STRING")))
        .coroutine
//        .createClient(ConnectionString("mongodb+srv://integral:atlasverma0830@codeflix.isxvimj.mongodb.net/"))
    private val database = client.getDatabase(System.getenv("DATABASE_NAME"))
//    private val database = client.getDatabase("codeflix_db")
    private val courseCollection: CoroutineCollection<Course> = database.getCollection<Course>("courses")
    private val folderCollection: CoroutineCollection<Folder> = database.getCollection<Folder>("folders")
    private val videoCollection: CoroutineCollection<Video> = database.getCollection<Video>("videos")

    suspend fun addCourse(course: Course) {
        courseCollection.insertOne(course)
    }

    suspend fun addFolder(folder: Folder) {
        folderCollection.insertOne(folder)
    }

    suspend fun addVideo(video: Video) {
        videoCollection.insertOne(video)
    }

    suspend fun getAllCourses(): CourseResponse =
        CourseResponse(
            success = true,
            courses = courseCollection
                .find()
                .toList()
        )

    suspend fun getAllFolders(courseId: String): FolderResponse =
        FolderResponse(
            success = true,
            folders = folderCollection
                .find(Folder::courseId eq courseId)
                .toList()
        )

    suspend fun getAllVideos(folderId: String): VideoResponse =
        VideoResponse(
            success = true,
            videos = videoCollection
                .find(Video::folderId eq folderId)
                .toList()
        )

    suspend fun updateCourse(course: Course): Boolean {
        val list = courseCollection
            .find(Course::id eq course.id)
            .toList()
        if (list.isNotEmpty()) {
            courseCollection.updateOne(Course::id eq course.id, course)
            return true
        } else {
            return false
        }
    }

    suspend fun updateFolder(folder: Folder): Boolean {
        val list = folderCollection
            .find(Folder::folderId eq folder.folderId)
            .toList()
        if (list.isNotEmpty()) {
            folderCollection.updateOne(Folder::folderId eq folder.folderId, folder)
            return true
        } else {
            return false
        }

    }

    suspend fun updateVideo(video: Video): Boolean {
        val list = videoCollection
            .find(Video::videoId eq video.videoId)
            .toList()
        if (list.isNotEmpty()) {
            videoCollection.updateOne(Video::videoId eq video.videoId, video)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteCourse(courseId: String): Boolean {
        val list = courseCollection
            .find(Course::id eq courseId)
            .toList()
        if (list.isNotEmpty()) {
            courseCollection.deleteOne(Course::id eq courseId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteFolder(folderId: String): Boolean {
        val list = folderCollection
            .find(Folder::folderId eq folderId)
            .toList()
        if (list.isNotEmpty()) {
            folderCollection.deleteOne(Folder::folderId eq folderId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteVideo(videoId: String): Boolean {
        val list = videoCollection
            .find(Video::videoId eq videoId)
            .toList()
        if (list.isNotEmpty()) {
            videoCollection.deleteOne(Video::videoId eq videoId)
            return true
        } else {
            return false
        }
    }
}