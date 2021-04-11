package com.overeasy.homework.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.homework.R
import com.overeasy.homework.ViewModel
import com.overeasy.homework.databinding.FragmentDetailBinding
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post

class DetailFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var callback: OnBackPressedCallback
    private val detailAdapter: DetailAdapter by lazy {
        DetailAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setHasOptionsMenu(true)

        init()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.constraintLayout.visibility = View.GONE
        binding.progressBar.apply {
            isIndeterminate = true
            visibility = View.VISIBLE
        }
        // onResume 동안 프로그레스 바 보여주기
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).replaceMainFragment()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).replaceMainFragment()
        }
        return true
    }

    private fun init() {
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ViewModel::class.java)

        binding.apply {
            recyclerView.adapter = detailAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(30))
        }

        viewModel.getDetailDatas().observe(viewLifecycleOwner, { detailDatas ->
            detailAdapter.comments = detailDatas[0] as ArrayList<Comment>
            binding.apply {
                progressBar.apply {
                    isIndeterminate = false
                    visibility = View.GONE
                }
                constraintLayout.visibility = View.VISIBLE
                post = detailDatas[1] as Post
            }
        })
    }

    private fun println(data: String) = Log.d("DetailFragment", data)
}