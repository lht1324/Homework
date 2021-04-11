package com.overeasy.homework.repository.network

import android.util.Log
import com.overeasy.homework.pojo.Post
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    fun getPosts(start: Int) = getData().getPostsPage(start, 10)

    fun getComments(id: Int) = getData().getComments(id)

    fun updatePost(id: Int, post: Post) = getData().updatePost(id, post)

    fun deletePost(id: Int) = getData().deletePost(id)

    private fun getData(): RetrofitService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)

    private fun println(data: String) = Log.d("Client", data)
}