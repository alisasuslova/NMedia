package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = ""
)
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData<FeedModel>() //меняем только внутри класса
    val data : LiveData<FeedModel> // публичное сво-во
        get() = _data

    val edited = MutableLiveData(empty)

    init {
        load()
    }

    fun load() {
        // создаем фоновый поток
        thread {
            _data.postValue(FeedModel(loading = true)) // в момент создания состояние загрузки включено

            _data.postValue(
                try {
                    val posts = repository.getAll()
                    FeedModel(posts = posts, empty = posts.isEmpty()) // скачиваем посты, все успешно
                } catch (e: Exception) {
                    (FeedModel(error = true)) // если ошибка
                }
            )
        }
    }

    fun changeContentAndSave(text: String) {
        thread {
            edited.value?.let {
                if (it.content != text.trim()) {
                    repository.save(it.copy(content = text))
                }
            }
            edited.postValue(empty)
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun edit(post : Post) {
        edited.value = post
    }
    fun removeById(id: Long) = repository.removeById(id)

    fun editCancel() {
        edited.value = empty
    }
    fun playVideo(id: Long) = repository.playVideo(id)
}

