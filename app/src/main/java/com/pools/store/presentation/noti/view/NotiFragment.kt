package com.pools.store.presentation.noti.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.pools.store.base.BaseFragment
import com.pools.store.core.DialogNotice
import com.pools.store.databinding.FragmentNotiBinding
import com.pools.store.extension.textGradientColor
import com.pools.store.presentation.main.MainViewModel
import com.pools.store.presentation.noti.tabLayout.adapter.NotiViewPagetAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotiFragment : BaseFragment<FragmentNotiBinding, MainViewModel>() {

    override fun getViewBinding() = FragmentNotiBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            viewPaperWallet.adapter = NotiViewPagetAdapter(requireActivity())
            TabLayoutMediator(tabLayout,viewPaperWallet){ tab , position ->
                tab.text = when(position){
                    0->"System"
//                    1->"Usage"
                    else -> ""
                }
            }.attach()


            tvClear.setOnClickListener {
                val dialogFragment = DialogNotice()
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "dialog_notice"
                )
            }
            tvClear.paint.setShader(textGradientColor(tvClear))
        }
    }
}