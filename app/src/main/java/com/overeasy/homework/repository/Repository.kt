package com.overeasy.homework.repository

import androidx.lifecycle.MutableLiveData
import com.overeasy.homework.pojo.Post
import com.overeasy.homework.repository.network.Client

class Repository {
    private val client: Client by lazy {
        Client()
    }

    fun getDataPosts(start: Int) = client.getDataPosts(start)

    fun getDataComments(post: Post) = client.getDataComments(post)

    fun updatePost(post: Post) = client.updatePost(post)

    fun deletePost(post: Post) = client.deletePost(post)

    fun getPosts() = client.getPosts()

    fun getDetailDatas() = client.getDetailDatas()

    fun getDeleteResult() = client.getDeleteResult()

    fun getUpdateResult() = client.getUpdateResult()
}