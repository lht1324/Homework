package com.overeasy.homework.repository.network

import android.util.Log
import com.overeasy.homework.pojo.Post
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    // MainFragment의 어댑터에서 사용되는 posts를 받아온다.
    fun getPosts(start: Int) = getData().getPostsPage(start, 10)

    // DetailFragment에서 쓰이는 각 포스트의 댓글들을 받아온다.
    fun getComments(id: Int) = getData().getComments(id)

    // 유저가 수정한 결과를 받아 업데이트한 뒤 결과를 받아온다.
    fun updatePost(id: Int, post: Post) = getData().updatePost(id, post)

    // 삭제한 결과를 받아온다.
    fun deletePost(id: Int) = getData().deletePost(id)

    // 서비스를 생성한다
    private fun getData(): RetrofitService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)

    // 로그 출력
   private fun println(data: String) = Log.d("Client", data)
}