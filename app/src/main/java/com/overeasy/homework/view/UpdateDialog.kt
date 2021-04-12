package com.overeasy.homework.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.overeasy.homework.R
import com.overeasy.homework.databinding.DialogUpdateBinding
import com.overeasy.homework.pojo.Post

// Post를 업데이트할 때 내용을 수정하는 다이얼로그
class UpdateDialog(private val mContext: Context) : Dialog(mContext) {
    // dismissListener에서 post를 받기 위해 선언한다.
    private lateinit var updateDialog: UpdateDialog

    private lateinit var binding: DialogUpdateBinding
    private val layoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams()
    }
    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.dialog_update, null, false)

        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.post = post

        // editText(title), editText2(body)의 내용이 바뀌면 post의 title과 body의 내용을 editText에서 받아 변경한다.
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                post.title = p0.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                post.body = p0.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        layoutParams.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            dimAmount = 0.5f
        }

        window!!.attributes = layoutParams
        updateDialog = this
    }
}