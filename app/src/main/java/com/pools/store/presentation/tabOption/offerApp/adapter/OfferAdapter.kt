package com.pools.store.presentation.tabOption.offerApp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemRowGameNormalBinding
import com.pools.store.domain.model.AppDomain
import javax.inject.Inject

class OfferAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<AppDomain>() {
    private val diffCallback = object : DiffUtil.ItemCallback<AppDomain>() {
        override fun areItemsTheSame(
            oldItem: AppDomain,
            newItem: AppDomain
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AppDomain,
            newItem: AppDomain
        ): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemRowGameNormalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemOfferHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemOfferHolder).bind(item)
    }

    inner class ItemOfferHolder(
        private val binding: ItemRowGameNormalBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<AppDomain> {
        override fun bind(item: AppDomain) {
            binding.apply {
                tvTitle.text =  item.name
                tvSubTitle.text = item.name
                tvPriceOld.paintFlags = tvPriceOld.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(binding.ivImage.context).load(item.imageContentUrls[0]).into(ivImage)
                rBar.rating = item.averageStar.toFloat()

                if (item.pricing > 0){
                    binding.apply {
                        tvPriceNew.visibility = View.VISIBLE
                        tvPriceOld.visibility = View.VISIBLE
                        tvDiscount.visibility = View.VISIBLE
                    }
                }else {
                    binding.apply {
                        tvPriceNew.visibility = View.GONE
                        tvPriceOld.visibility = View.GONE
                        tvDiscount.visibility = View.GONE
                    }
                }

                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }

        }
    }
}