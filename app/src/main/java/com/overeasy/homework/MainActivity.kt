package com.overeasy.homework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private val mainFragment: MainFragment by lazy {
        MainFragment()
    }
    private val detailFragment: DetailFragment by lazy {
        DetailFragment()
    }
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, ViewModel.Factory(application)).get(ViewModel::class.java)

        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, mainFragment)
                .add(android.R.id.content, detailFragment)
                .commit()

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(30))
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) { // 1: 최하단 도달, -1: 최상단 도달
                        // 최하단에 도달했을 때 불러온 걸 추가하면 되는 거 아냐
                        // 생각해보니 getPosts() 할 조건은 2개다
                        // Detail View 들어갔다가 나오거나
                    }
                }
            })
        }

        viewModel.getPosts().observe(this, { posts ->
            adapter.posts = posts
            binding.recyclerView.adapter = adapter
        })
        // Observable.merge로 Post랑 Comments 동시에 묶어서 열어야 하나?
        viewModel.getComments().observe(this, { comments ->

            supportActionBar!!.title = "Post Detail"
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, DetailFragment(comments))
                    .commit()
        })
        adapter.onItemClicked.observe(this, { id ->
            viewModel.publishSubject.onNext(id)
        })
    }

    // println에는 문제가 없다
    private fun println(data: String) = Log.d("MainActivity", data)
}