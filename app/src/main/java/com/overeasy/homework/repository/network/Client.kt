package com.overeasy.homework.repository.network

import android.util.Log
import com.overeasy.homework.pojo.Post
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class Client {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    fun getPosts(start: Int): Single<Response<ArrayList<Post>>> {
        println("getPosts() in Client is executed.")
        return getData().getPostsPage(start, 10)
    }

    fun getComments(id: Int) = getData().getComments(id)

    fun updatePost(id: Int, post: Post) = getData().updatePost(id, post)

    fun deletePost(id: Int) = getData().deletePost(id)

    /* fun getDeleteResult() = deleteResult

    fun getUpdateResult() = updateResult

    fun getDataPosts(start: Int) = getData().getPostsPage(start, 10)
        .enqueue(object : retrofit2.Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                posts.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                println("onFailure() is executed in getDataPosts() of Client.")
            }
        }) */

    /* fun getDataComments(post: Post) = getData().getComments(post.id.toDouble().toInt().toString())
        .enqueue(object : retrofit2.Callback<ArrayList<Comment>> {
            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                val tempList = ArrayList<Any>()
                tempList.add(response.body() as ArrayList<Comment>)
                tempList.add(post)

                detailDatas.value = tempList
            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                println("onFailure() is executed in getDataComments() of Client.")
            }
        })

    fun updatePost(post: Post) = getData().patchPost(post.id.toDouble().toInt().toString(), post)
        .enqueue(object : retrofit2.Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val tempList = ArrayList<Any>()
                tempList.add(response.body() as Post)
                tempList.add(response.code())

                updateResult.value = tempList
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                println("onFailure() is executed in updatePost() of Client.")
            }
        })

    fun deletePost(post: Post) = getData().deletePost(post.id.toDouble().toInt().toString())
        .enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                deleteResult.value = response.code()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("onFailure() is executed in deletePost() of Client.")
            }
        }) */

    private fun getData(): RetrofitService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)

    private fun println(data: String) = Log.d("Client", data)
}