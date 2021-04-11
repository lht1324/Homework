package com.overeasy.homework.repository.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Client {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"
    private val posts = MutableLiveData<ArrayList<Post>>()
    private val detailDatas = MutableLiveData<ArrayList<Any>>()
    private val deleteResult = MutableLiveData<Int>()
    private val updateResult = MutableLiveData<ArrayList<Any>>()

    fun getPosts() = posts

    fun getDetailDatas() = detailDatas

    fun getDeleteResult() = deleteResult

    fun getUpdateResult() = updateResult

    fun getDataPosts(start: Int) = getData().getPostsPage(start, 10)
        .enqueue(object : retrofit2.Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                posts.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                println("onFailure() is executed in getDataPosts() of Client.")
            }
        })

    fun getDataComments(post: Post) = getData().getComments(post.id.toDouble().toInt().toString())
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
        })

    private fun getData(): RetrofitService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    fun println(data: String) = Log.d("Client", data)
}