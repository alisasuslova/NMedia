package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    authorAvatar = ""
)
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData<FeedModel>() //меняем только внутри класса
    val data : LiveData<FeedModel> // публичное сво-во
        get() = _data

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val edited = MutableLiveData(empty)


    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error


    init {
        load()
    }


    fun load() {

            _data.postValue(FeedModel(loading = true)) // в момент создания состояние загрузки включено

            repository.getAllAsync(object: PostRepository.NMediaCallback<List<Post>>{
                override fun onSuccess(data: List<Post>) {
                    _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

            })

    }

    //fun changeContentAndSave(text: String) {
        fun save() {
        edited.value?.let {
            repository.saveAsync(it, object :PostRepository.NMediaCallback<Post> {
                override fun onSuccess(data: Post) {
                    _postCreated.postValue(Unit)
                    edited.postValue(empty)
                }

                override fun onError(e: Exception) {
                    edited.postValue(empty)
                }

            })
        }
    }


    fun likeById(id: Long) {
        repository.likeById(id, object : PostRepository.NMediaCallback<Post> {
            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(error = true))
            }

            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })

    }

    fun unlikeById(id: Long) {
        repository.unlikeById(id, object : PostRepository.NMediaCallback<Post> {
            override fun onError(e: Exception) {
                //_data.postValue(_data.value?.copy(error = true))
                error(e.message)
            }

            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun edit(post : Post) {
        edited.value = post
    }
    //fun removeById(id: Long) = repository.removeById(id)
    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object : PostRepository.NMediaCallback<Unit> {
            override fun onSuccess(data: Unit) {
                _data.postValue(
                    _data.value?.copy
                        (posts = _data.value?.posts.orEmpty().filter {
                        it.id != id
                    })
                )
            }

            override fun onError(e: Exception) {
                edited.postValue(empty)
            }

        })

    }

    fun editCancel() {
        edited.value = empty
    }
    fun playVideo(id: Long) = repository.playVideo(id)
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }



    private fun error(error: String?) {
        error.let {
            _error.postValue(it)
        }
    }

}

