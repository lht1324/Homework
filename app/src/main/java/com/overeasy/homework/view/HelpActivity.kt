package com.overeasy.homework.view


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.overeasy.homework.R
import com.overeasy.homework.databinding.ActivityHelpBinding


class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private val pagerAdapter by lazy {
        PagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_help)

        init()
    }

    override fun onBackPressed() = finish()

    private fun init() {
        supportActionBar!!.hide()

        pagerAdapter.fragments.add(HelpFragment("아이템을 터치하면\n댓글을 볼 수 있습니다."))
        pagerAdapter.fragments.add(HelpFragment("아이템을 꾹 누르면\n내용을 바꿀 수 있습니다."))
        pagerAdapter.fragments.add(HelpFragment("아이템을 밀면\n삭제됩니다."))

        binding.apply {
            // 페이지 변경 시 페이지 수도 변경
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textView.text = (viewPager.currentItem + 1).toString()
                }
            })

            viewPager.adapter = pagerAdapter


            buttonSkip.setOnClickListener { finish() }

            buttonNext.setOnClickListener {
                val position = viewPager.currentItem

                // 마지막 페이지가 아니면 다음 페이지로 이동
                if (position < pagerAdapter.fragments.size) {
                    viewPager.setCurrentItem(position + 1, true)
                }
                // 마지막 페이지면 MainActivity로 복귀
                if (position == pagerAdapter.fragments.size - 1)
                    finish()
            }

            // 전체 페이지 수
            textView2.text = pagerAdapter.fragments.size.toString()
        }
    }

    // 로그 확인용
    fun println(data: String) = Log.d("HelpActivity", data)
}