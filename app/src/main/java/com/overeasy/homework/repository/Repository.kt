package com.overeasy.homework.repository

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

    /* fun getPosts() = client.getPosts()

    fun getDetailDatas() = client.getDetailDatas()

    fun getDeleteResult() = client.getDeleteResult()

    fun getUpdateResult() = client.getUpdateResult() */
}