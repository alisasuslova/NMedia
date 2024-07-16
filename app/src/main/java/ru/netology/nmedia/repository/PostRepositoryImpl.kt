package ru.netology.nmedia.repository

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

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9998/"
        private val jsonType = "application/json".toMediaType()

    }


    override fun getAll(): List<Post> {
        //запрос на сервер
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        /*//ответ
        val response = client.newCall(request)
            .execute()
        // получили тело запроса в виде строки
        val responseText = response.body?.string() ?: error("response body is null")
        //перевели в список постов
        return  gson.fromJson(responseText, type)*/

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null")}
            .let { gson.fromJson(it, typeToken.type) }


    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {

        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback{
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


    override fun likeById(id: Long): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id/likes")
            .post(EMPTY_REQUEST)
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it,Post::class.java) }
    }

    override fun unlikeById(id: Long): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id/likes")
            .delete()
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it,Post::class.java) }
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {

        thread {
            val request = Request.Builder()
                .url("${BASE_URL}api/slow/posts/$id")
                .delete()
                .build()

            client.newCall(request)
                .execute()
                .close()
        }
    }

    override fun save(post: Post): Post {

        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType)) //отправляем данные на сервер в виде текста
            .build()

        val response = client.newCall(request)
            .execute()

        val responseText = response.body?.string() ?: error("response body is null")

        return  gson.fromJson(responseText, Post::class.java)
    }

    override fun playVideo(id: Long) {
        TODO("Not yet implemented")
    }


}

