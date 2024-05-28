package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.launch
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostContract
import ru.netology.nmedia.activity.NewPostContract
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
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




            likes.isChecked = post.likedByMe
            /*likes.setImageResource(
                if (post.likedByMe) R.drawable.heart_like_red_20 else R.drawable.heart_like_20
            )*/
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shares.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
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
        }
    }
}

object PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem.id == newItem.id // проверка на id элемента

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    //проверка на равенство элементов
}

