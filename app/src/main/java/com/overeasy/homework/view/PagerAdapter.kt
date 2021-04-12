package com.overeasy.homework.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    var fragments = ArrayList<HelpFragment>()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}