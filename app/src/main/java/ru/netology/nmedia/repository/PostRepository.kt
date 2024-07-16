package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    //fun getAll(): List<Post>
    //fun getAllAsync(callback: GetAllCallback)
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    //fun save(post: Post) : Post
    fun playVideo(id: Long)

    /*interface GetAllCallback {
        fun onSuccess(data: List<Post>)
        fun onError(e: Exception)
    }*/

    /*fun saveAsync(callback: SaveCallback)

    interface SaveCallback {
        fun onSuccess(data: Post)
        fun onError(e: Exception)
    }*/

    //мой вариант:
    /*fun likeByIdAsync(callback: likeByIdCallback)

    interface likeByIdCallback {
        fun onSuccess(data: Post)
        fun onError(e: Exception)
    }

    fun removeByIdAsync(callback: removeByIdCallback)

    interface removeByIdCallback {
        fun onSuccess(data: Post)
        fun onError(e: Exception)
    }*/


    //В идеале:
    fun getAllAsync(callback: NMediaCallback<List<Post>>)

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }

    fun saveAsync(post: Post, callback: NMediaCallback <Post>)

}