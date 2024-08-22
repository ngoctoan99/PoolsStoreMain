package com.pools.store.presentation.noti.tabLayout.adapter


import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.pools.store.base.BaseAdapterPaging

import com.pools.store.databinding.ItemNotiBinding
import com.pools.store.domain.model.NotiHistoryDomain
import com.pools.store.extension.formatDayMonthYear
import com.pools.store.extension.formatHourDayMonthYear
import com.pools.store.extension.formatNumber
import com.pools.store.utils.AppFormatter
import javax.inject.Inject

class NotiPagingAdapter @Inject constructor() :
    BaseAdapterPaging<NotiHistoryDomain>(diffCallbackTip) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotiBinding.inflate(inflater, parent, false)
        return NotiViewHolder(binding)
    }


    inner class NotiViewHolder(private val binding: ItemNotiBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<NotiHistoryDomain> {
        override fun bind(item: NotiHistoryDomain) {
            // Bind your data to the views in the ViewHolder
            Log.d("TTT NotiViewHolder" ,"${item}")
            binding.apply {
                tvTitle.text = AppFormatter().formatStringTag(item.type)
//                tvPointAdd.text = item.point.toString()
                tvPointAdd.text = formatNumber(item.point)
                tvTime.text = item.createdAt.formatHourDayMonthYear()
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }


        }

    }


    companion object {
        val diffCallbackTip = object : DiffUtil.ItemCallback<NotiHistoryDomain>() {
            override fun areItemsTheSame(oldItem: NotiHistoryDomain, newItem: NotiHistoryDomain): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NotiHistoryDomain, newItem: NotiHistoryDomain): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }


}