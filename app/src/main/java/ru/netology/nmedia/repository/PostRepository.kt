package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {


    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun shareById(id: Long)
    //fun removeById(id: Long)
    //fun save(post: Post) : Post
    fun playVideo(id: Long)

    //fun getAll(): List<Post>
    //fun getAllAsync(callback: GetAllCallback)
    /*interface GetAllCallback {
        fun onSuccess(data: List<Post>)
        fun onError(e: Exception)
    }*/


    fun getAllAsync(callback: NMediaCallback<List<Post>>)

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }

    fun saveAsync(post: Post, callback: NMediaCallback <Post>)

    fun removeByIdAsync(id: Long, callback: NMediaCallback <Post>)

    //fun likeByIdAsync(id: Long, callback: NMediaCallback <Post>)
}