package com.overeasy.homework.repository

import android.util.Log
import com.overeasy.homework.pojo.Post
import com.overeasy.homework.repository.network.Client

class Repository {
    private val client: Client by lazy {
        Client()
    }

    fun getPosts(start: Int) = client.getPosts(start)

    fun getComments(id: Int) = client.getComments(id)

    fun updatePost(id: Int, post: Post) = client.updatePost(id, post)

    fun deletePost(id: Int) = client.deletePost(id)

    private fun println(data: String) = Log.d("Repository", data)
}