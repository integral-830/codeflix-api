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
//        .createClient(ConnectionString("mongodb+srv://integral:atlasverma0830@codeflix.isxvimj.mongodb.net/"))
        .coroutine

        private val database = client.getDatabase(System.getenv("DATABASE_NAME"))
//    private val database = client.getDatabase("codeflix_db")
    private val courseCollection: CoroutineCollection<Course> = database.getCollection<Course>("courses")
    private val folderCollection: CoroutineCollection<Folder> = database.getCollection<Folder>("folders")
    private val videoCollection: CoroutineCollection<Video> = database.getCollection<Video>("videos")

    private val androidCourseCollection: CoroutineCollection<Course> = database.getCollection<Course>("androidCourses")
    private val androidFolderCollection: CoroutineCollection<Folder> = database.getCollection<Folder>("androidFolders")
    private val androidVideoCollection: CoroutineCollection<Video> = database.getCollection<Video>("androidVideos")

    private val webCourseCollection: CoroutineCollection<Course> = database.getCollection<Course>("webCourses")
    private val webFolderCollection: CoroutineCollection<Folder> = database.getCollection<Folder>("webFolders")
    private val webVideoCollection: CoroutineCollection<Video> = database.getCollection<Video>("WebVideos")

    suspend fun addGeneralCourse(course: Course) {
        courseCollection.insertOne(course)
    }

    suspend fun addWebCourse(course: Course) {
        webCourseCollection.insertOne(course)
    }

    suspend fun addAndroidCourse(course: Course) {
        androidCourseCollection.insertOne(course)
    }

    suspend fun addGeneralFolder(folder: Folder) {
        folderCollection.insertOne(folder)
    }

    suspend fun addAndroidFolder(folder: Folder) {
        androidFolderCollection.insertOne(folder)
    }

    suspend fun addWebFolder(folder: Folder) {
        webFolderCollection.insertOne(folder)
    }

    suspend fun addGeneralVideo(video: Video) {
        videoCollection.insertOne(video)
    }

    suspend fun addAndroidVideo(video: Video) {
        androidVideoCollection.insertOne(video)
    }

    suspend fun addWebVideo(video: Video) {
        webVideoCollection.insertOne(video)
    }

    suspend fun getGeneralCourses(page: Int, limit: Int): CourseResponse =
        CourseResponse(
            success = true,
            courses = courseCollection
                .find()
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getWebCourses(page: Int, limit: Int): CourseResponse =
        CourseResponse(
            success = true,
            courses = webCourseCollection
                .find()
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getAndroidCourses(page: Int, limit: Int): CourseResponse =
        CourseResponse(
            success = true,
            courses = androidCourseCollection
                .find()
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getGeneralFolders(courseId: String, page: Int, limit: Int): FolderResponse =
        FolderResponse(
            success = true,
            folders = folderCollection
                .find(Folder::courseId eq courseId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getWebFolders(courseId: String, page: Int, limit: Int): FolderResponse =
        FolderResponse(
            success = true,
            folders = webFolderCollection
                .find(Folder::courseId eq courseId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getAndroidFolders(courseId: String, page: Int, limit: Int): FolderResponse =
        FolderResponse(
            success = true,
            folders = androidFolderCollection
                .find(Folder::courseId eq courseId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getGeneralVideos(folderId: String, page: Int, limit: Int): VideoResponse =
        VideoResponse(
            success = true,
            videos = videoCollection
                .find(Video::folderId eq folderId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getWebVideos(folderId: String, page: Int, limit: Int): VideoResponse =
        VideoResponse(
            success = true,
            videos = webVideoCollection
                .find(Video::folderId eq folderId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun getAndroidVideos(folderId: String, page: Int, limit: Int): VideoResponse =
        VideoResponse(
            success = true,
            videos = androidVideoCollection
                .find(Video::folderId eq folderId)
                .skip(skip = (page - 1) * limit)
                .limit(limit = limit)
                .partial(true)
                .toList()
        )

    suspend fun updateGeneralCourse(course: Course): Boolean {
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

    suspend fun updateWebCourse(course: Course): Boolean {
        val list = webCourseCollection
            .find(Course::id eq course.id)
            .toList()
        if (list.isNotEmpty()) {
            webCourseCollection.updateOne(Course::id eq course.id, course)
            return true
        } else {
            return false
        }
    }

    suspend fun updateAndroidCourse(course: Course): Boolean {
        val list = androidCourseCollection
            .find(Course::id eq course.id)
            .toList()
        if (list.isNotEmpty()) {
            androidCourseCollection.updateOne(Course::id eq course.id, course)
            return true
        } else {
            return false
        }
    }

    suspend fun updateGeneralFolder(folder: Folder): Boolean {
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

    suspend fun updateWebFolder(folder: Folder): Boolean {
        val list = webFolderCollection
            .find(Folder::folderId eq folder.folderId)
            .toList()
        if (list.isNotEmpty()) {
            webFolderCollection.updateOne(Folder::folderId eq folder.folderId, folder)
            return true
        } else {
            return false
        }

    }

    suspend fun updateAndroidFolder(folder: Folder): Boolean {
        val list = androidFolderCollection
            .find(Folder::folderId eq folder.folderId)
            .toList()
        if (list.isNotEmpty()) {
            androidFolderCollection.updateOne(Folder::folderId eq folder.folderId, folder)
            return true
        } else {
            return false
        }

    }

    suspend fun updateGeneralVideo(video: Video): Boolean {
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

    suspend fun updateWebVideo(video: Video): Boolean {
        val list = webVideoCollection
            .find(Video::videoId eq video.videoId)
            .toList()
        if (list.isNotEmpty()) {
            webVideoCollection.updateOne(Video::videoId eq video.videoId, video)
            return true
        } else {
            return false
        }
    }

    suspend fun updateAndroidVideo(video: Video): Boolean {
        val list = androidVideoCollection
            .find(Video::videoId eq video.videoId)
            .toList()
        if (list.isNotEmpty()) {
            androidVideoCollection.updateOne(Video::videoId eq video.videoId, video)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteGeneralCourse(courseId: String): Boolean {
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

    suspend fun deleteWebCourse(courseId: String): Boolean {
        val list = webCourseCollection
            .find(Course::id eq courseId)
            .toList()
        if (list.isNotEmpty()) {
            webCourseCollection.deleteOne(Course::id eq courseId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteAndroidCourse(courseId: String): Boolean {
        val list = androidCourseCollection
            .find(Course::id eq courseId)
            .toList()
        if (list.isNotEmpty()) {
            androidCourseCollection.deleteOne(Course::id eq courseId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteGeneralFolder(folderId: String): Boolean {
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

    suspend fun deleteWebFolder(folderId: String): Boolean {
        val list = webFolderCollection
            .find(Folder::folderId eq folderId)
            .toList()
        if (list.isNotEmpty()) {
            webFolderCollection.deleteOne(Folder::folderId eq folderId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteAndroidFolder(folderId: String): Boolean {
        val list = androidFolderCollection
            .find(Folder::folderId eq folderId)
            .toList()
        if (list.isNotEmpty()) {
            androidFolderCollection.deleteOne(Folder::folderId eq folderId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteGeneralVideo(videoId: String): Boolean {
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

    suspend fun deleteWebVideo(videoId: String): Boolean {
        val list = webVideoCollection
            .find(Video::videoId eq videoId)
            .toList()
        if (list.isNotEmpty()) {
            webVideoCollection.deleteOne(Video::videoId eq videoId)
            return true
        } else {
            return false
        }
    }

    suspend fun deleteAndroidVideo(videoId: String): Boolean {
        val list = androidVideoCollection
            .find(Video::videoId eq videoId)
            .toList()
        if (list.isNotEmpty()) {
            androidVideoCollection.deleteOne(Video::videoId eq videoId)
            return true
        } else {
            return false
        }
    }
}