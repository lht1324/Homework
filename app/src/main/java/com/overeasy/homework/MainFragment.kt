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
import com.overeasy.homework.pojo.Post

class MainFragment() : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentMainBinding
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        init()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        binding.apply {
            progressBar.apply {
                isIndeterminate = true
                visibility = View.VISIBLE
            }

            recyclerView.apply {
                adapter = mainAdapter
                layoutManager = LinearLayoutManager(activity)
                addItemDecoration(RecyclerViewDecoration(30))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val comparePositionAndCount =
                                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition() ==
                                        recyclerView.adapter!!.itemCount - 1

                        if (!recyclerView.canScrollVertically(1) && comparePositionAndCount) {
                            mainAdapter.stopLoading()
                            viewModel.scrollLoad(++(mainAdapter.page))
                        }
                    }
                })
            }
        }

        viewModel.getPosts().observe((activity as MainActivity), { posts ->
            mainAdapter.setList(posts)
            mainAdapter.notifyItemRangeInserted((mainAdapter.page - 1) * 10, 10)
            binding.progressBar.apply {
                isIndeterminate = false
                visibility = View.GONE
            }
        })

        mainAdapter.onItemClicked.observe((activity as MainActivity), { post ->
            viewModel.publishSubject.onNext(post)
            (activity as MainActivity).replaceDetailFragment()
        })
    }

    private fun println(data: String) = Log.d("MainFragment", data)
}