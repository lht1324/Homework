package com.overeasy.homework.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.overeasy.homework.R
import com.overeasy.homework.databinding.ActivityHelpBinding

// 도움말 화면 액티비티
class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private val pagerAdapter by lazy {
        PagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // HelpActivity에서도 테마를 AppTheme로 변경해야 한다.
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_help)

        init()
    }

    override fun onBackPressed() = finish()

    private fun init() {
        // 액션 바 숨기기
        supportActionBar!!.hide()

        // 프래그먼트 생성
        pagerAdapter.fragments.add(HelpFragment("아이템을 터치하면\n댓글을 볼 수 있습니다."))
        pagerAdapter.fragments.add(HelpFragment("아이템을 꾹 누르면\n내용을 바꿀 수 있습니다."))
        pagerAdapter.fragments.add(HelpFragment("아이템을 밀면\n삭제됩니다."))

        binding.apply {
            viewPager.apply {
                // 페이지 변경 시 페이지 수도 변경
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        textView.text = (viewPager.currentItem + 1).toString()
                    }
                })

                adapter = pagerAdapter
            }

            // 완료 버튼 ( ✓ )
            buttonSkip.setOnClickListener { finish() }

            // 다음 버튼 ( > )
            buttonNext.setOnClickListener {
                val position = viewPager.currentItem

                // 마지막 페이지가 아니면 다음 페이지로 이동
                if (position < pagerAdapter.fragments.size) {
                    viewPager.setCurrentItem(position + 1, true)
                }
                // 마지막 페이지면 HelpActivity 종료
                if (position == pagerAdapter.fragments.size - 1)
                    finish()
            }

            // 전체 페이지 수
            textView2.text = pagerAdapter.fragments.size.toString()
        }
    }

    // 로그 출력
    fun println(data: String) = Log.d("HelpActivity", data)
}