package com.pools.store.presentation.detailGame.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemRatingBarBinding
import javax.inject.Inject

class RatingBarAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<Long>() {



    private val diffCallback = object : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(
            oldItem: Long,
            newItem: Long
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Long,
            newItem: Long
        ): Boolean {
            return oldItem == newItem
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemRatingBarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemRatingHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemRatingHolder).bind(item)
    }

    inner class ItemRatingHolder(
        private val binding: ItemRatingBarBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<Long> {
        override fun bind(item: Long) {
            binding.apply {
                var total : Long = 0
                for(element in list){
                    total += element
                }
                val number = absoluteAdapterPosition  + 1
               tvNumberStar.text = number.toString()

                if(item > 0){
                    val process  = (item.toDouble() / total.toDouble())
                    processBarRating.progress = (process * 100).toInt()
                }else {
                    processBarRating.progress = 0
                }
//                itemView.setOnClickListener {
//                    onItemClickListener?.let {
//                        it(item)
//                    }
//                }
            }

        }
    }
}