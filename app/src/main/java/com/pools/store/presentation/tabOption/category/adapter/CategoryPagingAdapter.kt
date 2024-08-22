package com.pools.store.presentation.tabOption.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapterPaging
import com.pools.store.databinding.ItemRowGameCategoryBinding
import com.pools.store.domain.model.CategoryDomain
import javax.inject.Inject

class CategoryPagingAdapter @Inject constructor() :
    BaseAdapterPaging<CategoryDomain>(diffCallbackTip) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowGameCategoryBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }


    inner class ItemViewHolder(private val binding: ItemRowGameCategoryBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<CategoryDomain> {
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


    companion object {
        val diffCallbackTip = object : DiffUtil.ItemCallback<CategoryDomain>() {
            override fun areItemsTheSame(oldItem: CategoryDomain, newItem: CategoryDomain): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryDomain, newItem: CategoryDomain): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }


}