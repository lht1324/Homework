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
            .add(R.id.constraintLayout, MainFragment())
            .commitNowAllowingStateLoss()
    }

    fun replaceDetailFragment() {
        val detailFragment: DetailFragment by lazy {
            DetailFragment()
        }

        supportActionBar!!.title = "Post"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.constraintLayout, detailFragment, "detailFragment")
            .commit()
    }

    fun replaceMainFragment() {
        val mainFragment: MainFragment by lazy {
            MainFragment()
        }

        supportActionBar!!.title = "Lorem Ipsum"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.constraintLayout, mainFragment, "mainFragment")
            .commit()
    }

    private fun println(data: String) = Log.d("MainActivity", data)
}