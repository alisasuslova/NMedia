package ru.netology.nmedia.repository


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {


    override fun getAll(): List<Post> {

        return ApiService.service.getAll()
            .execute()
            .let { it.body() ?: throw RuntimeException("Body is null") }

    }

    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {

        ApiService.service
            .getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                    if(!response.isSuccessful) { //проверка кода на вхождение в диапозон 200..299
                        //response.message() - статус сообщения ответа
                        //response.code() - статус код ответа
                        //response.errorBody() - raw-body ответа
                       // callback.onError(RuntimeException(response.message()))
                        callback.onError(throw RuntimeException("Error"))
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

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
        ApiService.service.removeById(id)
            .execute()
    }

    override fun saveAsync(post: Post, callback: PostRepository.NMediaCallback<Post>) {

        ApiService.service.savePost(post)
            .execute()
    }

    override fun playVideo(id: Long) {
        TODO("Not yet implemented")
    }


}

