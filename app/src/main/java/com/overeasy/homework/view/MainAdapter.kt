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

// 메인 화면의 리사이클러뷰에 사용되는 어댑터. MainFragment에서 사용된다.
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
    val onItemSwiped = MutableLiveData<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Post를 보여주는 아이템, 프로그레스 바를 보여주는 아이템을 구분해 뷰홀더를 따로 생성한다.
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

    // viewType을 position에 따라 다르게 설정한다.
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
        // 바인딩
        fun bind(post: Post) {
            binding.post = post
            binding.viewHolder = this
        }

        // 아이템 터치
        fun onClick(post: Post) {
            onItemClicked(post)
        }

        // 아이템 길게 눌림
        override fun onLongClick(view: View?): Boolean {
            onItemLongPressed(binding.post!!)
            return true
        }
    }

    // 무한 스크롤에서 마지막 아이템에 프로그레스 바를 보여주기 위해 따로 만든 뷰홀더
    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    fun setList(posts: ArrayList<Post>) {
        this.posts.addAll(posts)

        // 10페이지 이전엔 프로그레스 바를 보여주는 아이템을 추가한다.
        if (page < 10)
            this.posts.add(Post(" ", " ", " "))
    }

    // 업데이트된 데이터를 받아온 게 관찰되면 어댑터의 리스트에서 변경된 아이템을 받아 바꾼다.
    fun updatePost(post: Post) {
        posts[updatedPosition] = post
        notifyItemChanged(updatedPosition)
    }

    // 무한 스크롤 도중 아이템을 받아오면 프로그레스 바를 보여주는 아이템을 삭제한다.
    fun stopLoading() = posts.removeAt(posts.lastIndex)

    // 아이템을 옆으로 밀었을 때 어댑터 내부 리스트에서 아이템을 삭제한다
    fun onSwiped(position: Int) {
        onItemSwiped.value = posts[position].id
        posts.removeAt(position)
        notifyItemRemoved(position)
    }

    // 로그 출력
    private fun println(data: String) = Log.d("MainAdapter", data)
}