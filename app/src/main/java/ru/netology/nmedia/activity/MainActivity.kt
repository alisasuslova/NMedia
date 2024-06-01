package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        //регистрация контракта на создание нового поста
        val newPostLauncher = registerForActivityResult(NewPostContract) {
            val result = it ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }

        //регистрация контракта на редактирование
        val editPostLauncher = registerForActivityResult(EditPostContract) {
            val result = it ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }


        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {

                //сама механика передачи данных в другие прилодения
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                //больше выбор приложений
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)

                // добавить подсчет количества!
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost =
                posts.size > adapter.currentList.size  //чтобы не перескакивало вверх при нажатии на реакции
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }


//        viewModel.edited.observe(this) {
//            if (it.id != 0L) {
//                binding.content.setText(it.content)
//                binding.editText2.setText(it.content)
//                binding.content.focusAndShowKeyboard()
//                binding.editGroup.visibility = View.VISIBLE
//            }
//        }

        binding.save.setOnClickListener {
            newPostLauncher.launch()
        }

        viewModel.edited.observe(this) {

            if (it.id != 0L) {
                editPostLauncher.launch(it.content)
            }


            /*binding.save.setOnClickListener {
    *//*val content = binding.content.text.toString()

    if (content.isBlank()) {
        Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }
    viewModel.changeContentAndSave(content)

    binding.content.setText("") //чтобы поле для ввода текста отличаалось после добаления поста
    binding.content.clearFocus() // убирает мигающий курсор
    binding.editGroup.visibility = View.GONE
    AndroidUtils.hideKeyboard(binding.content) // убирает клавиатуру после добавления поста*/

            /*


    newPostLauncher.launch()
}

binding.menu_edit.setOnClickListener{
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, post.content)
    }
    editPostLauncher.launch(intent)
}*/



//        binding.cansel.setOnClickListener {
//            binding.content.setText("")
//            binding.content.clearFocus()
//            binding.editGroup.visibility = View.GONE
//            viewModel.editCancel()
//            AndroidUtils.hideKeyboard(binding.content)
//        }
        }
        
        binding.play.setOnClickListener {
     
        }
    }

}