package com.pools.store.presentation.noti.tabLayout.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseFragment
import com.pools.store.base.BaseViewModel
import com.pools.store.databinding.FragmentNotiUsageBinding
import com.pools.store.presentation.main.MainViewModel
import com.pools.store.presentation.noti.adapter.ItemNotiAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UsageNotiChildTabFragment : BaseFragment<FragmentNotiUsageBinding, BaseViewModel>() {


    override val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var itemNotiAdapter: ItemNotiAdapter

    override fun getViewBinding(): FragmentNotiUsageBinding =
        FragmentNotiUsageBinding.inflate(layoutInflater)

    override fun onBackFragment() {


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewStateChange()
    }


    private fun initViewStateChange() {


    }

    private fun initView() {
        binding.apply {
            rvUsageNoti.apply {
                adapter = itemNotiAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
//            val list = listOf("","","","","","","","","")
//            itemNotiAdapter.list = list
//            itemNotiAdapter.notifyDataSetChanged()
        }

    }


}