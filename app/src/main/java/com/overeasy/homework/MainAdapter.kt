package com.overeasy.homework

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
    var page = 1
    val onItemClicked = MutableLiveData<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            viewTypeItem -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPostBinding.inflate(inflater, parent, false)
                ItemViewHolder(binding, onItemClicked = { post ->
                    onItemClicked.value = post
                })
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
            " " -> viewTypeLoading
            else -> viewTypeItem
        }
    }

    override fun getItemCount() = posts.size

    inner class ItemViewHolder(
        private val binding: ItemPostBinding,
        private val onItemClicked: (Post) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.viewHolder = this
        }

        fun onClick(post: Post) {
            onItemClicked(post)
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    fun setList(posts: ArrayList<Post>) {
        this.posts.addAll(posts)
        if (page != 10)
            this.posts.add(Post(" ", " ", " "))
    }

    fun stopLoading() = posts.removeAt(posts.lastIndex)

    private fun println(data: String) = Log.d("MainAdapter", data)
}