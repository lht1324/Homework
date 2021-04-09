package com.overeasy.homework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.databinding.FragmentMainBinding

class MainFragment() : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentMainBinding
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        init()

        return binding.root
    }

    private fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        binding.apply {
            progressBar.apply {
                isIndeterminate = true
                visibility = View.VISIBLE
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(30))
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) { // 1: 최하단 도달, -1: 최상단 도달
                        // 최하단에 도달했을 때 불러온 걸 추가하면 되는 거 아냐
                        // 생각해보니 getPosts() 할 조건은 2개다
                        // Detail View 들어갔다가 나오거나
                        // viewModel.scrollLoad()
                    }
                }
            })
        }
        viewModel.getPosts().observe((activity as MainActivity), { posts ->
            adapter.posts = posts
            binding.recyclerView.adapter = adapter
            binding.progressBar.apply {
                isIndeterminate = false
                visibility = View.GONE
            }
        })

        adapter.onItemClicked.observe((activity as MainActivity), { post ->
            viewModel.publishSubject.onNext(post)
            (activity as MainActivity).replaceDetailFragment()
        })
    }

    private fun println(data: String) = Log.d("MainFragment", data)
}