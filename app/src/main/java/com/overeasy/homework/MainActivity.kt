package com.overeasy.homework

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.overeasy.homework.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityMainBinding
    private val mainFragment by lazy {
        MainFragment()
    }
    private val detailFragment by lazy {
        DetailFragment()
    }
    // 비동기로 처리해야 할 것
    // UI 제외한 나머지
    // 네트워킹, 데이터 받아오기
    // 네트워킹은 Retrofit으로 했다

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
            .add(R.id.constraintLayout, mainFragment)
            .commit()
    }

    fun replaceDetailFragment() {
        val detailFragment: DetailFragment by lazy {
            DetailFragment()
        }

        supportActionBar!!.title = "Post"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // First execution
        // mainFragment is not null
        // detailFragment is null
        // hide mainFragment
        // show detailFragment

        supportFragmentManager
                .beginTransaction()
                .hide(mainFragment)
                .add(R.id.constraintLayout, detailFragment)
                .addToBackStack(null)
                .commit()
        /* supportFragmentManager
            .beginTransaction()
            .replace(R.id.constraintLayout, detailFragment)
            .commit() */
    }

    fun replaceMainFragment() {
        val mainFragment: MainFragment by lazy {
            MainFragment()
        }

        supportActionBar!!.title = "Lorem Ipsum"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportFragmentManager
                .popBackStack()
        supportFragmentManager
                .beginTransaction()
                .show(mainFragment)
                .commit()
        /* supportFragmentManager
            .beginTransaction()
            .replace(R.id.constraintLayout, mainFragment)
            .commit() */
    }

    private fun println(data: String) = Log.d("MainActivity", data)
}