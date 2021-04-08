package com.overeasy.homework.repository

import androidx.lifecycle.MutableLiveData
import com.overeasy.homework.pojo.Post
import com.overeasy.homework.repository.network.Client

class Repository {
    private val client: Client by lazy {
        Client()
    }

    fun getDataPosts(start: Int) = client.getDataPosts(start)

    fun getDataComments(id: Int) = client.getDataComments(id)

    fun getPosts() = client.posts

    fun getComments() = client.comments
}