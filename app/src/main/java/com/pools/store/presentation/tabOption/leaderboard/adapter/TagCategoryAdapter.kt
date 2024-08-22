package com.pools.store.presentation.tabOption.leaderboard.adapter

import android.os.Binder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseAdapter
import com.pools.store.databinding.ItemRowGameNormalBinding
import com.pools.store.databinding.ItemRowTagBinding
import com.pools.store.utils.AppFormatter
import javax.inject.Inject

class TagCategoryAdapter @Inject constructor(
    private val tagCategoryList: List<String>
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
            ItemRowTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemTagCategoryHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemTagCategoryHolder).bind(item)
    }

    inner class ItemTagCategoryHolder(
        private val binding: ItemRowTagBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<String> {
        override fun bind(item: String) {
            binding.apply {
//                tvTag.text = item
                tvTag.text = AppFormatter().formatStringTag(item)
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }

        }
    }
}