package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun getAllAsync(callback: GetAllCallback)
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post) : Post
    fun playVideo(id: Long)

    interface GetAllCallback {
        fun onSuccess(data: List<Post>)
        fun onError(e: Exception)
    }
}