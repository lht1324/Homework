package com.overeasy.homework

import android.content.Context
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

    /* override fun onAttach(context: Context) {
        super.onAttach(context)
        println("onAttach() is executed. posts")
    }

    override fun onDetach() {
        super.onDetach()
        println("onDetach() is executed. posts")
    } */

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.getPosts().removeObservers(viewLifecycleOwner)
        mainAdapter.onItemClicked.removeObservers(viewLifecycleOwner)
    }

    private fun init() {
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
                            // 페이지가 9 미만이면 로드
                            // 아니면 로드 안 하고 토스트
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
            println("page ${mainAdapter.page}'s id is ${posts[0].id}")
            mainAdapter.setList(posts)
            mainAdapter.notifyItemRangeInserted((mainAdapter.page - 1) * 10 + 1, 10)
            // positionStart부터 몇 개가 들어가느냐를 알리는 것이니 start는 기존 마지막 인덱스 + 1이어야 한다.
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