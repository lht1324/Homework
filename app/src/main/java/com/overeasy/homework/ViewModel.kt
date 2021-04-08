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
    private val repository: Repository by lazy {
        Repository()
    }
    val publishSubject: PublishSubject<Int> = PublishSubject.create()

    fun getPosts() = posts

    fun getComments() = comments

    private fun getDataPosts(start: Int) = repository.getDataPosts(start)

    private fun getDataComments(id: Int) = repository.getDataComments(id)

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
        repository.getComments().observeForever {
            comments.value = it
        }
        publishSubject.subscribe { id ->
            getDataComments(id)
        }
    }

    private fun println(data: String) = Log.d("ViewModel", data)
}