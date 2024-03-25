package ru.netology.nmedia

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
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
            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.heart_like_red_20)
            }
            likes?.setOnClickListener{
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if(post.likedByMe) {
                        R.drawable.heart_like_red_20
                        R.layout.likes_count++
                    } else {
                        R.drawable.heart_like_20
                        R.layout.likes_count--
                    }
                )

            }

        }







    }


}