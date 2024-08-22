package com.pools.store.presentation.favorite.adapter


import android.graphics.Paint
import android.util.Log
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
import com.pools.store.domain.model.FavoritesDomain
import javax.inject.Inject

class FavoriteAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<FavoritesDomain>() {
    private val diffCallback = object : DiffUtil.ItemCallback<FavoritesDomain>() {
        override fun areItemsTheSame(
            oldItem: FavoritesDomain,
            newItem: FavoritesDomain
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FavoritesDomain,
            newItem: FavoritesDomain
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
    ) : RecyclerView.ViewHolder(binding.root), Binder<FavoritesDomain> {
        override fun bind(item: FavoritesDomain) {
            binding.apply {
                tvTitle.text =  item.name
                tvSubTitle.text = item.name
                tvPriceOld.paintFlags = tvPriceOld.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(binding.ivImage.context).load(item.imageContentUrls[0]).into(ivImage)
                rBar.rating = item.averageStar.toFloat()
                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
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
            }

        }
    }
}