package com.pools.store.presentation.tabOption.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pools.store.presentation.tabOption.category.view.CategoryFragment
import com.pools.store.presentation.tabOption.leaderboard.view.LeaderBoardFragment
import com.pools.store.presentation.tabOption.offerApp.view.OfferAppFragment

class TabOptionAppAdapter(fragmentManager: FragmentActivity
) : FragmentStateAdapter(fragmentManager) {

    private var childFragment: Array<Fragment>

    init {
        val leaderBoardFragment = LeaderBoardFragment()
        val offerAppFragment = OfferAppFragment()
        val categoryFragment = CategoryFragment()

        childFragment = arrayOf(
            leaderBoardFragment,
            offerAppFragment,
            categoryFragment
        )
    }

    override fun getItemCount(): Int {
        return childFragment.size
    }

    override fun createFragment(position: Int): Fragment {

        return childFragment[position]


    }
}
