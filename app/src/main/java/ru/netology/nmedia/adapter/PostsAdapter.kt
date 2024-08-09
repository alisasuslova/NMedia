package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun playVideo(post: Post)
    fun openPost(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.text = post.likes.toString()
            shares.text = post.shares.toString()


            val url = "http://10.0.2.2:9998/avatars/${post.authorAvatar}"
            Glide.with(itemView)
                .load(url)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)



            likes.isChecked = post.likedByMe

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shares.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.option_post)
                    setOnMenuItemClickListener {item ->
                        when(item.itemId) {
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)

                                true
                            }
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            content.setOnClickListener {
                onInteractionListener.openPost(post)
            }

        if(post.video != null) {
            videoGroup.visibility = View.VISIBLE
        } else {
            videoGroup.visibility = View.GONE
        }

            playButton.setOnClickListener() {
                onInteractionListener.playVideo(post)
            }

            videoImg.setOnClickListener() {
                onInteractionListener.playVideo(post)
            }

        }
    }
}

object PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem.id == newItem.id // проверка на id элемента

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    //проверка на равенство элементов
}

