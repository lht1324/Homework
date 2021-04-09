package com.overeasy.homework

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import com.overeasy.homework.repository.Repository
import io.reactivex.rxjava3.subjects.PublishSubject

class ViewModel(application: Application) : ViewModel() {
    private val posts = MutableLiveData<ArrayList<Post>>()
    private val comments = MutableLiveData<ArrayList<Comment>>()
    private val detailDatas = MutableLiveData<ArrayList<Any>>() // comments, post
    private val repository: Repository by lazy {
        Repository()
    }
    val publishSubject: PublishSubject<Post> = PublishSubject.create()

    fun getPosts() = posts

    fun getDetailDatas() = detailDatas

    fun getComments() = comments

    private fun getDataPosts(start: Int) = repository.getDataPosts(start)

    private fun getDataComments(post: Post) = repository.getDataComments(post)

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T{
            return ViewModel(application) as T
        }
    }

    init {
        getDataPosts(0)
        repository.getPosts().observeForever {
            posts.value = it
        }
        repository.getDetailDatas().observeForever {
            detailDatas.value = it
        }
        publishSubject.subscribe { post ->
            getDataComments(post)
        }
    }

    fun scrollLoad(page: Int) {
        getDataPosts(page * 10)
    }

    private fun println(data: String) = Log.d("ViewModel", data)
}