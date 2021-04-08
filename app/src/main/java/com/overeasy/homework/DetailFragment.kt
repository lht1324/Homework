package com.overeasy.homework

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.homework.databinding.FragmentDetailBinding
import com.overeasy.homework.pojo.Comment
import com.overeasy.homework.pojo.Post

class DetailFragment : Fragment() {
    // onClick 눌리면 Unit 전송해서 액티비티에서 실행하는 걸로 하자
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var callback: OnBackPressedCallback
    private val adapter: DetailAdapter by lazy {
        DetailAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        setHasOptionsMenu(true)

        init()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home)
                (activity as MainActivity).replaceMainFragment()
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).replaceMainFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun init() {
        // viewModel = ViewModelProvider(requireActivity(), ViewModel.Factory(application)).get(ViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        viewModel.getComments()

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(30))
        }
        // Observable.merge로 Post랑 Comments 동시에 묶어서 열어야 하나?

        viewModel.getDetailDatas().observe((activity as MainActivity), { detailDatas ->
            adapter.comments = detailDatas[0] as ArrayList<Comment>
            binding.apply {
                post = detailDatas[1] as Post
                recyclerView.adapter = adapter
            }
        })
    }

    private fun println(data: String) = Log.d("DetailFragment", data)
}