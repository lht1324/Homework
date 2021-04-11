package com.overeasy.homework

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post
import com.overeasy.homework.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ViewModel : ViewModel() {
    private lateinit var post: Post
    private val compositeDisposable = CompositeDisposable()
    private val posts = MutableLiveData<ArrayList<Post>>()
    private val detailDatas = MutableLiveData<ArrayList<Any>>()
    private val deleteResult = MutableLiveData<Int>()
    private val updateResult = MutableLiveData<ArrayList<Any>>()
    private val repository: Repository by lazy {
        Repository()
    }

    init {
        getDataPosts(0)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun getDataPosts(start: Int) = addDisposable(repository.getPosts(start)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { posts.postValue(it.body()) },
                    { println("response error in deletePost: ${it.message}")}
            ))

    fun getDataComments(post: Post) {
        this.post = post

        addDisposable(repository.getComments(post.id.toDouble().toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val tempList = ArrayList<Any>()
                            tempList.add(it.body() as ArrayList<Comment>)
                            tempList.add(post)
                            detailDatas.postValue(tempList)
                        },
                        { println("response error in updatePost: ${it.message}") }
                ))
    }

    fun updatePost(post: Post) = addDisposable(repository.updatePost(post.id.toDouble().toInt(), post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    {
                        val tempList = ArrayList<Any>()
                        tempList.add(it.body() as Post)
                        tempList.add(it.code())
                        updateResult.postValue(tempList)
                    },
                    { println("response error in updatePost: ${it.message}")}
            ))

    fun deletePost(id: String) = addDisposable(repository.deletePost(id.toDouble().toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { deleteResult.postValue(it.code()) },
                    { println("response error in deletePost: ${it.message}")}
            ))

    fun getPosts() = posts

    fun getDetailDatas() = detailDatas

    fun getDeleteResult() = deleteResult

    fun getUpdateResult() = updateResult

    fun scrollLoad(page: Int) = getDataPosts(page * 10)

    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    private fun println(data: String) = Log.d("ViewModel", data)
}