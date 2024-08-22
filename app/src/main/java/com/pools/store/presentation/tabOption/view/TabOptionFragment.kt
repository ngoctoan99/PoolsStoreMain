//package com.pools.store.presentation.tabOption.view
//
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.viewModels
//import com.google.android.material.tabs.TabLayoutMediator
//import com.pools.store.base.BaseFragment
//import com.pools.store.databinding.FragmentTabOptionBinding
//import com.pools.store.presentation.main.MainViewModel
//import com.pools.store.presentation.tabOption.adapter.TabOptionAdapter
//import com.pools.store.utils.Constant
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class TabOptionFragment : BaseFragment<FragmentTabOptionBinding, MainViewModel>() {
//
//    override fun getViewBinding() = FragmentTabOptionBinding.inflate(layoutInflater)
//    override val viewModel: MainViewModel by viewModels()
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        setDataTabOptionViewPaper2()
//    }
//
//    private fun setDataTabOptionViewPaper2() {
//        binding.apply {
//            viewPaperTabOption.adapter = TabOptionAdapter(requireActivity(), Constant.KEY_TYPE_FRAGMENT_GAME)
//            viewPaperTabOption.isUserInputEnabled = false
//            TabLayoutMediator(tabOptionChose,viewPaperTabOption){ tab , position ->
//                tab.text = when(position){
//                    0->"Leader Board"
//                    1->"Offer"
//                    2 -> "Category"
//                    else -> ""
//                }
//            }.attach()
//        }
//    }
//}