package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

data class Post(
    val id : Long,
    val author : String,
    val content : String,
    val published : String,
    var likedByMe : Boolean = false
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
            likedByMe  = false
        )


        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            var likesCountVar = likesCount.text
            val intLikeCount = (likesCountVar.toString()).toInt()
            var newNumber: Int

            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.heart_like_red_20)
            }
            likes?.setOnClickListener{
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if(post.likedByMe) {
                        newNumber = intLikeCount + 1  // 1 вариант
                        likesCountVar = shortNote(newNumber)
                        R.drawable.heart_like_red_20
                    } else {
                        newNumber = intLikeCount - 1  // 1 вариант
                        likesCountVar = shortNote(newNumber)
                        R.drawable.heart_like_20
                    }

                )

                    //newNumber = if (post.likedByMe) intLikeCount + 1 else intLikeCount - 1  // 2 вариант

                likesCount.text = likesCountVar

            }
        }
    }
}

fun shortNote(int : Int) : String {

    val temp : BigDecimal = int.toBigDecimal().divide(1_000.toBigDecimal())
    val temp1 : BigDecimal = int.toBigDecimal().divide(1_000_000.toBigDecimal())

    return when (int) {
        in 1..999 -> int.toString()
        in 1000..999_999-> String.format("%.1f", temp.setScale(1, RoundingMode.FLOOR)) + "K"
        else -> {
            String.format("%.1f", temp1.setScale(1, RoundingMode.FLOOR)) + "M"
        }
    }
}

