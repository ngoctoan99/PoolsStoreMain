package com.pools.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemRowGameBoardBinding
import com.pools.store.databinding.ItemRowGameCategoryBinding
import com.pools.store.databinding.ItemRowGameNormalBinding
import javax.inject.Inject

class TabOptionChoseAdapter @Inject constructor(
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

//        val binding =
//            ItemRowGameBoardBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return TypeLeaderboardViewHolder(binding)
        return when (viewType) {
            VIEW_TYPE_1 -> {
                val binding = ItemRowGameBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TypeLeaderboardViewHolder(binding)
            }
            VIEW_TYPE_2 -> {
                val binding = ItemRowGameNormalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TypeOfferViewHolder(binding)
            }
//            VIEW_TYPE_3 -> {
//                val binding = ItemRowGameCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                TypeCategoryViewHolder(binding)
//            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = list[position]
    when (holder.itemViewType) {
        VIEW_TYPE_1 -> {
            val viewHolder = holder as TypeLeaderboardViewHolder
            viewHolder.bind(currentItem)
        }
        VIEW_TYPE_2 -> {
            val viewHolder = holder as TypeOfferViewHolder
            viewHolder.bind(currentItem)
        }
//        VIEW_TYPE_3 -> {
//            val viewHolder = holder as TypeCategoryViewHolder
//            viewHolder.bind(currentItem)
//        }
    }
}

    private val VIEW_TYPE_1 = 1
    private val VIEW_TYPE_2 = 2
    private val VIEW_TYPE_3 = 3
    // ViewHolder for each view type
    inner class TypeLeaderboardViewHolder(private val binding: ItemRowGameBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvTitle.setText("title name qaz")
        }
    }
    inner class TypeOfferViewHolder(private val binding: ItemRowGameNormalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvTitle.setText("title name qaz")
        }
    }
    inner class TypeCategoryViewHolder(private val binding: ItemRowGameCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvTitle.setText("title name qaz")
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position % 3) {
            0 -> VIEW_TYPE_1
            1 -> VIEW_TYPE_2
//            2 -> VIEW_TYPE_3
            else -> VIEW_TYPE_1 // Default case
        }
    }
    data class Item(val text: String, val type: ItemType)
    enum class ItemType  {
        TYPE_DEFAULT,
        TYPE_OFFER,
        TYPE_CATEGORY
    }

}
