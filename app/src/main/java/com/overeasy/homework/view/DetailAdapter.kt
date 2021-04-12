package com.overeasy.homework.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.databinding.ItemCommentBinding
import com.overeasy.homework.pojo.Comment

// 디테일 뷰 내부의 리사이클러뷰에 사용되는 어댑터
class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    var comments = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(comments[position])

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }
}