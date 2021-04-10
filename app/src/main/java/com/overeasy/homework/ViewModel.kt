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
    private var page = 0
    val publishSubject: PublishSubject<Post> = PublishSubject.create()
    val publishSubjectDelete: PublishSubject<Post> = PublishSubject.create()
    private val repository: Repository by lazy {
        Repository()
    }

    fun getPosts() = repository.getPosts()

    fun getDetailDatas() = repository.getDetailDatas()

    fun getResponseCode() = repository.getResponseCode()

    private fun getDataPosts(start: Int) = repository.getDataPosts(start)

    private fun getDataComments(post: Post) = repository.getDataComments(post)

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T{
            return ViewModel(application) as T
        }
    }

    init {
        getDataPosts(0)
        publishSubject.subscribe { post ->
            getDataComments(post)
        }
        publishSubjectDelete.subscribe { post ->
            deletePost(post)
        }
    }

    fun scrollLoad(page: Int) {
        this.page = page
        getDataPosts(page * 10)
    }

    fun updatePost(post: Post) = repository.updatePost(post)

    private fun deletePost(post: Post) = repository.deletePost(post)

    private fun println(data: String) = Log.d("ViewModel", data)
}