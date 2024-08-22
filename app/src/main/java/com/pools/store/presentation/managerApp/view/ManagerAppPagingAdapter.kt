package com.pools.store.presentation.managerApp.view
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapterPaging
import com.pools.store.data.remote.dto.ItemsDownloadHistoryDto
import com.pools.store.databinding.ItemAppStoreBinding
import com.pools.store.domain.model.AppDomain
import com.pools.store.domain.model.ItemsDownloadHistoryDomain
import javax.inject.Inject

class ManagerAppPagingAdapter @Inject constructor() :
    BaseAdapterPaging<ItemsDownloadHistoryDomain>(diffCallbackTip) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSwapTokenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppStoreBinding.inflate(inflater, parent, false)
        return SearchSwapTokenViewHolder(binding)
    }


    inner class SearchSwapTokenViewHolder(private val binding: ItemAppStoreBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<ItemsDownloadHistoryDomain> {
        override fun bind(item: ItemsDownloadHistoryDomain) {
            // Bind your data to the views in the ViewHolder
            binding.apply {
                tvApp.text = item.apk.name
                tvAuthor.text = item.apk.name
//                ivApp.setImageResource( R.drawable.ic_app_example)
                Glide.with(binding.ivApp.context).load(item.apk.imageContentUrls[0]).into(ivApp)
                tvPriceReduce.paintFlags = tvPriceReduce.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (item.apk.pricing > 0){
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

                rBar.rating = item.apk.averageStar.toFloat()
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }


        }

    }


    companion object {
        val diffCallbackTip = object : DiffUtil.ItemCallback<ItemsDownloadHistoryDomain>() {
            override fun areItemsTheSame(oldItem: ItemsDownloadHistoryDomain, newItem: ItemsDownloadHistoryDomain): Boolean {
                return oldItem.apk.id == newItem.apk.id
            }

            override fun areContentsTheSame(oldItem: ItemsDownloadHistoryDomain, newItem: ItemsDownloadHistoryDomain): Boolean {
                return oldItem.apk.name == newItem.apk.name
            }
        }
    }


}