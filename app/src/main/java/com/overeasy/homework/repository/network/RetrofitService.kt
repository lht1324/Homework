package com.overeasy.homework.repository.network

import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    // https://jsonplaceholder.typicode.com/ (Base URL)
    // https://jsonplaceholder.typicode.com/posts?_start=0&_limit=10 (get posts using pagination)
    // https://jsonplaceholder.typicode.com/posts/4 (get a post, last number is id) Detail View에서 사용?
    // https://jsonplaceholder.typicode.com/posts/4/comments (get a comment of post, last number is post's id)
    @GET("posts?")
    fun getPostsPage(@Query("_start") start: Int, @Query("_limit") limit: Int): Call<ArrayList<Post>>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") id: String): Call<ArrayList<Comment>>

    @PUT("posts/{id}")
    fun patchPost(@Path("id") id: String, @Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(id: String): Call<Post>
}