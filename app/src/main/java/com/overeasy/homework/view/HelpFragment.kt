package com.overeasy.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.overeasy.homework.R
import com.overeasy.homework.databinding.FragmentHelpBinding

// HelpActivity에서 사용되는 프래그먼트
class HelpFragment(
        private val description: String) : Fragment() {
    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)

        binding.apply {
            description = this@HelpFragment.description
        }

        return binding.root
    }
}