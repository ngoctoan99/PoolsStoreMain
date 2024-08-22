package com.pools.store.presentation.noti.tabLayout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pools.store.presentation.noti.tabLayout.view.SystemNotiChildTabFragment
import com.pools.store.presentation.noti.tabLayout.view.UsageNotiChildTabFragment

class NotiViewPagetAdapter(
    fragmentManager: FragmentActivity,
) : FragmentStateAdapter(fragmentManager) {


    private var childFragment: Array<Fragment>

        init {
            val introChildFragment1 = SystemNotiChildTabFragment()

            val introChildFragment2 = UsageNotiChildTabFragment()



            childFragment = arrayOf(
                introChildFragment1,
//                introChildFragment2,
            )
        }

        override fun getItemCount(): Int {
        return childFragment.size
    }

    override fun createFragment(position: Int): Fragment {

        return childFragment[position]


    }

}