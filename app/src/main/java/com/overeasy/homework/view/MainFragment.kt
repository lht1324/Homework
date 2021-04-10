package com.overeasy.homework.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.homework.R
import com.overeasy.homework.ViewModel
import com.overeasy.homework.databinding.FragmentMainBinding

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

    override fun onPause() {
        super.onPause()
        viewModel.getPosts().removeObservers(viewLifecycleOwner)
        mainAdapter.onItemClicked.removeObservers(viewLifecycleOwner)
    }

    private fun init() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainAdapter.onSwiped(viewHolder.adapterPosition)

            }
        }).attachToRecyclerView(binding.recyclerView)

        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ViewModel::class.java)

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

                        if (!recyclerView.canScrollVertically(1)) { // 끝까지 스크롤 했을 때
                            if (mainAdapter.page < 10) { // 마지막 페이지에선 mainAdapter.page == 10
                                mainAdapter.stopLoading()
                                viewModel.scrollLoad((mainAdapter.page)++)
                            }
                            else
                                Toast.makeText(activity, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        viewModel.getPosts().removeObservers(viewLifecycleOwner)

        viewModel.getPosts().observe(viewLifecycleOwner, { posts ->
            mainAdapter.setList(posts)
            mainAdapter.notifyItemRangeInserted((mainAdapter.page - 1) * 10 + 1, 10)
            // positionStart부터 몇 개가 들어가느냐를 알리는 것이니 start는 (기존 posts의 마지막 인덱스 + 1)이어야 한다.
            binding.progressBar.apply {
                isIndeterminate = false
                visibility = View.GONE
            }
        })

        mainAdapter.onItemClicked.observe(viewLifecycleOwner, { post ->
            viewModel.publishSubject.onNext(post)
            (activity as MainActivity).replaceDetailFragment()
        })
    }

    private fun println(data: String) = Log.d("MainFragment", data)
}