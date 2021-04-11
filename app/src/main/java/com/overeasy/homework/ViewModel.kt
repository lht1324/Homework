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

class ViewModel : ViewModel() {
    val publishSubject: PublishSubject<Post> = PublishSubject.create()
    val publishSubjectUpdate: PublishSubject<Post> = PublishSubject.create()
    val publishSubjectDelete: PublishSubject<Post> = PublishSubject.create()
    private val repository: Repository by lazy {
        Repository()
    }

    init {
        getDataPosts(0)
        publishSubject.subscribe { post ->
            getDataComments(post)
        }
        publishSubjectUpdate.subscribe { post ->
            updatePost(post)
        }
        publishSubjectDelete.subscribe { post ->
            deletePost(post)
        }
    }

    fun getPosts() = repository.getPosts()

    fun getDetailDatas() = repository.getDetailDatas()

    fun getDeleteResult() = repository.getDeleteResult()

    fun getUpdateResult() = repository.getUpdateResult()

    fun scrollLoad(page: Int) = getDataPosts(page * 10)

    private fun getDataPosts(start: Int) = repository.getDataPosts(start)

    private fun getDataComments(post: Post) = repository.getDataComments(post)

    private fun updatePost(post: Post) = repository.updatePost(post)

    private fun deletePost(post: Post) = repository.deletePost(post)

    private fun println(data: String) = Log.d("ViewModel", data)
}