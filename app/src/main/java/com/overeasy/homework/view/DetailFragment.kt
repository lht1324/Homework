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

// 디테일 뷰를 보여주는 프래그먼트
class DetailFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var callback: OnBackPressedCallback
    private val detailAdapter: DetailAdapter by lazy {
        DetailAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        // onBackPressedDispatcher에 onAttach()에서 초기화된 콜백 추가
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // 액션 바 메뉴 활성화
        setHasOptionsMenu(true)

        init()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 프래그먼트가 열렸을 때 기존 데이터를 보여주기 때문에 새로운 데이터를 받아올 때까지 레이아웃을 숨긴다.
        binding.constraintLayout.visibility = View.GONE

        // onResume 동안 프로그레스 바 보여주기
        binding.progressBar.apply {
            isIndeterminate = true
            visibility = View.VISIBLE
        }
    }

    // 뒤로 가기 버튼을 누를 때 액티비티의 onBackPressed가 아니라 MainActivity로 돌아가는 기능 실행하는 콜백 초기화
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).changeToMainFragment()
            }
        }
    }

    // onBackPressedCallback 삭제
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    // 실행하지 않으면 MainFragment에서만 보여야 할 도움말 버튼이 보인다.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        return super.onCreateOptionsMenu(menu, inflater)
    }

    // 액션 바의 뒤로 가기 버튼(Home) 눌렀을 때 MainActivity로 돌아가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).changeToMainFragment()
        }
        return true
    }

    private fun init() {
        /*
        ViewModel을 MainFragment와 DetailFragment에서 공유하기 위해
        ViewModelStoreOwner를 activity로 설정
         */
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ViewModel::class.java)

        binding.recyclerView.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(RecyclerViewDecoration(30))
        }

        // ViewModel의 detailDatas() (Post + ArrayList<Comment>)를 관찰한다.
        viewModel.getDetailDatas().observe(viewLifecycleOwner, { detailDatas ->
            detailAdapter.comments = detailDatas[0] as ArrayList<Comment>
            binding.apply {
                post = detailDatas[1] as Post

                // 데이터를 받아온 뒤 레이아웃을 보여주고 프로그레스 바를 숨긴다.
                constraintLayout.visibility = View.VISIBLE
                progressBar.apply {
                    isIndeterminate = false
                    visibility = View.GONE
                }
            }
        })
    }

    // 로그 출력
    private fun println(data: String) = Log.d("DetailFragment", data)
}