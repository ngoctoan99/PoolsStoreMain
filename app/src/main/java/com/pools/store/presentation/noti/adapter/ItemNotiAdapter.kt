package com.pools.store.presentation.noti.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemAppStoreBinding
import com.pools.store.databinding.ItemNotiBinding
import com.pools.store.domain.model.NotiHistoryDomain
import javax.inject.Inject

class ItemNotiAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<NotiHistoryDomain>() {



    private val diffCallback = object : DiffUtil.ItemCallback<NotiHistoryDomain>() {
        override fun areItemsTheSame(
            oldItem: NotiHistoryDomain,
            newItem: NotiHistoryDomain
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotiHistoryDomain,
            newItem: NotiHistoryDomain
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemNotiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemNotiHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemNotiHolder).bind(item)
    }

    inner class ItemNotiHolder(
        private val binding: ItemNotiBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<NotiHistoryDomain> {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: NotiHistoryDomain) {
            binding.apply {
                tvTitle.text = item.name
            }

        }
    }




}