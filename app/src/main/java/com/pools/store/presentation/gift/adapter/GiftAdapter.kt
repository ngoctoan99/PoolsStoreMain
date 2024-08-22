package com.pools.store.presentation.gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseAdapter
import com.pools.store.core.DialogClaimSuccess
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemGiftBinding
import javax.inject.Inject

class GiftAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<String>() {



    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemGiftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemGiftHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemGiftHolder).bind(item)
    }

    inner class ItemGiftHolder(
        private val binding: ItemGiftBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<String> {
        override fun bind(item: String) {
            binding.apply {
                tvClaim.text = "Claim"
                tvPoint.text = "10 Point"
                tvTitle.text = "Lorem"
                tvSubTitle.text = "Lorem ipsum dolor sit amet consectetur."
                tvClaim.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }

        }
    }




}