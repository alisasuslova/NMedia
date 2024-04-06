package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.math.BigDecimal
import java.math.RoundingMode

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likes = 10999,
        shares = 6
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {

        var localLikesCount = post.likes

        if (post.likedByMe) {
            post = post.copy(likes = localLikesCount - 1)
        } else {
            post = post.copy(likes = localLikesCount + 1)
        }

        post = post.copy(likedByMe = !post.likedByMe)

        data.value = post
    }

    override fun share() {
        var localSharesCount = post.shares
        post = post.copy(shares = localSharesCount + 1)
        data.value = post
    }
}

/*
fun shortNote(int: Int): String {

    val temp: BigDecimal = int.toBigDecimal().divide(1_000.toBigDecimal())
    val temp1: BigDecimal = int.toBigDecimal().divide(1_000.toBigDecimal())
    val temp2: BigDecimal = int.toBigDecimal().divide(1_000_000.toBigDecimal())

    return when (int) {
        in 1..999 -> int.toString()
        in 1_000..9_999 -> String.format("%.1f", temp.setScale(1, RoundingMode.FLOOR)) + "K"
        in 10_000..999_999 -> String.format("%.0f", temp1.setScale(0, RoundingMode.FLOOR)) + "K"
        else -> {
            String.format("%.1f", temp2.setScale(1, RoundingMode.FLOOR)) + "M"
        }
    }
}*/

