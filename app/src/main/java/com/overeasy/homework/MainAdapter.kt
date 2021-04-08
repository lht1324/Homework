package com.overeasy.homework

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.databinding.ItemPostBinding
import com.overeasy.homework.pojo.Post

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var posts = ArrayList<Post>()
    val onItemClicked = MutableLiveData<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)

        return ViewHolder(binding,
            onItemClicked = { post ->
                onItemClicked.value = post
            })
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(posts[position])

    override fun getItemCount() = posts.size

    inner class ViewHolder(
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

    private fun println(data: String) = Log.d("MainAdapter", data)
}