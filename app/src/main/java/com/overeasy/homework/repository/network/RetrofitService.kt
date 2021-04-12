package com.overeasy.homework.repository.network

import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    // https://jsonplaceholder.typicode.com/posts?_start={_start}&_limit={_limit}
    @GET("posts?")
    fun getPostsPage(@Query("_start") start: Int, @Query("_limit") limit: Int): Single<Response<ArrayList<Post>>>

    // https://jsonplaceholder.typicode.com/posts/{id}/comments
    @GET("posts/{id}/comments")
    fun getComments(@Path("id") id: Int): Single<Response<ArrayList<Comment>>>

    // https://jsonplaceholder.typicode.com/posts/{id}
    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body post: Post): Single<Response<Post>>

    // https://jsonplaceholder.typicode.com/posts/{id}
    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Single<Response<Void>>
}