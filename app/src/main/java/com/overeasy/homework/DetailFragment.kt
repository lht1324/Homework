package com.overeasy.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.homework.databinding.FragmentDetailBinding
import com.overeasy.homework.pojo.Comment

class DetailFragment : Fragment() {
    // onClick 눌리면 Unit 전송해서 액티비티에서 실행하는 걸로 하자
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding
    private val adapter: DetailAdapter by lazy {
        DetailAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }

        return binding.root
    }

    fun init() {
        // viewModel = ViewModelProvider(requireActivity(), ViewModel.Factory(application)).get(ViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(30))
        }
        // Observable.merge로 Post랑 Comments 동시에 묶어서 열어야 하나?
        viewModel.getComments().observe(this, { comments ->

            (activity as MainActivity).supportActionBar!!.title = "Post Detail"
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, DetailFragment(comments))
                    .commit()
        })
    }
}