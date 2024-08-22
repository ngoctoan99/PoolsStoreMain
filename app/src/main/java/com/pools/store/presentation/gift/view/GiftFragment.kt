package com.pools.store.presentation.gift.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseFragment
import com.pools.store.core.DialogClaimSuccess
import com.pools.store.databinding.FragmentGiftBinding
import com.pools.store.presentation.gift.adapter.GiftAdapter
import com.pools.store.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GiftFragment : BaseFragment<FragmentGiftBinding, MainViewModel>() {

    override fun getViewBinding() = FragmentGiftBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var eventAdapter: GiftAdapter

    @Inject
    lateinit var dailyMissionAdapter: GiftAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onBackFragment() {
        findNavController().navigateUp()
    }

    private fun initView() {
        binding.apply {
            rvEvent.apply {
                adapter = eventAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            rvDailyMission.apply {
                adapter = dailyMissionAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            ivLeft.setOnClickListener {
                onBackFragment()
            }

            eventAdapter.setItemClickListener {
                val dialogFragment =
                    DialogClaimSuccess("",
                        object : DialogClaimSuccess.ActionSuccessInterface {
                            override fun click() {

                            }
                        })
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "dialog_claim_success"
                )
            }
            dailyMissionAdapter.setItemClickListener {
                val dialogFragment =
                    DialogClaimSuccess("",
                        object : DialogClaimSuccess.ActionSuccessInterface {
                            override fun click() {
                                onBackFragment()
                            }
                        })
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "dialog_claim_success"
                )
            }


        }

        val list = listOf("a","b","c","d","e")
        eventAdapter.list = list
        eventAdapter.notifyDataSetChanged()

        dailyMissionAdapter.list = list
        dailyMissionAdapter.notifyDataSetChanged()
    }
}