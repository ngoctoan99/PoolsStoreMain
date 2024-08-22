package com.pools.store.presentation.tabOption.category.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapter
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.databinding.ItemRowGameCategoryBinding
import com.pools.store.domain.model.CategoryDomain
import javax.inject.Inject

class CategoryAdapter @Inject constructor(
    private val preferencesHelper: CachePreferencesHelper,
) : BaseAdapter<CategoryDomain>() {
    private val diffCallback = object : DiffUtil.ItemCallback<CategoryDomain>() {
        override fun areItemsTheSame(
            oldItem: CategoryDomain,
            newItem: CategoryDomain
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryDomain,
            newItem: CategoryDomain
        ): Boolean {
            return oldItem.createdAt == newItem.createdAt
        }
    }
    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemRowGameCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemCategoryHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemCategoryHolder).bind(item)
    }

    inner class ItemCategoryHolder(
        private val binding: ItemRowGameCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<CategoryDomain> {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: CategoryDomain) {
            binding.apply {
                tvTitle.text = (item.name).toString()
                Glide.with(binding.imgCategory.context).load(item.iconUrl).into(imgCategory)
                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }
}