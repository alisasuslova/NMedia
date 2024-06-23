
package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
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

            override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                viewModel.playVideo(post.id)
                viewModel.editCancel()
            }

            override fun openPost(post: Post) {
                viewModel.playVideo(post.id)
                viewModel.editCancel()
            }

        }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { model ->
            binding.editGroup.isVisible = model.error
            binding.emptyState.isVisible = model.empty
            binding.progress.isVisible = model.loading
            adapter.submitList(model.posts)
        }

        binding.retry.setOnClickListener {
            viewModel.load()
        }

    }
}
