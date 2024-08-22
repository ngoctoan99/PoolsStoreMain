package com.pools.store.presentation.favorite.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pools.store.R
import com.pools.store.base.BaseFragment
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.FragmentFavoriteBinding
import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.PublisherIdDomain
import com.pools.store.presentation.favorite.adapter.FavoriteAdapter
import com.pools.store.presentation.favorite.viewModel.FavoriteViewModel
import com.pools.store.presentation.favorite.viewModel.GetListFavoriteAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    override fun getViewBinding() = FragmentFavoriteBinding.inflate(layoutInflater)
    override val viewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var favoriteAdapter:FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        binding.apply {
            rvFavorite.apply {
                adapter = favoriteAdapter
                layoutManager = GridLayoutManager(requireContext(),2)
            }


            favoriteAdapter.setItemClickListener {
                val appDomain = AppDomain(
                    name = it.name ,
                    packageName = it.packageName ,
                    version = it.version ,
                    fileUrl = it.fileUrl,
                    iconUrl = it.iconUrl ,
                    types = it.types ,
                    tags = it.tags ,
                    accessType = it.accessType ,
                    acceptDownload = it.acceptDownload ,
                    size = it.size ,
                    pricing = it.pricing ,
                    id = it.id ,
                    countDownload = it.countDownload.toInt() ,
                    averageStar = it.averageStar,
                    publisherId = PublisherIdDomain(id = it.publisherId, name = ""),
                    imageContentUrls = it.imageContentUrls
                )
                val bundle = bundleOf("dataApp" to appDomain)
                findNavController().navigate(R.id.action_favoriteFragment_to_detailGameFragment, bundle)
            }

            ivLeft.setOnClickListener{onBackFragment()}
        }
    }

    private fun initData() {
        viewModel.getListFavoriteAppState.mapNotNull { it }.onEach (this::onViewStateListAppChange)
            .launchIn(lifecycleScope)
        viewModel.getListFavoriteApp(CachePreferencesHelper(requireContext()).accessToken)



    }

    override fun onBackFragment() {
        super.onBackFragment()
        findNavController().navigateUp()
    }
    private fun onViewStateListAppChange(event : GetListFavoriteAppState){
        when(event){

            is GetListFavoriteAppState.Error ->{
                handleLoading(false)
                handleErrorMessage(message = event.error, statusCode = event.statusCode)
                Log.d("TTT E"," ${event.error} ${event.statusCode}")
            }
            is GetListFavoriteAppState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetListFavoriteAppState.Success ->{
                handleLoading(false)
                Log.i("TTT S"," ${event}")
                if(event.data.favorites.isNotEmpty()){
                    binding.apply {
                        rvFavorite.visibility = View.VISIBLE
                        tvNoDataFavorites.visibility = View.GONE
                        favoriteAdapter.list = event.data.favorites
                        favoriteAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }
}