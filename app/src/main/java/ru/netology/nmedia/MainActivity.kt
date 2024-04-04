package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int = 10999,
//    var likes: Int = 10_500,
    var shares: Int = 6
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с\n" +
                    "        интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению.\n" +
                    "        Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.\n" +
                    "        Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше,\n" +
                    "        целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку\n" +
                    "        перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )


        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            likesCount.text = shortNote(post.likes)
            repostCount.text = shortNote(post.shares)
            
            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.heart_like_red_20)
            }
            likes?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) {
                        post.likes++
                        R.drawable.heart_like_red_20
                    } else {
                        post.likes--
                        R.drawable.heart_like_20
                    }
                )
                likesCount.text = shortNote(post.likes)
            }

            repost?.setOnClickListener {
                repostCount.text = shortNote(post.shares++)
            }

        }

    }
}

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
}


