package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

//private const val BASE_URL = "http://10.0.2.2:9998/api/slow/"
private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create()) //конвертация в обе стороны
    .client(client)
    .baseUrl(BASE_URL)
    .build()

interface PostApiService {
    @GET("posts")
    fun getAll() : Call <List<Post>>

    //39:25

    @POST("posts")
    fun savePost(post: Post) : Call <Post>

    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long): Call <Unit>

    @DELETE("posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call <Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call <Post>


}

object ApiService {
    val service: PostApiService by lazy {
        retrofit.create<PostApiService>()
    }
}