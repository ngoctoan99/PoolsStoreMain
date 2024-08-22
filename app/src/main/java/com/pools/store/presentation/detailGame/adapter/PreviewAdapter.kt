package com.pools.store.presentation.detailGame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemRatingPreviewBinding
import com.pools.store.domain.model.PreviewDomain
import com.pools.store.extension.formatDayMonthYear
import javax.inject.Inject

class PreviewAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<PreviewDomain>() {



    private val diffCallback = object : DiffUtil.ItemCallback<PreviewDomain>() {
        override fun areItemsTheSame(
            oldItem: PreviewDomain,
            newItem: PreviewDomain
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PreviewDomain,
            newItem: PreviewDomain
        ): Boolean {
            return oldItem.description == newItem.description
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemRatingPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemPreviewHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemPreviewHolder).bind(item)
    }

    inner class ItemPreviewHolder(
        private val binding: ItemRatingPreviewBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<PreviewDomain> {
        override fun bind(item: PreviewDomain) {
            binding.apply {
                tvName.text = item.fullName
                tvReview.text= item.description
                Glide.with(ivAvatar.context).load(item.avatarUrl).into(ivAvatar)
                rBar.rating = item.star.toFloat()
                tvTime.text =  item.createdAt.formatDayMonthYear()
            }

        }
    }




}