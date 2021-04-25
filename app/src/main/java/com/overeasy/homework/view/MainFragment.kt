package com.overeasy.homework.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.overeasy.homework.pojo.Post

// 메인 화면을 보여주는 프래그먼트
class MainFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var callback: OnBackPressedCallback
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // 액션 바 메뉴 활성화
        setHasOptionsMenu(true)

        init()

        return binding.root
    }

    // 뒤로 가기 버튼 누를 때 MainActivity의 onBackPressedMainFragment() 실행
    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).onBackPressedMainFragment()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).onBackPressedMainFragment()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    // onBackPressedCallback 삭제
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    // 액션 바의 도움말 버튼 출력
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    // 도움말 버튼 터치했을 때 HelpActivity 띄우기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.help)
            startActivity(Intent(activity, HelpActivity::class.java))

        return true
    }

    private fun init() {
        // viewLifecycleOwner의 사용은 onCreateView() 이후부터 가능하니 init()에서 실행
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // '밀어서 삭제' 구현을 위한 ItemTouchHelper
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder) = true

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) = mainAdapter.onSwiped(viewHolder.adapterPosition)

            override fun getSwipeDirs(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder) = if (!mainAdapter.swipeControl) 0
                else super.getSwipeDirs(recyclerView, viewHolder)
        }).attachToRecyclerView(binding.recyclerView)

        /*
        viewModel을 MainFragment와 DetailFragment에서 공유하기 위해
        ViewModelStoreOwner를 activity로 설정
         */
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ViewModel::class.java)

        binding.apply {
            // 데이터를 받아오기 전 프로그레스 바를 보여준다.
            progressBar.apply {
                isIndeterminate = true
                visibility = View.VISIBLE
            }

            recyclerView.apply {
                adapter = mainAdapter
                layoutManager = LinearLayoutManager(activity)
                addItemDecoration(RecyclerViewDecoration(30))

                // 무한 스크롤
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (!recyclerView.canScrollVertically(1)) { // 끝까지 스크롤 했을 때
                            // 무한 스크롤 로딩 중 아이템을 삭제하면 튕긴다
                            // 데이터가 100개 있으니 10페이지를 넘기면 (10개 * 10페이지 = 100개) 프로그레스 바가 출력되지 않도록 제한을 건다.
                            if (mainAdapter.page < 10) {
                                viewModel.scrollLoad((mainAdapter.page)++)
                                mainAdapter.stopLoading()
                            }
                            else
                                showToast("마지막 페이지입니다.")
                        }
                    }
                })
            }
        }

        // Client에서 posts를 받아오면 posts가 변동된다
        viewModel.getPosts().observe(viewLifecycleOwner, { posts ->
            mainAdapter.setList(posts)
            mainAdapter.swipeControl = true

            // positionStart부터 몇 개가 들어가느냐를 알리는 것이니 start는 (기존 posts의 마지막 인덱스 + 1)이어야 한다.
            mainAdapter.notifyItemRangeInserted((mainAdapter.page - 1) * 10 + 1, 10)

            // 앱을 처음 실행할 때 어댑터의 posts에 새로운 데이터가 들어오면 프로그레스 바를 종료한다.
            if (mainAdapter.page == 1) {
                binding.progressBar.apply {
                    isIndeterminate = false
                    visibility = View.GONE
                }
            }
        })

        // deletePost()가 실행되고 통신이 성공적으로 실행되면 status code를 받아 정상적으로 삭제되었는지 확인한다.
        viewModel.getDeleteResult().observe(viewLifecycleOwner, { responseCode ->
            showToast(
                if (responseCode == 200)
                    "삭제되었습니다.\n(responseCode = $responseCode)"
                else
                    "삭제되지 않았습니다.\n(responseCode = $responseCode)")
        })

        /*
        updatePost()가 실행되고 통신이 성공하면 status code와 업데이트된 post를 받아
        어댑터를 업데이트하고 status code를 확인한다.
         */
        viewModel.getUpdateResult().observe(viewLifecycleOwner, { updatedResult ->
            /*
            updatedResult[0] = response.body()
            updatedResult[1] = response.code()
             */
            showToast(
                    if (updatedResult[1] == 200)
                        "수정되었습니다.\n(responseCode = ${updatedResult[1]})"
                    else
                        "수정되지 않았습니다.\n(responseCode = ${updatedResult[1]})")
            mainAdapter.updatePost(updatedResult[0] as Post)
        })

        // 아이템이 터치되었는지 관찰
        mainAdapter.onItemClicked.observe(viewLifecycleOwner, { post ->
            viewModel.getDataComments(post)
            callback.remove()
            (activity as MainActivity).changeToDetailFragment()
        })

        // 아이템이 길게 눌렸는지 관찰
        mainAdapter.onItemLongPressed.observe(viewLifecycleOwner, { post ->
            openUpdateDialog(post)
        })

        // 아이템이 옆으로 밀렸는지 관찰
        mainAdapter.onItemSwiped.observe(viewLifecycleOwner, { id ->
            viewModel.deletePost(id)
        })
    }

    // 아이템이 길게 눌렸을 때 Dialog를 연다.
    private fun openUpdateDialog(post: Post) {
        val updateDialog = UpdateDialog(requireActivity())

        /*
        post를 그대로 대입하면 updateDialog.post의 내용이 post에 복사되기만 하고 UI가 업데이트 되지 않는다.
        원인 발견 못 함.
        Rx로 교체 후 발생했는데 정확한 원인은 찾지 못했다.
         */
        updateDialog.post = Post(post.id, post.title, post.body)

        updateDialog.setOnDismissListener {
            if (post.title != updateDialog.post.title || post.body != updateDialog.post.body) {
                mainAdapter.updatedPosition = updateDialog.post.id.toDouble().toInt() - 1
                viewModel.updatePost(updateDialog.post)
            } // Dialog를 열어 내용을 바꾸지 않고 닫기만 한 경우 제외
        }

        updateDialog.setCancelable(true)
        updateDialog.show()
    }

    // 로그 출력
    private fun println(data: String) = Log.d("MainFragment", data)

    // 토스트 출력
    private fun showToast(data: String) = Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
}