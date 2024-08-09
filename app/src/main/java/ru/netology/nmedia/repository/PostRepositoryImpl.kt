package ru.netology.nmedia.repository


import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {

    /*private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type
    private val typeToken = object : TypeToken<List<Post>>() {}

    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg")

    companion object {
        //private const val BASE_URL = "http://10.0.2.2:9998/"
        private val jsonType = "application/json".toMediaType()

    }*/

    //стало:
    override fun getAll(): List<Post> {

        return ApiService.service.getAll()
            .execute()
            .let { it.body() ?: throw RuntimeException("Body is null") }

    }

    //было:
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

    //стало:
    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {

        ApiService.service
            .getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                    if(!response.isSuccessful) { //проверка кода на вхождение в диапозон 200..299
                        //response.message() - статус сообщения ответа
                        //response.code() - статус код ответа
                        //response.errorBody() - raw-body ответа
                        callback.onError(RuntimeException(response.message()))
                        Toast.makeText(this@PostRepositoryImpl, "Not Success! Install app falled as usual…", Toast.LENGTH_LONG).show()
                        return
                    }

                    val body: List<Post> = response.body() ?: throw RuntimeException("Body is null")
                    //response.headers() - заголовки ответа
                    //response.raw() - необработанное тело ответа
                    //response.body() - приведенное с помощью конвертера к <List<Post>>
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }

    override fun likeById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.likeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                    } else {
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }

    override fun unlikeById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.unlikeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                    } else {
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }


    /*override fun likeByIdAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        TODO("Not yet implemented")

        *//*val request = if(post.likedByMe) {
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

            })*//*

    }*/

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    //было:
    /*override fun removeByIdAsync(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
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
    }*/

    //стало:
    override fun removeByIdAsync(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
        ApiService.service.removeById(id)
            .execute()
    }

    //было:
    /*override fun saveAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {
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
    }*/


    //стало:
    override fun saveAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {

        ApiService.service.savePost(post)
            .execute()
    }

    override fun playVideo(id: Long) {
        TODO("Not yet implemented")
    }


}

