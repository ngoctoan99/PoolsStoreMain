package com.pools.store.presentation.tabOption.leaderboard.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.pools.store.base.BaseAdapter
import com.pools.store.databinding.ItemRowGameBoardBinding
import com.pools.store.domain.model.AppDomain
import com.pools.store.presentation.detailGame.adapter.TagAdapter
import javax.inject.Inject

class LeaderBoardAdapter @Inject constructor(
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
            ItemRowGameBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemHolder).bind(item)

    }

    inner class ItemHolder(
        private val binding: ItemRowGameBoardBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<AppDomain> {
        override fun bind(item: AppDomain) {
            binding.apply {
                tvTitle.text = item.name
                tvItemSize.text = item.size
                tvNumberDownload.text = item.countDownload.toString()
                Glide.with(binding.ivImage.context).load(item.imageContentUrls[0]).into(ivImage)
                rBar.rating = item.averageStar.toFloat()

                val tagCategoryAdapter = TagCategoryAdapter(item.tags)

                rvTagCategory.apply {

//                    val flexboxLayoutManager = FlexboxLayoutManager(context)


                    layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                    adapter = tagCategoryAdapter

                }
                tagCategoryAdapter.list = item.tags


                tagCategoryAdapter.setItemClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }


                rootView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

//                llInfo.setOnClickListener {
//                    onDetailClickListener?.let {
//                        it(item)
//                    }
//                }


            }
        }
    }
}