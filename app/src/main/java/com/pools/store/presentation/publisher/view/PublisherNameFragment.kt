package com.pools.store.presentation.publisher.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentPublisherNameBinding
import com.pools.store.presentation.home.adapter.AppAdapter
import com.pools.store.presentation.publisher.viewModel.GetDetailPublisherState
import com.pools.store.presentation.publisher.viewModel.PublisherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PublisherNameFragment : BaseFragment<FragmentPublisherNameBinding, PublisherViewModel>() {

    override fun getViewBinding() = FragmentPublisherNameBinding.inflate(layoutInflater)
    override val viewModel: PublisherViewModel by viewModels()
    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }
    @Inject
    lateinit var highLightedAdapter: AppAdapter

    @Inject
    lateinit var allApplicationAdapter: AppAdapter
    val args: PublisherNameFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateChange()
        initView()

    }

    private fun initViewStateChange() {
        viewModel.getDetailPublisherState.mapNotNull { it }.onEach(this::onViewStateDetailPublisherChange)
            .launchIn(lifecycleScope)
        viewModel.getDetailPublisher(CachePreferencesHelper(requireContext()).accessToken,args.publisherId)
    }

    private fun initView() {
        binding.apply {
            ivLeft.setOnClickListener {
                onBackFragment()
            }
            rvHighlight.apply {
                adapter = highLightedAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }

            rvAllApplication.apply {
                adapter = allApplicationAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }

            highLightedAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)

                findNavController().navigate(R.id.action_publishFragment_to_detailGameFragment,bundle)
            }
            allApplicationAdapter.setItemClickListener {
                val bundle = bundleOf("dataApp" to it)

                findNavController().navigate(R.id.action_publishFragment_to_detailGameFragment,bundle)
            }
        }
    }

    private fun onViewStateDetailPublisherChange(event : GetDetailPublisherState){
        when(event){
            is GetDetailPublisherState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)

            }
            is GetDetailPublisherState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetDetailPublisherState.Success ->{
                handleLoading(false)
                    binding.apply {
                        Glide.with(requireContext()).load(event.data.backgroundUrl).into(ivTheme)
                        Glide.with(requireContext()).load(event.data.avatarUrl).into(ivAvatar)
                        tvName.text  = event.data.name
                        tvType.text = event.data.bio

                        highLightedAdapter.list = event.data.apks
                        highLightedAdapter.notifyDataSetChanged()

                        allApplicationAdapter.list = event.data.apks
                        allApplicationAdapter.notifyDataSetChanged()
                    }

            }
        }
    }
}