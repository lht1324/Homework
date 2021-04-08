package com.overeasy.homework.repository.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"
    val posts = MutableLiveData<ArrayList<Post>>()
    val comments = MutableLiveData<ArrayList<Comment>>()

    fun getDataPosts(start: Int) = getPost().getPostsPage(start, 10)
        .enqueue(object : retrofit2.Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                posts.value = Parser.parsePosts(response.body().toString())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("onFailure is executed in getDataPosts() of Client.")
            }
        })

    fun getDataComments(id: Int) = getPost().getComments(id.toString())
        .enqueue(object : retrofit2.Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                println("id = $id")
                comments.value = Parser.parseComments(response.body().toString())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("onFailure is executed in getDataComments() of Client.")
            }
        })

    private fun getPost(): RetrofitService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    object Parser {
        private val gson = Gson()

        fun parsePosts(response: String): ArrayList<Post> {
            val responseTemp = response.replace("}, {", "},{")
                .replace("{", "{\"") // { 오른쪽
                .replace("=", "\"=\"") // = 왼쪽 오른쪽
                .replace(", ", "\", \"") // ", " 왼쪽 오른쪽 (이 경우엔 }와 { 사이에 ", "가 있다면 제외)
                .replace("}", "\"}") // } 왼쪽
                .replace("},{", "}, {")
            // json의 value가 String일 때 공백을 포함하고 있으면
            // 'JSONException:Unterminated object..." 에러가 공백 다음 2번째 글자에서 발생

            val jsonArray = JSONArray(responseTemp)
            val posts = ArrayList<Post>()

            for (i in 0 until jsonArray.length())
                posts.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Post::class.java))

            return posts
        }

        fun parsePost(response: String) = gson.fromJson(response, Post::class.java)

        fun parseComments(response: String): ArrayList<Comment> {
            val responseTemp = response.replace("}, {", "},{")
                .replace("{", "{\"")
                .replace("=", "\"=\"")
                .replace(", ", "\", \"")
                .replace("}", "\"}")
                .replace("},{", "}, {")

            println("responseTemp = $responseTemp")
            val jsonArray = JSONArray(responseTemp)
            val comments = ArrayList<Comment>()

            for (i in 0 until jsonArray.length())
                comments.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Comment::class.java))

            return comments
        }
    }

    fun println(data: String) = Log.d("Client", data)
}