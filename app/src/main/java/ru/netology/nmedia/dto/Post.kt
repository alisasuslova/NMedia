package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val sharesByMe: Boolean = false,
    val shares: Int = 0,
    val video: String? = null,
    val view: Int = 0
)
