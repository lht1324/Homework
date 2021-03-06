package com.overeasy.homework.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.overeasy.homework.R
import com.overeasy.homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment by lazy {
        MainFragment()
    }
    private var backPressedLast: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashTheme에서 AppTheme로 교체
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        // 다크 모드 해제
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.constraintLayout, mainFragment)
                    .commit()
    }

    /*
    MainFragment에서 사용할 수 있도록 override 하지 않았다.
    뒤로 가기를 2번 누르면 앱이 종료된다.
     */
    fun onBackPressedMainFragment() {
        if (System.currentTimeMillis() - backPressedLast < 2000) {
            finish()
            return
        }

        Toast.makeText(this, "종료하려면 뒤로 가기 버튼을\n한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        backPressedLast = System.currentTimeMillis()
    }

    private fun init() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.constraintLayout, mainFragment)
            .commit()
    }

    // DetailFragment로 전환
    fun changeToDetailFragment() {
        supportActionBar!!.title = "Post"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
                .beginTransaction()
                .hide(mainFragment)
                .add(R.id.constraintLayout, DetailFragment())
                .addToBackStack(null)
                .commit()
    }

    // MainFragment로 전환
    fun changeToMainFragment() {
        supportActionBar!!.title = "Lorem Ipsum"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportFragmentManager
                .popBackStack()
        supportFragmentManager
                .beginTransaction()
                .show(mainFragment)
                .commit()
    }

    // 로그 출력
    private fun println(data: String) = Log.d("MainActivity", data)
}