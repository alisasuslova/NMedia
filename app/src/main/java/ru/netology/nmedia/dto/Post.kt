package ru.netology.nmedia.dto

data class Post(
   /* val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val sharesByMe: Boolean = false,
    val shares: Int = 0,
    val video: String? = null,
    val view: Int = 0,
    val authorAvatar: String*/


    val id: Long,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    var attachment: Attachment? = null,
)