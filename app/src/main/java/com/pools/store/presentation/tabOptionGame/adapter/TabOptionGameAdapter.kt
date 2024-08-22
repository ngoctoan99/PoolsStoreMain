package com.pools.store.presentation.tabOptionGame.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pools.store.presentation.tabOptionGame.view.category.view.CategoryGameFragment
import com.pools.store.presentation.tabOptionGame.view.leaderboard.view.LeaderBoardGameFragment
import com.pools.store.presentation.tabOptionGame.view.offer.view.OfferGameFragment

class TabOptionGameAdapter(fragmentManager: FragmentActivity
) : FragmentStateAdapter(fragmentManager) {

    private var childFragment: Array<Fragment>

    init {
        val leaderBoardGameFragment = LeaderBoardGameFragment()
        val offerGameFragment = OfferGameFragment()
        val categoryGameFragment = CategoryGameFragment()

        childFragment = arrayOf(
            leaderBoardGameFragment,
            offerGameFragment,
            categoryGameFragment
        )



    }

    override fun getItemCount(): Int {
        return childFragment.size
    }

    override fun createFragment(position: Int): Fragment {

        return childFragment[position]


    }
}
