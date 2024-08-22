package com.pools.store.presentation.home.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.R
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemAppStoreBinding
import com.pools.store.domain.model.AppDomain
import javax.inject.Inject


class AppAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<AppDomain>() {



    private val diffCallback = object : DiffUtil.ItemCallback<AppDomain>() {
        override fun areItemsTheSame(
            oldItem: AppDomain,
            newItem: AppDomain
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AppDomain,
            newItem: AppDomain
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemAppStoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemAppHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemAppHolder).bind(item)
    }

    inner class ItemAppHolder(
        private val binding: ItemAppStoreBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<AppDomain> {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: AppDomain) {
            binding.apply {
                tvApp.text = item.name
                tvAuthor.text = item.name
//                ivApp.setImageResource( R.drawable.ic_app_example)
                Glide.with(binding.ivApp.context).load(item.imageContentUrls[0]).into(ivApp)
                tvPriceReduce.paintFlags = tvPriceReduce.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (item.pricing > 0){
                    binding.apply {
                        tvPrice.visibility = View.VISIBLE
                        tvPriceReduce.visibility = View.VISIBLE
                        tvPercentReduce.visibility = View.VISIBLE
                    }
                }else {
                    binding.apply {
                        tvPrice.visibility = View.GONE
                        tvPriceReduce.visibility = View.GONE
                        tvPercentReduce.visibility = View.GONE
                    }
                }
                rBar.rating = item.averageStar.toFloat()
                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }

        }
    }




}