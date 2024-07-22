package ru.netology.nmedia.repository

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class PostRepositoryImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type
    private val typeToken = object : TypeToken<List<Post>>() {}

    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg")

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9998/"
        private val jsonType = "application/json".toMediaType()

    }

    /*override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {

        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e) //ошибка
                }

                override fun onResponse(call: Call, response: Response) {
                    //ответ сервера
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e) //ошибка
                    }
                    //Looper.getMainLooper() == Looper.myLooper() //false - не главный поток!
                }
            })
    }*/

    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {

        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e) //ошибка
                }

                override fun onResponse(call: Call, response: Response) {
                    //ответ сервера
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), typeToken.type))

                    } catch (e: Exception) {
                        callback.onError(e) //ошибка
                    }
                    //Looper.getMainLooper() == Looper.myLooper() //false - не главный поток!
                }
            })
    }

    override fun likeByIdAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = if(post.likedByMe) {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .delete()
                .build()
        } else {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .post(EMPTY_REQUEST)
                .build()
        }

        return client.newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })

    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }


    override fun removeByIdAsync(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
        thread {
            val request = Request.Builder()
                .url("${BASE_URL}api/slow/posts/$id")
                .delete()
                .build()

            client.newCall(request)
                .enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            callback.onSuccess(Unit)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }

                })
        }
    }


    override fun saveAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e) //ошибка
                    }
                }
            })
    }

    override fun playVideo(id: Long) {
        TODO("Not yet implemented")
    }


}

