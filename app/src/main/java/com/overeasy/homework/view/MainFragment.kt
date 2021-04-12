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

class MainFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var callback: OnBackPressedCallback
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        setHasOptionsMenu(true)

        init()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).onBackPressedFragment()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.help)
            startActivity(Intent(activity, HelpActivity::class.java))

        return true
    }

    private fun init() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder) = true

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int) = mainAdapter.onSwiped(viewHolder.adapterPosition)
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
                                showToast("마지막 페이지입니다.")
                        }
                    }
                })
            }
        }

        viewModel.getPosts().observe(viewLifecycleOwner, { posts ->
            mainAdapter.setList(posts)
            mainAdapter.notifyItemRangeInserted((mainAdapter.page - 1) * 10 + 1, 10)
            // positionStart부터 몇 개가 들어가느냐를 알리는 것이니 start는 (기존 posts의 마지막 인덱스 + 1)이어야 한다.
            binding.progressBar.apply {
                isIndeterminate = false
                visibility = View.GONE
            }
        })

        viewModel.getDeleteResult().observe(viewLifecycleOwner, { responseCode ->
            showToast(
                if (responseCode == 200)
                    "삭제되었습니다.\n(responseCode = $responseCode)"
                else
                    "삭제되지 않았습니다.\n(responseCode = $responseCode)")
        })

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

        mainAdapter.onItemClicked.observe(viewLifecycleOwner, { post ->
            viewModel.getDataComments(post)
            callback.remove()
            (activity as MainActivity).replaceDetailFragment()
        })

        mainAdapter.onItemLongPressed.observe(viewLifecycleOwner, { post ->
            openUpdateDialog(post)
        })

        mainAdapter.onItemSwiped.observe(viewLifecycleOwner, { id ->
            viewModel.deletePost(id)
        })
    }

    private fun openUpdateDialog(post: Post) {
        val updateDialog = UpdateDialog(requireActivity())
        updateDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        updateDialog.post = Post(post.id, post.title, post.body)
        // post를 그대로 대입하면 updateDialog.post의 내용이 post에 복사된다.
        // 원인 발견 못 함.
        // Rx로 교체 후 발생했는데 정확한 원인은 찾지 못했다.

        updateDialog.setOnDismissListener {
            if (post.title != updateDialog.post.title || post.body != updateDialog.post.body) {
                mainAdapter.updatedPosition = updateDialog.post.id.toDouble().toInt() - 1
                viewModel.updatePost(updateDialog.post)
            } // 내용을 바꾸지 않고 꾹 누르기만 한 경우 제외
        }

        updateDialog.setCancelable(true)
        updateDialog.show()
    }

    private fun println(data: String) = Log.d("MainFragment", data)

    private fun showToast(data: String) = Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
}