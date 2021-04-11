package com.overeasy.homework.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.databinding.ItemLoadingBinding
import com.overeasy.homework.databinding.ItemPostBinding
import com.overeasy.homework.pojo.Post

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItem = 0
    private val viewTypeLoading = 1
    private val posts by lazy {
        ArrayList<Post>()
    }
    var updatedPosition = 0
    var page = 1 // 앱 첫 실행 시 이미 0으로 시작
    val onItemClicked = MutableLiveData<Post>()
    val onItemLongPressed = MutableLiveData<Post>()
    val onItemSwiped = MutableLiveData<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            viewTypeItem -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPostBinding.inflate(inflater, parent, false)
                ItemViewHolder(
                        binding,
                        onItemClicked = { post ->
                            onItemClicked.value = post },
                        onItemLongPressed = { post ->
                            onItemLongPressed.value = post }
                )
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
            if (viewHolder is ItemViewHolder)
                viewHolder.bind(posts[position])
            else {}

    override fun getItemViewType(position: Int): Int {
        return when (posts[position].title) {
            " " -> {
                viewTypeLoading
            }
            else -> {
                viewTypeItem
            }
        }
    }

    override fun getItemCount() = posts.size

    inner class ItemViewHolder(
        private val binding: ItemPostBinding,
        private val onItemClicked: (Post) -> Unit,
        private val onItemLongPressed: (Post) -> Unit) :
            RecyclerView.ViewHolder(binding.root),
            View.OnLongClickListener {
        fun bind(post: Post) {
            binding.post = post
            binding.viewHolder = this
        }

        fun onClick(post: Post) {
            onItemClicked(post)
        }

        override fun onLongClick(view: View?): Boolean {
            onItemLongPressed(binding.post!!)
            println("Item ${binding.post!!.id} is pressed.")
            return true
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    fun setList(posts: ArrayList<Post>) {
        this.posts.addAll(posts)
        if (page < 10)
            this.posts.add(Post(" ", " ", " "))
    }

    fun updatePost(post: Post) {
        posts[updatedPosition] = post
        notifyItemChanged(updatedPosition)
    }

    fun stopLoading() = posts.removeAt(posts.lastIndex)

    fun onSwiped(position: Int) {
        onItemSwiped.value = posts[position]
        posts.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun println(data: String) = Log.d("MainAdapter", data)
}